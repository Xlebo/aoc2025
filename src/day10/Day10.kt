package day10

import getOrFetchInputData
import getTestInput
import java.util.Stack

typealias Lights = List<Boolean>
typealias Joltage = List<Int>

fun List<Joltage>.allCombinations(): List<Pair<Joltage, Int>> =
    this.fold(listOf((List(this.first().size) { 0 }) to 0)) { acc, element ->
        acc + acc.map { current ->
            element.indices.map { element[it] + current.first[it] } to current.second + 1
        }
    }

fun Joltage.half(): Joltage = this.map { it / 2 }

fun Joltage.minus(other: Joltage): Joltage = this.indices.map { this[it] - other[it] }

fun main() {
    val rlights = "\\[(.+?)]".toRegex()
    val rbutton = "\\((.+?)\\)".toRegex()
    val rjoltage = "\\{(.+?)}".toRegex()

    data class JoltageConfiguration(
        val joltage: Joltage,
        val buttons: List<Joltage>,
        val patterns: Map<Joltage, List<Pair<Joltage, Int>>>
    ) {
        //https://www.reddit.com/r/adventofcode/comments/1pk87hl/comment/ntp4njq/
        //graph with dijkstra was not enough trying to apply this
        // 18030 too high
        fun solveJoltage(
            joltage: Joltage = this.joltage,
            cache: MutableMap<Joltage, Int>
        ): Int {
            val stack = Stack<Joltage>()
            stack.push(joltage)

            while (!stack.isEmpty()) {
                val current = stack.pop()
                if (cache.containsKey(current)) {
                    continue
                }
                val pattern = current.map { it % 2 }
                if (!patterns.containsKey(pattern)) {
                    // lights configuration cannot be solved in this system
                    cache[current] = 1_000_000
                    continue
                }

                /*
                    first - pattern, first is the sum of buttons pressed, second amount of buttons pressed
                    second = (current vector - pattern vector) / 2
                */
                val halvedCombinations = patterns[pattern]!!
                    // applying pattern would result in more presses that needed, filter those cases
                    .filter { pattern -> pattern.first.indices.all { pattern.first[it] <= current[it] } }
                    .map { it to current.minus(it.first).half() }
                if (halvedCombinations.isEmpty()) {
                    cache[current] = 1_000_000
                    continue
                }

                if (!halvedCombinations.all { cache.containsKey(it.second) }) {
                    stack.push(current)
                    halvedCombinations.forEach {
                        if (!cache.containsKey(it.second)) {
                            stack.push(it.second)
                        }
                    }
                    continue
                }
                cache[current] = halvedCombinations.minOf {
                    (cache[it.second]!! * 2) + it.first.second
                }
            }
            return cache[joltage]!!.also { println("Result for $joltage: ${cache[joltage]}") }
        }
    }

    data class LightsConfiguration(
        val lights: Lights,
        val buttons: List<Lights>
    ) {
        fun solveLights(): Int {
            val queue = ArrayDeque<Pair<Lights, Int>>()
            val discovered = HashSet<Lights>()
            queue.addLast(List(lights.size) { false } to 0)

            while (queue.isNotEmpty()) {
                val current = queue.removeFirst()
                if (current.first == lights)
                    return current.second
                if (current.first !in discovered) {
                    discovered.add(current.first)
                    buttons.forEach { button ->
                        val new = button.indices.map { index ->
                            if (button[index]) {
                                !current.first[index]
                            } else {
                                current.first[index]
                            }
                        }
                        queue.addLast(new to current.second + 1)
                    }
                }
            }
            return -1
        }
    }

    fun part1(input: List<String>): Int = input.sumOf { row ->
        val lights = rlights.find(row)!!.groupValues[1].map { light -> light == '#' }
        val buttons = rbutton.findAll(row).map { it.groupValues[1] }.map {
            val array = BooleanArray(lights.size) { false }
            it.split(",").forEach { light ->
                array[light.toInt()] = true
            }
            array.toList()
        }.toList()
        LightsConfiguration(lights, buttons).solveLights()
    }

    fun part2(input: List<String>): Int = input.sumOf { row ->
        val joltage = rjoltage.find(row)!!.groupValues[1].split(",").map { it.toInt() }.toList()
        val buttons = rbutton.findAll(row).map { it.groupValues[1] }.map {
            val array = IntArray(joltage.size) { 0 }
            it.split(",").forEach { light ->
                array[light.toInt()] = 1
            }
            array.toList()
        }.toList()
        val lights = buttons.allCombinations().groupBy { jolt -> jolt.first.map { it % 2 } }
        val configuration = JoltageConfiguration(
            joltage = joltage,
            buttons = buttons,
            patterns = lights
        )
        val cache =
            lights.values.flatten().groupBy({ it.first }, { it.second }).mapValues { (_, values) -> values.min() }
                .toMutableMap()
        val res = configuration.solveJoltage(cache = cache)
        res
    }

    val testInput = getTestInput(10, "example")
    val test1 = part1(testInput)
    check(test1 == 7) { "Got $test1" }

//    val test2 = part2(testInput)
//    check(test2 == 33) { "Got $test2" }

    val input = getOrFetchInputData(10)
    println(part1(input))
    println(part2(input))
}