package com.example.proyecto01.ui.components

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import com.example.proyecto01.preferences.PreferenceHelper

class ThemeViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = PreferenceHelper(application)

    var primaryColor by mutableStateOf(Color(prefs.readFavoriteColor()))
        private set

    var darkTheme by mutableStateOf(prefs.isDarkMode())
        private set

    fun updateColor(color: Color) {
        primaryColor = color
    }

    fun updateDarkTheme(enabled: Boolean) {
        darkTheme = enabled
    }

    fun loadFromPreferences() {
        primaryColor = Color(prefs.readFavoriteColor())
        darkTheme = prefs.isDarkMode()
    }
}
