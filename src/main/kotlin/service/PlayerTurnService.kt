package service

import entity.*
import java.lang.Exception

/**
 * Service class for managing player turns and actions.
 * @param rootService The root service providing access to the current game state.
 */
class PlayerTurnService(private val rootService: RootService) : AbstractRefreshingService() {
    /**
     * Places a route tile at the specified coordinate.
     * @param space The coordinate where the tile is to be placed.
     * @param tile The tile to be placed.
     * @throws Exception if the placement is invalid.
     */

    fun placeRouteTile(space: Coordinate, tile: Tile) {
        val currentGame = rootService.currentGame
        // Check if the game has started
        checkNotNull(currentGame) { "The game has not started yet" }
        val firstAppearance = currentGame.copyTo()
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

            val lastGame = rootService.currentGame?.copyTo()
            firstAppearance.nextGameState = lastGame
            lastGame?.previousGameState = firstAppearance
            rootService.currentGame?.nextGameState = lastGame
            rootService.currentGame = rootService.currentGame?.nextGameState
            if (rootService.gameService.endGame()) {
                onAllRefreshables { refreshAfterEndGame() }
            }
        } else {
            throw Exception("Invalid space, choose another space please")
        }
    }


    /**
     * Undoes one game move and returns to the previous game state,
     * it is possible to undo moves until the beginning of the game
     */
    fun undo() {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        if (currentGame.previousGameState != null) {
            //currentGame.nextGameState = currentGame
            rootService.currentGame = currentGame.previousGameState

        } else {
            println("Previous game state doesn't exist, cannot undo the move")
        }
        onAllRefreshables { refreshAfterUndo() }
    }


    /**
     * Redoes one game move and returns to the next game state,
     * it is possible to redo moves from the beginning until the last made move
     */
    fun redo() {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        if (currentGame.nextGameState != null) {
            //currentGame.previousGameState = currentGame
            rootService.currentGame = currentGame.nextGameState
        } else {
            println("Next game state doesn't exist, cannot redo the move")
        }
        onAllRefreshables { refreshAfterRedo() }
    }

    /**
     * Rotates the tile to the left.
     * @param tile The tile to be rotated.
     * @throws IllegalStateException if no game is running
     */
    fun rotateTileLeft(tile: Tile) {    // Add the first edge to the end of the list
        val game = rootService.currentGame
        checkNotNull(game) { "No game found."}

        tile.edges.addAll(tile.edges.subList(0, 1))
        // Remove the original first edge
        tile.edges.removeAll(tile.edges.subList(0, 1))
        onAllRefreshables { refreshAfterLeftRotation(game!!.currentPlayerIndex) }
    }

    /**
     * Rotates the tile to the right.
     * @param tile The tile to be rotated.
     * @throws IllegalStateException if no game is running
     */
    fun rotateTileRight(tile: Tile) {    // Add the last edge to the beginning of the list
        val game = rootService.currentGame
        checkNotNull(game) { "No game found."}

        tile.edges.addAll(0, tile.edges.subList(tile.edges.size - 1, tile.edges.size))
        // Remove the original last edge
        tile.edges.subList(tile.edges.size - 1, tile.edges.size).clear()
        onAllRefreshables { refreshAfterRightRotation(game!!.currentPlayerIndex) }
    }

    /**
     *  the extension function [copyTo] is a function wich create a deep copy of Indigo
     *  with the necessary data
     *
     *  @return Returning a new [Indigo] which are independent of the current game
     */
    private fun Indigo.copyTo(): Indigo {
        val copiedGems = mutableListOf<Gem>()
        for (gem in gems) {
            copiedGems.add(gem)
        }
        val copiedGameBoardTiles = gameBoard.gameBoardTiles.toMutableMap()
        val copiedGateTokens = this.gameBoard.gateTokens.toList()
        val copiedGameBoard = GameBoard()
        copiedGameBoard.gameBoardTiles.putAll(copiedGameBoardTiles)
        copiedGameBoard.gateTokens = copiedGateTokens
        val copiedPlayers = settings.players.map {
            Player(
                it.name,
                it.age,
                it.color,
                it.isAI
            ).apply {
                score = it.score
                gemCounter = it.gemCounter
                // Copy the handTile
                handTile = it.handTile?.copy()
            }
        }.toList()
        val copiedSettings = GameSettings(copiedPlayers)
        val copiedIndigo = Indigo(
            copiedSettings,
            copiedGameBoard,
            this.allTiles,
            copiedGems,
            this.tokens
        )
        copiedIndigo.currentPlayerIndex = this.currentPlayerIndex
        copiedIndigo.nextGameState = this.nextGameState
        copiedIndigo.previousGameState = this.previousGameState
        copiedIndigo.middleTile.gemPosition.clear()
        for (i in 0 until this.middleTile.gemPosition.size) {
            val gem = this.middleTile.gemPosition[i]
            copiedIndigo.middleTile.gemPosition[i] = gem!!
        }
        copiedIndigo.routeTiles = this.routeTiles.toMutableList()
        return copiedIndigo
    }
}

