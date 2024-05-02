const val MAX_CARDS = 52
const val INIT_CARDS_TABLE = 4
const val DEAL_CARDS_PLAYER = 6
const val PLAYER_TURN = 'P'
const val PC_TURN = 'X'

class IndigoGame(val playFirst: Boolean = false) {
    private val deck: Deck = Deck().also { it.shuffle() }
    var table: List<Card> = listOf()
    var playerHand: List<Card> = listOf()
    var pcHand: List<Card> = listOf()
    var currentPlayer = if (playFirst) PLAYER_TURN else PC_TURN
    var isGameOver = false

    fun dealCards(nbCards: Int = DEAL_CARDS_PLAYER) {
        if (deck.cards.isEmpty() && table.size == MAX_CARDS) {
            printStateTable()
            endGame()
        } else if (pcHand.isEmpty() && playerHand.isEmpty()) {
            playerHand = deck.takeCards(nbCards)
            pcHand = deck.takeCards(nbCards)
        }
    }

    fun playCard(player: Char, index: Int) {
        if (player == PC_TURN) {
            table += pcHand.last().also { pcHand -= it }
        }else if (player == PLAYER_TURN) {
            table += playerHand[index].also { playerHand -= playerHand[index] }
        }
        nextPlayer()
    }

    fun endGame() {
        isGameOver = true
    }

    private fun nextPlayer() {
        currentPlayer = if (currentPlayer == PC_TURN)
            PLAYER_TURN
        else PC_TURN
    }

    fun initTable(nbCards: Int = INIT_CARDS_TABLE) {
        table += deck.takeCards(nbCards)
    }

    fun printPlayerHand() {
        val handString = playerHand.mapIndexed { index, card ->
            "${index + 1})$card"
        }.joinToString(" ")
        println("Cards in hand: $handString")
    }

    fun printStateTable() {
        println("\n${table.size} cards on the table, and the top card is ${table.last()}")
    }
}
