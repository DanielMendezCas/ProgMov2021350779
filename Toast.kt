package com.example.holatoast

import android.os.Bundle

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { UIPrincipal() }
    }
}

@Composable
fun UIPrincipal() {
    val context = LocalContext.current
    var nombre by rememberSaveable { mutableStateOf("") }
    Column(
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxSize()
    ) {
        Text(text = "Saludo",
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
        )
        Text(text = "Nombre",
            color = Color.Black,
            modifier = Modifier
                .padding(5.dp)
                .offset(5.dp, 5.dp)
        )
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Ingresa tu nombre", color = Color.Black) },
            modifier = Modifier.padding(5.dp)
        )
        Button(
            onClick = {
                Toast.makeText(
                    context, "Hola $nombre!",
                        Toast.LENGTH_SHORT
                ).show()
            },
            modifier = Modifier.padding(10.dp)
                .fillMaxWidth()
        )
        {
            Text("Saludar!")
        }

    }

}

@Preview(showBackground = true)
@Composable
fun Previsualizacion() {
    UIPrincipal()
}
