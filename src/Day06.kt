import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

fun main() {

    data class Race(
        val time: Long,
        val distance: Long
    )

    fun Race.winningWays(): Long {
        val time_d = time.toDouble()
        val distance_d = distance.toDouble()
        val x1 = (time_d - sqrt(time_d * time_d - 4 * distance_d))/2
        val x2 = (time_d + sqrt(time_d * time_d - 4 * distance_d))/2

        return (floor(x1.plus(1)).toLong()..ceil(x2.minus(1)).toLong()).count().toLong()
    }

    fun List<String>.parseToRaces(): List<Race> {
        return (this[0].match(digitRx) zip this[1].match(digitRx)).map { pair ->
            Race(pair.first.value.toLong(), pair.second.value.toLong())
        }.toList()
    }

    fun List<String>.parseToRace(): Race {
        return Race(
            this[0].match(digitRx).map { it.value }.joinToString("").toLong(),
            this[1].match(digitRx).map { it.value }.joinToString("").toLong()
        )
    }

    fun part1(input: List<String>): Long {
        return input.parseToRaces()
            .map { it.winningWays() }
            .reduce(Long::times)
    }

    fun part2(input: List<String>): Long {
        return input.parseToRace()
            .winningWays()
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288L)
    check(part2(testInput) == 71503L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}


