open class Player(
    val name: String = "",
    var cards: List<Card> = listOf(),
    var score: Int = 0,
    var cardsWon: List<Card> = listOf()
) {
    open fun playCard(): Card? {
        return when (this) {
            is AIPlayer -> this.playCard()
            else -> (this as HMPlayer).playCard()
        }
    }

    fun winCards(listCards: List<Card>, extraPrompt: Boolean = true) {
        cardsWon += listCards
        if (extraPrompt) println("$name wins cards")
        updateScore()
    }

    private fun updateScore() {
        score = cardsWon.sumOf { it.scoreValue }
    }
}

class AIPlayer(name: String = "Computer") : Player(name) {
    override fun playCard(): Card = cards.last().also {
        println("Computer plays $it")
        cards -= it
    }
}

class HMPlayer(name: String = "Player") : Player(name) {

    override fun playCard(): Card? {
        val indexInput = userIndexInput(cards.size)
        return if (indexInput == 0)
            null
        else cards[indexInput-1].also { cards -= cards[indexInput-1] }
    }

    private fun userIndexInput(lastIndex: Int): Int {
        println(this.toString())
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

    override fun toString(): String {
        val handString = cards.mapIndexed { index, card ->
            "${index + 1})$card"
        }.joinToString(" ")
        return "Cards in hand: $handString"
    }
}