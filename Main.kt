import java.io.File
import java.util.*

val scanner = Scanner(System.`in`)

fun main(args: Array<String>) {
    val logo: MutableList<String> = arrayListOf()
    var base: MutableList<String> = arrayListOf()
    var export = ""
    if (args.isNotEmpty())
        for (arg in args.indices)
            if (args[arg].toLowerCase() == "-import") importet(base, args[arg + 1])
            else if (args[arg].toLowerCase() == "-export") export = args[arg + 1]
    do {
        println("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):")
        val command = scanner.nextLine()
        logo.add(command)
        when (command) {
            "add" -> base = define(base, base.size)
            "remove" -> base = removet(base)
            "import" -> base = importet(base)
            "export" -> exportet(base)
            "ask" -> base = check(base)
            "log" -> log(logo)
            "hardest card" -> findError(base)
            "reset stats" -> {
                if (base.isNotEmpty()) for (index in base.indices step 3) base[index + 2] = "0"
                println("Card statistics has been reset.")
                //println(base)
            }
            "list" -> println(base)
        }
    } while (command != "exit")
    println("Bye bye!")
    if (export != "") exportet(base, export)
}

fun findError(base: MutableList<String>) {
    var error = false
    var max = 0
    var card = ""
    var notOne = false
    for (index in base.indices step 3) {
        val errorInt = (base[index + 2]).toInt()
        if (errorInt != 0 && errorInt >= max) {
            if (errorInt == max) {
                card += ", \"${base[index]}\""
                notOne = true
            } else {
                card += "\"${base[index]}\""
                error = true
                max = base[index + 2].toInt()
            }
        }
    }
    if (!error) {
        println("There are no cards with errors.")
    } else {
        println("The hardest card${if (notOne) "s" else ""} is $card. You have ${max} errors answering them.")
    } //         The hardest card is "France"
}

fun log(logo: MutableList<String>) {
    try {
        println("File name:")
        val fileName = scanner.nextLine()
        val file = File(fileName)
        file.writeText("")
        for (index in logo) {
            file.appendText("$index\n") //  ""
        }
        println("The log has been saved.")
    } catch (e: Exception) {
        println("Error write.")
    }
}

fun removet(base: MutableList<String>): MutableList<String> {
    println("The card:")
    var find = false
    val rem = scanner.nextLine()
    for (index in base.indices step 3) {
        if (base[index] == rem) {
            base.removeAt(index + 2)
            base.removeAt(index + 1)
            base.removeAt(index)
            println("The card has been removed.")
            find = true
            break
        }
    }
    if (!find) println("Can't remove \"$rem\": there is no such card.")
    return base
}

fun importet(base: MutableList<String>, path: String = ""): MutableList<String> {
    //val base: MutableList<String> = arrayListOf()
    var loadCard = 0
    val fileName: String
    if (path == "") {
        println("File name:")
        fileName = scanner.nextLine()
    } else fileName = path
    val lines: Any
    try {
        lines = File(fileName).readLines()
    } catch (e: Exception) {
        println("File not found.")
        return base
    }
    var lineNum = 0
    for (line in lines) {
        if (line != "") {
            if (base.size > 2 && lineNum != 2) {
                if (test(base, lineNum, line, false)) {
                    base.removeAt(base.indexOf(line) + 1)
                    base.removeAt(base.indexOf(line))
                }
                //println(temp)
            }
            base.add(line)
            if (lineNum == 0) loadCard++
        }
        lineNum = (lineNum + 1) % 3
    }
    println("$loadCard cards have been loaded.")

    //    throw Exception("File not found.")
    //
    //val lines = File(fileName).readLines()
    if (base.size % 3 != 0) base.remove(base.last())
    if (base.size % 3 != 0) base.remove(base.last())
    //println(base)
    return base
}

fun exportet(base: MutableList<String>, path: String = "") {
    try {
        val fileName: String
        if (path == "") {
            println("File name:")
            fileName = scanner.nextLine()
        } else fileName = path
        val file = File(fileName)
        file.writeText("")
        for (index in base) {
            file.appendText(if (index == base.first()) index else "\n$index") //  ""
        }
        println("${base.size / 3} cards have been saved.")
    } catch (e: Exception) {
        println("Error write.")
    }

}

fun check(base: MutableList<String>): MutableList<String> {
    println("How many times to ask?")
    val num = scanner.nextLine().toInt()
    for (index in 1..num) {
        val def = (Math.random() * (base.size / 3)).toInt()
        val card = base[def * 3]
        val correct = base[def * 3 + 1]
        val error = base[def * 3 + 2].toInt()
        println("Print the definition of \"$card\":")
        val answer = scanner.nextLine()
        var str = "The correct one is \"$correct\""
        if (correct != answer) {
            for (indexin in 0..base.lastIndex step 3) {
                if (answer == base[indexin + 1]) str += ", you've just written the definition of \"${base[indexin]}\""
            }
            println("Wrong answer. $str.")
            base[def * 3 + 2] = (error + 1).toString()
            // base.upd
        } else println("Correct answer.")
    }
    return base
}

fun define(base: MutableList<String>, size: Int): MutableList<String> {
    val card: String
    if (size != 0) {
        println("The card:")
        var num: String = scanner.nextLine()
        if (test(base, 0, num, true)) return base
        base.add(num)
        card = base.last()
        println("The definition of the card #${base.last()}:")
        num = scanner.nextLine()
        if (test(base, 1, num, true)) {
            base.removeAt(base.lastIndex)
            return base
        }
        base.add(num)
    } else {
        println("The card:")
        base.add(scanner.nextLine())
        card = base.last()
        println("The definition of the card #${card}:")
        base.add(scanner.nextLine())
    }
    val correct = base.last()
    base.add("0")
    println("The pair (\"${card}\":\"${correct}\") has been added.")
    return base
}

fun test(base: MutableList<String>, tests: Int, num: String, ask: Boolean): Boolean {
    val post = if (tests == 0) "card" else "definition"
    for (index in base.indices step 3) {
        if (index != base.lastIndex && base[index + tests] == num) {
            if (ask) { // если при вводе с ком. строки
                println("The $post \"${num}\" already exists.") // Try again:")
            }
            return true
        }
    }
    return false
}