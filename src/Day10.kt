val compatibilityMap = mapOf(
    '|' to listOf(Pair(-1, 0), Pair(1, 0)),
    '-' to listOf(Pair(0, -1), Pair(0, 1)),
    'L' to listOf(Pair(-1, 0), Pair(0, 1)),
    'J' to listOf(Pair(-1, 0), Pair(0, -1)),
    '7' to listOf(Pair(1, 0), Pair(0, -1)),
    'F' to listOf(Pair(1, 0), Pair(0, 1)),
    'S' to listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1)),
    '.' to emptyList()
)

val signLookup = mapOf(
    Pair('L', 'J') to false,
    Pair('F', '7') to false,
    Pair('L', '7') to true,
    Pair('F', 'J') to true,
)

val signRx = """[-|LJ7F.S]""".toRegex()

data class Part(var sign: Char, val row: Int, val column: Int) {

    fun isConnectedTo(other: Part): Boolean {
        return Pair(other.row - row, other.column - column) in compatibilityMap[this.sign]!!
    }
}

data class Matrix(val data: List<List<Part>>, val start: Part) {

    private fun findConnected(part: Part): List<Part> {
        return compatibilityMap[part.sign]!!.mapNotNull { other ->
            data.getOrNull(part.row + other.first)
                ?.getOrNull(part.column + other.second)
                ?.takeIf { it.isConnectedTo(part) }
        }
    }

    fun recognizeStart(): Matrix {
        val found = findConnected(start).map { Pair(it.row - start.row, it.column - start.column) }
        val newSign = compatibilityMap.toMutableMap()
            .filter { it.value.containsAll(found) && it.key != 'S' }
            .keys.single()

        start.sign = newSign
        return this
    }

    fun getCycle(): List<Part> {

        var previous = start
        var current = findConnected(start).first()

        val result = mutableListOf<Part>()
        result.add(previous)
        result.add(current)
        do {
            val next = findConnected(current).first { it != previous }
            previous = current
            current = next

            result.add(current)
        } while (current != start)

        return result
    }

    fun clarify(): Matrix {
        val cycle = getCycle()
        data.forEach { line ->
            line.forEach {
                if (it !in cycle) it.sign = '.'
            }
        }

        return this
    }

    fun countTiles(): Long {
        return data.sumOf { line ->
            var isCounting = false
            var holder: Part? = null
            line.fold(0L) { acc, part ->
                when (part.sign) {
                    '.' -> acc + if (isCounting) 1 else 0
                    '-' -> acc
                    '|' -> {
                        isCounting = !isCounting
                        acc
                    }
                    else -> {
                        when (holder) {
                            null -> {
                                holder = part
                                acc
                            }
                            else -> {
                                isCounting = isCounting xor signLookup[Pair(holder?.sign, part.sign)]!!
                                holder = null
                                acc
                            }
                        }
                    }
                }
            }
        }
    }
}

fun matrixOf(lines: List<String>): Matrix {
    var start: Part? = null
    val data = lines.mapIndexed { row, line ->
        line.match(signRx)
            .mapIndexed { column, match ->
                Part(match.value.single(), row, column).also { if (it.sign == 'S') start = it }
            }.toList()
    }

    return Matrix(data, start ?: throw Error())
}

fun main() {

    fun part1(input: List<String>): Long {
        return matrixOf(input)
            .getCycle()
            .size.div(2L)
    }

    fun part2(input: List<String>): Long {
        return matrixOf(input)
            .recognizeStart()
            .clarify()
            .countTiles()
    }

    val testInput = readInput("Day10_test")
//    check(part1(testInput) == 4L)
//    check(part2(testInput) == 0L)

    val input = readInput("Day10")
//    part1(input).println()
    part2(input).println()
}