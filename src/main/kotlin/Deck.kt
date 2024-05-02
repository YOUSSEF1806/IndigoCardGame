
private val ranks = List(13) {
    when (it) {
        0 -> "A"
        10 -> "J"
        11 -> "Q"
        12 -> "K"
        else -> "${it+1}"
    }
}

enum class Suits(val stringCode: Int) {
    DIAMOND(0x2666),
    HEART(0x2665),
    SPADE(0x2660),
    CLUB(0x2663)
}

class Deck(var cards: List<Card> = generateFullDeck()) {
    fun shuffle() = cards.shuffled().also { cards = it }

    fun takeCards(nbCards: Int): List<Card> {
        cards.take(nbCards).run {
            cards = cards - this.toSet()
            return this
        }
    }
    companion object {
        fun generateFullDeck(): List<Card> {
            return Suits.values().flatMap { suit ->
                ranks.map { Card(it, suit) }
            }
        }
    }
}

data class Card(val rank: String, val suit: Suits) {
    override fun toString(): String {
        return "$rank${suit.stringCode.toChar()}"
    }
}
