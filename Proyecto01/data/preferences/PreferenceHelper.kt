package com.example.proyecto01.preferences

import android.content.Context
import androidx.core.content.edit

class PreferenceHelper(context: Context) {
    private val preferences = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)

    var currentUser: String
        get() = preferences.getString("current_user", "default") ?: "default"
        set(value) = preferences.edit { putString("current_user", value) }

    fun saveUserName(userName: String, userKey: String = currentUser) {
        preferences.edit { putString("nombre_usuario_$userKey", userName) }
    }

    fun readUserName(userKey: String = currentUser): String {
        return preferences.getString("nombre_usuario_$userKey", "") ?: ""
    }

    fun saveFavoriteColor(userColor: Int, userKey: String = currentUser) {
        preferences.edit { putInt("color_usuario_$userKey", userColor) }
    }

    fun readFavoriteColor(userKey: String = currentUser): Int {
        return preferences.getInt("color_usuario_$userKey", 0xFF6200EE.toInt()) // Morado por defecto
    }

    fun getAllUsers(): List<Pair<String, String>> {
        val allEntries = preferences.all
        return allEntries.keys
            .filter { it.startsWith("nombre_usuario_") }
            .map { it.removePrefix("nombre_usuario_") }
            .distinct()
            .map { userId -> userId to readUserName(userId) }
    }

    fun deleteUser(userKey: String) {
        preferences.edit {
            remove("nombre_usuario_$userKey")
            remove("color_usuario_$userKey")
        }
    }

    fun setDarkMode(enabled: Boolean, userKey: String = currentUser) {
        preferences.edit().putBoolean("dark_mode_$userKey", enabled).apply()
    }

    fun isDarkMode(userKey: String = currentUser): Boolean {
        return preferences.getBoolean("dark_mode_$userKey", false)
    }

    fun isUserNameTaken(name: String): Boolean {
        return getAllUsers().map { it.second }.any { it.equals(name, ignoreCase = true) }
    }

}
