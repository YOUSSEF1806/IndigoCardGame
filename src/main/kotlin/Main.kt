fun main() {
    println("Indigo Card Game")
    val game = IndigoGame(playFirst = userChoiceFirstPlay())

    game.initTable()
    println("Initial cards on the table: ${game.table.joinToString(" ")}")
    game.dealCards()

    while (!game.isGameOver) {
        game.printStateTable()
        if (game.currentPlayer == PC_TURN) {
            println("Computer plays ${game.pcHand.last()}")
            game.playCard(PC_TURN, game.pcHand.lastIndex)
        } else {
            game.printPlayerHand()
            val userIndex = userIndexInput(game.playerHand.size)
            if (userIndex > 0) {
                game.playCard(PLAYER_TURN, userIndex - 1)
            } else if (userIndex == 0) {
                game.endGame()
            }
        }
        game.dealCards()
    }
    println("Game Over")
}

private fun userChoiceFirstPlay(): Boolean {
    while (true) {
        println("Play first?")
        val userInput = readln().trim()
        if (userInput.equals("yes", true)) {
            return true
        } else if (userInput.equals("no", true)) {
            return false
        }
    }
}

private fun userIndexInput(lastIndex: Int): Int {
    while (true) {
        println("Choose a card to play (1-$lastIndex):")
        val userInput = readln().trim()
        if (userInput.equals("exit", true)) {
            return 0
        } else {
            try {
                val index = userInput.toInt()
                if (index in (1..lastIndex)) {
                    return index
                }
            } catch (_: NumberFormatException) {
            }
        }
    }
}
