package service.ai

import entity.*

/**
 * The `ServiceAi` class provides AI-related functionalities for the Indigo game.
 */
class SerivceAi {

    /**
     * Companion object containing AI-related functions.
     */
    companion object {

        /**
         * Executes a move in the game by placing a tile on the board at the specified coordinate.
         *
         * @param newIndigoo The current state of the Indigo game.
         * @param coordinate The coordinate where the tile should be placed.
         * @return The updated state of the Indigo game after the move.
         */
        fun doMove(newIndigoo: Indigo, coordinate: Coordinate): Indigo {
            // Create a copy of the current Indigo state to modify
            val newIndigo = newIndigoo.copyTo()

            // Get the tile from the current player's hand or route tiles
            var tile = newIndigo.players[newIndigo.currentPlayerIndex].handTile

            /*
            // Check if the player has a tile in hand
            if (tile != null) {
                // Place the tile on the board at the specified coordinate
                servicee(newIndigo).placeTile(coordinate, tile, true)

                // Move gems, check collisions, distribute new tiles, and change the player
                val neighbors = servicee(newIndigo).getNeighboringCoordinates(coordinate)
                for (i in neighbors.indices) {
                    servicee(newIndigo).moveGems(coordinate, neighbors[i], i)
                }
            } else {
                // If no tile in hand, take the first tile from the route tiles and place it on the board
                tile = newIndigo.routeTiles.removeFirst()
                servicee(newIndigo).placeTile(coordinate, tile, true)
            }
*/

            if (tile == null) {
                //tile = newIndigo.routeTiles.removeFirst()
                servicee(newIndigo).distributeNewTile()
            }

            if (servicee(newIndigo).checkPlacement(coordinate, tile!!, false)) { //when it blocks an exist we rotate it till it s correct
                // Place the tile on the board at the specified coordinate
                servicee(newIndigo).placeTile(coordinate, tile, true)
            } else { // Rotate the tile until it can be placed
                while (!servicee(newIndigo).checkPlacement(coordinate, tile, false)) {
                    rotateTileLeft(tile)
                }
                // Place the tile on the board at the specified coordinate after rotations
                servicee(newIndigo).placeTile(coordinate, tile, true)
            }
             val neighbors = servicee(newIndigo).getNeighboringCoordinates(coordinate)
            /*for (i in neighbors.indices) {
                servicee(newIndigo).moveGems(coordinate, neighbors[i], i)
             }*/


            // Return the updated Indigo state after the move
            return newIndigo
        }


        /**
         * Checks if the game is over by determining if there are any available moves left on the game board.
         *
         * @param state The current state of the Indigo game.
         * @return `true` if there are no available moves left, indicating the game is over; `false` otherwise.
         */
        fun isGameOver(state: Indigo): Boolean {
            // List to store available coordinates for placing tiles
            val availableMoves: MutableList<Coordinate> = mutableListOf()

            // Iterate over the game board and find available moves
            for (row in -4..4) {
                for (col in Integer.max(-4, -row - 4)..Integer.min(4, -row + 4)) {
                    val coordinate = Coordinate(row, col)

                    // Check if placing the tile at the coordinate is a valid move
                    // depth
                    //drtaw stack
                    // postions

                    if (state.gameBoard.gameBoardTiles[coordinate] == null) {
                        availableMoves.add(Coordinate(row, col))
                    }
                }
            }

            // If the list of available moves is empty, the game is over
            return availableMoves.isEmpty()
        }


        private fun rotateTileLeft(tile: Tile) {    // Add the first edge to the end of the list
            //val game = rootService.currentGame
            //checkNotNull(game) { "No game found." }
            tile.edges.addAll(tile.edges.subList(0, 1))
            // Remove the original first edge
            tile.edges.removeAll(tile.edges.subList(0, 1))
        }
    }

}



