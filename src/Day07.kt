fun main() {

    val cards = listOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A')

    val cards2 = listOf('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A')

    fun String.findRank(): Int {
        val map = this.toList().associateWith { char -> this.count { it == char } }

        if (map.containsValue(5)) return 7
        if (map.containsValue(4)) return 6
        if (map.containsValue(3) && map.containsValue(2)) return 5
        if (map.containsValue(3)) return 4
        if (map.containsValue(2)) {
            if (map.filterValues { it == 2 }.size == 2) return 3
            return 2
        }
        return 1
    }

    fun String.findPossibleRanks(): Int {
        if (this.indexOf('J') == -1) return this.findRank()
        return cards2.drop(1).maxOf { this.replaceFirst('J', it).findPossibleRanks() }
    }

    fun part1(input: List<String>): Int {
        return input.asSequence().map { it.split(' ') }
            .sortedWith(compareBy<List<String>> { it.first().findRank() }.thenComparator { (o1), (o2) ->
                (o1 zip o2).map { (first, second) -> cards.indexOf(first) - cards.indexOf(second) }
                    .firstOrNull { it != 0 } ?: 0
            })
            .mapIndexed { index, line -> (index + 1) * line[1].toInt() }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return input.asSequence().map { it.split(' ') }
            .map { (hand, bet) -> Triple(hand, hand.findPossibleRanks(), bet.toInt()) }
            .sortedWith(compareBy<Triple<String, Int, Int>> { it.second }.then { o1, o2 -> (o1.first zip o2.first).map { (first, second) -> cards2.indexOf(first) - cards2.indexOf(second) }
                .firstOrNull { it != 0 } ?: 0 })
            .mapIndexed { index, pair -> (index + 1) * pair.third }
            .sum()
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
