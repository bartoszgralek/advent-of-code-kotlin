fun main() {

    data class Roll(
        val red: Int = 0,
        val green: Int = 0,
        val blue: Int = 0
    )

    data class Game(
        val id: Int,
        val rolls: List<Roll>
    )

    // only 12 red cubes, 13 green cubes, and 14 blue cubes
    fun Game.isValid(): Boolean = rolls.all { it.red <= 12 && it.green <= 13 && it.blue <= 14 }

    fun Game.power(): Int {
        return rolls.maxOf { it.red } * rolls.maxOf { it.blue } * rolls.maxOf { it.green }
    }

    val gameRegex = "^Game \\d+:".toRegex()
    val colorRegex = "\\d+ (green|red|blue)".toRegex()

    fun String.toGame(): Game  {
        val match = gameRegex.find(this)?.value
        val index = match?.filter { it.isDigit() }?.toInt() ?: throw Exception("No game")
        val rolls = this.removePrefix(match).trim().split(";")
            .map { roll ->
                val map = hashMapOf<String, Int>()
                colorRegex.findAll(roll).toList().map { color ->
                    color.value.split(" ").also { map[it.last()] = it.first().toInt() }
                }
                Roll(
                    map["red"] ?: 0,
                    map["green"] ?: 0,
                    map["blue"] ?: 0
                )
            }
        return Game(index, rolls)
    }

    fun part1(input: List<String>): Int {
        return input.map {it.toGame() }.filter { it.isValid() }.sumOf { it.id }
    }

    fun part2(input: List<String>): Int {
        return input.map { it.toGame() }.sumOf { it.power() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}