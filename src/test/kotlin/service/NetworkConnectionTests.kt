package service

import entity.Edge
import entity.Indigo
import entity.Tile
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
import kotlin.test.assertNull
import kotlin.test.assertTrue

class NetworkConnectionTests {
    private lateinit var hostRootService: RootService
    private lateinit var guestRootService: RootService

    private lateinit var coroutineScope: CoroutineScope

    companion object {
        const val SESSIONID = "Test123 "
    }

    private val RootService.testNetworkService: TestNetworkService
        get() = this.networkService as TestNetworkService

    private val TestNetworkService.testClient: TestNetworkClient?
        get() = this.client as? TestNetworkClient

    val treasureTiles = listOf(
        Tile(listOf(Pair(Edge.TWO, Edge.FOUR)), mutableMapOf()),
        Tile(listOf(Pair(Edge.THREE, Edge.FIVE)), mutableMapOf()),
        Tile(listOf(Pair(Edge.ZERO, Edge.FOUR)), mutableMapOf()),
        Tile(listOf(Pair(Edge.TWO, Edge.FOUR)), mutableMapOf()),
        Tile(listOf(Pair(Edge.THREE, Edge.FIVE)), mutableMapOf()),
        Tile(listOf(Pair(Edge.ZERO, Edge.FOUR)), mutableMapOf()),
    )

    lateinit var gameInitMessage: Indigo

    private fun <T> Property<T>.await(state: T, timeout: Duration = Duration.ofSeconds(5)) {
        runBlocking {
            var running = true
            val listener: ((T, T) -> Unit) = { _, nV -> running = nV != state }
            this@await.addListenerAndInvoke(this@await.value, listener)
            try {
                withTimeout(timeout) { while (running) delay(100) }
            } catch (e: TimeoutCancellationException) {
                throw TimeoutException(
                    "Property ${this@await} with value ${this@await.value}" +
                            " did not reach the expected state" +
                            " $state within the specified timeout."
                )
            } finally {
                this@await.removeListener(listener)
            }
        }
    }

    @BeforeTest
    fun setup() {
        hostRootService = RootService().apply { networkService = TestNetworkService(this) }
        guestRootService = RootService().apply { networkService = TestNetworkService(this) }
        coroutineScope = CoroutineScope(Dispatchers.Default)
    }

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
            assertNull(host.testClient)
            /* Host game */
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
                onCreateGameResponse =
                    {
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
            client.connect(name = guestPlayerName)
            /*Join game */
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
                hostThread,
                guestThread
            )
            hostRootService.networkService.disconnect()
            guestRootService.networkService.disconnect()
        }
    }



}