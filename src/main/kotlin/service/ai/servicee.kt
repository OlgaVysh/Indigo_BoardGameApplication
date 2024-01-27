package service.ai

import entity.Indigo
import entity.*
import kotlin.math.abs
/**
 * Service class responsible for managing game-related logic.
 *
 * @property currentGame The current game instance of type Indigo.
 */
class servicee (var currentGame: Indigo) {

    /**
     * Checks whether placing a tile at a specified coordinate is valid.
     *
     * @param space The coordinate where the tile is intended to be placed.
     * @param tile The tile to be placed.
     * @param isAiCalled Indicates whether the check is called by an AI component.
     * @return True if the placement is valid, false otherwise.
     */
    fun checkPlacement(space: Coordinate, tile: Tile, isAiCalled: Boolean = false): Boolean {
        //val currentGame = rootService.currentGame
        //checkNotNull(currentGame)
        if (space == Coordinate(0, 0)) {
            //if (!isAiCalled) //onAllRefreshables { refreshAfterCheckPlacement() }
            return false
        }
        // Check if the space is occupied
        if (currentGame.gameBoard.gameBoardTiles[space] != null) {
            return false
        }
        // Check if the space has an exit
        return if (!coordinateHasExit(space)) {
            true
        } else {
            // Check if the tile blocks an exit
            return if (!tileBlocksExit(space, tile)) {
              true
            } else {
                if (!isAiCalled) {
                    return false
                } else return true
            }
        }
    }

    /*
    //for AI returning false instead of throwing exception
    fun checkPlacementAI(space: Coordinate, tile: Tile): Boolean {
      //  val currentGame = rootService.currentGame
        checkNotNull(currentGame)

        if (space == Coordinate(0, 0) || currentGame.gameBoard.gameBoardTiles[space] != null) {
            // Return false when the space is occupied or is (0, 0)
            return false
        }

        // Check if the space has an exit
        if (!coordinateHasExit(space)) {
            placeTile(space, tile)
            return true
        }

        // Check if the tile blocks an exit
        if (!tileBlocksExit(space, tile)) {
            placeTile(space, tile)
            return true
        }

        // Return false when the tile blocks an exit
        return false
    }*/

    /**
     * Checks if the given coordinate has an exit.
     * @param space The coordinate to check.
     * @return True if the coordinate has an exit, false otherwise.
     */
    private fun coordinateHasExit(space: Coordinate): Boolean {
        // List of  gates with exits
        val exits = listOf(
            Coordinate(1, -4),
            Coordinate(2, -4),
            Coordinate(3, -4),
            Coordinate(4, -3),
            Coordinate(4, -2),
            Coordinate(4, -1),
            Coordinate(3, 1),
            Coordinate(2, 2),
            Coordinate(1, 3),
            Coordinate(-1, 4),
            Coordinate(-2, 4),
            Coordinate(-3, 4),
            Coordinate(-4, 3),
            Coordinate(-4, 2),
            Coordinate(-4, 1),
            Coordinate(-3, -1),
            Coordinate(-2, -2),
            Coordinate(-1, -3)
        )
        return exits.contains(space)
    }

    /**
     * Places a tile at the specified coordinate.
     * @param space The coordinate where the tile is to be placed.
     * @param tile The tile to be placed.
     * @param isAiCalled (optional) [Boolean] to prevent refreshes when simulating moves for the AI, defaults false
     */
    fun placeTile(space: Coordinate, tile: Tile, isAiCalled: Boolean = false) {
        //val currentGame = rootService.currentGame
        //checkNotNull(currentGame)
        currentGame.gameBoard.gameBoardTiles[space] = tile
        // if (!isAiCalled) onAllRefreshables { refreshAfterPlaceTile(space, tile) }
    }

