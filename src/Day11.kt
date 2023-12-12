import kotlin.math.abs

data class Universe(
    val matrix: MutableList<MutableList<Char>>,
    val galaxies: List<Galaxy>
) {

    companion object {
        fun from(list: List<String>): Universe {
            val matrix = list.map { it.toCharArray().toMutableList() }.toMutableList()
            val galaxies = matrix.flatMapIndexed { row, line ->
                line.foldIndexed(mutableListOf<Galaxy>()) { col, acc, c ->
                    if (c == '#') acc.addLast(Galaxy(row, col))
                    acc
                }
            }
            return Universe(matrix, galaxies)
        }
    }

    fun expand(scale: Int = 2): Universe {
        val rowSize = matrix.first().size

        val rowIds = matrix.indices.filter { row -> matrix[row].all { it == '.' } }
        val colIds = (0..<rowSize).filter { col -> matrix.all { it[col] == '.' } }

        println("Rows ids: $rowIds, cols ids: $colIds")

        val newGalaxies = galaxies.map { galaxy ->
            val newRow = galaxy.row + galaxy.row.let { row -> rowIds.count { it < row } }.times(scale - 1)
            val newCol = galaxy.col + galaxy.col.let { col -> colIds.count { it < col } }.times(scale - 1)

            Galaxy(newRow, newCol)
        }

        return Universe(matrix, newGalaxies)
    }

    fun distances(): List<Long> {
        return galaxies
            .selfProduct()
            .map { it.first.distanceTo(it.second).toLong() }
    }
}

fun <T> Collection<T>.selfProduct(): List<Pair<T, T>> = this x this

infix fun <T, U> Collection<T>.x(c2: Collection<U>): List<Pair<T, U>> {
    return flatMap { lhsElem -> c2.map { rhsElem -> lhsElem to rhsElem } }
}

data class Galaxy(val row: Int, val col: Int) {

    fun distanceTo(other: Galaxy): Int {
        return abs(row - other.row) + abs(col - other.col)
    }
}

fun main() {

    fun part1(input: List<String>): Long {
        return Universe.from(input)
            .expand()
            .distances()
            .sum().div(2)
    }

    fun part2(input: List<String>): Long {
        return Universe.from(input).expand(1_000_000).distances().sum().div(2)
    }

    val testInput = readInput("Day11_test")
    part2(testInput).println()
//    check(part1(testInput) == 0L)
//    check(part2(testInput) == 0L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}