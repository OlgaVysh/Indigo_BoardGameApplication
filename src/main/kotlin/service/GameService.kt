package service

import entity.*
import java.lang.Exception

/**
 * Service class for managing the game logic.
 * @param rootService The root service providing access to the current game state.
 */
class GameService(private val rootService: RootService) {
    /**
     * Starts a new game with the specified list of players or an empty list if none is provided.
     *
     * @param players A mutable list of players. Defaults to an empty list if none is provided.
     */
    fun startGame(
        players: MutableList<Player> = mutableListOf(),
        notSharedGate: Boolean = false,
        random: Boolean = false
    ) {
        if (random) {
            players.shuffle()
        }

        val gameBoard = GameBoard()
        val allTiles = initializeTiles()
        val gems = initializeGems()
        val tokens = initializeTokens()

        val settings = GameSettings(players, 0, false)
        rootService.currentGame = Indigo(settings, gameBoard, allTiles, gems, tokens)
        rootService.currentGame!!.gameBoard.gateTokens = createGateTokens(players, notSharedGate)
        val treasureTiles = rootService.currentGame!!.treasureTiles
        val listCoordinate = listOf(
            Coordinate(-4, 4),
            Coordinate(0, 4),
            Coordinate(4, 0),
            Coordinate(4, -4),
            Coordinate(0, -4),
            Coordinate(-4, 0)
        )
        for (i in listCoordinate.indices) {
            val coordinate = listCoordinate[i]
            rootService.currentGame!!.gameBoard.gameBoardTiles[coordinate] = treasureTiles[i]
        }
        rootService.currentGame!!.routeTiles.shuffle()
        repeat(players.size) {
            distributeNewTile()
            changePlayer()
        }
    }

    /**
     * Restarts the current game.
     */
    fun restartGame() {
        return this.startGame(players = mutableListOf())
    }

    /**
     * Ends the current game.
     */
    fun endGame() {
        TODO(/*refresh*/)
    }

    /**
     * Checks if placing a tile at the specified coordinate is valid.
     * @param space The coordinate where the tile is to be placed.
     * @param tile The tile to be placed.
     * @return True if placement is valid, false otherwise.
     * @throws Exception if the space is already occupied or the tile blocks an exit.
     */
    fun checkPlacement(space: Coordinate, tile: Tile): Boolean {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        if (space == Coordinate(0, 0)) return false
        // Check if the space is occupied
        if (currentGame.gameBoard.gameBoardTiles[space] != null) {
            return false
            throw Exception("this place is occupied")
        }
        // Check if the space has an exit
        return if (!coordinateHasExit(space)) {
            return false
            placeTile(space, tile)
            true
        } else {
            // Check if the tile blocks an exit
            return if (!tileBlocksExit(space, tile)) {
                placeTile(space, tile)
                true
            } else {
                return false
                throw Exception("tile blocks exit, please rotate Tile")
            }
        }
    }

    /**
     * Checks if the given coordinate has an exit.
     * @param space The coordinate to check.
     * @return True if the coordinate has an exit, false otherwise.
     */
    private fun coordinateHasExit(space: Coordinate): Boolean {
        // List of  gates with exits

        return (space == Coordinate(1, -4) || space == Coordinate(2, -4) || space == Coordinate(
            3,
            -4
        ) || space == Coordinate(4, -3) || space == Coordinate(4, -2) || space == Coordinate(
            4,
            -1
        ) || space == Coordinate(3, 1) || space == Coordinate(2, 2) || space == Coordinate(1, 3) || space == Coordinate(
            -1,
            4
        ) || space == Coordinate(-2, 4) || space == Coordinate(-3, 4) || space == Coordinate(
            -4,
            3
        ) || space == Coordinate(-4, 2) || space == Coordinate(-4, 1) || space == Coordinate(
            -3,
            -1
        ) || space == Coordinate(-2, -2) || space == Coordinate(-1, -3))
    }

    /**
     * Places a tile at the specified coordinate.
     * @param space The coordinate where the tile is to be placed.
     * @param tile The tile to be placed.
     */
    private fun placeTile(space: Coordinate, tile: Tile) {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        currentGame.gameBoard.gameBoardTiles[space] = tile
    }

