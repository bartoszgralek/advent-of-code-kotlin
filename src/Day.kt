fun main() {

    fun part1(input: List<String>): Long {
        return 0L
    }

    fun part2(input: List<String>): Long {
        return 0L
    }

    val testInput = readInput("@_test")
    check(part1(testInput) == 0L)
    check(part2(testInput) == 0L)

    val input = readInput("@")
    part1(input).println()
    part2(input).println()
}