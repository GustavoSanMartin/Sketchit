package ca.gustavo.sketchit.domain

import ca.gustavo.sketchit.model.WebsocketRepository
import javax.inject.Inject

class SendNameUseCase @Inject constructor(private val websocketRepository: WebsocketRepository) {
    operator fun invoke(name: String) {
        websocketRepository.sendName(name)
    }
}