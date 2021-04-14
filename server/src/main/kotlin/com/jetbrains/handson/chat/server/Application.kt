package com.jetbrains.handson.chat.server

import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.LinkedHashSet


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

private const val COORDINATE_TYPE = "c"
private const val NAME_TYPE = "n"

@Suppress("unused")
fun Application.module() {
    install(WebSockets)
    routing {
        val connections = Collections.synchronizedSet<Connection>(LinkedHashSet())
        webSocket("/draw") {
            suspend fun updateNames() {
                for (connection in connections) {
                    val response = connections.mapNotNull { it.name }.joinToString()

                    val msg = ServerMsg(NAME_TYPE, response)
                    println("sending to ${connection.id}: $msg")
                    connection.session.send(Json.encodeToString(msg))
                }
            }

            val thisConnection = Connection(this)
            println("adding user ${thisConnection.id}")
            connections += thisConnection

            try {
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    try {
                        val serverMsg = Json.decodeFromString<ServerMsg>(receivedText)

                        when (serverMsg.type) {
                            COORDINATE_TYPE -> {
                                println("received coordinates from ${thisConnection.id}: $serverMsg")
                                for (connection in connections) {
                                    if (connection.id == thisConnection.id) continue
                                    connection.session.send(Json.encodeToString(serverMsg))
                                }
                            }
                            NAME_TYPE -> {
                                thisConnection.name = serverMsg.payload
                                println("received name from ${thisConnection.id}: $serverMsg")
                                updateNames()
                            }
                        }
                    } catch (e: SerializationException) {
                        println("$receivedText was not a server message. Discarding")
                    }

                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                println("removing user ${thisConnection.id}!")
                connections -= thisConnection
                updateNames()
            }
        }
    }
}

data class Connection(val session: DefaultWebSocketSession, var name: String? = null) {
    val id = lastId.getAndIncrement()

    companion object {
        var lastId = AtomicInteger(0)
    }
}

@Serializable
data class ServerMsg(val type: String, val payload: String)