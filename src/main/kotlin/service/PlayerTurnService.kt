package service

import entity.*
import java.lang.Exception

/**
 * Service class for managing player turns and actions.
 * @param rootService The root service providing access to the current game state.
 */
class PlayerTurnService(private val rootService: RootService) {
    /**
     * Places a route tile at the specified coordinate.
     * @param space The coordinate where the tile is to be placed.
     * @param tile The tile to be placed.
     * @throws Exception if the placement is invalid.
     */

    fun placeRouteTile(space: Coordinate, tile: Tile) {
        var currentGame = rootService.currentGame

        // Check if the game has started
        checkNotNull(currentGame) { "The game has not started yet" }
        // Check if the tile placement is valid
        if (rootService.gameService.checkPlacement(space, tile)) {
            // Move gems, check collisions, distribute new tiles, and change the player
            val neighbors = rootService.gameService.getNeighboringCoordinates(space)
            for (i in neighbors.indices) {
                rootService.gameService.moveGems(space, neighbors[i], i)
            }
            // change rows with moveGems?
            rootService.gameService.distributeNewTile()
            rootService.gameService.changePlayer()

            rootService.currentGame!!.nextGameState = currentGame
            currentGame.nextGameState!!.previousGameState = currentGame
            rootService.currentGame = currentGame.nextGameState
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

    /**
     * Rotates the tile to the left.
     * @param tile The tile to be rotated.
     */
    fun rotateTileLeft(tile: Tile) {    // Add the first edge to the end of the list
        tile.edges.addAll(tile.edges.subList(0, 1))
        // Remove the original first edge
        tile.edges.removeAll(tile.edges.subList(0, 1))
    }

    /**
     * Rotates the tile to the right.
     * @param tile The tile to be rotated.
     */
    fun rotateTileRight(tile: Tile) {    // Add the last edge to the beginning of the list
        tile.edges.addAll(0, tile.edges.subList(tile.edges.size - 1, tile.edges.size))
        // Remove the original last edge
        tile.edges.subList(tile.edges.size - 1, tile.edges.size).clear()
    }

}