package com.example.proyecto01.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ColorSlider(label: String, value: Float, onValueChange: (Float) -> Unit) {
    Column {
        Text("$label: ${value.toInt()}")
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..255f,
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = when (label) {
                    "Rojo" -> Color.Red
                    "Verde" -> Color.Green
                    "Azul" -> Color.Blue
                    else -> Color.Gray
                }
            )
        )
    }
}