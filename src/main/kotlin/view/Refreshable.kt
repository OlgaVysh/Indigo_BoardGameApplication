package view

import entity.Coordinate
import entity.Tile
import tools.aqua.bgw.net.common.response.JoinGameResponseStatus


/**
 * Interface for objects that can be refreshed in response to different game events.
 */
interface Refreshable {
    /**
     * Refreshes the state after starting a local game.
     */
    fun refreshAfterStartGame() {}

    /**
     * Refreshes the state after starting a network game.
     */

    fun refreshAfterStartNetGame() {}

    /**
     * Refreshes the state after restarting the game.
     */
    fun refreshAfterRestartGame() {}

    /**
     * Refreshes the state after the game has ended.
     */
    fun refreshAfterEndGame() {}

    /**
     * Refreshes the state after checking place for game tile.
     */
    fun refreshAfterCheckPlacement() {}

    /**
     * Refreshes the state after placing game tile.
     */
    fun refreshAfterPlaceTile(coordinate: Coordinate, tile: Tile) {}

    /**
     * Refreshes the state after moving gems.
     */
    fun refreshAfterMoveGems() {}

    /**
     * Refreshes the state after the player win the gem, and then the gem will be removed from game board.
     */
    fun refreshAfterRemoveGems() {}

    /**
     * Refreshes the state after a collision in the game.
     */
    fun refreshAfterCollision() {}

    /**
     * Refreshes the state after left rotating tile.
     */
    fun refreshAfterLeftRotation(currentPlayerIndex: Int) {}

    /**
     * Refreshes the state after right rotating tile.
     */
    fun refreshAfterRightRotation(currentPlayerIndex: Int) {}

    /**
     * Refreshes the state after redoing a game action.
     */
    fun refreshAfterRedo() {}

    /**
     * Refreshes the state after undoing a game action.
     */
    fun refreshAfterUndo() {}

    /**
     * Refreshes the state after saving the game.
     */
    fun refreshAfterSaveGame() {}

    /**
     * Refreshes the state after loading a saved game.
     */
    fun refreshAfterLoadGame() {}

    /**
     * Refreshes the state after the AI takes its turn.
     */
    fun refreshAfterAITurn() {}

    /**
     * Refreshes the state after changing the active player.
     */
    fun refreshAfterChangePlayer() {}

    /**
     * Refreshes the state after distributing a new tile.
     */
    fun refreshAfterDistributeNewTile() {}

    /**
     *  The function refreshes the view layer after a new Player joined the game for
     *  the host.
     */
    fun refreshAfterPlayerJoined(newPlayerName: String) {}

    /**
     *  The function refreshes the view layer after a Player leaved the game for
     *  the host.
     */
    fun refreshAfterPlayerLeft(playerLeftName: String) {}

    /**
     *  the function updated the view layer after received the tilePlaceMessage
     */
    fun refreshAfterStartNewJoinedGame() {}

    /**
     * The function updated the view layer after joining a Game
     */
    fun refreshAfterJoinGame() {}

    /**
     * The function updated the view Layer after a host Game started
     */
    fun refreshAfterHostGame() {}

    /**
     * After receiving a Game Responese update the view Layer
     */
    fun refreshAfterOnCreateGameResponse(sessionID: String?) {}

    /**
     *  update the Gui with the status
     */
    fun refreshAfterOnJoinGameResponse(responseStatus: JoinGameResponseStatus) {}

    /**
     *  updated the gui with corrected button showing
     */
    fun refreshAfterNetworkPlayerTurn(){}

    /**
     * updated give the gui the correct handTile for variable PlaceTile
     */
    fun refreshAfterReceivedTile() {}
}