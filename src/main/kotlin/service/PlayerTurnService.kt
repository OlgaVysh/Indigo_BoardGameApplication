package service

import entity.*
import java.lang.Exception

class PlayerTurnService(private val rootService: RootService) {

    fun placeRouteTile(tile: Tile, space: Coordinate) {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame) { "The game has not started yet" }

        if (chooseSpace(space)) {
            currentGame.gameBoard.gameBoardTiles[space] = tile
            rootService.gameService.checkPlacement()
            rootService.gameService.distributeNewTile()
            rootService.gameService.changePlayer()
            //in progress
        } else {
            throw Exception("Invalid space, can not place tile")
        }
    }


    private fun chooseSpace(space: Coordinate): Boolean {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        if (currentGame.gameBoard.gameBoardTiles[space] != null) {
            throw Exception("this place is occupied")
        }
        if (!coordinateHasExit()) {
            placeTile(space)
        } else {
            if (!tileBlocksExit(space, tile:Tile)) {
                placeTile(space)
            }
            else{ throw Exception("tile blocks exit")}
        }
        //in progress
        return true
    }

    /**
     * Undoes one game move and returns to the previous game state,
     * it is possible to undo moves until the beginning of the game
     */
    fun undo() {
        var currentGame = rootService.currentGame
        checkNotNull(currentGame)
        if (currentGame.previousGameState != null) {
            currentGame.nextGameState = currentGame
            currentGame = currentGame.previousGameState

        } else {
            println("Previous game state doesn't exist, cannot undo the move")
        }
    }

    /**
     * Redoes one game move and returns to the next game state,
     * it is possible to redo moves from the beginning until the last made move
     */
    fun redo() {
        var currentGame = rootService.currentGame
        checkNotNull(currentGame)
        if (currentGame.nextGameState != null) {
            currentGame.previousGameState = currentGame
            currentGame = currentGame.nextGameState
        } else {
            println("Next game state doesn't exist, cannot redo the move")
        }
    }


    fun rotateTileRight(tile: Tile): Tile {
        if (tile.paths == listOf(
                Pair(Edge.ZERO, Edge.TWO),
                Pair(Edge.ONE, Edge.FOUR),
                Pair(Edge.THREE, Edge.FIVE)
            )
        ) {
            tile.paths = listOf(
                Pair(Edge.ONE, Edge.THREE),
                Pair(Edge.TWO, Edge.FIVE),
                Pair(Edge.FOUR, Edge.ONE)
            )
        } else if (tile.paths == listOf(
                Pair(Edge.TWO, Edge.FIVE),
                Pair(Edge.ONE, Edge.FOUR),
                Pair(Edge.ZERO, Edge.THREE)
            )
        ) {
            tile.paths = listOf(
                Pair(Edge.THREE, Edge.ZERO),
                Pair(Edge.TWO, Edge.FIVE),
                Pair(Edge.ONE, Edge.FOUR)
            )
        } else if (tile.paths == listOf(
                Pair(Edge.ZERO, Edge.FIVE),
                Pair(Edge.ONE, Edge.FOUR),
                Pair(Edge.TWO, Edge.THREE)
            )
        ) {
            tile.paths = listOf(
                Pair(Edge.ZERO, Edge.ONE),
                Pair(Edge.TWO, Edge.FIVE),
                Pair(Edge.THREE, Edge.FOUR)
            )
        } else if (tile.paths == listOf(
                Pair(Edge.ZERO, Edge.FIVE),
                Pair(Edge.ONE, Edge.THREE),
                Pair(Edge.TWO, Edge.FOUR)
            )
        ) {
            tile.paths = listOf(
                Pair(Edge.ZERO, Edge.ONE),
                Pair(Edge.TWO, Edge.FOUR),
                Pair(Edge.THREE, Edge.FIVE)
            )
        } else if (tile.paths == listOf(
                Pair(Edge.ZERO, Edge.FIVE),
                Pair(Edge.ONE, Edge.TWO),
                Pair(Edge.THREE, Edge.FOUR)
            )
        ) {
            tile.paths = listOf(
                Pair(Edge.ZERO, Edge.ONE),
                Pair(Edge.TWO, Edge.THREE),
                Pair(Edge.FOUR, Edge.FIVE)
            )
        }
        return tile
    }

    fun rotateTileLeft(tile: Tile): Tile {
        if (tile.paths == listOf(
                Pair(Edge.ZERO, Edge.TWO),
                Pair(Edge.ONE, Edge.FOUR),
                Pair(Edge.THREE, Edge.FIVE)
            )
        ) {
            tile.paths = listOf(
                Pair(Edge.FIVE, Edge.ONE),
                Pair(Edge.ZERO, Edge.THREE),
                Pair(Edge.TWO, Edge.FOUR)
            )
        } else if (tile.paths == listOf(
                Pair(Edge.TWO, Edge.FIVE),
                Pair(Edge.ONE, Edge.FOUR),
                Pair(Edge.ZERO, Edge.THREE)
            )
        ) {
            tile.paths = listOf(
                Pair(Edge.FOUR, Edge.ONE),
                Pair(Edge.ZERO, Edge.THREE),
                Pair(Edge.TWO, Edge.FIVE)
            )
        } else if (tile.paths == listOf(
                Pair(Edge.ZERO, Edge.FIVE),
                Pair(Edge.ONE, Edge.FOUR),
                Pair(Edge.TWO, Edge.THREE)
            )
        ) {
            tile.paths = listOf(
                Pair(Edge.FIVE, Edge.FOUR),
                Pair(Edge.ZERO, Edge.THREE),
                Pair(Edge.ONE, Edge.TWO)
            )
        } else if (tile.paths == listOf(
                Pair(Edge.ZERO, Edge.FIVE),
                Pair(Edge.ONE, Edge.THREE),
                Pair(Edge.TWO, Edge.FOUR)
            )
        ) {
            tile.paths = listOf(
                Pair(Edge.FIVE, Edge.FOUR),
                Pair(Edge.ZERO, Edge.TWO),
                Pair(Edge.ONE, Edge.THREE)
            )
        } else if (tile.paths == listOf(
                Pair(Edge.ZERO, Edge.FIVE),
                Pair(Edge.ONE, Edge.TWO),
                Pair(Edge.THREE, Edge.FOUR)
            )
        ) {
            tile.paths = listOf(
                Pair(Edge.FIVE, Edge.FOUR),
                Pair(Edge.ZERO, Edge.ONE),
                Pair(Edge.THREE, Edge.TWO)
            )
        }
        return tile
    }
}