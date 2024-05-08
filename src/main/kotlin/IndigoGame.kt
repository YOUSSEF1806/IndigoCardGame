const val INIT_CARDS_TABLE = 4
const val DEAL_CARDS_PLAYER = 6
const val FINAL_EXTRA_POINTS = 3

class IndigoGame {

    private val deck: Deck = Deck().also { it.shuffle() }
    private var playFirst: Boolean = false
    private var table: TableStack = TableStack()
    private var hmPlayer: Player = HMPlayer()
    private var aiPlayer: Player = AIPlayer()
    private var currentPlayer = aiPlayer
    private var lastWin: Player = Player()
    private var quitGame = false
    private var isGameOver = false

    fun initGame() {
        println("Indigo Card Game")
        playFirst = userChoiceFirstPlay()
        currentPlayer = if (playFirst) hmPlayer else aiPlayer
        table.initTableAndPrint(deck.dealLastCards(INIT_CARDS_TABLE))
        dealCards()
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

    fun gameLoop() {
        while (!isGameOver && !quitGame) {
            println(table)
            nextPlay()
            dealCards()
        }
    }

    private fun dealCards() {
        if (hmPlayer.cards.isEmpty() && aiPlayer.cards.isEmpty()) {
            if (deck.cards.isEmpty()) {
                println(table)
                isGameOver = true
                return
            }
            hmPlayer.cards = deck.dealLastCards(DEAL_CARDS_PLAYER)
            aiPlayer.cards = deck.dealLastCards(DEAL_CARDS_PLAYER)
        }
    }

    private fun nextPlay() {
        val cardPlayed = currentPlayer.findCardToPlay(table.cards.lastOrNull())
        if (cardPlayed == null) {
            quitGame = true
            return
        }
        if (table.cards.isNotEmpty() && table.isLastPlayWin(cardPlayed)) {
            playerWinsTableWith(listOf( cardPlayed))
            lastWin = currentPlayer
            table.clearTable()
        } else {
            table.cards += cardPlayed
        }
        nextPlayer()
    }

    private fun playerWinsTableWith(cardPlayed: List<Card> = emptyList()) {
        currentPlayer.winCards(table.cards + cardPlayed)
        printScoreBoard()
    }

    fun finalizeGame() {
        if (isGameOver) {
            endGame()
        }
        println("Game Over")
    }

    private fun endGame() {
        if (table.cards.isNotEmpty()) {
            if (lastWin.name != "") {
                lastWin.winCards(table.cards, false)
            } else {
                if (playFirst) hmPlayer.winCards(table.cards, false)
                else aiPlayer.winCards(table.cards, false)
            }
        }
        when {
            hmPlayer.cardsWon.size > aiPlayer.cardsWon.size -> {
                hmPlayer.score += FINAL_EXTRA_POINTS
            }
            hmPlayer.cardsWon.size < aiPlayer.cardsWon.size -> {
                aiPlayer.score += FINAL_EXTRA_POINTS
            }
            else -> {
                if (playFirst) hmPlayer.score += FINAL_EXTRA_POINTS
                else aiPlayer.score += FINAL_EXTRA_POINTS
            }
        }
        printScoreBoard()
    }

    private fun printScoreBoard() {
        println("Score: Player ${hmPlayer.score} - Computer ${aiPlayer.score}")
        println("Cards: Player ${hmPlayer.cardsWon.size} - Computer ${aiPlayer.cardsWon.size}")
    }

    private fun nextPlayer() {
        currentPlayer = if (currentPlayer == hmPlayer)
            aiPlayer
        else hmPlayer
    }
}