    /**
     * Checks if placing a tile at the specified coordinate blocks an exit.
     * @param space The coordinate where the tile is to be placed.
     * @param tile The tile to be placed.
     * @return True if placement blocks an exit, false otherwise.
     */
    private fun tileBlocksExit(space: Coordinate, tile: Tile): Boolean {
        // val currentGame = rootService.currentGame
        // checkNotNull(currentGame)
        // Define coordinates for each gate
        val gate1 = listOf(Coordinate(-4, 1), Coordinate(-4, 2), Coordinate(-4, 3))
        val gate2 = listOf(Coordinate(-3, 4), Coordinate(-2, 4), Coordinate(-1, 4))
        val gate3 = listOf(Coordinate(1, 3), Coordinate(2, 2), Coordinate(3, 1))
        val gate4 = listOf(Coordinate(4, -1), Coordinate(4, -2), Coordinate(4, -3))
        val gate5 = listOf(Coordinate(3, -4), Coordinate(2, -4), Coordinate(1, -4))
        val gate6 = listOf(Coordinate(-3, -1), Coordinate(-2, -2), Coordinate(-1, -3))
        val gates = listOf(gate1, gate2, gate3, gate4, gate5, gate6)

        var position1 = 0
        var position2 = 1
        // Check which gate the space belongs to
        for (i in gates.indices) {
            if (gates[i].contains(space)) {
                val edge1 = tile.edges[position1]
                val edge2 = getAnotherEdge(edge1, tile)
                if (edge2 == position2) {
                    return true
                }
            }
            position1 += 1
            position2 += 1
            position1 %= 6
            position2 %= 6
        }

        return false
    }

    /**
     * Gets the index of the second edge in the tile's edges list.
     * @param edge1 The first edge to find the index for.
     * @param tile The tile containing the edges.
     * @return The index of the second edge in the tile's edges list.
     */
    private fun getAnotherEdge(edge1: Edge, tile: Tile): Int {

        val paths = tile.paths
        val edges = tile.edges

        var secondEdge: Edge? = null
        for (path in paths) {
            if (path.first == edge1) secondEdge = path.second
            if (path.second == edge1) secondEdge = path.first
        }
        return edges.indexOf(secondEdge)
    }

    /**
     * Checks for collisions between gems at the beginning and end of paths on the given tile.
     *
     * This function examines each path on the provided tile to determine if gems are present at both
     * the beginning and end of the path. If such a collision is detected, the gems are considered
     * to be colliding, and the collision is resolved by removing the gems from their positions.
     *
     * @param tile The tile to check for gem collisions.
     * @return `true` if a collision is detected and resolved, `false` otherwise.
     */
    fun checkCollision(tile: Tile): Boolean {//done
        for (path in tile.paths) {
            val gemAtBeginning = tile.gemEndPosition[path.first.ordinal]
            val gemAtEnd = tile.gemEndPosition[path.second.ordinal]
            //Checks if the beginning and the end of the path gave gems
            if (gemAtBeginning != null && gemAtEnd != null && gemAtBeginning != gemAtEnd) {
                //Two gems are colliding
                tile.gemEndPosition.remove(path.first.ordinal)
                tile.gemEndPosition.remove(path.second.ordinal)
                //onAllRefreshables { refreshAfterCollision(gemAtEnd, gemAtBeginning) }
                return true
            }
        }
        return false
    }

    /**
     * Removes gems from the specified tile and updates scores based on the gate coordinates.
     * @param tile The tile containing the gems.
     * @param coordinate The coordinate of the tile.
     */