    /**
     * Checks if placing a tile at the specified coordinate blocks an exit.
     * @param space The coordinate where the tile is to be placed.
     * @param tile The tile to be placed.
     * @return True if placement blocks an exit, false otherwise.
     */
    private fun tileBlocksExit(space: Coordinate, tile: Tile): Boolean {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        // Define coordinates for each gate
        val gate1 = listOf(Coordinate(1, -4), Coordinate(2, -4), Coordinate(3, -4))
        val gate2 = listOf(Coordinate(4, -3), Coordinate(4, -2), Coordinate(4, -1))
        val gate3 = listOf(Coordinate(3, 1), Coordinate(2, 2), Coordinate(1, 3))
        val gate4 = listOf(Coordinate(-1, 4), Coordinate(-2, 4), Coordinate(-3, 4))
        val gate5 = listOf(Coordinate(-4, 3), Coordinate(-4, 2), Coordinate(-4, 1))
        val gate6 = listOf(Coordinate(-3, -1), Coordinate(-2, -2), Coordinate(-1, -3))
        val gates = listOf(gate1, gate2, gate3, gate4, gate5, gate6)

        var position1 = 5
        var position2 = 0
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
        var indexInEdges = 0
        // Find the index of the second edge in the edges list
        for (i in edges.indices) {
            if (secondEdge!! == edges[i]) {
                indexInEdges = i
            }
        }
        return indexInEdges
    }

