package service

import entity.Coordinate
import entity.Indigo
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
    fun makeRandomTurn() {

        // Get the current game state
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)

        // Get the available moves for the current player
        val availableMoves = findAvailableMoves(currentGame)

        // If there are available moves, make a random move
        if (availableMoves.isNotEmpty()) {
            val randomMove = availableMoves.random()
            val (coordinate, tile) = randomMove

            // Place the tile at the random coordinate
            rootService.gameService.placeTile(coordinate, tile)

            // Move gems to neighboring coordinates, check for collision and remove gems
            val neighbors = rootService.gameService.getNeighboringCoordinates(coordinate)
            for (i in neighbors.indices) {
                rootService.gameService.moveGems(coordinate, neighbors[i], i)
            }

            rootService.gameService.distributeNewTile()
            rootService.gameService.changePlayer()
        }
    }

    /**
     * Finds available moves on the game board.
     */
    private fun findAvailableMoves(currentGame: Indigo): List<Pair<Coordinate, Tile>> {
        val availableMoves = mutableListOf<Pair<Coordinate, Tile>>()

        // Iterate over the game board and find available moves
        for (q in -4..4) {
            for (r in Integer.max(-4, -q - 4)..Integer.min(4, -q + 4)) {
                val coordinate = Coordinate(q, r)
                val playerTile = currentGame.players[currentGame.currentPlayerIndex].handTile ?: continue

                // Check if placing the tile at the coordinate is a valid move
                if (rootService.gameService.checkPlacementAI(coordinate, playerTile)) {
                    availableMoves.add(Pair(coordinate, playerTile))
                }
            }
        }

        return availableMoves
    }
}
