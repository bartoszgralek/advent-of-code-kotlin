data class Universe(
    val matrix: List<List<Char>>
) {

    fun expand(): Universe {
        return this
    }

    fun countDistances(): List<Long> {
        return emptyList()
    }

    companion object {
        fun from(list: List<String>): Universe {


            return Universe(emptyList())
        }
    }
}

fun main() {

    fun part1(input: List<String>): Long {
        return Universe.from(input).expand().countDistances().sum()
    }

    fun part2(input: List<String>): Long {
        return 0L
    }

    val testInput = readInput("Day11_test")
    check(part1(testInput) == 0L)
    check(part2(testInput) == 0L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}