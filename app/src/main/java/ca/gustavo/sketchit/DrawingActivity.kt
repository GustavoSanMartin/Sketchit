package ca.gustavo.sketchit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ca.gustavo.sketchit.view.DrawingListener
import ca.gustavo.sketchit.view.DrawingView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DrawingActivity : AppCompatActivity() {

    val drawingCoordinates: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawing)

        val drawingDocument = Firebase.firestore
            .collection("rounds")
            .document("etqjkzJY0MV16aLYau7b")

        findViewById<DrawingView>(R.id.drawing_view).setOnDrawListener(object : DrawingListener {
            override fun onStartDraw(x: Float, y: Float) {
                drawingCoordinates.add("${x.toInt()},${y.toInt()}")
                drawingDocument.update("points", drawingCoordinates)
            }

            override fun onDraw(x: Float, y: Float) {
                drawingCoordinates.add("${x.toInt()},${y.toInt()}")
                drawingDocument.update("points", drawingCoordinates)
            }

            override fun onEndDraw() {
                drawingCoordinates.add("-1,-1")
                drawingDocument.update("points", drawingCoordinates)
            }
        })
    }
}
