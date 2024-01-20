package service

import AI.AiActionService
import entity.Indigo
import service.network.NetworkMappingService
import service.network.NetworkService
import view.Refreshable

/**
 *  The class [RootService] is the service class which connect the service layer
 *  with the entity layer
 *  @property currentGame The property currentGame have saved the current [Indigo] Game
 *  @property networkService The [NetworkService]
 *  @property networkMappingService The [NetworkMappingService]
 *  @property playerTurnService to get all function of the [PlayerTurnService]
 *  @property gameService to get all function of the [GameService]
 *  @property ioService to get all function of the [IOService]
 *  @property aiActionService to get all functions of the [AiActionService]
 */
class RootService {

    var networkService = NetworkService(this)
    val networkMappingService = NetworkMappingService(this)
    val playerTurnService = PlayerTurnService(this)
    val gameService = GameService(this)
    val ioService = IOService(this)
    val aiActionService = AiActionService(this)

    /**
     * The currently active game. Can be `null`, if no game has started yet.
     */
    var currentGame: Indigo? = null

    /**
     * Adds the provided [newRefreshable] to all services connected
     * to this root service
     */
    private fun addRefreshable(newRefreshable: Refreshable) {

        networkService.addRefreshable(newRefreshable)
        networkMappingService.addRefreshable(newRefreshable)
        playerTurnService.addRefreshable(newRefreshable)
        gameService.addRefreshable(newRefreshable)
        ioService.addRefreshable(newRefreshable)
    }

    /**
     * Adds each of the provided [newRefreshables] to all services
     * connected to this root service
     */
    fun addRefreshables(vararg newRefreshables: Refreshable) {
        newRefreshables.forEach { addRefreshable(it) }
    }
}