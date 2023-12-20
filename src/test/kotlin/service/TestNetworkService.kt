package service

import service.network.ConnectionState
import service.network.NetworkService
import tools.aqua.bgw.observable.lists.ObservableArrayList
import tools.aqua.bgw.observable.properties.Property

class TestNetworkService(rootService: RootService) : NetworkService(rootService) {

    var opponentsProperty: ObservableArrayList<String> = ObservableArrayList()
    var connectionStateProperty : Property<ConnectionState> = Property(connectionState)
    companion object {
        const val serverAdress = "sopra.cs.tu-dortmund.de:80/bgw-net/connect"

        const val gameID = "Indigo"
    }

    override fun connect(secret: String, name: String): Boolean {
        check(this.client?.isOpen == true) {
            "client is already connected"
        }
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