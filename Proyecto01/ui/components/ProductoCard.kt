package com.example.proyecto01.ui.components

import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyecto01.models.Producto

@Composable
fun ProductoCard(producto: Producto, navController: NavController, onEliminar: () -> Unit) {
    val context = LocalContext.current

    val imagenBitmap = producto.imagen?.let {
        BitmapFactory.decodeByteArray(it, 0, it.size).asImageBitmap()
    }

    var mostrarDialogo by remember { mutableStateOf(false) }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar \"${producto.nombre}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    Toast.makeText(context, "Producto Eliminado", Toast.LENGTH_SHORT).show()

                    onEliminar()
                    mostrarDialogo = false
                }) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            if (imagenBitmap != null) {
                Image(
                    bitmap = imagenBitmap,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Sin imagen", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                Text("$${producto.precio}", style = MaterialTheme.typography.bodyMedium)
                producto.descripcion?.let {
                    Text(it, style = MaterialTheme.typography.bodySmall)
                }
            }
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically)
            ) {
                IconoRedondo(
                    icon = Icons.Default.Edit,
                    onClick = { navController.navigate("editar/${producto.id}") },
                    contentDescription = "Editar"
                )
                Spacer(modifier = Modifier.height(8.dp))
                IconoRedondo(
                    icon = Icons.Default.Delete,
                    onClick = { mostrarDialogo = true },
                    backgroundColor = MaterialTheme.colorScheme.error,
                    contentDescription = "Eliminar"
                )
            }
        }
    }
}