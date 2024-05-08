val ranks = List(13) {
    when (it) {
        0 -> "A"
        10 -> "J"
        11 -> "Q"
        12 -> "K"
        else -> "${it + 1}"
    }
}

enum class Suits(val stringCode: Int) {
    DIAMOND(0x2666),
    HEART(0x2665),
    SPADE(0x2660),
    CLUB(0x2663)
}

open class StackCards(var cards: List<Card>) {
    fun dealLastCards(nbCards: Int): List<Card> {
        cards.takeLast(nbCards).run {
            cards = cards - this.toSet()
            return this
        }
    }
}

class Deck(cards: List<Card> = generateFullDeck()) : StackCards(cards) {
    fun shuffle() = cards.shuffled().also { cards = it }

    companion object {
        fun generateFullDeck(): List<Card> {
            return Suits.values().flatMap { suit ->
                ranks.map { Card(it, suit) }
            }
        }
    }
}

class TableStack(cards: List<Card> = listOf()) : StackCards(cards) {
    fun isLastPlayWin(cardPlayed: Card): Boolean = cards.last().isWinnerCard(cardPlayed)

    fun initTableAndPrint(listCards: List<Card>) {
        cards = listCards
        println("Initial cards on the table: ${cards.joinToString(" ")}")
    }

    fun clearTable() {
        cards = listOf()
    }

    override fun toString(): String {
        return if (cards.isNotEmpty())
            "\n${cards.size} cards on the table, and the top card is ${cards.last()}"
        else
            "\nNo cards on the table"
    }
}

data class Card(val rank: String, val suit: Suits) {
    val scoreValue = when (rank) {
        in listOf("A", "10", "J", "Q", "K") -> 1
        else -> 0
    }

    override fun toString(): String = "$rank${suit.stringCode.toChar()}"

    fun isWinnerCard(card: Card): Boolean = rank == card.rank || suit == card.suit
}


fun List<Card>.getSameSuitCards(): List<Card> =
    Suits.values().map { suit ->
        this.filter { it.suit == suit }
    }.maxByOrNull { it.size } ?: emptyList()

fun List<Card>.getSameRankCards(): List<Card> =
    ranks.map { rank ->
        this.filter { it.rank == rank }
    }.maxByOrNull { it.size } ?: emptyList()
