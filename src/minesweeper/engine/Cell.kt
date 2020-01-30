package minesweeper.engine


class Cell(val x: Int, val y: Int) {
	val emptySign = '/'
	val mineSign = 'X'
	val hiddenSign = '.'
	val markSign = '*'

	var value = emptySign
	var mineCounter = 0
	var hidden = true
	var marked = false

	fun isMine(): Boolean {
		return this.value == this.mineSign
	}

	fun isEmpty(): Boolean {
		return this.value == this.emptySign
	}

	fun becomeMine() {
		this.value = this.mineSign
	}

	fun toggleMark() {
		this.marked = !this.marked
	}

	fun discover() {
		this.hidden = false
	}

	fun toDisplay(): Char {
		if (this.hidden && this.marked)
			return this.markSign
		else if (this.hidden)
			return this.hiddenSign
		else if (this.mineCounter > 0)
			return this.mineCounter.toString()[0]
		return this.value
	}
}
