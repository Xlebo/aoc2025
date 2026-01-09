package day09

import getOrFetchInputData
import getTestInput
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {

    fun inclusiveRangeOf(first: Long, second: Long): LongRange = if (first > second) {
        second..first
    } else {
        first..second
    }

    fun getNewRanges(previousVertical: List<LongRange>, currentVertical: LongRange): List<LongRange> {
        if (previousVertical.isEmpty()) {
            return listOf(currentVertical)
        }
        val affectedRanges =
            previousVertical.filter { it.contains(currentVertical.first) || it.contains(currentVertical.last) }
        return when (affectedRanges.size) {
            0 -> previousVertical + listOf(currentVertical)
            1 -> {
                val affectedRange = affectedRanges.first()
                // remove affected vertical and add new one(s)
                previousVertical.filterNot { it == affectedRange } + when {
                    // the previous vertical is equal to affected vertical, just remove it from the list
                    affectedRange == currentVertical -> listOf()

                    // the previous vertical contains current vertical, meaning it splits it into 2 verticals
                    affectedRange.contains(currentVertical.first - 1) &&
                            affectedRange.contains(currentVertical.last + 1) -> {
                        listOf(
                            inclusiveRangeOf(affectedRange.first, currentVertical.first),
                            inclusiveRangeOf(currentVertical.last, affectedRange.last)
                        )
                    }

                    // the previous vertical is shrunk/extended by current vertical
                    else -> {
                        listOf(
                            when {
                                affectedRange.first == currentVertical.first -> inclusiveRangeOf(affectedRange.last, currentVertical.last)
                                affectedRange.first == currentVertical.last -> inclusiveRangeOf(affectedRange.last, currentVertical.first)
                                affectedRange.last == currentVertical.first -> inclusiveRangeOf(affectedRange.first, currentVertical.last)
                                affectedRange.last == currentVertical.last -> inclusiveRangeOf(affectedRange.first, currentVertical.first)
                                else -> { throw IllegalStateException("$affectedRange, $currentVertical") }
                            }
                        )
                    }
                }
            }
            // current vertical joins 2 affected verticals
            2 -> {
                assert(max(affectedRanges[0].first, affectedRanges[1].first) == currentVertical.last)
                assert(min(affectedRanges[0].last, affectedRanges[1].last) == currentVertical.first)

                previousVertical.filterNot { it in affectedRanges } + listOf(
                    inclusiveRangeOf(
                        min(affectedRanges[0].first, affectedRanges[1].first()),
                        max(affectedRanges[0].last, affectedRanges[1].last)
                    )
                )
            }

            else -> throw IllegalStateException("idk this should not happen\n previousVertical:$previousVertical\n currentVertical:$currentVertical\n affectedRanges:$affectedRanges")
        }
    }

    fun part1(input: List<String>): Long =
        input.map { row -> row.split(",").map { it.toLong() } }.map { it[0] to it[1] }.let { points ->
            points.flatMapIndexed { index, first ->
                (index + 1..<points.size).map { secondIndex ->
                    first to points[secondIndex]
                }
            }
        }.maxOf { (abs(it.first.first - it.second.first) + 1) * (abs(it.first.second - it.second.second) + 1) }


    fun part2(input: List<String>): Long {
        val verticalBorders = input.map { row -> row.split(",").map { it.toLong() } }.map { it[0] to it[1] }
            .let { points ->
                points.groupBy { it.second }.map { inclusiveRangeOf(it.value[0].first, it.value[1].first) to it.key }
            }
            .sortedBy { it.second }

        val verticalFill = mutableListOf<Pair<LongRange, List<LongRange>>>()

        var previousBorder = verticalBorders.first()
        var previousRanges = getNewRanges(emptyList(), previousBorder.first)
        verticalBorders.drop(1).forEach { border ->
            verticalFill.add(previousBorder.second..border.second to previousRanges)
            previousBorder = border
            previousRanges = getNewRanges(previousRanges, border.first)
        }

        return input.map { row -> row.split(",").map { it.toLong() } }.map { it[0] to it[1] }.let { points ->
            points.flatMapIndexed { index, first ->
                (index + 1..<points.size).map { secondIndex ->
                    first to points[secondIndex]
                }
            }
        }
            .map {
                inclusiveRangeOf(it.first.second, it.second.second) to inclusiveRangeOf(it.first.first, it.second.first)
            }
            // filter vertical lines of the square
            .filter { vectors ->
                verticalFill.any { fill -> vectors.first.first in fill.first && fill.second.any { second -> vectors.second.first in second && vectors.second.last in second } } &&
                        verticalFill.any { fill -> vectors.first.last in fill.first && fill.second.any { second -> vectors.second.first in second && vectors.second.last in second } }
            }
            // filter horizontal lines of the square - need to check one by one as I don't store the whole ranges for the blocks
            .filter { vectors ->
                vectors.first.all { first ->
                    verticalFill.find { fill ->
                        fill.first.contains(first) &&
                                fill.second.any { it.contains(vectors.second.first) } &&
                                fill.second.any { it.contains(vectors.second.last) }
                    } != null
                }

            }
            .maxOf { (abs(it.first.last - it.first.first) + 1) * (abs(it.second.last - it.second.first) + 1) }
    }

    val testInput = getTestInput(9, "example")
    val test1 = part1(testInput)
    check(test1 == 50L) { "Got $test1" }

    val test2 = part2(testInput)
    check(test2 == 24L) { "Got $test2" }

    val input = getOrFetchInputData(9)
    println(part1(input))
    println(part2(input))
}