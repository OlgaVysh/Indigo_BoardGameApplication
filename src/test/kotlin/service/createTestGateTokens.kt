package service

import  entity.Indigo
import entity.Token

/**
 *  the function [createTestGateTokens] is a function a test set of gateTokens.
 *
 *  @param game The indigo game which you want to create gateTokens
 *  @param notSharedGates The notSharedGates is to if the gateTokens have
 *  sharedGates or notSharedGates
 */
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
