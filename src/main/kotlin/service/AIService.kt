package service

import entity.Coordinate
import entity.Tile

/**
 * The class [AIService] provides functionality for the AI player
 *
 * @property rootService The [RootService]
 */
class AIService(val rootService: RootService) {

    /**
     * Makes a random turn for the AI player.
     */
    fun randomTurn() {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame) { "The game has not started yet" }

        // Assuming the current player is an AI player
        val currentPlayer = currentGame.players[currentGame.currentPlayerIndex]
        if (currentPlayer.isAI) {
            // Replace this logic with the actual AI decision-making process
            val randomSpace = getRandomEmptySpace()
            val randomTile = getRandomTileForAI()

            // Place the tile randomly
            rootService.gameService.checkPlacement(randomSpace, randomTile)
           // rootService.gameService.moveGems()
            //rootService.gameService.checkCollision()
            rootService.gameService.distributeNewTile()
            rootService.gameService.changePlayer()
        }
    }

    /**
     * @return A random [Tile] for the AI player.
     */
    private fun getRandomTileForAI(): Tile {
        val allTiles = rootService.currentGame?.allTiles
        return allTiles!!.random()
    }


    /**
     * @return A random empty [Coordinate] on the game board.
     */
    private fun getRandomEmptySpace(): Coordinate {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame) { "The game has not started yet" }

        // Get a list of available spaces on the board
        val availableSpaces = currentGame.gameBoard.gameBoardTiles.filter { it.value == null }.keys.toList()

        if (availableSpaces.isNotEmpty()) {
            // Choose a random space
            return availableSpaces.random()
        } else {
            // If no available spaces, throw an exception or handle accordingly
            throw IllegalStateException("No available spaces on the game board.")
        }
    }


    fun calculatedTurn() {}

    private fun calculateTurn() {}

    private fun makeTurn(position: Coordinate) {}


}
