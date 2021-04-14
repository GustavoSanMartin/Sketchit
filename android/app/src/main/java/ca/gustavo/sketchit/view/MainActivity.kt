package ca.gustavo.sketchit.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ca.gustavo.sketchit.MyApplication
import ca.gustavo.sketchit.R
import ca.gustavo.sketchit.di.injector
import ca.gustavo.sketchit.domain.MainViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> { injector.mainViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as MyApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
