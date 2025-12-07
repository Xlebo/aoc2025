package day06

import getOrFetchInputData
import getTestInput

fun main() {

    fun part1(input: List<String>): Long {
        val split = input.map { it.trim().split(Regex("\\s+")) }
        return split[0].indices.sumOf { index ->
            val operation: (x: Long, y: Long) -> Long = when {
                split.last()[index] == "+" -> { x, y -> x + y }
                else -> { x, y -> x * y }
            }
            (1..<split.size - 1).fold(split[0][index].toLong()) { acc, i ->
                operation(acc, split[i][index].toLong())
            }
        }
    }

    fun part2(input: List<String>): Long {
        var currentGroup = mutableListOf<Long>()
        val numberGroups = mutableListOf<List<Long>>()
        input[0].indices.forEach { column ->
            input.subList(0, input.size - 1).let { noSigns ->
                if (noSigns.all { it[column] == ' ' }) {
                    numberGroups.add(currentGroup.map { it })
                    currentGroup = mutableListOf()
                } else {
                    var num = ""
                    noSigns.forEach { row ->
                        num += row[column]
                    }
                    currentGroup.add(num.trim().toLong())
                }
            }
        }
        numberGroups.add(currentGroup)
        return input.last().trim().split(Regex("\\s+")).foldIndexed(0L) { index, sum, sign ->
            val operation: (x: Long, y: Long) -> Long = when {
                sign == "+" -> { x, y -> x + y }
                else -> { x, y -> x * y }
            }
            sum + numberGroups[index].subList(1, numberGroups[index].size).fold(numberGroups[index][0]) { acc, num ->
                operation(acc, num)
            }
        }

    }


    val testInput = getTestInput(6, "example")
    val test1 = part1(testInput)
    check(test1 == 4277556L) { "Got $test1" }

    val test2 = part2(testInput)
    check(test2 == 3263827L) { "Got $test2" }

    val input = getOrFetchInputData(6)
    println(part1(input))
    println(part2(input))
}