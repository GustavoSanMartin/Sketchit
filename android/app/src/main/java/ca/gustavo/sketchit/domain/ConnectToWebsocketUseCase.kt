package ca.gustavo.sketchit.domain

import ca.gustavo.sketchit.model.WebsocketRepository
import javax.inject.Inject

class ConnectToWebsocketUseCase @Inject constructor(private val websocketRepository: WebsocketRepository) {
    suspend operator fun invoke() {
        websocketRepository.startWebsocket()
    }
}