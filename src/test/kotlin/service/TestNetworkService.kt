package service

import service.network.ConnectionState
import service.network.NetworkService
import tools.aqua.bgw.observable.lists.ObservableArrayList
import tools.aqua.bgw.observable.properties.Property

/**
 *  The [TestNetworkService] creates is a [NetworkService]
 *  for test proposes
 *
 *  @property rootService to get all information
 */
class TestNetworkService(rootService: RootService) : NetworkService(rootService) {

    var opponentsProperty: ObservableArrayList<String> = ObservableArrayList()
    var connectionStateProperty : Property<ConnectionState> = Property(connectionState)
    companion object {
        const val serverAdress = "sopra.cs.tu-dortmund.de:80/bgw-net/connect"

        const val gameID = "Indigo"
    }


    override fun connect(secret: String, name: String): Boolean {
        this.client = TestNetworkClient(
            name,
            serverAdress,
            secret,
            networkService = this
        )
      //  check(this.client?.isOpen == true ) {
        //    "client is already connected"
        //}
        val client = this.client ?: return false
        val isConnected = client.connect()
        if (isConnected) {
            this.connectionState = ConnectionState.CONNECTED
        } else {
            this.connectionState = ConnectionState.DISCONNECTED
        }
        return isConnected
    }

}