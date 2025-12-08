package day08

import getOrFetchInputData
import getTestInput
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

typealias Distance = Pair<Pair<Point, Point>, Double>

data class Point(val x: Int, val y: Int, val z: Int) {
    fun distanceTo(other: Point): Double = sqrt(
        abs(x - other.x).toDouble().pow(2.toDouble()) +
                abs(y - other.y).toDouble().pow(2.toDouble()) +
                abs(z - other.z).toDouble().pow(2.toDouble())
    )
}

fun Distance.join(groups: MutableList<MutableList<Point>>) {
    val firstGroup = groups.indexOfFirst { it.contains(this.first.first) }
    val secondGroup = groups.indexOfFirst { it.contains(this.first.second) }
    when {
        firstGroup == -1 && secondGroup == -1 -> groups.add(mutableListOf(this.first.first, this.first.second))
        firstGroup == secondGroup -> return
        firstGroup == -1 && secondGroup != -1 -> groups[secondGroup].add(this.first.first)
        firstGroup != -1 && secondGroup == -1 -> groups[firstGroup].add(this.first.second)
        else -> {
            groups[firstGroup].addAll(groups[secondGroup])
            groups.removeAt(secondGroup)
        }
    }
}

fun main() {

    fun part1(input: List<String>, cables: Int): Int {
        val points = input.map { point -> point.split(",").map { it.toInt() } }.map { Point(it[0], it[1], it[2]) }

        val distances = points.indices.flatMap { x ->
            (x + 1..<points.size).map { y ->
                (points[x] to points[y]) to points[x].distanceTo(points[y])
            }
        }.sortedBy { it.second }

        val groups = mutableListOf<MutableList<Point>>()

        (0..<cables).forEach { cable ->
            distances[cable].join(groups)
        }

        return groups.map { it.size }.sortedDescending().take(3).reduce(Int::times)
    }

    fun part2(input: List<String>): Long {
        val points = input.map { point -> point.split(",").map { it.toInt() } }.map { Point(it[0], it[1], it[2]) }

        val distances = points.indices.flatMap { x ->
            (x + 1..<points.size).map { y ->
                (points[x] to points[y]) to points[x].distanceTo(points[y])
            }
        }.sortedBy { it.second }

        val groups = mutableListOf<MutableList<Point>>()
        var index = -1

        do {
            index++
            distances[index].join(groups)
        } while (points.any { point -> !groups.any { group -> group.contains(point) } })

        return distances[index].first.first.x.toLong() * distances[index].first.second.x
    }

    val testInput = getTestInput(8, "example")
    val test1 = part1(testInput, 10)
    check(test1 == 40) { "Got $test1" }

    val test2 = part2(testInput)
    check(test2 == 25272L) { "Got $test2" }

    val input = getOrFetchInputData(8)
    println(part1(input, 1000))
    println(part2(input))
}