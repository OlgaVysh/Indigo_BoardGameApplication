package service.network

import service.AbstractRefreshingService

/**
 *  the class [NetworkRefreshingService] is a Service  which are used to trigger the refreshes
 *  for the view Layer
 */
class NetworkRefreshingService : AbstractRefreshingService()  {

    /**
     * The function trigger the refresh function for refreshAfterPlayerJoined
     */
    fun refreshAfterPlayerJoined(playerJoinedName : String){
        onAllRefreshables {refreshAfterPlayerJoined(playerJoinedName) }
    }

    /**
     * The function trigger the refresh function for refreshAfterPlayerJoined
     */
    fun refreshAfterPlayerLeft(playerLeftName : String){
        onAllRefreshables { refreshAfterPlayerLeft(playerLeftName) }
    }

    fun refreshAfterOnCreateGameResponse(sessionID : String? ){
        onAllRefreshables { refreshAfterOnCreateGameResponse(sessionID) }
    }
}