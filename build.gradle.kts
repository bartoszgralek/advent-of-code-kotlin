plugins {
    kotlin("jvm") version "1.9.20"
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }
}

val counterFile = File("src/counter.txt")
var currentDay = if (counterFile.exists()) counterFile.readText().toInt() else 0


// Create a task using the task type
tasks.register("day") {
    val check = "\u001B[32m✅\u001B[0m"

    doFirst {
        val dayName = String.format("Day%02d", currentDay)
        val fileName = "src/$dayName.kt"

        // Replace @ with day number
        val templateName = "src/Day.kt"
        val fileContents = file(templateName).readText()
        val replacedContents = fileContents.replace("@", dayName)

        // Write to new file
        file(fileName).writeText(replacedContents)
        println("$check The file $fileName has been created")

        // Add to git
        exec {
            commandLine("git", "add", fileName)
        }.exitValue
        println("$check The file $fileName has added to git")

        // Create test files
        file("src/resources/$dayName.txt").createNewFile()
        file("src/resources/${dayName}_test.txt").createNewFile()
        println("$check Test files created")

    }

    doLast {
        val incrementedDay = currentDay + 1
        counterFile.writeText(incrementedDay.toString())
        println("Current counter value: $incrementedDay")
    }
}