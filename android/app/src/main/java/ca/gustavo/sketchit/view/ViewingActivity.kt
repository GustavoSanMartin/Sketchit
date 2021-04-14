package ca.gustavo.sketchit.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import ca.gustavo.sketchit.MyApplication
import ca.gustavo.sketchit.R
import ca.gustavo.sketchit.di.injector
import ca.gustavo.sketchit.domain.MainViewModel
import ca.gustavo.sketchit.model.Coordinate

class ViewingActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> { injector.mainViewModelFactory() }

    private lateinit var drawingView: DrawingView
    private var isDrawing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as MyApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewing)

        drawingView = findViewById(R.id.viewing_view)

        viewModel.startListeningToDrawingPoints()
        viewModel.drawingPoints.observe(this, {
            println("goose: viewing: $it")
            onReceiveSnapshot(it)
        })

        viewModel.isGuessSuccessful.observe(this, Observer { isCorrect ->
            if (isCorrect == null) return@Observer
            val toastMsg = if (isCorrect) R.string.success_msg else R.string.incorrect_msg
            Toast.makeText(this, toastMsg, Toast.LENGTH_SHORT).show()
        })

        findViewById<Button>(R.id.guess_btn).setOnClickListener {
            viewModel.checkGuess(findViewById<EditText>(R.id.guess).text.toString())
        }
    }

    private fun onReceiveSnapshot(point: Coordinate) {
        println(point)
        if (point.x == -1F && point.y == -1F) {
            isDrawing = false
            runOnUiThread {
                drawingView.endDraw()
            }
        } else if (!isDrawing) {
            isDrawing = true
            runOnUiThread {
                drawingView.startDraw(point.x, point.y)
            }
        } else {
            runOnUiThread {
                drawingView.draw(point.x, point.y)
            }
        }
    }
}