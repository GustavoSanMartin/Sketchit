package ca.gustavo.sketchit.di

import android.app.Activity
import androidx.fragment.app.Fragment
import ca.gustavo.sketchit.MyApplication

val Activity.injector: ApplicationComponent get() = (application as MyApplication).appComponent
val Fragment.injector: ApplicationComponent get() = (requireActivity().application as MyApplication).appComponent