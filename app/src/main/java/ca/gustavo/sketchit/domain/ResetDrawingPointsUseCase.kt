package ca.gustavo.sketchit.domain

import ca.gustavo.sketchit.model.FirestoreRepository
import javax.inject.Inject

class ResetDrawingPointsUseCase @Inject constructor(private val firestoreRepository: FirestoreRepository) {
    operator fun invoke() = firestoreRepository.resetDrawing()
}