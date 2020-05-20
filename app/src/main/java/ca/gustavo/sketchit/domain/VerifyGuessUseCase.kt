package ca.gustavo.sketchit.domain

import ca.gustavo.sketchit.model.FirestoreRepository
import javax.inject.Inject

class VerifyGuessUseCase @Inject constructor(private val repository: FirestoreRepository) {
    suspend operator fun invoke(guess: String): Boolean {
        return guess == repository.getDrawingPrompt()
    }
}