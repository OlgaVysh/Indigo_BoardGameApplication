package service

//import entity.*
import view.Refreshable

/**
 * [Refreshable] implementation that refreshes nothing, but remembers
 * if a refresh method has been called (since last [reset])
 */
class RefreshableTest : Refreshable {
    var refreshAfterStartGameCalled: Boolean = false
        private set

    var refreshAfterStartNetGameCalled: Boolean = false
        private set

    var refreshAfterRestartGameCalled: Boolean = false
        private set

    var refreshAfterEndGameCalled: Boolean = false
        private set

    var refreshAfterPlacementCalled: Boolean = false
        private set
    var refreshAfterMoveGemsCalled: Boolean = false
        private set

    var refreshAfterCollisionCalled: Boolean = false
        private set

    var refreshAfterRotationCalled: Boolean = false
        private set

    var refreshAfterRedoCalled: Boolean = false
        private set

    var refreshAfterUndoCalled: Boolean = false
        private set
    var refreshAfterSaveGameCalled: Boolean = false
        private set

    var refreshAfterLoadGameCalled: Boolean = false
        private set

    var refreshAfterAITurnCalled: Boolean = false
        private set
    var refreshAfterChangePlayerCalled: Boolean = false
        private set

    var refreshAfterDistributeNewTileCalled: Boolean = false
        private set

    var refreshAfterHostGameCalled = false
    private set

    var refreshAfterJoinGameCalled = false
    private set

    var refreshAfterStartNewJoinedGameCalled = false
    private set


    var refreshAfterPlayerJoinedCalled = false
    private set

    var refreshAfterPlayerLeavedCalled = false
        private set
    /**
     * resets all *Called properties to false
     */
    fun reset() {
        refreshAfterStartGameCalled = false
        refreshAfterStartNetGameCalled = false
        refreshAfterRestartGameCalled = false
        refreshAfterEndGameCalled = false
        refreshAfterPlacementCalled = false
        refreshAfterMoveGemsCalled = false
        refreshAfterCollisionCalled = false
        refreshAfterRotationCalled = false
        refreshAfterRedoCalled = false
        refreshAfterUndoCalled = false
        refreshAfterSaveGameCalled = false
        refreshAfterLoadGameCalled = false
        refreshAfterAITurnCalled = false
        refreshAfterChangePlayerCalled = false
        refreshAfterDistributeNewTileCalled = false
        refreshAfterHostGameCalled = false
        refreshAfterJoinGameCalled = false
        refreshAfterStartNewJoinedGameCalled = false
        refreshAfterPlayerJoinedCalled = false
        refreshAfterPlayerLeavedCalled = false


    }

    override fun refreshAfterStartGame() {
        refreshAfterStartGameCalled = true
    }

    override fun refreshAfterStartNetGame() {
        refreshAfterStartNetGameCalled = true
    }

    override fun refreshAfterRestartGame() {
        refreshAfterRestartGameCalled = true
    }

    override fun refreshAfterEndGame() {
        refreshAfterEndGameCalled = true
    }

    override fun refreshAfterPlacement() {
        refreshAfterPlacementCalled = true
    }

    override fun refreshAfterMoveGems() {
        refreshAfterMoveGemsCalled = true
    }

    override fun refreshAfterCollision() {
        refreshAfterCollisionCalled = true
    }

    override fun refreshAfterRotation() {
        refreshAfterRotationCalled = true
    }

    override fun refreshAfterRedo() {
        refreshAfterRedoCalled = true
    }

    override fun refreshAfterUndo() {
        refreshAfterUndoCalled = true
    }

    override fun refreshAfterSaveGame() {

        refreshAfterSaveGameCalled = true
    }
    override fun refreshAfterLoadGame() {
        refreshAfterLoadGameCalled= true
    }
    override fun refreshAfterAITurn() {
        refreshAfterAITurnCalled = true
    }
    override fun refreshAfterChangePlayer() {
        refreshAfterChangePlayerCalled = true
    }
    override fun refreshAfterDistributeNewTile() {
        refreshAfterDistributeNewTileCalled = true
    }

    override fun refreshAfterHostGame() {
        refreshAfterHostGameCalled = true
    }

    override fun refreshAfterPlayerJoined(newPlayerName: String) {
        refreshAfterPlayerJoinedCalled = true
    }

    override fun refreshAfterJoinGame() {
        refreshAfterJoinGameCalled = true
    }

    override fun refreshAfterPlayerLeaved(playerLeavedName: String) {
        refreshAfterPlayerLeavedCalled = true
    }

    override fun refreshAfterStartNewJoinedGame() {
        refreshAfterStartNewJoinedGameCalled = true
    }
}
