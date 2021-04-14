package ca.gustavo.sketchit.model

import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private const val COORDINATE_TYPE = "c"
private const val NAME_TYPE = "n"

@Singleton
class WebsocketRepository @Inject constructor(private val ktorClient: HttpClient) {

    private val incomingCoordinatesChannel = ConflatedBroadcastChannel<Coordinate>()
    val incomingCoordinatesFlow = incomingCoordinatesChannel.asFlow()

    private val incomingNamesChannel = ConflatedBroadcastChannel<List<String>>()
    val incomingNamesFlow = incomingNamesChannel.asFlow()

    private val outgoingCoordinatesChannel = ConflatedBroadcastChannel<Coordinate>()
    private val outgoingCoordinatesFlow = outgoingCoordinatesChannel.asFlow().map {
        val coordinate = "${it.x},${it.y}"
        ServerMsg(COORDINATE_TYPE, coordinate)
    }

    private val outgoingNamesChannel = ConflatedBroadcastChannel<String>()
    private val outgoingNamesFlow = outgoingNamesChannel.asFlow().map {
        ServerMsg(NAME_TYPE, it)
    }

    private val outgoingFlow: Flow<ServerMsg> = merge(outgoingNamesFlow, outgoingCoordinatesFlow)

    fun sendCoordinate(coordinate: Coordinate) {
        outgoingCoordinatesChannel.offer(coordinate)
    }

    fun sendName(name: String) {
        outgoingNamesChannel.offer(name)
    }

    suspend fun startWebsocket() {

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
                        incomingCoordinatesChannel.offer(coordinate)
                    }
                    NAME_TYPE -> {
                        val nameList = serverMsg.payload.split(",")
                        incomingNamesChannel.offer(nameList)
                    }
                }

            } catch (e: Exception) {
                println("${message.readText()} was not a server message")
            }
        }
    }

    private suspend fun DefaultClientWebSocketSession.inputMessages() {
        outgoingFlow.collect {
            try {
                send(Json.encodeToString(it))
            } catch (e: SerializationException) {
                println("$it was not a server message. Discarding")
            }
        }
    }
}

@Serializable
data class ServerMsg(val type: String, val payload: String)

@Serializable
data class Coordinate(val x: Float, val y: Float)
