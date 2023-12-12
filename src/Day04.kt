fun main() {

    val digitRx = "\\d+".toRegex()

    val memo = hashMapOf<Int, Int>()

    fun String.cardWorth(): Int {
        val split = this.split(":", "|")
        val winningNumbers = digitRx.findAll(split[1]).map { it.value }.toSet()
        val numbersYouHave = digitRx.findAll(split[2]).map { it.value }.toSet()

        return winningNumbers.count { it in numbersYouHave }
    }

    fun countCards(cards: List<String>, index: Int): Int {
        if (index > cards.lastIndex) return 0
        return memo.getOrPut(index) {
            val worth = cards[index].cardWorth()
            1 + (1..worth).sumOf { countCards(cards, index + it) }
        }
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { it.cardWorth() }
    }

    fun part2(input: List<String>): Int {
        return input.indices.sumOf { index -> countCards(input, index) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}