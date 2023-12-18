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
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        return (space == Coordinate(3, 0) || space == Coordinate(3, -1)
                || space == Coordinate(3, -2) || space == Coordinate(3, -3)
                || space == Coordinate(2, -3) || space == Coordinate(1, -3)
                || space == Coordinate(0, -3) || space == Coordinate(-1, -2)
                || space == Coordinate(2, -1) || space == Coordinate(-3, 0)
                || space == Coordinate(-3, 1) || space == Coordinate(-3, 2)
                || space == Coordinate(-3, 3) || space == Coordinate(-2, 3)
                || space == Coordinate(-1, 3) || space == Coordinate(0, 3)
                || space == Coordinate(1, 2) || space == Coordinate(2, 1))
    }

    private fun placeTile(space: Coordinate, tile: Tile) {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        currentGame.gameBoard.gameBoardTiles[space] = tile
    }

    private fun tileBlocksExit(space: Coordinate, tile: Tile): Boolean {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)

        return true
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