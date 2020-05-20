package ca.gustavo.sketchit.model

import android.util.Log
import ca.gustavo.sketchit.BuildConfig
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class FirestoreRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val drawingCoordinates: MutableList<String> = mutableListOf()

    fun getDrawingPoints(): Flow<List<Pair<Float, Float>>> {
        val channel = ConflatedBroadcastChannel<List<Pair<Float, Float>>>()
        firestore.collection(BuildConfig.COLLECTION_NAME)
            .document(BuildConfig.TEST_DOCUMENT) // TODO: search for rounds
            .addSnapshotListener { snapshot, exception ->
                val drawing = snapshot?.get("points") as? List<*>

                if (drawing != null) {
                    val points = drawing.filterIsInstance<String>().map { points ->
                        val pair = points.split(",").map { it.toFloat() }
                        pair.first() to pair.last()
                    }

                    channel.offer(points)

                } else if (exception != null) {
                    Log.d("Firebase exception", exception.message.toString())
                }
            }

        return channel.asFlow()
    }

    fun resetDrawing() {
        firestore.collection(BuildConfig.COLLECTION_NAME)
            .document(BuildConfig.TEST_DOCUMENT)
            .set(emptyMap<String, String>())
    }

    fun updateDrawing(x: Int, y: Int) {
        drawingCoordinates.add("$x,$y")
        firestore.collection(BuildConfig.COLLECTION_NAME)
            .document(BuildConfig.TEST_DOCUMENT)
            .update("points", drawingCoordinates)
    }

    suspend fun getDrawingPrompt(): String {
        val snapshot = firestore.collection(BuildConfig.COLLECTION_NAME)
            .document(BuildConfig.TEST_DOCUMENT).get().await()

        return snapshot.get("prompt") as? String ?: ""
    }

    fun pushDrawingPrompt(prompt: String) {
        firestore.collection(BuildConfig.COLLECTION_NAME)
            .document(BuildConfig.TEST_DOCUMENT)
            .update("prompt", prompt)
    }
}

suspend fun <T> Task<T>.await(): T {
    // fast path
    if (isComplete) {
        val e = exception
        return if (e == null) {
            if (isCanceled) {
                throw CancellationException("Task $this was cancelled normally.")
            } else {
                @Suppress("UNCHECKED_CAST")
                result as T
            }
        } else {
            throw e
        }
    }
    return suspendCancellableCoroutine { cont ->
        addOnCompleteListener {
            val e = exception
            if (e == null) {
                @Suppress("UNCHECKED_CAST")
                if (isCanceled) cont.cancel() else cont.resume(result as T, {})
            } else {
                cont.resumeWithException(e)
            }
        }
    }
}