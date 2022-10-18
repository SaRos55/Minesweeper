package minesweeper

import java.util.*
import kotlin.random.Random

const val SAFE_CELL = '.'
const val MARK = '*'
const val FREE = '/'
const val MINE = 'X'
const val SIZE = 9

val minefield = MutableList(SIZE) { MutableList(SIZE) { false } }
val minefieldNumbers = MutableList(SIZE) { MutableList(SIZE) { 0 } }
val minefieldMarks = MutableList(SIZE) { MutableList(SIZE) { false } }

var endGame = false

fun main() {
    println("How many mines do you want on the field?")
    val mineNumber = readln().toInt()
    var i = 0
    while (i < mineNumber) {
        val random = Random.nextInt(SIZE * SIZE)
        val x = random / SIZE
        val y = random % SIZE
        if (!minefield[x][y]) {
            i++
            minefield[x][y] = true
        }
    }

    while (!endGame) {
        printMinefield()
        println("Set/unset mine marks or claim a cell as free:")
        val scanner = Scanner(System.`in`)
        val inputY = scanner.nextInt() - 1
        val inputX = scanner.nextInt() - 1
        val inputType = scanner.next() ?: ""
        if (inputType == "free") {
            if (minefield[inputX][inputY]) {
                printMinefield(true)
                println("You stepped on a mine and failed!")
                endGame = true
            } else setFree(inputX, inputY)
        } else if (inputType == "mine") {
            if (minefieldNumbers[inputX][inputY] == 0) setMark(inputX, inputY)
        }
        if (minefield == minefieldMarks) {
            printMinefield()
            println("Congratulations! You found all the mines!")
            endGame = true
        }
    }
}

fun setFree(inputX: Int, inputY: Int) {
    if (minefieldNumbers[inputX][inputY] != 0) return
    var mineCount = 0
    val x1 = if (inputX - 1 < 0) 0 else inputX - 1
    val x2 = if (inputX + 1 > SIZE - 1) inputX else inputX + 1
    val y1 = if (inputY - 1 < 0) 0 else inputY - 1
    val y2 = if (inputY + 1 > SIZE - 1) inputY else inputY + 1
    for (x in x1..x2) {
        for (y in y1..y2) {
            if (minefield[x][y]) mineCount++
        }
    }
    minefieldMarks[inputX][inputY] = false
    if (mineCount > 0) {
        minefieldNumbers[inputX][inputY] = mineCount
        return
    }
    minefieldNumbers[inputX][inputY] = -1
    for (x in x1..x2) {
        for (y in y1..y2) {
            if (!(x == inputX && y == inputY)) setFree(x, y)
        }
    }
}

fun setMark(inputX: Int, inputY: Int) {
    minefieldMarks[inputX][inputY] = !minefieldMarks[inputX][inputY]
}

fun printMinefield(withMine: Boolean = false) {
    println()
    print(" |")
    repeat(SIZE) {
        print("${it + 1}")
    }
    println('|')
    println("—│—————————│")
    for (i in minefield.indices) {
        print("${i + 1}|")
        for (j in minefield[i].indices) {
            print(
                when {
                    withMine && minefield[i][j] -> MINE
                    minefieldMarks[i][j] -> MARK
                    minefieldNumbers[i][j] == 0 -> SAFE_CELL
                    minefieldNumbers[i][j] == -1 -> FREE
                    else -> minefieldNumbers[i][j]
                }
            )
        }
        println('|')
    }
    println("—│—————————│")
}