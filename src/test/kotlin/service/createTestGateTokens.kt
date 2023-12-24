package service

import entity.Indigo
import entity.Token

fun createTestGateTokens(game: Indigo, notSharedGates: Boolean): MutableList<Token> {
    val players = game.players
    val playerSize = players.size
    val gateTokens = mutableListOf<Token>()

    if (notSharedGates) {
        for (i in 0 until 6) {
            gateTokens.add(Token(players[i % playerSize].color))
            gateTokens.add(Token(players[i % playerSize].color))
        }
    } else {
        if (playerSize == 4) {
            gateTokens.add(Token(players[0].color))
            gateTokens.add(Token(players[1].color))
            gateTokens.add(Token(players[1].color))
            gateTokens.add(Token(players[2].color))
            gateTokens.add(Token(players[0].color))
            gateTokens.add(Token(players[3].color))
            gateTokens.add(Token(players[3].color))
            gateTokens.add(Token(players[1].color))
            gateTokens.add(Token(players[2].color))
            gateTokens.add(Token(players[0].color))
            gateTokens.add(Token(players[2].color))
            gateTokens.add(Token(players[3].color))
        }
        if (playerSize == 3) {
            gateTokens.add(Token(players[0].color))
            gateTokens.add(Token(players[0].color))
            gateTokens.add(Token(players[0].color))
            gateTokens.add(Token(players[1].color))
            gateTokens.add(Token(players[2].color))
            gateTokens.add(Token(players[2].color))
            gateTokens.add(Token(players[2].color))
            gateTokens.add(Token(players[0].color))
            gateTokens.add(Token(players[1].color))
            gateTokens.add(Token(players[1].color))
            gateTokens.add(Token(players[1].color))
            gateTokens.add(Token(players[2].color))
        }
    }
    return gateTokens
}
