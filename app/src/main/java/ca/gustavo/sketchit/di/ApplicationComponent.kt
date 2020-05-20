package ca.gustavo.sketchit.di

import ca.gustavo.sketchit.domain.MainViewModel
import ca.gustavo.sketchit.view.DrawingActivity
import ca.gustavo.sketchit.view.MainActivity
import ca.gustavo.sketchit.view.ViewingActivity
import dagger.Component

@Component(modules = [NetworkModule::class])
interface ApplicationComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: ViewingActivity)
    fun inject(activity: DrawingActivity)

    fun mainViewModelFactory(): ViewModelFactory<MainViewModel>
}