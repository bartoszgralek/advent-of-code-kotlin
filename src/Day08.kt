import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlin.math.abs

data class Node(val label: String)

data class Edge(val from: Node, val to: Node)

data class Graph(
    val nodes: List<Node> = mutableListOf(),
    val edges: List<Edge> = mutableListOf()
) {

    fun getNode(label: String): Node {
        return nodes.find { it.label == label } ?: Node(label).also { nodes.addLast(it) }
    }

    fun addEdge(from: Node, to: Node) {
        edges.addLast(Edge(from, to))
    }

    fun traverse(step: Char, from: Node = nodes.first()): Node {
        return when (step) {
            'L' -> edges.first { it.from == from }.to
            'R' -> edges.last { it.from == from }.to
            else -> throw Error("That should never happen")
        }
    }
}

val lettersRx = "[A-Z]{3}".toRegex()
val lettersNumbersRx = "[A-Z0-9]{3}".toRegex()

fun List<String>.toGraph(regex: Regex = lettersRx): Graph {
    return fold(Graph()) { graph, line ->
        regex.findAll(line).map { it.value }
            .map { graph.getNode(it) }.toList()
            .also { (origin, left, right) ->
                graph.addEdge(origin, left)
                graph.addEdge(origin, right)
            }
        graph
    }
}

fun String.toRepeatingSequence(from: Int = 0): Sequence<Char> {
    return generateSequence(from) { (it + 1L).mod(this.length) }.map { this[it] }
}

fun Long.LCM(other: Long) = abs(this * other) / GCF(this, other)

fun GCF(a: Long, b: Long): Long {
    return if (b == 0L) a
    else GCF(b, a % b)
}

fun main() {

    fun part1(input: List<String>): Int {
        val instructions = input.first()
        val graph = input.drop(2).toGraph()

        var current = graph.getNode("AAA")
        val last = graph.getNode("ZZZ")

        return instructions.toRepeatingSequence()
            .takeWhile { _ -> current != last }
            .onEach { current = graph.traverse(it, current) }
            .count()
    }

    fun part2(input: List<String>): Long {
        val instructions = input.first()
        val graph = input.drop(2).toGraph(lettersNumbersRx)

        val initials = graph.nodes.filter { it.label.endsWith('A') }

        runBlocking {
            initials.map { initial ->
                async {
                    var current = initial
                    instructions.toRepeatingSequence()
                        .takeWhile { _ -> !current.label.endsWith('Z') }
                        .onEach { current = graph.traverse(it, current)}
                        .count()
                }
            }.awaitAll().map { it.toLong() }.reduce { acc, i ->
                acc.LCM(i)
            }.println()
        }
        return 0L
    }


    val testInput = readInput("Day08_test")
    val testInput2 = readInput("Day08_test_2")
//    check(part1(testInput) == 6)
//    check(part2(testInput2) == 6)

    val input = readInput("Day08")
//    part1(input).println()
    part2(input).println()
}