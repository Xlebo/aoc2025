package day09

import getOrFetchInputData
import getTestInput
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

fun main() {

    fun part1(input: List<String>): Long =
        input.map { row -> row.split(",").map { it.toLong() } }.map { it[0] to it[1] }.let { points ->
            points.flatMapIndexed { index, first ->
                (index + 1..<points.size).map { secondIndex ->
                    first to points[secondIndex]
                }
            }
        }
            .maxOf { (abs(it.first.first - it.second.first) + 1) * (abs(it.first.second - it.second.second) + 1) }


//    fun part2(input: List<String>): Long {
//    }

    val testInput = getTestInput(9, "example")
    val test1 = part1(testInput)
    check(test1 == 50L) { "Got $test1" }

//    val test2 = part2(testInput)
//    check(test2 == 25272L) { "Got $test2" }

    val input = getOrFetchInputData(9)
    println(part1(input))
//    println(part2(input))
}