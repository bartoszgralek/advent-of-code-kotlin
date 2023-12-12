import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.util.LinkedList
import java.util.PriorityQueue

fun String.evaluate(): List<Int> {
    return damageRx.findAll(this)
        .flatMap { match ->
            match.groups.mapNotNull { it?.range?.count() }
        }.toList()
}

tailrec fun guesses(heap: MutableList<String> = mutableListOf()): MutableList<String> {
    if (heap.first().indexOf('?') == -1) {
        return heap
    }
    val element = heap.removeFirst()
    heap.add(element.replaceFirst('?', '.'))
    heap.add(element.replaceFirst('?', '#'))
    return guesses(heap)
}

fun String.guesses() = guesses(mutableListOf(this))

val damageRx = "#+".toRegex()

val springRx = "[?.#]+".toRegex()

fun main() {

    fun part1(input: List<String>): Long {
        val springs = input.map { line -> springRx.findAll(line).map { it.value }.first() }
        val digits = input.map { line -> digitRx.findAll(line).map { it.value.toInt() }.toList() }

        val result = runBlocking {
            (springs zip digits).map { schema ->
                async {schema.first.guesses().map { guess -> guess.evaluate() }.count { it == schema.second }.toLong() }
            }.awaitAll().sum()
        }
        return result
    }

    fun part2(input: List<String>): Long {
        return 0L
    }

    val testInput = readInput("Day12_test")
    part1(testInput).println()
//    check(part1(testInput) == 21L)
//    check(part2(testInput) == 0L)

    val input = readInput("Day12")
    part1(input).println()
//    part2(input).println()
}