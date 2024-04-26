import Deck.Companion.Suits
import Deck.Companion.ranks

fun main() {
    println(ranks.joinToString(" "))
    println(Suits.entries.map { it.stringCode.toChar() }.joinToString(" "))

    val deck = Deck(Deck.generateFullDeck())
    deck.shuffle()
    println(deck.deck.joinToString(" "))
}


class Deck(var deck: List<Card>) {
    fun shuffle() {
        deck = deck.shuffled()
    }

    companion object {
        val ranks = List(13) {
            when(it) {
                1 -> "A"
                11 -> "J"
                12 -> "Q"
                13 -> "K"
                else -> "$it"
            }
        }
        enum class Suits(val stringCode: Int) {
            DIAMOND(0x2666),
            HEART(0x2665),
            SPADE(0x2660),
            CLUB(0x2663)
        }

        fun generateFullDeck(): List<Card> {
            val deck = Suits.entries.map { suit ->
                ranks.map { Card(it, suit) }
            }.reduce { acc, cards -> acc + cards }
            return deck
        }
    }
    data class Card(val rank: String, val suit: Suits) {
        override fun toString(): String {
            return "$rank${suit.stringCode.toChar()}"
        }
    }
}