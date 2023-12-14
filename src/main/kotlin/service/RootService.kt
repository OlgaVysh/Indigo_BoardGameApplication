package service

import entity.Indigo

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

    val networkMappingService = NetworkMappingService(this)
}