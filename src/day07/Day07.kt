package day07

import getOrFetchInputData
import getTestInput

fun main() {

    fun part1(input: List<String>): Int {
        var beams = setOf(input[0].indexOf('S'))
        var splitters = 0
        input.subList(1, input.size).forEachIndexed { index, row ->
            val newBeams = mutableSetOf<Int>()
            beams.forEach { beam ->
                when {
                    row[beam] == '^' -> {
                        splitters++
                        newBeams.add(beam - 1)
                        newBeams.add(beam + 1)
                    }

                    else -> newBeams.add(beam)
                }
            }
            beams = newBeams
        }
        return splitters
    }


    fun part2(input: List<String>): Long {
        val cache = mutableMapOf<Pair<Int, Int>, Long>()

        fun evaluateBeam(x: Int, y: Int): Long {
            fun evalCached(x: Int, y: Int): Long = cache.getOrPut(x to y) { evaluateBeam(x, y) }

            return when {
                y >= input.size -> 1
                input[y][x] == '^' -> evalCached(x - 1, y + 1) + evalCached(x + 1, y + 1)
                else -> evalCached(x, y + 1)
            }
        }

        return evaluateBeam(input[0].indexOf('S'), 0)
    }


    val testInput = getTestInput(7, "example")
    val test1 = part1(testInput)
    check(test1 == 21) { "Got $test1" }

    val test2 = part2(testInput)
    check(test2 == 40L) { "Got $test2" }

    val input = getOrFetchInputData(7)
    println(part1(input))
    println(part2(input))
}