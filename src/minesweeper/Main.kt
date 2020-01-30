package minesweeper

import java.util.Scanner

import minesweeper.engine.Grid


fun main() {
	val scanner = Scanner(System.`in`)
	val mineField = Grid(9, 9)
	var firstTry = true

	mineField.initGrid()

	print("How many mines do you want on the field? ")
	val nmine = scanner.nextInt()
	println()

	mineField.displayBoard()

	while (true) {
		print("Set/unset mines marks or claim a cell as free: ")
		val y = scanner.nextInt() - 1
		val x = scanner.nextInt() - 1
		val action = scanner.next()
		val cell = mineField.land[mineField.indexOf(x, y)]
		println()

		if (action == "free" && cell.hidden) {
			if (firstTry) {
				mineField.addMines(nmine, cell)
				firstTry = false
			}
			if (cell.isMine()) {
				mineField.showMines()
				mineField.displayBoard()
				println("You stepped on a mine and failed!")
				break
			}
			mineField.discoverCell(cell)
		} else if (action == "mine" && cell.hidden) {
			cell.toggleMark()
		}

		mineField.displayBoard()

		if (!firstTry && mineField.playerWin()) {
			println("Congratulations! You found all mines!")
			break
		}
	}
}
