package service

import entity.*
import java.lang.Exception

/**
 * Service class for managing the game logic.
 * @param rootService The root service providing access to the current game state.
 */
class GameService(private val rootService: RootService) {
    /**
     * Starts a new game.
     */
    fun startGame() {} //players: List<Player> :Indigo

    /**
     * Restarts the current game.
     */
    fun restartGame() {}//players: List<Player> :Indigo

    /**
     * Ends the current game.
     */
    fun endGame() {}

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
        // Check if the space is occupied
        if (currentGame.gameBoard.gameBoardTiles[space] != null) {
            throw Exception("this place is occupied")
        }
        // Check if the space has an exit
        if (!coordinateHasExit(space)) {
            placeTile(space, tile)
        } else {
            // Check if the tile blocks an exit
            if (!tileBlocksExit(space, tile)) {
                placeTile(space, tile)
            } else {
                throw Exception("tile blocks exit")
            }
        }
        return true
    }

    /**
     * Checks if the given coordinate has an exit.
     * @param space The coordinate to check.
     * @return True if the coordinate has an exit, false otherwise.
     */
    private fun coordinateHasExit(space: Coordinate): Boolean {
        // List of  gates with exits

        return (space == Coordinate(1, -4) || space == Coordinate(2, -4)
                || space == Coordinate(3, -4) || space == Coordinate(4, -3)
                || space == Coordinate(4, -2) || space == Coordinate(4, -1)
                || space == Coordinate(3, 1) || space == Coordinate(2, 2)
                || space == Coordinate(1, 3) || space == Coordinate(-1, 4)
                || space == Coordinate(-2, 4) || space == Coordinate(-3, 4)
                || space == Coordinate(-4, 3) || space == Coordinate(-4, 2)
                || space == Coordinate(-4, 1) || space == Coordinate(-3, -1)
                || space == Coordinate(-2, -2) || space == Coordinate(-1, -3))
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
        val gate1 = listOf(Coordinate(-4, 1), Coordinate(-4, 2), Coordinate(-4, 3))
        val gate2 = listOf(Coordinate(-3, 4), Coordinate(-2, 4), Coordinate(-1, 4))
        val gate3 = listOf(Coordinate(1, 3), Coordinate(2, 2), Coordinate(3, 1))
        val gate4 = listOf(Coordinate(4, -1), Coordinate(4, -2), Coordinate(4, -3))
        val gate5 = listOf(Coordinate(1, -4), Coordinate(2, -4), Coordinate(3, -4))
        val gate6 = listOf(Coordinate(-1, -3), Coordinate(-2, -2), Coordinate(-3, -1))

        val edge1: Edge
        val edge2: Int
        // Check which gate the space belongs to
        when {
            gate1.contains(space) -> {
                edge1 = tile.edges[0]
                edge2 = getAnotherEdge(edge1, tile)
                if (edge2 == 5) {
                    return false
                }
            }

            gate2.contains(space) -> {
                edge1 = tile.edges[0]
                edge2 = getAnotherEdge(edge1, tile)
                if (edge2 == 1) {
                    return false
                }
            }

            gate3.contains(space) -> {
                edge1 = tile.edges[1]
                edge2 = getAnotherEdge(edge1, tile)
                if (edge2 == 2) {
                    return false
                }
            }

            gate4.contains(space) -> {
                edge1 = tile.edges[2]
                edge2 = getAnotherEdge(edge1, tile)
                if (edge2 == 3) {
                    return false
                }
            }

            gate5.contains(space) -> {
                edge1 = tile.edges[3]
                edge2 = getAnotherEdge(edge1, tile)
                if (edge2 == 4) {
                    return false
                }
            }

            gate6.contains(space) -> {
                edge1 = tile.edges[4]
                edge2 = getAnotherEdge(edge1, tile)
                if (edge2 == 5) {
                    return false
                }
            }

            else -> return true
        }

        return true
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
        // Check if the edge is in any of the pairs
        if (paths.any { it.first == edge1 || it.second == edge1 }) {
            // Get the second edge from the first pair that contains the edge
            secondEdge = paths.first { it.first == edge1 || it.second == edge1 }.second
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

    fun checkCollision() {}//:Unit
    fun saveGame() {}
    fun loadGame() {}

    /**
     * function to assign a [Gem] to a given [Player]
     * @param gem [Gem] to be assigned
     * @param player [Player] to receive the [Gem]
     */
    fun assignGem(gem: Gem,player: Player) {
        player.score += gem.gemColor.ordinal+1
        player.gemCounter++
        TODO(/*refresh*/)
    }
    fun changePlayer() {}//Unit
    fun moveGems() {}//(gem: Gem) :Unit
    fun addPoints() {}//(player: Player, amount: Int) : Unit

    /**
     * function to give the current [Player] a new route [Tile] at the end of their turn (last in list)
     *
     * @throws IllegalStateException if currentGame in [rootService] is null or no route [Tile]s remain
     */
    fun distributeNewTile() {
        val game = rootService.currentGame
        checkNotNull(game)
        check(game.routeTiles.size > 0)
        game.players[game.currentPlayerIndex].handTile = game.routeTiles.removeLast()
        TODO(/*refresh*/)
    }

    /**
     * @return [List] of [Tile]s, first 6 are treasure tiles starting at the top right of the board going clockwise
     */
    fun initializeTiles():List<Tile> {
        val allTiles: MutableList<Tile> = mutableListOf()
        //Position of gem is determined, path starts and ends on the adjacent sides
        for (i in 0 until 6){
            val gemPos = (i+3)%6
            allTiles.add(Tile(listOf(Pair(Edge.values()[gemPos-1],Edge.values()[gemPos+1])),
                mapOf(Pair(gemPos,Gem(GemColor.AMBER)))))
        }
        //TypeID 0 Route Tiles are added
        var path1 = Pair(Edge.ZERO,Edge.TWO)
        var path2 = Pair(Edge.ONE,Edge.FOUR)
        var path3 = Pair(Edge.THREE,Edge.FIVE)
        for (i in 0 until 14){
            allTiles.add(Tile(listOf(path1,path2,path3)))
        }
        //TypeID 1 Route Tiles are added
        path1 = Pair(Edge.TWO,Edge.FIVE)
        path2 = Pair(Edge.ONE,Edge.FOUR)
        path3 = Pair(Edge.ZERO,Edge.THREE)
        for (i in 0 until 6){
            allTiles.add(Tile(listOf(path1,path2,path3)))
        }
        //TypeID 2 Route Tiles are added
        path1 = Pair(Edge.ZERO,Edge.FIVE)
        path2 = Pair(Edge.ONE,Edge.FOUR)
        path3 = Pair(Edge.TWO,Edge.THREE)
        for (i in 0 until 14){
            allTiles.add(Tile(listOf(path1,path2,path3)))
        }
        //TypeID 3 Route Tiles are added
        path1 = Pair(Edge.ZERO,Edge.FIVE)
        path2 = Pair(Edge.ONE,Edge.THREE)
        path3 = Pair(Edge.TWO,Edge.FOUR)
        for (i in 0 until 14){
            allTiles.add(Tile(listOf(path1,path2,path3)))
        }
        //TypeID 4 Route Tiles are added
        path1 = Pair(Edge.ZERO,Edge.FIVE)
        path2 = Pair(Edge.ONE,Edge.TWO)
        path3 = Pair(Edge.THREE,Edge.FOUR)
        for (i in 0 until 6){
            allTiles.add(Tile(listOf(path1,path2,path3)))
        }
        return allTiles.toList()
    }

    /**
     * @return list of all [Gem]s not on the board at the start of a game (6 Amber, 5 Emerald, 1 Sapphire)
     */
    fun initializeGems():MutableList<Gem> {
        val gems:MutableList<Gem> = mutableListOf()
        for (i in 0 until 6) gems.add(Gem(GemColor.AMBER))
        for (i in 0 until 5) gems.add(Gem(GemColor.EMERALD))
        gems.add(Gem(GemColor.SAPPHIRE))
        return gems
    }
    private fun removeGems() {}//(gems:List<Gem>):Unit
}