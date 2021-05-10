package ca.gustavo.sketchit_server

import io.ktor.http.cio.websocket.*
import kotlinx.serialization.Serializable
import java.util.concurrent.atomic.AtomicInteger

data class Connection(val session: DefaultWebSocketSession, var name: String? = null) {
    val id = lastId.getAndIncrement()

    companion object {
        var lastId = AtomicInteger(0)
    }
}

@Serializable
data class ServerMsg(val type: String, val payload: String)