    fun removeGemsReachedGate(tile: Tile, coordinate: Coordinate) {//done
        //val currentGame = rootService.currentGame
       // checkNotNull(currentGame)
        val players = currentGame.players

        val gateTokens = currentGame.gameBoard.gateTokens
        val gate1 = listOf(Coordinate(-4, 1), Coordinate(-4, 2), Coordinate(-4, 3))
        val gate2 = listOf(Coordinate(-3, 4), Coordinate(-2, 4), Coordinate(-1, 4))
        val gate3 = listOf(Coordinate(1, 3), Coordinate(2, 2), Coordinate(3, 1))
        val gate4 = listOf(Coordinate(4, -1), Coordinate(4, -2), Coordinate(4, -3))
        val gate5 = listOf(Coordinate(1, -4), Coordinate(2, -4), Coordinate(3, -4))
        val gate6 = listOf(Coordinate(-1, -3), Coordinate(-2, -2), Coordinate(-3, -1))

        val gatesListe = mutableListOf(gate1, gate2, gate3, gate4, gate5, gate6)
        for (i in 0 until 6) {
            if (gatesListe[i].contains(coordinate)) {
                //check existence of two gems not of the same path of tile, but on the two edges beyond the gate.
                val gem1 = tile.gemEndPosition[(0 + i) % 6]
                val gem2 = tile.gemEndPosition[(1 + i) % 6]
                val gems = mutableListOf<Gem>()
                if (gem1 != null) {
                    gems.add(gem1)
                }
                if (gem2 != null) {
                    gems.add(gem2)
                }
                for (gem in gems) {
                    if (gateTokens[(i * 2)].color == gateTokens[(i * 2) + 1].color) {
                        for (player in players) {
                            if (player.color == gateTokens[(i * 2)].color) {
                                assignGem(gem, player)
                                if (gem == gem2)
                                    tile.gemEndPosition.remove((1 + i) % 6)
                                if (gem == gem1)
                                    tile.gemEndPosition.remove((0 + i) % 6)
                            }
                        }

                    } else {
                        for (player in players) {
                            if (player.color == gateTokens[(i * 2)].color) {
                                assignGem(gem, player)
                            }
                            if (player.color == gateTokens[(i * 2) + 1].color) {
                                assignGem(gem, player)
                            }
                            if (gem == gem2)
                                tile.gemEndPosition.remove((1 + i) % 6)
                            if (gem == gem1)
                                tile.gemEndPosition.remove((0 + i) % 6)
                        }
                    }
                   // onAllRefreshables { refreshAfterRemoveGems(gem) }
                    println(gem.gemColor)
                    val removedElement = currentGame.gems.find { it.gemColor == gem.gemColor }
                    currentGame.gems.remove(removedElement)
                }
            }
        }
    }


    /**
     * function to assign a [Gem] to a given [Player]
     * @param gem [Gem] to be assigned
     * @param player [Player] to receive the [Gem]
     */
    private fun assignGem(gem: Gem, player: Player) {
        player.score += gem.gemColor.ordinal + 1
        player.collectedGems.add(gem)
    }

    /**
     * Changes the current player to the next player in the list.
     */
    /* fun changePlayer() {
        //val currentGame = rootService.currentGame
        //checkNotNull(currentGame)

        val playerSize = currentGame.players.size
        val currentPlayerIndex = currentGame.currentPlayerIndex

        currentGame.currentPlayerIndex = (currentPlayerIndex + 1) % playerSize

        // It's AI's turn
        val connectionState = rootService.networkService.connectionState
        if(connectionState==ConnectionState.DISCONNECTED) {
            //Xue Code
            val currentPlayer = currentGame.players[currentGame.currentPlayerIndex]
            if (currentPlayer.isAI) {
                when (currentPlayer) {
                    is CPUPlayer -> {
                        rootService.aiActionService.AiMove(currentPlayer.difficulty)
                    }
                }
            }
        }

    }*/

