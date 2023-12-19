package service

import entity.*
import java.lang.Exception

class PlayerTurnService(private val rootService: RootService) {

    fun placeRouteTile(space: Coordinate, tile: Tile) {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame) { "The game has not started yet" }

        if (rootService.gameService.checkPlacement(space, tile)) {
            rootService.gameService.moveGems()
            rootService.gameService.checkCollision()
            rootService.gameService.distributeNewTile()
            rootService.gameService.changePlayer()
            //in progress
        } else {
            throw Exception("Invalid space, choose another space please")
        }
    }


    /**
     * Undoes one game move and returns to the previous game state,
     * it is possible to undo moves until the beginning of the game
     */
    fun undo() {
        var currentGame = rootService.currentGame
        checkNotNull(currentGame)
        if (currentGame.previousGameState != null) {
            currentGame.nextGameState = currentGame
            currentGame = currentGame.previousGameState

        } else {
            println("Previous game state doesn't exist, cannot undo the move")
        }
    }


    /**
     * Redoes one game move and returns to the next game state,
     * it is possible to redo moves from the beginning until the last made move
     */
    fun redo() {
        var currentGame = rootService.currentGame
        checkNotNull(currentGame)
        if (currentGame.nextGameState != null) {
            currentGame.previousGameState = currentGame
            currentGame = currentGame.nextGameState
        } else {
            println("Next game state doesn't exist, cannot redo the move")
        }
    }

    fun rotateTileLeft(tile: Tile): Tile {
        tile.edges.addAll(tile.edges.subList(0, 1))
        tile.edges.removeAll(tile.edges.subList(0, 1))
        return tile
    }

    fun rotateTileRight(tile: Tile): Tile {
        tile.edges.addAll(0, tile.edges.subList(tile.edges.size - 1, tile.edges.size))
        tile.edges.subList(tile.edges.size - 1, tile.edges.size).clear()
        return tile
    }

}