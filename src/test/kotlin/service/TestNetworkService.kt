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
    var connectionStateProperty : Property<ConnectionState> = Property(this.connectionState)
    companion object {
        const val SERVER_ADDRESS = "sopra.cs.tu-dortmund.de:80/bgw-net/connect"
    }

    override fun connect(secret: String, name: String): Boolean {
        this.client = TestNetworkClient(
            name,
            SERVER_ADDRESS,
            secret,
            networkService = this
        )
        check(this.client?.isOpen == false ) {
            "client is already connected"
        }
        val client = this.client ?: return false
        val isConnected = client.connect()
        if (isConnected) {
            updateConnectionState(ConnectionState.CONNECTED)
        } else {
            updateConnectionState(ConnectionState.DISCONNECTED)
        }
        return isConnected
    }

}