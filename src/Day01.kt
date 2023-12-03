fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val firstDigit = line.first { it.isDigit() }
            val lastDigit = line.last { it.isDigit() }
            String(charArrayOf(firstDigit, lastDigit)).toInt()
        }
    }

    val words = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->

            val digitPairs = line
                .mapIndexed { index, c -> Pair(index, c) }
                .filter { it.second.isDigit() }

            val wordPairs = words
                .flatMap { it.toRegex().findAll(line).toList() }
                .flatMap { it.range.map { index -> Pair(index, words.indexOf(it.value).inc().digitToChar()) } }

            val result = (digitPairs + wordPairs).sortedBy { it.first }

            val firstDigit = result.first().second
            val lastDigit = result.last().second

            "$firstDigit$lastDigit".toInt()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part2(testInput) == 253)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}