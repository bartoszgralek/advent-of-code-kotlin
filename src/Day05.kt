import kotlin.math.max
import kotlin.math.min
import kotlin.system.measureTimeMillis

fun main() {

    data class Single(
        val offset: Long,
        val range: LongRange
    ) : Comparable<Single> {
        override fun compareTo(other: Single): Int {
            return when(this.range.first - other.range.first) {
                in 1..Long.MAX_VALUE -> 1
                0L -> return 0
                else -> return -1
            }
        }
    }

    fun LongRange.intersect(other: LongRange): Triple<LongRange, LongRange, LongRange> {
        return Triple(
            this.first..<min(other.first, this.last + 1L),
            max(this.first, other.first)..min(this.last, other.last),
            max(other.last + 1L, this.first)..this.last
        )
    }

    fun LongRange.plus(value: Long): LongRange {
        return (first+value)..(last+value)
    }

    data class Mapper(val singles: List<Single>) {

        fun map(number: Long): Long {
            return singles.firstOrNull { number in it.range }?.let { number + it.offset } ?: number
        }

        fun map(range: LongRange): List<LongRange> {
            val list = mutableListOf<LongRange>()
            var restOfRange = range
            val iterator = singles.iterator()
            while (iterator.hasNext()) {
                val single = iterator.next()
                val intersect = restOfRange.intersect(single.range)

                if (intersect.second.isEmpty()) {
                    if (!intersect.first.isEmpty()) {
                        list.add(intersect.first)
                        return list
                    }
                    restOfRange = intersect.third
                } else {
                    if (!intersect.first.isEmpty()) {
                        list.add(intersect.first)
                    }
                    list.add(intersect.second.plus(single.offset))

                    if (intersect.third.isEmpty()) return list
                    else restOfRange = intersect.third
                }
                if (!iterator.hasNext()) {
                    list.add(restOfRange)
                    return list
                }
            }

            return list
        }
    }

    val digitRx = "\\d+".toRegex()

    fun buildMappers(input: List<String>): List<Mapper> {
        return buildList {
            val tmp = mutableListOf<Single>()
            val iterator = input.iterator()
            while (iterator.hasNext()) {
                val numbers = digitRx.findAll(iterator.next()).toList().map { it.value.toLong() }
                if (numbers.isNotEmpty()) {
                    tmp.add(
                        Single(
                            numbers[0] - numbers[1],
                            numbers[1]..<numbers[1] + numbers[2]
                        )
                    )
                    if (!iterator.hasNext()) {
                        add(Mapper(tmp.sorted()))
                        tmp.clear()
                    }
                } else {
                    if (tmp.isNotEmpty()) {
                        add(Mapper(tmp.sorted()))
                        tmp.clear()
                    }
                }
            }
        }
    }

    fun part1(input: List<String>): Long {
        var seeds = digitRx.findAll(input.removeFirst()).toMutableList().map { it.value.toLong() }
        val mappers = buildMappers(input)

        mappers.forEach { mapper ->
            seeds = seeds.map { mapper.map(it) }
        }

        return seeds.min()
    }

    fun part2(input: List<String>): Long {
        var seeds = digitRx.findAll(input.removeFirst())
            .map { it.value.toLong() }
            .chunked(2)
            .map { (first, length) -> first..<first+length }
            .toList()
        val mappers = buildMappers(input)

        mappers.forEach { mapper ->
            seeds = seeds.flatMap { mapper.map(it) }
        }

        return seeds.minBy { it.first }.first
    }

    val testInput = readInput("Day05_test")
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    measureTimeMillis { part2(input).println() }.println()

}