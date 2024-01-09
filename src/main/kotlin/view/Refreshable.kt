package view


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
     * Refreshes the state after placing game elements.
     */
    fun refreshAfterPlacement() {}

    /**
     * Refreshes the state after moving gems.
     */
    fun refreshAfterMoveGems() {}

    /**
     * Refreshes the state after a collision in the game.
     */
    fun refreshAfterCollision() {}

    /**
     * Refreshes the state after rotating game elements.
     */
    fun refreshAfterRotation() {}

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


}