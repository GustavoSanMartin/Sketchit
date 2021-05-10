package ca.gustavo.sketchit.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun Menu(navController: NavController) {

    Column {
        var name: String by remember { mutableStateOf("") }
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
        Button(onClick = { navController.navigate("lobby/$name") }) {
            Text(text = "join")
        }

    }
}