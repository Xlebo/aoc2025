package day01

import getOrFetchInputData
import getTestInput

fun main() {
    fun part1(input: List<String>): Int {
        var zerosCount = 0
        var current = 50
        for (line in input) {
            val dir = line.first()
            val num = line.substring(1).toInt()
            when (dir) {
                'R' -> current += num
                'L' -> current -= num
            }
            current = Math.floorMod(current, 100)
            if (current == 0) zerosCount++
        }
        return zerosCount
    }

    fun part2(input: List<String>): Int {
        var zerosCount = 0
        var current = 50
        for (line in input) {
            val dir = line.first()
            val clicks = line.substring(1).toInt()
            val startedOnZero = current == 0

            zerosCount += Math.floorDiv(clicks, 100)
            val rotation = clicks % 100
            when (dir) {
                'R' -> current += rotation
                'L' -> current -= rotation
            }
            if ((current < 1 && !startedOnZero) || current > 99) zerosCount++
            current = Math.floorMod(current, 100)
        }
        return zerosCount
    }

    val testInput = getTestInput(1, "example")
    val test1 = part1(testInput)
    check(test1 == 3) { "Got $test1" }

    val test2 = part2(testInput)
    check(test2 == 6) { "Got $test2" }

    val input = getOrFetchInputData(1)
    println(part1(input))
    println(part2(input))
}
