package com.example.proyecto01.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RGBColorPicker(color: Color, onColorChange: (Color) -> Unit) {
    var red by remember { mutableStateOf(color.red * 255f) }
    var green by remember { mutableStateOf(color.green * 255f) }
    var blue by remember { mutableStateOf(color.blue * 255f) }

    LaunchedEffect(color) {
        red = color.red * 255f
        green = color.green * 255f
        blue = color.blue * 255f
    }

    val composedColor = Color(red / 255f, green / 255f, blue / 255f)

    LaunchedEffect(red, green, blue) {
        onColorChange(composedColor)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Selecciona un color:", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        ColorSlider("Rojo", red) { red = it }
        ColorSlider("Verde", green) { green = it }
        ColorSlider("Azul", blue) { blue = it }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(composedColor)
                .border(2.dp, Color.Black)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "HEX: ${composedColor.toHex()}",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}

fun Color.toHex(): String {
    val r = (red * 255).toInt().coerceIn(0, 255)
    val g = (green * 255).toInt().coerceIn(0, 255)
    val b = (blue * 255).toInt().coerceIn(0, 255)
    return String.format("#%02X%02X%02X", r, g, b)
}