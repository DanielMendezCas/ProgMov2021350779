package com.example.calculadora_imc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "principal") {
        composable("principal") { UIPrincipal(navController) }
        composable("resultado/{imc}") { backStackEntry ->
            val imc = backStackEntry.arguments?.getString("imc") ?: "Sin resultado"
            UIResultado(imc, navController)
        }
    }
}

@Composable
fun UIPrincipal(navController: NavController) {
    var estatura by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }

    fun btnCalcular() {
        val estaturaDouble = estatura.toDoubleOrNull()
        val pesoDouble = peso.toDoubleOrNull()

        if (estaturaDouble != null && pesoDouble != null) {
            val imc = pesoDouble / estaturaDouble.pow(2.0)
            val resultado = when {
                imc <= 18.4 -> "Bajo peso"
                imc in 18.5..24.9 -> "Normal"
                imc in 25.0..29.9 -> "Sobrepeso"
                imc in 30.0..34.9 -> "Obesidad clase 1"
                imc in 35.0..39.9 -> "Obesidad clase 2"
                imc >= 40.0 -> "Obesidad clase 3"
                else -> "Valores inválidos"
            }

            navController.navigate("resultado/${resultado}")
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text("Calculadora de IMC", Modifier.weight(5f))
        }

        Row(Modifier.fillMaxWidth().padding(15.dp)) {
            Text("Estatura (m)")
            TextField(
                value = estatura,
                onValueChange = { estatura = it },
                Modifier.weight(1f)
            )
        }

        Row(Modifier.fillMaxWidth().padding(15.dp)) {
            Text("Peso (kg)")
            TextField(
                value = peso,
                onValueChange = { peso = it },
                Modifier.weight(1f)
            )
        }

        Row(Modifier.fillMaxWidth().padding(15.dp)) {
            Button(onClick = { btnCalcular() }, Modifier.weight(1.5f)) {
                Text("Calcular")
            }
        }
    }
}

@Composable
fun UIResultado(imc: String, navController: NavController) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Resultado:")
        Text(imc)
        /*
        Button(onClick = { navController.popBackStack() }) {
            Text("Volver a calcular")
        }
        */
    }
}