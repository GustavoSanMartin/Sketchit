package ca.gustavo.sketchit.domain

import ca.gustavo.sketchit.model.Coordinate
import ca.gustavo.sketchit.model.WebsocketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDrawingPointsUseCase @Inject constructor(private val websocketRepository: WebsocketRepository) {
    operator fun invoke(): Flow<Coordinate> = websocketRepository.incomingFlow
}