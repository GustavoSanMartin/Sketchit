package ca.gustavo.sketchit.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import ca.gustavo.sketchit.domain.MainViewModel
import androidx.compose.runtime.getValue

@Composable
fun Lobby(
    navController: NavController,
    name: String,
    vm: MainViewModel = viewModel()
) {
    vm.sendName(name)
    val items: List<String> by vm.getPainterNames().collectAsState(initial = emptyList())
    Column {
        PainterList(painters = items)
        Button(onClick = { navController.navigate("draw") }) {
            Text(text = "start")
        }
    }
}

@Composable
fun PainterList(painters: List<String>) {
    LazyColumn {
        items(painters) { name ->
            PainterRow(name = name)
        }
    }
}

@Composable
fun PainterRow(name: String) {
    Text(text = name)
}