package service

import entity.Indigo
import service.network.NetworkMappingService
import service.network.NetworkService

/**
 *  The class [RootService] is the service class which connect the service layer
 *  with the entity layer
 *  @property currentGame The property currentGame have saved the current Indigo Game
 *  @property networkService The NetworkService
 *  @property networkMappingService The NetworkMappingService
 *  @property playerTurnService to get all function of the [PlayerTurnService]
 *  @property gameService to get all function of the [GameService]
 *  @property ioService to get all function of the [IOService]
 */
class RootService {
    var currentGame: Indigo? = null
    val networkService = NetworkService(this)
    val networkMappingService = NetworkMappingService(this)
    val playerTurnService = PlayerTurnService(this)
    val gameService = GameService(this)
    val ioService = IOService(this)
}