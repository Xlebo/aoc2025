package day05

import getOrFetchInputData
import getTestInput

fun main() {

    fun part1(input: List<String>): Int {
        val emptyLine = input.indexOfFirst { it.isBlank() }
        val ranges =
            input.subList(0, emptyLine).map { range -> range.split("-") }.map { it[0].toLong()..it[1].toLong() }
        val ids = input.subList(emptyLine + 1, input.size).map { it.toLong() }
        return ids.count { id ->
            ranges.any { it.contains(id) }
        }
    }

    fun part2(input: List<String>): Long {
        val emptyLine = input.indexOfFirst { it.isBlank() }
        return input.subList(0, emptyLine).map { range -> range.split("-") }.map { it[0].toLong()..it[1].toLong() }
            .sortedBy { it.first }
            .fold(mutableListOf<LongRange>()) { acc, range ->
                if (acc.isNotEmpty() && range.first in acc.last()) {
                    val newRange = acc.last().first..maxOf(range.last, acc.last().last)
                    acc.removeLast()
                    acc.addLast(newRange)
                } else {
                    acc.addLast(range)
                }
                acc
            }
            .sumOf { it.last - it.first + 1 }
    }


    val testInput = getTestInput(5, "example")
    val test1 = part1(testInput)
    check(test1 == 3) { "Got $test1" }

    val test2 = part2(testInput)
    check(test2 == 14L) { "Got $test2" }

    val input = getOrFetchInputData(5)
    println(part1(input))
    println(part2(input))
}