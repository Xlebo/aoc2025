package day03

import getOrFetchInputData
import getTestInput
import kotlin.collections.maxBy

fun main() {
    fun part1(input: List<String>): Int = input.map { row -> row.map { it.digitToInt() } }
        .sumOf { row ->
            val firstDigit = row.subList(0, row.size - 1).max()
            val secondDigit = row.subList(row.indexOf(firstDigit) + 1, row.size).max()
            10 * firstDigit + secondDigit
        }


    fun part2(input: List<String>): Long = input.map { row -> row.map { it.digitToInt() } }
        .sumOf { row ->
            var result = 0L
            var index = -1
            (11 downTo 0).forEach { digit ->
                index = row.subList(++index, row.size - digit).withIndex().maxBy { it.value }.index + index
                result = result * 10 + row[index]
            }
            result
        }


    val testInput = getTestInput(3, "example")
    val test1 = part1(testInput)
    check(test1 == 357) { "Got $test1" }

    val test2 = part2(testInput)
    check(test2 == 3121910778619L) { "Got $test2" }

    val input = getOrFetchInputData(3)
    println(part1(input))
    println(part2(input))
}