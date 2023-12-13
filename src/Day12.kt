import java.util.LinkedList

fun String.evaluate(): List<Int> {
    return damageRx.findAll(this)
        .flatMap { match ->
            match.groups.mapNotNull { it?.range?.count() }
        }.toList()
}

tailrec fun guesses(queue: LinkedList<String> = LinkedList()): LinkedList<String> {
    if (queue.first().indexOf('?') == -1) {
        return queue
    }
    val element = queue.removeFirst()
    queue.add(element.replaceFirst('?', '.'))
    queue.add(element.replaceFirst('?', '#'))
    return guesses(queue)
}
fun String.guesses() = guesses(LinkedList<String>().also { it.add(this) })

val damageRx = "#+".toRegex()

val springRx = "[?.#]+".toRegex()

fun main() {

    fun part2(input: List<String>): Long {
        val springs = input.map { springRx.find(it)?.value?.repeat(5) ?: throw Error() }
        val digits = input.map { digitRx.findAll(it).map { match -> match.value.toInt() }.toList().let { list ->
            List(5) { list }.flatten()
        } }

        return (springs zip digits).sumOf { schema ->
            println("Starting line for $schema")
            val result = schema.first.guesses().map { guess -> guess.evaluate() }.count { it == schema.second }.toLong()
            result
        }
    }

    val testInput = readInput("Day12_test")
//    part2(testInput).println()
    "????????????????????????????????".guesses().size.println()
}
