package service

import entity.*
import java.lang.Exception


class GameService(private val rootService: RootService) {
    fun startGame() {} //players: List<Player> :Indigo
    fun restartGame() {}//players: List<Player> :Indigo
    fun endGame() {}
    fun checkPlacement(space: Coordinate, tile: Tile): Boolean {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        if (currentGame.gameBoard.gameBoardTiles[space] != null) {
            throw Exception("this place is occupied")
        }
        if (!coordinateHasExit(space)) {
            placeTile(space, tile)
        } else {
            if (!tileBlocksExit(space, tile)) {
                placeTile(space, tile)
            } else {
                throw Exception("tile blocks exit")
            }
        }
        //in progress
        return true
    }

    private fun coordinateHasExit(space: Coordinate): Boolean {

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

    private fun placeTile(space: Coordinate, tile: Tile) {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        currentGame.gameBoard.gameBoardTiles[space] = tile
    }

    private fun tileBlocksExit(space: Coordinate, tile: Tile): Boolean {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)

        val gate1 = listOf(Coordinate(-4, 1), Coordinate(-4, 2), Coordinate(-4, 3))
        val gate2 = listOf(Coordinate(-3, 4), Coordinate(-2, 4), Coordinate(-1, 4))
        val gate3 = listOf(Coordinate(1, 3), Coordinate(2, 2), Coordinate(3, 1))
        val gate4 = listOf(Coordinate(4, -1), Coordinate(4, -2), Coordinate(4, -3))
        val gate5 = listOf(Coordinate(1, -4), Coordinate(2, -4), Coordinate(3, -4))
        val gate6 = listOf(Coordinate(-1, -3), Coordinate(-2, -2), Coordinate(-3, -1))

        if (gate1.contains(space)) {
            val edge1 = tile.edges[0]
            val edge2 = getAnotherEdge(edge1, tile)
            if (edge2 == 5) {
                return false
            }
        }
        if (gate2.contains(space)) {
            val edge1 = tile.edges[0]
            val edge2 = getAnotherEdge(edge1, tile)
            if (edge2 == 1) {
                return false
            }
        }
        if (gate3.contains(space)) {
            val edge1 = tile.edges[1]
            val edge2 = getAnotherEdge(edge1, tile)
            if (edge2 == 2) {
                return false
            }
        }
        if (gate4.contains(space)) {
            val edge1 = tile.edges[2]
            val edge2 = getAnotherEdge(edge1, tile)
            if (edge2 == 3) {
                return false
            }
        }
        if(gate5.contains(space)) {
            val edge1 = tile.edges[3]
            val edge2 = getAnotherEdge(edge1, tile)
            if (edge2 == 4) {
                return false
            }
        }
        if (gate6.contains(space)) {
            val edge1 = tile.edges[4]
            val edge2 = getAnotherEdge(edge1, tile)
            if (edge2 == 5) {
                return false
            }
        }
        return true
    }


    private fun getAnotherEdge(egde: Edge, tile: Tile): Int {
        val paths = tile.paths
        val edges = tile.edges
        for (i in 0 until paths.size) {
            if (edges[i].equals(paths)) {
                val pathsIndex = paths[i].(egde)
                val anotherEdge = edges[(i + 1) % 2][(pathsIndex)]
                return edges.indexOf(anotherEdge)
            }
        }
    }

    fun checkCollision() {}//:Unit
    fun saveGame() {}
    fun loadGame() {}
    fun assignGems() {}//gem:Gem, player: Player
    fun changePlayer() {}//Unit
    fun moveGems() {}//(gem: Gem) :Unit
    fun addPoints() {}//(player: Player, amount: Int) : Unit
    fun distributeNewTile() {}//:Unit
    fun initializeTiles() {}//:Unit
    fun initializeGems() {}//:Unit
    private fun removeGems() {}//(gems:List<Gem>):Unit
}