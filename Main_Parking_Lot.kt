package parking

import java.util.*
import kotlin.collections.ArrayList

fun main() {
    val scanner = Scanner(System.`in`)
    var exiting = true
    var parking = 0
    var command: String?
    var number: String?
    var colors: String?
    var colour = emptyArray<String?>()
    var spots: BooleanArray = BooleanArray(1, { true })
    var car = emptyArray<String?>()
    loop@ while (exiting) {
        command = scanner.next()
        //if (parking == 0 && command != "create") println("Sorry, parking lot is not created.")
        if (command == "create") {
            parking = scanner.nextInt()
            spots = BooleanArray(parking, { true })
            car = arrayOfNulls<String?>(parking)
            colour = arrayOfNulls<String?>(parking)
            println("Created a parking lot with $parking spots.")
        } else if (command == "park") {
            number = scanner.next()
            colors = scanner.next()
            if (!noCreate(parking)) continue
            for (i in 0 until parking) {
                if (spots[i]) {
                    println("$colors car parked on the spot ${i + 1}.")
                    spots[i] = false
                    car[i] = number
                    colour[i] = colors
                    break
                } else if (i == parking - 1) println("Sorry, parking lot is full.")
            }
        } else if (command == "leave") {
            number = scanner.next()
            if (!noCreate(parking)) {
                continue
            } else {
                if (!spots[number.toInt() - 1]) {
                    spots[number.toInt() - 1] = true
                    car[number.toInt() - 1] = ""
                    colour[number.toInt() - 1] = ""
                    println("Spot $number is free.")
                } else {
                    println("There is no car in the spot $number.")
                }
            }
        } else if (command == "exit") {
            exiting = false
        } else if (command == "status") {
            if (!noCreate(parking)) continue
            var status = 0
            for (i in 0 until parking) {
                if (!spots[i]) {
                    status++
                    println("${i + 1} ${car[i]} ${colour[i]}")
                }
            }
            if (status == 0) println("Parking lot is empty.")
        } else if (command == "reg_by_color") {
            colors = scanner.next()
            if (!noCreate(parking)) {
                continue@loop
            } else {
                var status = 0
                for (i in 0 until parking) {
                    if (colour[i]?.toLowerCase() ?: "" == colors.toLowerCase()) {
                        if (status!=0) print(", ")
                        print("${car[i]}")
                        status++
                    }
                }
                if (status == 0) println("No cars with color $colors were found.")
                else println()
            }
        } else if (command == "spot_by_reg") {
            number = scanner.next()
            if (!noCreate(parking)) {
                continue@loop
            } else {
                var status = 0
                for (i in 0 until parking) {
                    if (car[i]?.toLowerCase() ?: "" == number.toLowerCase()) {
                        if (status!=0) print(", ")
                        print("${i+1}")
                        status++
                        break
                    }
                }
                if (status == 0) println("No cars with registration number $number were found.")
                else println()
            }
        } else if (command == "spot_by_color") {
            colors = scanner.next()
            if (!noCreate(parking)) {
                continue@loop
            } else {
                var status = 0
                for (i in 0 until parking) {
                    if (colour[i]?.toLowerCase() ?: "" == colors.toLowerCase()) {
                        if (status!=0) print(", ")
                        print("${i+1}")
                        status++
                    }
                }
                if (status == 0) println("No cars with color $colors were found.")
                else println()
            }
        } else println("Command not right - $command")
        //println("White car has parked.")
        //println("Yellow car left the parking lot.")
        //println("Green car just parked here.")
    }
}

fun noCreate(parking: Int): Boolean {
    if (parking == 0) {
        println("Sorry, parking lot is not created.")
        return false
    }
    return true
}

