package ca.gustavo.sketchit.domain

import javax.inject.Inject

class VerifyGuessUseCase @Inject constructor() {
    suspend operator fun invoke(guess: String): Boolean {
        return true
    }
}