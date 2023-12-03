fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val firstDigit = line.first { it.isDigit() }
            val lastDigit = line.last { it.isDigit() }
            String(charArrayOf(firstDigit, lastDigit)).toInt()
        }
    }

    val digits = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

    fun String.findIndicesOfSubstring(string: String): MutableList<Pair<Int, String>> {
        val result = mutableListOf<Pair<Int, String>>()
        var index = this.indexOf(string)
        while (index >= 0) {
            result.add(Pair(index, string))
            index = this.indexOf(string, index + 1)
        }
        return result
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->

            var firstDigit = line.firstOrNull { it.isDigit() }

            var partToCheck = line.substring(0, if (firstDigit == null) line.count() else line.indexOf(firstDigit))
            if (partToCheck.isNotEmpty()) {
                digits.associateBy { partToCheck.indexOf(it) }
                    .filter { it.key > -1 }
                    .minByOrNull { it.key }
                    ?.also {
                        firstDigit = digits.indexOf(it.value).inc().digitToChar()
                    }
            }

            var lastDigit = line.lastOrNull { it.isDigit() }

            if (lastDigit != line.last()) {
                partToCheck = line.substring(if (lastDigit == null) 0 else line.indexOf(lastDigit) + 1)

                digits.flatMap { partToCheck.findIndicesOfSubstring(it) }.maxByOrNull { it.first }?.also {
                    lastDigit = digits.indexOf(it.second).inc().digitToChar()
                }
            }


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