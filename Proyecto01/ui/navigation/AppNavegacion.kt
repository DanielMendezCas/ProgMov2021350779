package com.example.proyecto01.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyecto01.preferences.PreferenceHelper
import com.example.proyecto01.ui.screens.Crear
import com.example.proyecto01.ui.screens.Editar
import com.example.proyecto01.ui.screens.Preferencias
import com.example.proyecto01.ui.screens.Principal
import com.example.proyecto01.ui.components.DynamicTheme

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val prefs = remember { PreferenceHelper(context) }

    var primaryColor by remember { mutableStateOf(Color(prefs.readFavoriteColor())) }
    var darkTheme by remember { mutableStateOf(prefs.isDarkMode()) }

    DynamicTheme(primaryColor = primaryColor, darkTheme = darkTheme) {
        NavHost(
            navController = navController,
            startDestination = "principal"
        ) {
            composable("principal") {
                Principal(
                    navController = navController
                )
            }
            composable("crear") {
                Crear(navController = navController)
            }
            composable("editar/{productoId}",
                arguments = listOf(navArgument("productoId") { type = NavType.StringType })
            ) { backStackEntry ->
                backStackEntry.arguments?.getString("productoId")?.toIntOrNull()?.let { id ->
                    Editar(navController = navController, productoId = id)
                } ?: navController.popBackStack()
            }
            composable("preferencias") {
                Preferencias(
                    navController = navController,
                    onColorUpdate = { newColor ->
                        primaryColor = newColor
                        prefs.saveFavoriteColor(newColor.toArgb())
                    },
                    onDarkModeToggle = { enabled ->
                        darkTheme = enabled
                        prefs.setDarkMode(enabled, prefs.currentUser)
                    }
                )
            }
        }
    }
}
