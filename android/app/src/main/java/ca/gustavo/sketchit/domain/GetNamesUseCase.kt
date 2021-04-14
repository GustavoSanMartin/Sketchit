package ca.gustavo.sketchit.domain

import ca.gustavo.sketchit.model.WebsocketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNamesUseCase @Inject constructor(private val websocketRepository: WebsocketRepository) {
    operator fun invoke(): Flow<List<String>> = websocketRepository.incomingNamesFlow
}