package com.example.proyecto01.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyecto01.data.db.eliminarProducto
import com.example.proyecto01.data.db.obtenerProductos
import com.example.proyecto01.models.Producto
import com.example.proyecto01.ui.components.ProductoCard
import com.example.proyecto01.ui.components.SeccionHeader
import com.example.proyecto01.ui.components.ThemeViewModel

@Composable
fun Principal(
    navController: NavController
) {
    val context = LocalContext.current
    val productos = remember { mutableStateListOf<Producto>() }
    val themeViewModel: ThemeViewModel = viewModel()

    LaunchedEffect(Unit) {
        productos.clear()
        productos.addAll(obtenerProductos(context))
        themeViewModel.loadFromPreferences()
    }

    Box(modifier = Modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            item {
                SeccionHeader(navController, productos)
            }
            items(productos, key = { it.id!! }) { producto ->
                ProductoCard(
                    producto = producto,
                    navController = navController,
                    onEliminar = {
                        eliminarProducto(context, producto.id!!)
                        productos.remove(producto)
                    }
                )
            }
        }
    }
}