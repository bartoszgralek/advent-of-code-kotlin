
fun main() {

    fun <T> List<List<T>>.transpose(): List<List<T>> {
        val length = this.first().size
        val width = this.size
        return List(length) { i ->
            List(width) { j ->
                this[j][i]
            }
        }
    }

    fun <T> List<List<T>>.match(i: Int, j: Int): Boolean {
        runCatching { get(i) == get(j) }
            .onFailure { return true }
            .onSuccess { return it && match(i - 1, j + 1) }
        return false
    }

    fun <T> List<List<T>>.findMirror(): List<Int>? {
        return indices.windowed(2).firstOrNull { (first, second) -> match(first, second) }
    }

    fun <T> List<List<T>>.countSmudges(i: Int, j: Int): Int {
        runCatching { get(i).indices.count { index -> get(j)[index] != get(i)[index] } }
            .onFailure { return 0 }
            .onSuccess { return it + countSmudges(i - 1, j + 1) }
        return 0
    }

    fun <T> List<List<T>>.findSmudgedMirror(): List<Int>? {
        return indices.windowed(2).firstOrNull { (first, second) -> countSmudges(first, second) == 1 }
    }

    fun List<Int>.getValue(wasTransposed: Boolean = false): Long {
        return first().plus(1).times(if (wasTransposed) 1L else 100L)
    }

    fun matricesOf(input: List<String>) =
        input.fold(mutableListOf(mutableListOf<String>())) { acc, line ->
            if (line.isEmpty()) acc.add(mutableListOf())
            else acc.last().add(line)
            acc
        }.map { matrix -> matrix.map { it.toCharArray().toList() } }

    fun part1(input: List<String>): Long {
        return matricesOf(input)
            .sumOf { matrix ->
                matrix.findMirror()?.getValue()
                    ?: matrix.transpose().findMirror()?.getValue(true) ?: throw Error()
            }
    }

    fun part2(input: List<String>): Long {
        return matricesOf(input)
            .sumOf { matrix ->
                matrix.findSmudgedMirror()?.getValue()
                    ?: matrix.transpose().findSmudgedMirror()?.getValue(true) ?: throw Error()
            }
    }

    val testInput = readInput("Day13_test")
    check(part1(testInput) == 405L)
    check(part2(testInput) == 400L)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}