package ca.gustavo.sketchit.di

import android.app.Activity
import ca.gustavo.sketchit.MyApplication

val Activity.injector: ApplicationComponent get() = (application as MyApplication).appComponent