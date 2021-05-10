package ca.gustavo.sketchit.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import ca.gustavo.sketchit.R

@Composable
fun Lobby(
    navController: NavController,
    name: String,
    vm: MainViewModel = viewModel()
) {
    vm.sendName(name)
    val items: List<String> by vm.getPainterNames().collectAsState(initial = emptyList())
    Column(Modifier.padding(16.dp)) {
        PainterList(painters = items)
        Button(onClick = { navController.navigate("draw") }) {
            Text(text = "Start")
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
    MaterialTheme {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_profile),
                contentDescription = "profile"
            )
            Text(text = name, Modifier.padding(start = 8.dp))
        }
    }
}