package ca.gustavo.sketchit.domain

import ca.gustavo.sketchit.model.Coordinate
import ca.gustavo.sketchit.model.WebsocketRepository
import javax.inject.Inject

class UpdateDrawingUseCase @Inject constructor(private val websocketRepository: WebsocketRepository) {
    operator fun invoke(x: Float, y: Float) {
        websocketRepository.sendCoordinate(Coordinate(x, y))
    }
}