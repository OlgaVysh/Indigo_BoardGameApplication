package service

import createTestRouteTile
import entity.*
import kotlinx.coroutines.*
import kotlinx.coroutines.time.withTimeout
import org.junit.jupiter.api.Test
import service.network.ConnectionState
import tools.aqua.bgw.net.common.response.GameActionResponseStatus
import tools.aqua.bgw.observable.properties.Property
import java.time.Duration
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeoutException
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class NetworkConnectionTests {
    private lateinit var hostRootService: RootService
    private lateinit var guestRootService: RootService

    private lateinit var coroutineScope: CoroutineScope

    companion object {
        const val SESSIONID = "Test123"
    }

    private val RootService.testNetworkService: TestNetworkService
        get() = this.networkService as TestNetworkService

    private val TestNetworkService.testClient: TestNetworkClient?
        get() = this.client as? TestNetworkClient

    private val treasureTiles = listOf(
        Tile(listOf(Pair(Edge.TWO, Edge.FOUR)), mutableMapOf(Pair(3, Gem(GemColor.AMBER)))),
        Tile(listOf(Pair(Edge.THREE, Edge.FIVE)), mutableMapOf(Pair(4, Gem(GemColor.AMBER)))),
        Tile(listOf(Pair(Edge.ZERO, Edge.FOUR)), mutableMapOf(Pair(5, Gem(GemColor.AMBER)))),
        Tile(listOf(Pair(Edge.TWO, Edge.FOUR)), mutableMapOf(Pair(3, Gem(GemColor.AMBER)))),
        Tile(listOf(Pair(Edge.THREE, Edge.FIVE)), mutableMapOf(Pair(4, Gem(GemColor.AMBER)))),
        Tile(listOf(Pair(Edge.ZERO, Edge.FOUR)), mutableMapOf(Pair(5, Gem(GemColor.AMBER)))),
    )

    private val testSettings = GameSettings(
        listOf(
            Player("host", color = TokenColor.RED), Player("guest", color = TokenColor.BLUE)
        )
    )


    private lateinit var gameInitMessage: Indigo

    /**
     *  The function await wait of a Time, if a specific connection state are reached.
     */
    private fun <T> Property<T>.await(state: T, timeout: Duration = Duration.ofSeconds(5)) {
        runBlocking {
            var running = true
            val listener: ((T, T) -> Unit) = { _, nV -> running = nV != state }
            this@await.addListenerAndInvoke(this@await.value, listener)
            try {
                withTimeout(timeout) { while (running) delay(100) }
            } catch (e: TimeoutCancellationException) {
                throw TimeoutException(
                    "Property ${this@await} with value ${this@await.value}" + " did not reach the expected state" + " $state within the specified timeout."
                )
            } finally {
                this@await.removeListener(listener)
            }
        }
    }

    /**
     *  The [setup] function is a setup before the tests start with initializing
     *  types.
     */
    @BeforeTest
    fun setup() {
        hostRootService = RootService().apply { networkService = TestNetworkService(this) }
        guestRootService = RootService().apply { networkService = TestNetworkService(this) }
        coroutineScope = CoroutineScope(Dispatchers.Default)
        val allType = treasureTiles.toMutableList()
        allType.addAll(createTestRouteTile())
        gameInitMessage = Indigo(
            testSettings,
            GameBoard(),
            allType,
            RootService().gameService.initializeGems(),
            RootService().gameService.initializeTokens()
        )
        gameInitMessage.gameBoard.gateTokens = createTestGateTokens(gameInitMessage, true)

    }

    /**
     *   The [exampleNetworkConnectionTest] is a test function tested the connection,
     *   disconnection and by the host the hostGame and by the guest joinGame functions.
     */
    @Test
    fun exampleNetworkConnectionTest() {
        val latch = CountDownLatch(2)
        val sessionIDQueue: BlockingQueue<String> = ArrayBlockingQueue(1)
        val hostPlayerName = "host"
        val guestPlayerName = "guest"


        val hostThread = coroutineScope.launch {
            println("[$hostPlayerName] Connecting...")
            val host = hostRootService.testNetworkService
            assertTrue(host.connect(name = hostPlayerName))
            host.connectionStateProperty = Property(host.connectionState)
            host.connectionStateProperty.await(ConnectionState.CONNECTED)
            host.disconnect()
            host.connectionStateProperty = Property(host.connectionState)
            host.connectionStateProperty.await(ConnectionState.DISCONNECTED)
            assertNull(host.testClient)/* Host game */
            println("[$hostPlayerName] Hosting game...")
            host.hostGame(name = hostPlayerName)
            host.testClient?.apply {
                onGameActionResponse = {
                    println("[$hostPlayerName] Received GameActionResponse with status ${it.status}")
                    when (it.status) {
                        GameActionResponseStatus.INVALID_JSON -> println("[$hostPlayerName] Invalid JSON: ${it.errorMessages}")
                        else -> {}
                    }
                }
                onCreateGameResponse = {
                    println("[$hostPlayerName] Received CreateGameResponse with status ${it.status}")
                }
            }
            Thread.sleep(5000)
            host.connectionStateProperty = Property(host.connectionState)
            host.connectionStateProperty.await(ConnectionState.WAITING_FOR_GUEST)
            val testclient = host.testClient
            checkNotNull(testclient)
            val sessionID = testclient.sessionID
            checkNotNull(sessionID)
            sessionIDQueue.put(sessionID)
            latch.countDown()
            latch.await()
        }

        val guestThread = coroutineScope.launch {
            println("[$guestPlayerName] Connecting...")
            val client = guestRootService.testNetworkService
            client.disconnect()
            client.connect(name = guestPlayerName)/*Join game */
            println("[$guestPlayerName] Join game...")
            client.joinGame(name = guestPlayerName, sessionID = sessionIDQueue.take())
            Thread.sleep(500)
            client.connectionStateProperty = Property(client.connectionState)
            client.connectionStateProperty.await(ConnectionState.WAITING_FOR_INIT)
            latch.countDown()
            latch.await()

        }

        runBlocking {
            joinAll(
                hostThread, guestThread
            )
            guestRootService.networkService.disconnect()
            hostRootService.networkService.disconnect()
        }
    }

    /**
     *  The [gameInitTest] test function test if the correct game init is sended and
     *  if the guest get the same [Indigo]
     */
    @Test
    fun gameInitTest() {
        val latch = CountDownLatch(2)
        val sessionIDQueue: BlockingQueue<String> = ArrayBlockingQueue(1)
        val hostPlayerName = "host"
        val guestPlayerName = "guest"

        val hostThread = coroutineScope.launch {
            val host = hostRootService.testNetworkService/* Host game */
            println("[$hostPlayerName] Hosting game...")
            host.hostGame(name = hostPlayerName, sessionID = SESSIONID)
            host.testClient?.apply {
                onGameActionResponse = {
                    println("[$hostPlayerName] Received GameActionResponse with status ${it.status}")
                    when (it.status) {
                        GameActionResponseStatus.INVALID_JSON -> println("[$hostPlayerName] Invalid JSON: ${it.errorMessages}")
                        else -> {}
                    }
                }
                onCreateGameResponse = {
                    println("[$hostPlayerName] Received CreateGameResponse with status ${it.status}")
                }
            }
            Thread.sleep(5000)
            val testclient = host.testClient
            checkNotNull(testclient)
            val sessionID = testclient.sessionID
            checkNotNull(sessionID)
            sessionIDQueue.put(sessionID)
            hostRootService.currentGame = gameInitMessage
            repeat(testSettings.players.size) {
                hostRootService.gameService.distributeNewTile()
                hostRootService.gameService.changePlayer()
            }
            Thread.sleep(1500)
            host.sendGameInitMessage()
            Property(host.connectionState).await(ConnectionState.PLAYING_MY_TURN)
            latch.countDown()
            latch.await()
        }

        val guestThread = coroutineScope.launch {
            println("[$guestPlayerName] Connecting...")
            val client = guestRootService.testNetworkService
            client.disconnect()
            client.connect(name = guestPlayerName)/*Join game */
            println("[$guestPlayerName] Join game...")
            client.joinGame(name = guestPlayerName, sessionID = sessionIDQueue.take())
            Thread.sleep(6000)
            val testGame = guestRootService.currentGame
            assertEquals(testGame!!.players.size, gameInitMessage.players.size)
            for (i in gameInitMessage.players.indices) {
                assertEquals(testGame.players[i].name, gameInitMessage.players[i].name)
                assertEquals(testGame.players[i].color, gameInitMessage.players[i].color)
            }
            assertEquals(gameInitMessage.routeTiles, testGame.routeTiles)
            assertEquals(gameInitMessage.gameBoard.gateTokens, testGame.gameBoard.gateTokens)
            assertEquals(testGame.tokens, gameInitMessage.tokens)
            Property(client.connectionState).await(ConnectionState.WAITING_FOR_OPPONENTS_TURN)
            latch.countDown()
            latch.await()

        }

        runBlocking {
            joinAll(
                hostThread, guestThread
            )
            guestRootService.networkService.disconnect()
            hostRootService.networkService.disconnect()
        }
    }


    /**
     *  The function [newHostGameStartTest] are to test, if the sending
     *  of a Route Tile are correct
     */
    @Test
    fun newHostGameStartTest() {
        val latch = CountDownLatch(2)
        val sessionIDQueue: BlockingQueue<String> = ArrayBlockingQueue(1)
        val hostPlayerName = "host"
        val guestPlayerName = "guest"
        val hostThread = coroutineScope.launch {
            val host = hostRootService.testNetworkService/* Host game */
            println("[$hostPlayerName] Hosting game...")
            host.hostGame(name = hostPlayerName)
            host.testClient?.apply {
                onGameActionResponse = {
                    println("[$hostPlayerName] Received GameActionResponse with status ${it.status}")
                    when (it.status) {
                        GameActionResponseStatus.INVALID_JSON -> println("[$hostPlayerName] Invalid JSON: ${it.errorMessages}")

                        else -> {}
                    }
                }
                onCreateGameResponse = {
                    println("[$hostPlayerName] Received CreateGameResponse with status ${it.status}")
                }
            }
            Thread.sleep(5000)
            val testclient = host.testClient
            checkNotNull(testclient)
            val sessionID = testclient.sessionID
            checkNotNull(sessionID)
            sessionIDQueue.put(sessionID)
            Thread.sleep(1500)
            val players = mutableListOf(
                Player("guest", color = TokenColor.RED), Player("host", color = TokenColor.BLUE)
            )
            host.startNewHostedGame(players, notSharedGates = true)
            Thread.sleep(500)
            Property(host.connectionState).await(ConnectionState.WAITING_FOR_OPPONENTS_TURN)
            latch.countDown()
            latch.await()
        }

        val guestThread = coroutineScope.launch {
            println("[$guestPlayerName] Connecting...")
            val client = guestRootService.testNetworkService
            client.disconnect()
            client.connect(name = guestPlayerName)/*Join game */
            println("[$guestPlayerName] Join game...")
            client.joinGame(name = guestPlayerName, sessionID = sessionIDQueue.take())
            Thread.sleep(6000)
            val testGame = guestRootService.currentGame
            assertEquals(hostRootService.currentGame!!.players.size, testGame!!.players.size)
            for (i in gameInitMessage.players.indices) {
                assertEquals(hostRootService.currentGame!!.players[i].name, testGame.players[i].name)
                assertEquals(hostRootService.currentGame!!.players[i].color, testGame.players[i].color)
                assertEquals(hostRootService.currentGame!!.players[i].handTile, testGame.players[i].handTile)
            }
            assertEquals(hostRootService.currentGame!!.gameBoard.gateTokens, testGame.gameBoard.gateTokens)
            assertEquals(52, testGame.routeTiles.size)
            assertEquals(hostRootService.currentGame!!.routeTiles, testGame.routeTiles)
            Property(client.connectionState).await(ConnectionState.PLAYING_MY_TURN)

            latch.countDown()
            latch.await()

        }

        runBlocking {
            joinAll(
                hostThread, guestThread
            )
            guestRootService.networkService.disconnect()
            hostRootService.networkService.disconnect()
        }
    }

    /**
     *  The function [receiveAndSendTilePlaceTest] are to test, if the sending
     *  of the received Tile are correct and in teh correct position
     */
    @Test
    fun receiveAndSendTilePlaceTest() {
        val latch = CountDownLatch(2)
        val sessionIDQueue: BlockingQueue<String> = ArrayBlockingQueue(1)
        val hostPlayerName = "host"
        val guestPlayerName = "guest"
        val hostThread = coroutineScope.launch {
            val host = hostRootService.testNetworkService/* Host game */
            println("[$hostPlayerName] Hosting game...")
            host.hostGame(name = hostPlayerName)
            host.testClient?.apply {
                onGameActionResponse = {
                    println("[$hostPlayerName] Received GameActionResponse with status ${it.status}")
                    when (it.status) {
                        GameActionResponseStatus.INVALID_JSON -> println("[$hostPlayerName] Invalid JSON: ${it.errorMessages}")

                        else -> {}
                    }
                }
                onCreateGameResponse = {
                    println("[$hostPlayerName] Received CreateGameResponse with status ${it.status}")
                }
            }
            Thread.sleep(5000)
            val testclient = host.testClient
            checkNotNull(testclient)
            val sessionID = testclient.sessionID
            checkNotNull(sessionID)
            sessionIDQueue.put(sessionID)
            Thread.sleep(1500)
            val players = mutableListOf(
                Player("host", color = TokenColor.RED),
                Player("Alice", color = TokenColor.WHITE),
                Player("guest", color = TokenColor.BLUE)
            )
            host.startNewHostedGame(players, notSharedGates = true)
            Thread.sleep(500)
            val currentPlayerIndex = hostRootService.currentGame!!.currentPlayerIndex
            val testTile = hostRootService.currentGame!!.players[currentPlayerIndex].handTile
            val coordinate = Coordinate(1, 1)
            hostRootService.gameService.changePlayer()
            host.sendPlacedTile(testTile!!, coordinate)
            Thread.sleep(500)
            Property(host.connectionState).await(ConnectionState.PLAYING_MY_TURN)
            hostRootService.playerTurnService.rotateTileLeft(testTile)
            hostRootService.gameService.changePlayer()
            host.sendPlacedTile(testTile,coordinate)
            Thread.sleep(500)
            Property(host.connectionState).await(ConnectionState.WAITING_FOR_OPPONENTS_TURN)
            latch.countDown()
            latch.await()
        }

        val guestThread = coroutineScope.launch {
            println("[$guestPlayerName] Connecting...")
            val client = guestRootService.testNetworkService/*Join game */
            println("[$guestPlayerName] Join game...")
            client.joinGame(name = guestPlayerName, sessionID = sessionIDQueue.take())
            Thread.sleep(6000)
            Thread.sleep(1000)
            Property(client.connectionState).await(ConnectionState.WAITING_FOR_OPPONENTS_TURN)
            Thread.sleep(1000)
            //Property(client.connectionState).await(ConnectionState.PLAYING_MY_TURN)
            latch.countDown()
            latch.await()

        }
        runBlocking {
            joinAll(
                hostThread, guestThread
            )
            guestRootService.networkService.disconnect()
            hostRootService.networkService.disconnect()
        }
    }
}