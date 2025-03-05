package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticaApp(onExit = {finish()})
        }
    }
}

@Composable
fun PracticaApp(onExit: () -> Unit) {
    var op by remember { mutableIntStateOf(0) }
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Opciones")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { op = 1 }) { Text("Ingresar tres números y sumarlos") }
        Button(onClick = { op = 2 }) { Text("Ingresar nombre completo") }
        Button(onClick = { op = 3 }) { Text("Calcular tiempo vivido") }
        Button(onClick = { onExit() }) { Text("Salir") }

        Spacer(modifier = Modifier.height(16.dp))

        when (op) {
            1 -> Suma()
            2 -> Nombre()
            3 -> TiempoVivido()
        }
    }
}


@Composable
fun Suma() {
    var num1 by remember { mutableStateOf("") }
    var num2 by remember { mutableStateOf("") }
    var num3 by remember { mutableStateOf("") }
    var resultado by remember { mutableIntStateOf(0) }
    Column {
        OutlinedTextField(value = num1, onValueChange = { num1 = it }, label = { Text("Número 1") })
        OutlinedTextField(value = num2, onValueChange = { num2 = it }, label = { Text("Número 2") })
        OutlinedTextField(value = num3, onValueChange = { num3 = it }, label = { Text("Número 3") })
        Button(onClick = { resultado = (num1.toIntOrNull() ?: 0) + (num2.toIntOrNull() ?: 0) + (num3.toIntOrNull() ?: 0) }) {
            Text("Sumar")
        }
        Text("Resultado: $resultado")
    }
}

@Composable
fun Nombre() {
    var nombre by remember { mutableStateOf("") }
    Column {
        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre Completo") })
        Text("Bienvenido, $nombre!")
    }
}

@Composable
fun TiempoVivido() {
    var fechaNacimiento by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf("") }

    fun calcular(){
        try {
            val fechaN = LocalDate.parse(fechaNacimiento)
            val fechaActual = LocalDate.now()
            val periodo = Period.between(fechaN, fechaActual)
            val mesesVividos = periodo.toTotalMonths()
            val dias = ChronoUnit.DAYS.between(fechaN, fechaActual)
            val semanas = dias / 7
            val horas = dias * 24
            val minutos = horas * 60
            val segundos = minutos * 60
            resultado = "Meses: $mesesVividos, Semanas: $semanas, Días: $dias, Horas: $horas, Minutos: $minutos, Segundos: $segundos"
        } catch (e: Exception) {
            resultado = "El formato de fecha es incorrecto"
        }
    }

    Column {
        OutlinedTextField(value = fechaNacimiento, onValueChange = { fechaNacimiento = it }, label = { Text("Fecha de nacimiento (AAAA-MM-DD)") })
        Button(onClick = {calcular()}) {
            Text("Calcular")
        }
        Text(resultado)
    }
}
