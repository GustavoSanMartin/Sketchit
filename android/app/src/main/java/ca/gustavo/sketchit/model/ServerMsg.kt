package ca.gustavo.sketchit.model

import kotlinx.serialization.Serializable

@Serializable
data class ServerMsg(val type: String, val payload: String)