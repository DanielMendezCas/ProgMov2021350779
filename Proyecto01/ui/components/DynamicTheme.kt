package com.example.proyecto01.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun DynamicTheme(
    primaryColor: Color,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val baseColorScheme = if (darkTheme) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }

    val colorScheme = baseColorScheme.copy(
        primary = primaryColor,
        primaryContainer = primaryColor.copy(alpha = 0.8f),
        onPrimary = Color.White,
        secondary = primaryColor.copy(alpha = 0.6f),
        onSecondary = Color.White
    )

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
