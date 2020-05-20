package ca.gustavo.sketchit.view

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import ca.gustavo.sketchit.R
import ca.gustavo.sketchit.di.injector
import ca.gustavo.sketchit.domain.MainViewModel

class DrawingActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> { injector.mainViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawing)

        viewModel.getRandomWord()

        viewModel.randomWord.observe(this, Observer {
            findViewById<TextView>(R.id.prompt).text =
                resources.getString(R.string.drawing_prompt_template, it)
        })

        findViewById<DrawingView>(R.id.drawing_view).setOnDrawListener(object : DrawingListener {
            override fun onStartDraw(x: Float, y: Float) {
                viewModel.updateDrawing(x, y)
            }

            override fun onDraw(x: Float, y: Float) {
                viewModel.updateDrawing(x, y)
            }

            override fun onEndDraw() {
                viewModel.updateDrawing(-1F, -1F)
            }
        })
    }
}
