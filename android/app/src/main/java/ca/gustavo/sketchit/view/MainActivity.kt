package ca.gustavo.sketchit.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ca.gustavo.sketchit.R
import ca.gustavo.sketchit.SketchitApp

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as SketchitApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
