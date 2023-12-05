fun main() {

    data class Single(
        val offset: Long,
        val range: LongRange
    )

    data class Mapper(
        val singles: List<Single>
    ) {
        fun map(number: Long): Long {
            return singles.firstOrNull { number in it.range }?.let { number + it.offset } ?: number
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
                        add(Mapper(tmp.toList()))
                        tmp.clear()
                    }
                } else {
                    if (tmp.isNotEmpty()) {
                        add(Mapper(tmp.toList()))
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

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)

    val input = readInput("Day05")
    part1(input).println()
//    part2(input).println()
}