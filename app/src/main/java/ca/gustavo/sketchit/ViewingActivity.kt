package ca.gustavo.sketchit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ca.gustavo.sketchit.view.DrawingView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ViewingActivity : AppCompatActivity() {

    private lateinit var drawingView: DrawingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewing)

        drawingView = findViewById(R.id.viewing_view)

        val firestore = Firebase.firestore

        firestore.collection(BuildConfig.COLLECTION_NAME)
            .document(BuildConfig.TEST_DOCUMENT) // TODO: search for rounds
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    onReceiveSnapshot(snapshot)
                } else if (exception != null) {
                    Log.d("Firebase exception", exception.message.toString())
                }
            }
    }

    private fun onReceiveSnapshot(snapshot: DocumentSnapshot) {
        val drawing = snapshot.get("points") as? List<*>

        if (drawing != null) {
            val points = drawing.filterIsInstance<String>().map { points ->
                val pair = points.split(",").map { it.toFloat() }
                pair.first() to pair.last()
            }

            var isDrawing = false
            points.forEach { point ->
                if (point.first == -1F && point.second == -1F) {
                    isDrawing = false
                    drawingView.endDraw()
                } else if (!isDrawing) {
                    isDrawing = true
                    drawingView.startDraw(point.first, point.second)
                } else {
                    drawingView.draw(point.first, point.second)
                }
            }
        }
    }
}