    /**
     *Function for checking if the moving gems are colliding
     *@param tile: Route tiles on the game board
     */
    fun checkCollision(tile: Tile): Boolean {
        for (path in tile.paths) {
            val gemAtBeginning = tile.gemEndPosition[path.first.ordinal]
            val gemAtEnd = tile.gemEndPosition[path.second.ordinal]
            //Checks if the beginning and the end of the path gave gems
            if (gemAtBeginning != null && gemAtEnd != null && gemAtBeginning != gemAtEnd) {
                //Two gems are colliding
                tile.gemEndPosition.remove(path.first.ordinal)
                tile.gemEndPosition.remove(path.second.ordinal)
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

    fun removeGemsReachedGate(tile: Tile, coordinate: Coordinate) {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        val players = currentGame.players

        val gateTokens = currentGame.gameBoard.gateTokens
        val gate1 = listOf(Coordinate(-4, 1), Coordinate(-4, 2), Coordinate(-4, 3))
        val gate2 = listOf(Coordinate(-3, 4), Coordinate(-2, 4), Coordinate(-1, 4))
        val gate3 = listOf(Coordinate(1, 3), Coordinate(2, 2), Coordinate(3, 3))
        val gate4 = listOf(Coordinate(4, -1), Coordinate(4, -2), Coordinate(4, -3))
        val gate5 = listOf(Coordinate(1, -4), Coordinate(2, -4), Coordinate(3, -4))
        val gate6 = listOf(Coordinate(-1, -3), Coordinate(-2, -2), Coordinate(-3, -1))

        val gatesListe = mutableListOf(gate1, gate2, gate3, gate4, gate5, gate6)
        for (i in 0 until 6) {
            if (gatesListe[i].contains(coordinate)) {
                //check existenz of two gems not of the same path of tile, but on the two edges beyond the gate.
                val gem1 = tile.gemEndPosition[(0 + i) % 6]
                val gem2 = tile.gemEndPosition[(5 + i) % 6]
                if (gem2 != null) {
                    if (gateTokens[(i * 2)].color == gateTokens[(i * 2) + 1].color) {
                        for (player in players) {
                            if (player.color == gateTokens[(i * 2)].color) {
                                assignGem(gem2, player)
                                tile.gemEndPosition.remove((5 + i) % 6)
                            }
                        }

                    } else {
                        for (player in players) {
                            if (player.color == gateTokens[(i * 2)].color) {
                                assignGem(gem2, player)
                            }
                            if (player.color == gateTokens[(i * 2) + 1].color) {
                                assignGem(gem2, player)
                            }
                            tile.gemEndPosition.remove((5 + i) % 6)
                        }
                    }
                }
                if (gem1 != null) {
                    if (gateTokens[(i * 2)].color == gateTokens[(i * 2) + 1].color) {
                        for (player in players) {
                            if (player.color == gateTokens[(i * 2)].color) {
                                assignGem(gem1, player)
                                tile.gemEndPosition.remove((0 + i) % 6)
                            }
                        }

                    } else {
                        for (player in players) {
                            if (player.color == gateTokens[(i * 2)].color) {
                                assignGem(gem1, player)
                            }
                            if (player.color == gateTokens[(i * 2) + 1].color) {
                                assignGem(gem1, player)
                            }
                            tile.gemEndPosition.remove((0 + i) % 6)
                        }
                    }
                }
            }
        }

    }

    /**
     * saves the current [Indigo] gameState as a JSON file at a given location using the [IOService]
     *
     * @param path location to be given to [IOService] for saving
     * @throws IllegalStateException if currentGame is null
     */
    fun saveGame(path: String) {
        val game = rootService.currentGame
        checkNotNull(game)
        rootService.ioService.saveGameToFile(game, path)
        TODO(/*refresh*/)
    }

    /**
     * loads an [Indigo] gameState from a JSON file at a given location using the [IOService]
     *
     * @param path location to be given to [IOService] for reading JSON file
     * @throws IllegalStateException if currentGame is null after loading
     */
    fun loadGame(path: String) {
        rootService.currentGame = rootService.ioService.readGameFromFile(path)
        checkNotNull(rootService.currentGame)
        TODO(/*refresh*/)
    }

    /**
     * function to assign a [Gem] to a given [Player]
     * @param gem [Gem] to be assigned
     * @param player [Player] to receive the [Gem]
     */
    private fun assignGem(gem: Gem, player: Player) {
        player.score += gem.gemColor.ordinal + 1
        player.gemCounter++
        //refresh
    }

    /**
     * Changes the current player to the next player in the list.
     */
    fun changePlayer() {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        val playerSize = currentGame.players.size
        val currentPlayerIndex = currentGame.currentPlayerIndex
        currentGame.currentPlayerIndex = (currentPlayerIndex + 1) % playerSize

    }

    /**
     * Moves gems from one tile to another based on the specified edge indices.
     * @param tile The tile from which gems are moved.
     * @param neighbourTile The tile to which gems are moved.
     * @param tileEnd The index of the edge in the tile where the gems are located.
     * @param neighbourStart The index of the edge in the neighbourTile where gems are moved.
     */
    fun moveGems(tile: Tile, neighbourTile: Tile, tileEnd: Int, neighbourStart: Int) {
        val tileGems = tile.gemEndPosition
        val neighbourGems = neighbourTile.gemEndPosition
        if (tileGems.contains(tileEnd)) {
            if (neighbourGems.contains(neighbourStart)) {
                tileGems.remove(tileEnd)
                neighbourGems.remove(neighbourStart)
                return
            }

            val neighbourEdge = neighbourTile.edges[neighbourStart]
            val neighbourEnd = getAnotherEdge(neighbourEdge, neighbourTile)

            if (neighbourGems.contains(neighbourEnd)) {
                tileGems.remove(tileEnd)
                neighbourGems.remove(neighbourEnd)
                return
            }
            val tileGem = tileGems[tileEnd]
            tileGems.remove(tileEnd)
            neighbourGems[neighbourEnd] = tileGem!!
        }
    }


    /**
     * function to give the current [Player] a new route [Tile] at the end of their turn (first in list)
     *
     * @throws IllegalStateException if currentGame in [rootService] is null or no route [Tile]s remain
     */
    fun distributeNewTile() {
        val game = rootService.currentGame
        checkNotNull(game)
        check(game.routeTiles.size > 0)
        game.players[game.currentPlayerIndex].handTile = game.routeTiles.removeFirst()
        //Refresh
    }

    /**
     * @return [List] of [Tile]s, first 6 are treasure tiles starting at the top right of the board going clockwise
     */
    private fun initializeTiles(): MutableList<Tile> {
        val allTiles: MutableList<Tile> = mutableListOf()
        //Position of gem is determined, path starts and ends on the adjacent sides
        for (i in 0 until 6) {
            val gemPos = (i + 3) % 6
            allTiles.add(
                Tile(
                    listOf(
                        Pair(
                            Edge.values()[(Edge.values().size + gemPos - 1) % 6],
                            Edge.values()[(Edge.values().size + gemPos + 1) % 6]
                        )
                    ), mutableMapOf(Pair(gemPos, Gem(GemColor.AMBER)))
                )
            )
        }
        //TypeID 0 Route Tiles are added
        var path1 = Pair(Edge.ZERO, Edge.TWO)
        var path2 = Pair(Edge.ONE, Edge.FOUR)
        var path3 = Pair(Edge.THREE, Edge.FIVE)
        for (i in 0 until 14) {
            allTiles.add(Tile(listOf(path1, path2, path3)))
        }
        //TypeID 1 Route Tiles are added
        path1 = Pair(Edge.TWO, Edge.FIVE)
        path2 = Pair(Edge.ONE, Edge.FOUR)
        path3 = Pair(Edge.ZERO, Edge.THREE)
        for (i in 0 until 6) {
            allTiles.add(Tile(listOf(path1, path2, path3)))
        }
        //TypeID 2 Route Tiles are added
        path1 = Pair(Edge.ZERO, Edge.FIVE)
        path2 = Pair(Edge.ONE, Edge.FOUR)
        path3 = Pair(Edge.TWO, Edge.THREE)
        for (i in 0 until 14) {
            allTiles.add(Tile(listOf(path1, path2, path3)))
        }
        //TypeID 3 Route Tiles are added
        path1 = Pair(Edge.ZERO, Edge.FIVE)
        path2 = Pair(Edge.ONE, Edge.THREE)
        path3 = Pair(Edge.TWO, Edge.FOUR)
        for (i in 0 until 14) {
            allTiles.add(Tile(listOf(path1, path2, path3)))
        }
        //TypeID 4 Route Tiles are added
        path1 = Pair(Edge.ZERO, Edge.FIVE)
        path2 = Pair(Edge.ONE, Edge.TWO)
        path3 = Pair(Edge.THREE, Edge.FOUR)
        for (i in 0 until 6) {
            allTiles.add(Tile(listOf(path1, path2, path3)))
        }
        return allTiles
    }

    /**
     * @return list of all [Gem]s not on the board at the start of a game (6 Amber, 5 Emerald, 1 Sapphire)
     */
    fun initializeGems(): MutableList<Gem> {
        val gems: MutableList<Gem> = mutableListOf()
        for (i in 0 until 6) gems.add(Gem(GemColor.AMBER))
        for (i in 0 until 5) gems.add(Gem(GemColor.EMERALD))
        gems.add(Gem(GemColor.SAPPHIRE))
        return gems
    }

    /**
     * Initializes and returns a MutableList of Token objects.
     *
     * This function creates a list of tokens with predefined colors and quantities.
     * The list includes six tokens of each color: WHITE, PURPLE, BLUE, and RED.
     *
     * @return A MutableList containing the initialized Token objects.
     */
    fun initializeTokens(): MutableList<Token> {
        val tokens: MutableList<Token> = mutableListOf()
        for (i in 0 until 6) tokens.add(Token(TokenColor.WHITE))
        for (i in 0 until 6) tokens.add(Token(TokenColor.PURPLE))
        for (i in 0 until 6) tokens.add(Token(TokenColor.BLUE))
        for (i in 0 until 6) tokens.add(Token(TokenColor.RED))
        return tokens
    }


    /**
     * Gets the neighboring coordinates for a given coordinate
     * @param coordinate The coordinate for which to find neighboring coordinates
     * @return List of neighboring coordinates
     */
    private fun getNeighboringCoordinates(coordinate: Coordinate): List<Coordinate> {
        val neighbors = mutableListOf<Coordinate>()

        //hexagonal grid
        neighbors.add(Coordinate(coordinate.row - 1, coordinate.column))      // Above
        neighbors.add(Coordinate(coordinate.row - 1, coordinate.column + 1))  // Top-right
        neighbors.add(Coordinate(coordinate.row, coordinate.column + 1))      // Bottom-right
        neighbors.add(Coordinate(coordinate.row + 1, coordinate.column))      // Below
        neighbors.add(Coordinate(coordinate.row + 1, coordinate.column - 1))  // Bottom-left
        neighbors.add(Coordinate(coordinate.row, coordinate.column - 1))      // Top-left

        return neighbors
    }

    /**
     * Gets the neighboring tiles for a given coordinate
     * @param coordinate The coordinate for which to find neighboring tiles
     * @return List of neighboring tiles
     */
    fun getNeighboringTiles(coordinate: Coordinate): List<Tile> {
        val neighboringTiles = mutableListOf<Tile>()
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)

        for (neighborCoordinate in getNeighboringCoordinates(coordinate)) {
            currentGame.gameBoard.gameBoardTiles[neighborCoordinate]?.let {
                neighboringTiles.add(it)
            }
        }
        return neighboringTiles
    }

    /**
     * The private function [createGateTokens]
     * create from the upcoming information of the network
     * to make the gate tokens
     *
     * @param players The players which are participate the game
     * @param notSharedGates  The [notSharedGates] is two decide which mode the gates are
     * initialize
     *
     * @return A mutable list of token as gateTokens
     */
    fun createGateTokens(players: List<Player>, notSharedGates: Boolean): MutableList<Token> {
        val gateTokens = mutableListOf<Token>()
        val playerSize = players.size
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
}
