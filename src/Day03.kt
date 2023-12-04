import kotlin.math.max
import kotlin.math.min

fun main() {

    val digitRx = "\\d+".toRegex()
    val symbols = charArrayOf('@', '#', '$', '%', '&', '*', '-', '+', '=', '/')

    fun String.containsAnyOf(chars: CharArray) = chars.any { this.contains(it) }
    fun Char.isIn(charArray: CharArray) = charArray.contains(this)
    fun String.allIndicesOf(char: Char) = this.indices.filter { this[it] == char }

    fun part1(input: List<String>): Int {
        val length = input[0].length

        return input.mapIndexed { index, line ->
            digitRx.findAll(line).toList().filter {
                val checkRange = max(it.range.first - 1, 0) until min(it.range.last + 2, length - 1)

                val above = input.getOrNull(index - 1)?.substring(checkRange)?.containsAnyOf(symbols) ?: false
                val below = input.getOrNull(index + 1)?.substring(checkRange)?.containsAnyOf(symbols) ?: false
                val front = line.getOrNull(it.range.first - 1)?.isIn(symbols) ?: false
                val behind = line.getOrNull(it.range.last + 1)?.isIn(symbols) ?: false

                above || below || front || behind
            }.sumOf { it.value.toInt() }
        }.sum()
    }

    fun List<Triple<Int, IntRange, Int>>.getByCoordinates(row: Int, column: Int) =
        this.firstOrNull { it.first == row && column in it.second }

    fun part2(input: List<String>): Int {
        val length = input[0].length
        val triples = input.flatMapIndexed { index, line ->
            digitRx.findAll(line).map { Triple(index, it.range, it.value.toInt()) }
        }

        return input.asSequence()
            .flatMapIndexed { index, line -> line.allIndicesOf('*').map { Pair(index, it) } }
            .map { it ->
                val topBoundary = max(0, it.first - 1)
                val bottomBoundary = min(input.size, it.first + 1)
                val leftBoundary = max(0, it.second - 1)
                val rightBoundary = min(length, it.second + 1)

                val hitList = buildList {
                    for (i in topBoundary..bottomBoundary) {
                        for (j in leftBoundary..rightBoundary) {
                            triples.getByCoordinates(i, j)?.also { hit -> add(hit.third) }
                        }
                    }
                }

                hitList.distinct().takeIf { it.size == 2 }?.reduce(Int::times)
            }
            .filterNotNull()
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    println(part2(testInput))
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}