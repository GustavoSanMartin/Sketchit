package ca.gustavo.sketchit.di

import ca.gustavo.sketchit.domain.MainViewModel
import ca.gustavo.sketchit.view.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface ApplicationComponent {
    fun inject(activity: MainActivity)

    fun mainViewModelFactory(): ViewModelFactory<MainViewModel>
}