package ca.gustavo.sketchit

import android.app.Application
import ca.gustavo.sketchit.di.ApplicationComponent
import ca.gustavo.sketchit.di.DaggerApplicationComponent

class MyApplication : Application() {
    val appComponent: ApplicationComponent = DaggerApplicationComponent.create()
}