    /**
     * Moves gems from one tile to another based on the specified edge indices.
     * @param currentCoordinate The tile coordinate  to which gems are moved.
     * @param neighborCoordinate The tile coordinate from which gems are moved.
     * @param currentGemPosition is the Position of the current tile which is used to check for collision
     * @param isAiCalled (optional) [Boolean] to prevent refreshes when simulating moves for the AI, defaults false
     * if both are on the Edge

     */
    fun moveGems(
        currentCoordinate: Coordinate, neighborCoordinate: Coordinate, currentGemPosition: Int,
        isAiCalled: Boolean = false
    ) {
        //val currentGame = rootService.currentGame
        //checkNotNull(currentGame)
        val middleTile = currentGame.middleTile
        val currentTile = currentGame.gameBoard.gameBoardTiles[currentCoordinate]
        val neighbourTile = currentGame.gameBoard.gameBoardTiles[neighborCoordinate]
        var neighbourStart = (currentGemPosition + 3) % 6
        val neighborCoordinates = getNeighboringCoordinates(currentCoordinate)
        if (currentTile == null) {
            return
        }

        // if in the middle are no more Gems
        if (neighborCoordinate.row == 0 && neighborCoordinate.column == 0) {
            val amountOfGems = middleTile.gemPosition.size
            if (amountOfGems <= 0) {
                return
            }
            if(neighbourStart == 5) neighbourStart = 1
            else {
                neighbourStart++
            }
            println("Neighbour Start before change$neighbourStart")
            val currentTileGem = currentTile.gemEndPosition[currentGemPosition]
            if (middleTile.gemPosition[neighbourStart] == null) {
                neighbourStart = (neighbourStart + 1) % 6
                if (neighbourStart == 0 && amountOfGems > 1) neighbourStart = (neighbourStart - 2 + 6) % 6
                if(middleTile.gemPosition[neighbourStart] == null){
                    neighbourStart = (neighbourStart - 2 + 6) % 6
                }
            }
            if (neighbourStart == 0 && amountOfGems > 1) {
                neighbourStart = (neighbourStart -1+6) % 6
                if (middleTile.gemPosition[neighbourStart] == null)neighbourStart = (neighbourStart +2) % 6
            }
            if(middleTile.gemPosition[neighbourStart]==null && amountOfGems == 2){
                for ((key) in middleTile.gemPosition){
                    if(key!=0){
                        neighbourStart = key
                    }
                }
            }
            if (amountOfGems == 1) neighbourStart = 0
            if (currentTileGem != null) {

                val removedElement =
                    currentGame.gems.find { it.gemColor == middleTile.gemPosition[neighbourStart]!!.gemColor }
                currentGame.gems.remove(removedElement)
                val removedGem =
                    currentGame.gems.find { it.gemColor == currentTile.gemEndPosition[currentGemPosition]!!.gemColor }
                currentGame.gems.remove(removedGem)
                middleTile.gemPosition.remove(amountOfGems - 1)
                currentTile.gemEndPosition.remove(currentGemPosition)
                return
            }
            val middleTileGem = middleTile.gemPosition[neighbourStart]
            val lastGemPosition = getAnotherEdge(currentTile.edges[currentGemPosition], currentTile)
            middleTile.gemPosition.remove(neighbourStart)
            if (currentTile.gemEndPosition[lastGemPosition] != null) {

                val removedElement = currentGame.gems.find { it.gemColor == middleTileGem!!.gemColor }
                currentGame.gems.remove(removedElement)
                val removedGem =
                    currentGame.gems.find { it.gemColor == currentTile.gemEndPosition[lastGemPosition]!!.gemColor }
                currentGame.gems.remove(removedGem)
                currentTile.gemEndPosition.remove(lastGemPosition)
                return
            }
            println("Gem Postion$neighbourStart")
            println("middleTileGem ${middleTileGem?.gemColor}")
            currentTile.gemEndPosition[lastGemPosition] = middleTileGem!!
            println(currentCoordinate.toString())
            println(lastGemPosition)

            moveGems(neighborCoordinates[lastGemPosition], currentCoordinate, ((lastGemPosition + 3) % 6))
        }
        if (neighbourTile == null) {
            return
        }

        val tileGems = currentTile.gemEndPosition
        val neighbourGems = neighbourTile.gemEndPosition
        val neighborEdge = neighbourTile.edges[neighbourStart]
        val neighborEnd = getAnotherEdge(neighborEdge, neighbourTile)

        if (tileGems.contains(currentGemPosition)) {
            if (neighbourGems.contains(neighbourStart)) {

                val removedElement = currentGame.gems.find { it.gemColor == tileGems[currentGemPosition]!!.gemColor }
                currentGame.gems.remove(removedElement)
                val removedGem =
                    currentGame.gems.find { it.gemColor == neighbourGems[neighbourStart]!!.gemColor }
                currentGame.gems.remove(removedGem)
                tileGems.remove(currentGemPosition)
                neighbourGems.remove(neighbourStart)
                return
            }

            if (neighbourGems.contains(neighborEnd)) {

                val removedElement = currentGame.gems.find { it.gemColor == tileGems[currentGemPosition]!!.gemColor }
                currentGame.gems.remove(removedElement)
                val removedGem =
                    currentGame.gems.find { it.gemColor == neighbourGems[neighborEnd]!!.gemColor }
                currentGame.gems.remove(removedGem)
                tileGems.remove(currentGemPosition)
                neighbourGems.remove(neighborEnd)
                return
            }

        }
        if (!neighbourTile.gemEndPosition.contains(neighbourStart)) return
        val currentEdge = currentTile.edges[currentGemPosition]
        val currentEnd = getAnotherEdge(currentEdge, currentTile)
        currentTile.gemEndPosition[currentEnd] = neighbourGems[neighbourStart]!!
        neighbourGems.remove(neighbourStart)
        if (currentTile.gemEndPosition[currentEnd] != null) {

        }
        println( currentCoordinate.toString())
        println("currentend $currentEnd")
        removeGemsReachedGate(currentTile, currentCoordinate)

        moveGems(
            neighborCoordinates[currentEnd], currentCoordinate, abs((currentEnd + 3)) % 6
        )
    }




