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

@Singleton
class WebsocketRepository @Inject constructor(private val ktorClient: HttpClient) {

    private val incomingChannel = ConflatedBroadcastChannel<Coordinate>()
    val incomingFlow = incomingChannel.asFlow()

    private val outgoingChannel = ConflatedBroadcastChannel<Coordinate>()
    private val outgoingFlow = outgoingChannel.asFlow()

    fun sendCoordinate(coordinate: Coordinate) {
        outgoingChannel.offer(coordinate)
    }

    suspend fun startWebsocket() {

        ktorClient.webSocket(
            method = HttpMethod.Get,
            host = "10.0.2.2",
            port = 8080,
            path = "/chat"
        ) {
            val messageOutputRoutine = launch { outputMessages() }
            val userInputRoutine = launch { inputMessages() }

            println("goose: joining")
            userInputRoutine.join() // Wait for completion; either "exit" or error
            messageOutputRoutine.cancelAndJoin()
            println("goose: done")
        }
    }

    private suspend fun DefaultClientWebSocketSession.outputMessages() {

        for (message in incoming) {
            message as? Frame.Text ?: continue
            try {
                val coordinate = Json.decodeFromString<Coordinate>(message.readText())
                println("goose: received: $coordinate")
                incomingChannel.offer(coordinate)
            } catch (e: Exception) {
                println("goose: ${message.readText()} was not a coordinate")
            }
        }
    }

    private suspend fun DefaultClientWebSocketSession.inputMessages() {
        outgoingFlow.collect {
            try {
                val coordinate = Json.encodeToString(it)
                println("goose: sent: $coordinate")
                send(coordinate)
            } catch (e: SerializationException) {
                println("goose: $it was not a coordinate. Discarding")
            }
        }
    }
}

sealed class ServerMsg

@Serializable
data class Coordinate(val x: Float, val y: Float) : ServerMsg()
