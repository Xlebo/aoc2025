package day02

import getOrFetchInputDataAsString
import getTestInputAsString

fun main() {
    fun part1(input: String): Long = input.split(",")
        .map { it.split("-").zipWithNext().first() }
        .sumOf { range ->
            (range.first.toLong()..range.second.toLong())
                .map { it.toString() }
                .filter { it.substring(0, it.length / 2) == it.substring(it.length / 2) }
                .sumOf { it.toLong() }
        }


    fun part2(input: String): Long = input.split(",")
        .map { it.split("-").zipWithNext().first() }
        .sumOf { range ->
            (range.first.toLong()..range.second.toLong())
                .map { it.toString() }
                .filter {
                    (1..it.length / 2).any { chunks ->
                        it.chunked(chunks).distinct().size == 1
                    }
                }
                .sumOf { it.toLong() }
        }

    val testInput = getTestInputAsString(2, "example")
    val test1 = part1(testInput)
    check(test1 == 1227775554L) { "Got $test1" }

    val test2 = part2(testInput)
    check(test2 == 4174379265L) { "Got $test2" }

    val input = getOrFetchInputDataAsString(2)
    println(part1(input))
    println(part2(input))
}