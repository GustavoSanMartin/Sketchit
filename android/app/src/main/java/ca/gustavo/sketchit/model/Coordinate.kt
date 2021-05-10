package ca.gustavo.sketchit.model

import kotlinx.serialization.Serializable

@Serializable
data class Coordinate(val x: Float = -1F, val y: Float = -1F)