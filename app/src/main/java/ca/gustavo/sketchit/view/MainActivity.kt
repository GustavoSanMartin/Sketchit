package ca.gustavo.sketchit.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ca.gustavo.sketchit.MyApplication
import ca.gustavo.sketchit.R
import ca.gustavo.sketchit.di.injector
import ca.gustavo.sketchit.domain.MainViewModel
import io.ktor.client.statement.*

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> { injector.mainViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as MyApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.connect()

        findViewById<Button>(R.id.btn_draw).setOnClickListener {
            val intent = Intent(this, DrawingActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btn_view).setOnClickListener {
            val intent = Intent(this, ViewingActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btn_reset).setOnClickListener {
            viewModel.resetDrawing()
        }
    }
}
