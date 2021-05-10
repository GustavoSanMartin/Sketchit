package ca.gustavo.sketchit.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import ca.gustavo.sketchit.R
import ca.gustavo.sketchit.SketchitApp
import ca.gustavo.sketchit.di.injector
import ca.gustavo.sketchit.domain.MainViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> { injector.mainViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as SketchitApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "menu") {
                composable("menu") {
                    Menu(navController = navController)
                }
                composable(
                    "lobby/{name}",
                    arguments = listOf(navArgument("name") { type = NavType.StringType })
                ) { navBackStackEntry ->
                    Lobby(
                        navController,
                        navBackStackEntry.arguments?.getString("name")!!,
                        viewModel
                    )
                }
                composable("draw") {
                    Draw(vm = viewModel)
                }
            }
        }
    }
}
