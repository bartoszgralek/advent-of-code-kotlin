
private fun String.toTrapeze(reverse: Boolean = false): MutableList<MutableList<Long>> {
    val result = mutableListOf<MutableList<Long>>()
    result.add(match().map { it.value.toLong() }.toMutableList().let { if (reverse) it.reversed().toMutableList() else it} )

    while (result.last().any { it != 0L }) {
        result.addLast(result.last().windowed(2).map { (first, second) -> second - first }.toMutableList())
    }
    return result
}

private fun MutableList<MutableList<Long>>.getValue(row: Int, index: Int): Long {
    return this.getOrNull(row)?.getOrNull(index) ?: run {
        val value = this.getValue(row, index - 1) + this.getValue(row + 1, index - 1)
        this[row].add(index, value)
        value
    }
}

private fun MutableList<MutableList<Long>>.addZero(): MutableList<MutableList<Long>> {
    last().addLast(0)
    return this
}


private fun MutableList<MutableList<Long>>.calculateLast(): Long {
    val result = this.getValue(0, first().lastIndex + 1)
    return result
}


fun main() {

    fun part1(input: List<String>): Long {
        return input.sumOf { it.toTrapeze().addZero().calculateLast() }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { it.toTrapeze(true).addZero().calculateLast() }
    }

    val testInput = readInput("Day09_test")
//    check(part1(testInput) == 114L)
//    check(part2(testInput) == 0)

    val input = readInput("Day09")
//    part1(input).println()
    part2(input).println()
}




