package service

import entity.Edge
import entity.Indigo
import entity.Tile
import kotlinx.coroutines.*
import kotlinx.coroutines.time.withTimeout
import org.junit.jupiter.api.Test
import service.network.ConnectionState
import tools.aqua.bgw.observable.properties.Property
import java.time.Duration
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeoutException
import kotlin.test.BeforeTest

class NetworkConnectionTests {
    private lateinit var hostRootService: RootService
    private lateinit var guestRootService: RootService

    private lateinit var coroutineScope: CoroutineScope

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
                throw TimeoutException("Property ${this@await} with value ${this@await.value} did not reach the expected state $state within the specified timeout.")
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
            host.connect(name = hostPlayerName)
            host.connectionStateProperty.await(ConnectionState.CONNECTED)
            latch.countDown()
            /* Host game */
            println("[$hostPlayerName] Hosting game...")
            host.hostGame(name = hostPlayerName)
            val testclient = host.testClient
            checkNotNull(testclient)
             val sessionID =testclient.sessionID
            checkNotNull(sessionID)
            sessionIDQueue.put(sessionID)
            host.connectionStateProperty.await(ConnectionState.WAITING_FOR_GUEST)
            latch.countDown()
        }

        val guestThread = coroutineScope.launch {
            latch.await()
            println("[$guestPlayerName] Connecting...")
            val client = guestRootService.testNetworkService
            client.connect(name = guestPlayerName)
            /*Join game */
            println("[$guestPlayerName] Join game...")
            client. joinGame(name = guestPlayerName, sessionID = sessionIDQueue.take())
            client.connectionStateProperty.await(ConnectionState.GUEST_WAITING_FOR_CONFIRMATION)
        }
    }
}