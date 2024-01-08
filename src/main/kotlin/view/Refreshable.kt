package view
/*
import entity.*
import service.*

/**
 * This interface provides a mechanism for the service layer classes to communicate
 * (usually to the view classes) that certain changes have been made to the entity
 * layer, so that the user interface can be updated accordingly.
 *
 * Default (empty) implementations are provided for all methods, so that implementing
 * UI classes only need to react to events relevant to them.
 *
 * @see AbstractRefreshingService
 *
 */
interface Refreshable {

    /**
     * perform refreshes that are necessary after a new game started
     */
    fun refreshAfterStartNewGame() {}

    /**
     * perform refreshes that are necessary after played cards have been evaluated and a winner exists
     * (i.e., card values are not equal)
     *
     * @param winningPlayer the player that won the evaluation and received cards
     * @param movedCards the cards that the winning player received onto their collected cards stack
     */
    fun refreshAfterEvaluateDrawnCards(winningPlayer: WarPlayer, movedCards: List<WarCard>) {}

    /**
     * perform refreshes that are necessary after a player has drawn from their left draw stack
     *
     * @param player the player that drawn from their left stack
     */
    fun refreshAfterDrawFromLeftStack(player: WarPlayer) {}

    /**
     * perform refreshes that are necessary after a player has drawn from their right draw stack
     *
     * @param player the player that drawn from their right stack
     */
    fun refreshAfterDrawFromRightStack(player: WarPlayer) {}

    /**
     * perform refreshes that are necessary after the last round was played
     */
    fun refreshAfterGameEnd() {}

    /**
     * refreshes the network connection status with the given information
     *
     * @param state the information to show
     */
    fun refreshConnectionState(state: ConnectionState) {}

}*/