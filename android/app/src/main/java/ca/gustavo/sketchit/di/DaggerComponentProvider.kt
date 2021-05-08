package ca.gustavo.sketchit.di

import android.app.Activity
import androidx.fragment.app.Fragment
import ca.gustavo.sketchit.SketchitApp

val Activity.injector: ApplicationComponent get() = (application as SketchitApp).appComponent
val Fragment.injector: ApplicationComponent get() = (requireActivity().application as SketchitApp).appComponent