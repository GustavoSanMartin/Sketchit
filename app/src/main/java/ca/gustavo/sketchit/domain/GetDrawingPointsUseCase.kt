package ca.gustavo.sketchit.domain

import ca.gustavo.sketchit.model.FirestoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDrawingPointsUseCase @Inject constructor(private val firestoreRepository: FirestoreRepository) {
    operator fun invoke(): Flow<List<Pair<Float, Float>>> = firestoreRepository.getDrawingPoints()
}