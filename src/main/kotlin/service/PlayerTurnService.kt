package service

import entity.*
import java.lang.Exception

class PlayerTurnService(val rootService: RootService) {

    fun placeRouteTile(tile: Tile, space: Coordinate){
        val currentGame = rootService.currentGame
        checkNotNull(currentGame){"The game has not started yet"}
        if (chooseSpace(space)){
            currentGame.gameBoard.gameBoardTiles[space] = tile
            rootService.gameService.checkPlacement()
            rootService.gameService.distributeNewTile()
            rootService.gameService.changePlayer()
            //in progress
        }else{
            throw Exception("Invalid space, can not place tile")
        }
    }

    /**
     * Undoes one game move and returns to the previous game state,
     * it is possible to undo moves until the beginning of the game
     */
    fun undo(){
        var currentState = rootService.currentGame
        if (currentState?.previousGameState !=null){
            currentState = rootService.currentGame?.previousGameState
        }
        else{
            println("Previous game state doesn't exist, cannot undo the move")
        }
    }
    /**
     * Redoes one game move and returns to the next game state,
     * it is possible to redo moves from the beginning until the last made move
     */
    fun redo(){
        var currentState = rootService.currentGame
        if(currentState?.nextGameState != null){
            currentState = rootService.currentGame?.nextGameState
        }
        else{
            println("Next game state doesn't exist, cannot redo the move")
        }
    }
    private fun chooseSpace(space: Coordinate): Boolean{
        val currentGame = rootService.currentGame
        //in progress
        return true
    }
    fun rotateTileRight(){}
    fun rotateTileLeft(){}
}