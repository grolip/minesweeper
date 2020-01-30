package minesweeper.engine

import kotlin.random.Random


class Grid(val ncol: Int, val nline: Int) {
	var land = mutableListOf<Cell>()
	var nmine = 0

	fun initGrid() {
		this.land.clear()

		for (x in 0 until this.ncol)
			for (y in 0 until this.nline)
				this.land.add(Cell(x, y)) 
	}

	fun indexOf(x: Int, y: Int): Int {
		for (i in 0 until this.ncol * this.nline) {
			if (this.land[i].x == x && this.land[i].y == y)
				return i
		}
		return -1
	}

	fun getAdjacentCells(begin: Cell): MutableList<Cell> {
		val result = mutableListOf<Cell>()
		val indexes = listOf<Int>(
			this.indexOf(begin.x - 1, begin.y),
			this.indexOf(begin.x, begin.y - 1),
			this.indexOf(begin.x - 1, begin.y - 1),
			this.indexOf(begin.x + 1, begin.y),
			this.indexOf(begin.x, begin.y + 1),
			this.indexOf(begin.x + 1, begin.y + 1),
			this.indexOf(begin.x - 1, begin.y + 1),
			this.indexOf(begin.x + 1, begin.y - 1))

		for (i in indexes) {
			if (i != -1)
				result.add(this.land[i])
		}
		return result
	}

	fun discoverAdjacentCells(begin: Cell) {
		this.getAdjacentCells(begin).forEach {
			if (it.hidden && !it.isMine()) {
				it.hidden = false
				// Si case vide sans mine aux alentours
				if (it.mineCounter == 0)
					this.discoverAdjacentCells(it)
			}
		}
	}

	fun countMines(begin: Cell) {
		this.getAdjacentCells(begin).forEach { 
			if (!it.isMine())
				it.mineCounter += 1 
		}
	}

	fun addMines(nmine: Int, begin: Cell) {
		val ncell = this.ncol * this.nline
		this.nmine = nmine

		repeat(nmine) {
			while (true) {
				val cell = this.land[Random.nextInt(0, ncell)]
				// Pas de mine sur la premier case
				if (begin == cell)
					continue
				// Si la case n'est pas déjà une mine
				if (!cell.isMine()) {
					cell.becomeMine()
					this.countMines(cell)
					break
				}
			}
		}
	}

	fun showMines() {
		this.land.forEach {
			if (it.hidden && it.isMine())
				it.hidden = false
		}
	}

	fun discoverCell(begin: Cell) {
		if (!begin.isMine()) {
			begin.hidden = false
			this.discoverAdjacentCells(begin)
		}
	}

	fun playerWin(): Boolean {
		var nmineMarked = 0
		var notherMarked = 0
		var nhidden = 0

		this.land.forEach {
			if (it.marked) {
				if (it.isMine()) nmineMarked++ else notherMarked++
			} else if (it.hidden && !it.isMine()) {
				nhidden++
			}
		}
		return notherMarked == 0 && (nhidden == 0 || nmineMarked == this.nmine)
	}

	fun displayHeaderBoard() {
		println(" |" + (1.rangeTo(this.ncol)).joinToString("") + "|")
		print("-|")
		for (i in 1..this.ncol)
			print("-")
		println("|")
	}

	fun displayFooterBoard() {
		print("-|")
		for (i in 1..this.nline)
			print("-")
		println("|")
	}

	fun displayBoard() {
		this.displayHeaderBoard()

		for (i in 0 until this.nline) {
			print((i + 1).toString() + '|')
			for (j in 0 until this.ncol)
				print(this.land[this.indexOf(i, j)].toDisplay())
			println('|')
		}
		this.displayFooterBoard()
	}
}
