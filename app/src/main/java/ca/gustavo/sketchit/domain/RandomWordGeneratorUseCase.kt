package ca.gustavo.sketchit.domain

import ca.gustavo.sketchit.model.WordRepository
import javax.inject.Inject

class RandomWordGeneratorUseCase @Inject constructor(
    private val wordRepository: WordRepository,
) {
    suspend operator fun invoke(): String {
//        val randomWord = wordRepository.getRandomWords().first()
//        firestoreRepository.pushDrawingPrompt(randomWord)
//        wordRepository.getResponse()
        return "randomWord"
    }
}