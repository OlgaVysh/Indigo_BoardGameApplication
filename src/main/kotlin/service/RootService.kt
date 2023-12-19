package service

import entity.Indigo
import service.Network.NetworkService

/**
 *  The class [RootService] is the service class which connect the service layer
 *  with the entity layer
 */
class RootService {
    /**
     * @property currentGame The property currentGame have saved the current Indigo Game
     */
    var currentGame: Indigo? = null
    /**
     *  @property networkService The NetworkService
     */
    val networkService = NetworkService(this)

    /**
     *  @property networkMappingService The NetworkMappingService
     */
    val networkMappingService = NetworkMappingService(this)

    /**
     * @property playerTurnService to get all function of the [PlayerTurnService]
     */
    val playerTurnService = PlayerTurnService(this)

    /**
     * @property gameService to get all function of the [GameService]
     */
    val gameService = GameService(this)
}