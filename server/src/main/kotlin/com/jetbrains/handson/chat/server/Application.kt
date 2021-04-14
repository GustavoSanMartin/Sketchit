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

@Suppress("unused")
fun Application.module() {
    install(WebSockets)
    routing {
        val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
        webSocket("/chat") {
            println("adding user")
            val thisConnection = Connection(this)
            connections += thisConnection

            try {
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    try {
                        val coordinate = Json.decodeFromString<Coordinates>(receivedText)
                        println(coordinate)

                        connections.forEach {
                            if (it != thisConnection) {
                                it.session.send(Json.encodeToString(coordinate))
                            }
                        }
                    } catch (e: SerializationException) {
                        println("$receivedText was not a coordinate. Discarding")
                    }

                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                println("Removing $thisConnection!")
                connections -= thisConnection
            }
        }
    }
}

data class Connection(val session: DefaultWebSocketSession) {
    private val lastId = AtomicInteger(0)
    val name = "user${lastId.getAndIncrement()}"
}

@Serializable
data class Status(val status: String)

@Serializable
data class Coordinates(val x: Float, val y: Float)