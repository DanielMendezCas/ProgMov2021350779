package com.example.proyecto01.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyecto01.preferences.PreferenceHelper
import com.example.proyecto01.ui.components.DynamicTheme
import com.example.proyecto01.ui.components.RGBColorPicker
import com.example.proyecto01.ui.components.ThemeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Preferencias(navController: NavController,
                 onColorUpdate: (Color) -> Unit,
                 onDarkModeToggle: (Boolean) -> Unit) {
    val context = LocalContext.current
    val prefs = remember { PreferenceHelper(context) }

    // Estados
    var userName by remember { mutableStateOf(prefs.readUserName()) }
    var text by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(Color(prefs.readFavoriteColor())) }
    var expanded by remember { mutableStateOf(false) }
    var allUsers by remember { mutableStateOf(prefs.getAllUsers()) }
    var currentUser by remember { mutableStateOf(prefs.currentUser) }
    var isDarkMode by remember { mutableStateOf(prefs.isDarkMode()) }
    val isDefaultUser by remember(currentUser) {
        derivedStateOf { currentUser == "default" }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val themeViewModel: ThemeViewModel = viewModel()


    LaunchedEffect(currentUser) {
        userName = prefs.readUserName()
        selectedColor = Color(prefs.readFavoriteColor())
        text = userName
        isDarkMode = prefs.isDarkMode(currentUser)
    }

    DynamicTheme(primaryColor = selectedColor,
                    darkTheme = isDarkMode) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Preferencias Compartidas",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    actions = {
                        Box(
                            modifier = Modifier
                                .wrapContentSize(Alignment.TopStart)
                        ) {
                            IconButton(
                                onClick = { expanded = true },
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Seleccionar usuario",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.fillMaxWidth(0.5f)
                            ) {
                                allUsers.forEach { (userId, userName) ->
                                    DropdownMenuItem(
                                        text = { Text(userName.ifBlank { "Usuario sin nombre ($userId)" }) },
                                        onClick = {
                                            prefs.currentUser = userId
                                            currentUser = userId
                                            expanded = false
                                        }
                                    )
                                }

                                HorizontalDivider()

                                DropdownMenuItem(
                                    text = { Text("Nuevo usuario") },
                                    onClick = {
                                        prefs.currentUser = "${System.currentTimeMillis()}"
                                        currentUser = prefs.currentUser
                                        text = ""
                                        selectedColor = Color(0xFF6200EE)
                                        allUsers = prefs.getAllUsers()
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(padding)
                    .padding(horizontal = 24.dp)
                    .padding(vertical = 15.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Usuario: $userName")

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Actualizar Nombre") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp)
                )

                RGBColorPicker(color = selectedColor, onColorChange = { selectedColor = it })

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Modo oscuro")
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = {
                            isDarkMode = it
                        }
                    )
                }

                Button(
                    onClick = {
                        if (text.isBlank()) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("El nombre de usuario no puede estar vacío")
                            }
                        }  else if (prefs.isUserNameTaken(text) && text != userName) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Este nombre ya está en uso")
                            }
                        }else {
                            prefs.saveUserName(text)
                            prefs.saveFavoriteColor(selectedColor.toArgb())
                            userName = prefs.readUserName()

                            onDarkModeToggle(isDarkMode)
                            onColorUpdate(selectedColor)

                            themeViewModel.updateColor(selectedColor)
                            themeViewModel.updateDarkTheme(isDarkMode)

                            Toast.makeText(context, "Preferencias Actualizadas", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Guardar Preferencias")
                }
                Button(
                    onClick = {
                        if (isDefaultUser) return@Button

                        Toast.makeText(context, "Usuario Eliminado", Toast.LENGTH_SHORT).show()
                        prefs.deleteUser(prefs.currentUser)
                        allUsers = prefs.getAllUsers()

                        if (allUsers.isNotEmpty()) {
                            prefs.currentUser = allUsers.first().toString()
                        } else {
                            prefs.currentUser = "default"
                        }

                        currentUser = prefs.currentUser
                        userName = prefs.readUserName()
                        selectedColor = Color(prefs.readFavoriteColor())
                        isDarkMode = prefs.isDarkMode(currentUser)

                        onColorUpdate(selectedColor)
                        onDarkModeToggle(isDarkMode)
                        themeViewModel.updateColor(selectedColor)
                        themeViewModel.updateDarkTheme(isDarkMode)

                        text = userName
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ),
                    enabled = !isDefaultUser
                ) {
                    Text("Eliminar Usuario Seleccionado")
                }

            }
        }
    }
}