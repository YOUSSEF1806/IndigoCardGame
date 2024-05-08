open class Player(
    val name: String = "",
    var cards: List<Card> = listOf(),
    var score: Int = 0,
    var cardsWon: List<Card> = listOf()
) {
    open fun findCardToPlay(cardTable: Card?): Card? {
        return when (this) {
            is AIPlayer -> this.findCardToPlay(cardTable)
            else -> (this as HMPlayer).findCardToPlay()
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
    override fun findCardToPlay(cardTable: Card?): Card =
        processCardsToPlay(cardTable).also {
            println(cards.joinToString(" "))
            println("Computer plays $it")
            cards -= it
        }

    private fun processCardsToPlay(card: Card? = null): Card {
        var sameSuitCards = cards.getSameSuitCards()
        var sameRankCards = cards.getSameRankCards()
        when {
            cards.size == 1 -> return cards.first()
            card == null -> return bestCard(cards, sameSuitCards, sameRankCards)
            else -> {
                val candidateCards = getCandidateCards(card)
                when {
                    candidateCards.isEmpty() -> return bestCard(cards, sameSuitCards, sameRankCards)
                    candidateCards.size == 1 -> return candidateCards.first()
                    else -> {
                        sameSuitCards = candidateCards.getSameSuitCards()
                        sameRankCards = candidateCards.getSameRankCards()
                        return bestCard(candidateCards, sameSuitCards, sameRankCards)
                    }
                }
            }
        }
    }

    private fun bestCard(baseCards: List<Card>, sameSuitCards: List<Card>, sameRankCards: List<Card>): Card {
        if (sameSuitCards.isEmpty() && sameRankCards.isEmpty()) {
            return baseCards.random()
        }
        if (sameSuitCards.size >= sameRankCards.size)
            return sameSuitCards.minBy { it.scoreValue }
        return sameRankCards.random()
    }

    private fun getCandidateCards(card: Card): List<Card> = cards.filter { it.isWinnerCard(card) }
}

class HMPlayer(name: String = "Player") : Player(name) {

    fun findCardToPlay(): Card? {
        val indexInput = userIndexInput(cards.size)
        return if (indexInput == 0)
            null
        else cards[indexInput - 1].also { cards -= cards[indexInput - 1] }
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