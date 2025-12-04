package day04

import getOrFetchInputData
import getTestInput

fun main() {
    val neighbours = listOf(
        -1 to -1, -1 to 0, -1 to 1,
        0 to -1, 0 to 1,
        1 to -1, 1 to 0, 1 to 1
    )

    fun List<String>.isPaper(pos: Pair<Int, Int>): Boolean = this.getOrNull(pos.first)?.getOrNull(pos.second) == '@'

    fun List<CharArray>.isPaper(pos: Pair<Int, Int>): Boolean = this.getOrNull(pos.first)?.getOrNull(pos.second) == '@'

    fun part1(input: List<String>): Int = input.indices
        .sumOf { y ->
            input[y].indices.sumOf { x ->
                when {
                    input[y][x] != '@' -> 0
                    neighbours.count { input.isPaper(it.first + y to it.second + x) } < 4 -> 1
                    else -> 0
                }
            }
        }


    fun part2(input: List<String>): Int {
        val map = input.map { it.toCharArray() }
        var removedPaper: List<Pair<Int, Int>>
        var removedCount = 0
        do {
            removedPaper = mutableListOf()
            map.indices.forEach { y ->
                map[y].indices.forEach { x ->
                    if (map.isPaper(y to x) && neighbours.count { map.isPaper(it.first + y to it.second + x) } < 4) {
                        removedPaper.add(y to x)
                    }
                }
            }
            removedPaper.forEach { (y, x) ->
                map[y][x] = 'x'
            }
            removedCount += removedPaper.size
        } while (removedPaper.isNotEmpty())
        return removedCount
    }


    val testInput = getTestInput(4, "example")
    val test1 = part1(testInput)
    check(test1 == 13) { "Got $test1" }

    val test2 = part2(testInput)
    check(test2 == 43) { "Got $test2" }

    val input = getOrFetchInputData(4)
    println(part1(input))
    println(part2(input))
}