    /**
     * function to give the current [Player] a new route [Tile] at the end of their turn (first in list)
     *
     * @param isAiCalled (optional) [Boolean] to prevent refreshes when simulating moves for the AI, defaults false
     * @throws IllegalStateException if currentGame in [rootService] is null or no route [Tile]s remain
     */
    fun distributeNewTile(isAiCalled: Boolean = false) {
        //val game = rootService.currentGame
        //checkNotNull(game)
        if (currentGame.routeTiles.isEmpty()) {
            currentGame.players[currentGame.currentPlayerIndex].handTile = null
        } else {
            val newHandTile = currentGame.routeTiles.removeAt(0)
            val currentPlayerIndex = currentGame.currentPlayerIndex
            currentGame.settings.players[currentPlayerIndex].handTile = newHandTile
        }
        //if (!isAiCalled) onAllRefreshables { refreshAfterDistributeNewTile() }
    }

    /**
     * Gets the neighboring coordinates for a given coordinate, which the contains a tile
     * @param coordinate The coordinate for which to find neighboring coordinates
     * @return List of neighboring coordinates
     */
    fun getNeighboringCoordinates(coordinate: Coordinate): List<Coordinate> {
        val neighbors = mutableListOf<Coordinate>()
        //hexagonal grid
        neighbors.add(Coordinate(coordinate.row - 1, coordinate.column))      // Above
        neighbors.add(Coordinate(coordinate.row - 1, coordinate.column + 1))  // Top-right
        neighbors.add(Coordinate(coordinate.row, coordinate.column + 1))      // Bottom-right
        neighbors.add(Coordinate(coordinate.row + 1, coordinate.column))      // Below
        neighbors.add(Coordinate(coordinate.row + 1, coordinate.column - 1))  // Bottom-left
        neighbors.add(Coordinate(coordinate.row, coordinate.column - 1)) // Top-left

        return neighbors
    }
/*
/**
 * Checks if the game is over based on the current state of the Indigo game.
 *
 * @param state The current game state of type Indigo.
 * @return True if the game is over, false otherwise.
 */
        fun isGameOver(state: Indigo): Boolean {
            val availableMoves:MutableList<Coordinate> = mutableListOf()

            // Iterate over the game board and find available moves
            for (row in -4..4) {
                for (col in Integer.max(-4, -row - 4)..Integer.min(4, -row + 4)) {
                    val coordinate = Coordinate(row, col)
                    // Check if placing the tile at the coordinate is a valid move
                    if (state.gameBoard.gameBoardTiles[coordinate]==null) {
                        availableMoves.add(Coordinate(row,col))
                    }
                }
            }
            return availableMoves.isEmpty()     }*/


    }

