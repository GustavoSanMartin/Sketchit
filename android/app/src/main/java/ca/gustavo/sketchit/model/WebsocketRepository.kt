package ca.gustavo.sketchit.model

import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.ConnectException
import javax.inject.Inject
import javax.inject.Singleton

private const val COORDINATE_TYPE = "c"
private const val NAME_TYPE = "n"

@Singleton
class WebsocketRepository @Inject constructor(private val ktorClient: HttpClient) {

    private val _incomingCoordinatesFlow = MutableStateFlow<Coordinate?>(null)
    val incomingCoordinatesFlow: Flow<Coordinate> = _incomingCoordinatesFlow.filterNotNull()

    private val _incomingNamesFlow = MutableStateFlow<List<String>?>(null)
    val incomingNamesFlow: Flow<List<String>> = _incomingNamesFlow.filterNotNull()

    private val _outgoingCoordinatesFlow = MutableStateFlow<Coordinate?>(null)
    private val outgoingCoordinatesFlow = _outgoingCoordinatesFlow.filterNotNull().map {
        val coordinate = "${it.x},${it.y}"
        ServerMsg(COORDINATE_TYPE, coordinate)
    }

    private val _outgoingNamesFlow = MutableStateFlow<String?>(null)
    private val outgoingNamesFlow = _outgoingNamesFlow.filterNotNull().map {
        ServerMsg(NAME_TYPE, it)
    }

    private val outgoingFlow: Flow<ServerMsg> = merge(outgoingNamesFlow, outgoingCoordinatesFlow)

    fun sendCoordinate(coordinate: Coordinate) {
        _outgoingCoordinatesFlow.value = coordinate
    }

    fun sendName(name: String) {
        _outgoingNamesFlow.value = name
    }

    suspend fun startWebsocket() {

        try {
            ktorClient.webSocket(
                method = HttpMethod.Get,
                host = "10.0.2.2",
                port = 8080,
                path = "/draw"
            ) {
                val messageOutputRoutine = launch { outputMessages() }
                val userInputRoutine = launch { inputMessages() }

                userInputRoutine.join() // Wait for completion; either "exit" or error
                messageOutputRoutine.cancelAndJoin()
            }
        } catch (e: ConnectException) {
            print(e.message)
        }

    }

    private suspend fun DefaultClientWebSocketSession.outputMessages() {

        for (message in incoming) {
            message as? Frame.Text ?: continue
            try {
                val serverMsg = Json.decodeFromString<ServerMsg>(message.readText())
                when (serverMsg.type) {
                    COORDINATE_TYPE -> {
                        val coordinateList = serverMsg.payload
                            .split(",")
                            .map { it.toFloat() }

                        val coordinate = Coordinate(coordinateList[0], coordinateList[1])
                        print("goose: incoming coordinate $coordinate")
                        _incomingCoordinatesFlow.value = coordinate
                    }
                    NAME_TYPE -> {
                        val nameList = serverMsg.payload.split(",")
                        print("goose: incoming name $nameList")
                        _incomingNamesFlow.value = nameList
                    }
                }

            } catch (e: Exception) {
                println("${message.readText()} was not a server message")
            }
        }
    }

    private suspend fun DefaultClientWebSocketSession.inputMessages() {
        outgoingFlow.collect {
            print("goose: outgoing $it")
            try {
                send(Json.encodeToString(it))
            } catch (e: SerializationException) {
                println("$it was not a server message. Discarding")
            }
        }
    }
}
