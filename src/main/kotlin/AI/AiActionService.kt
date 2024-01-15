package AI
import entity.*
import service.AbstractRefreshingService
import service.RootService

class AiActionService(private val rootService: RootService) : AbstractRefreshingService() {
        /**
         * Simulates a game move for a given player in the AI analysis.
         *
         * @param indigo - the current [Indigo] object representing the game state.
         * @param coordinate - the [Coordinate] object, representing the game move.
         * @return a new [Indigo] object, representing the game state after the move.
         * @throws Exception if the player's handTile is null.
         *
         */
        fun doMove(indigo: Indigo, coordinate: Coordinate): Indigo {
            val newIndigo = indigo.copyTo()
            val currentPlayer = newIndigo.players[newIndigo.currentPlayerIndex]
            val gameService = rootService.gameService

            // Get the tile from the player's hand
            val tile = currentPlayer.handTile

            if (tile != null) {
                // Place the tile on the game board
                gameService.placeTile(Coordinate(coordinate.row, coordinate.column), tile)

                // Move gems to neighboring coordinates, check for collision, and remove gems
                val neighbors = gameService.getNeighboringCoordinates(Coordinate(coordinate.row, coordinate.column))
                for (i in neighbors.indices) {
                    gameService.moveGems(Coordinate(coordinate.row, coordinate.column), neighbors[i], i)
                }

                // Distribute a new tile
                gameService.distributeNewTile()
            } else {
                throw Exception("Player's handTile is null.")
            }
            return newIndigo
        }

        /**
         * Creates a deep copy of the current Indigo object for analysis.
         *
         * @return a new Indigo object, which is a copy of the current one.
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


