package ca.gustavo.sketchit.domain

import ca.gustavo.sketchit.model.FirestoreRepository
import javax.inject.Inject

class UpdateDrawingUseCase @Inject constructor(private val repository: FirestoreRepository) {
    operator fun invoke(x: Float, y: Float) = repository.updateDrawing(x.toInt(), y.toInt())
}