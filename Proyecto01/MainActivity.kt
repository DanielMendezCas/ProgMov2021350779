package com.example.proyecto01

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { UIPrincipal() }
    }
}

fun obtenerProductos(context: Context): List<Producto> {
    val dbHelper = DBHelper(context)
    val db = dbHelper.readableDatabase
    val cursor = db.rawQuery("SELECT * FROM producto", null)

    val productos = mutableListOf<Producto>()
    while (cursor.moveToNext()) {
        val id = cursor.getInt(cursor.getColumnIndexOrThrow("id_producto"))
        val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
        val precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio"))
        val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"))
        val imagen = cursor.getBlob(cursor.getColumnIndexOrThrow("imagen"))

        productos.add(Producto(id, nombre, precio, descripcion, imagen))
    }

    cursor.close()
    db.close()
    return productos
}


@Composable
fun UIPrincipal() {
    val context = LocalContext.current
    val productos = remember { obtenerProductos(context) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        item {
            EncabezadoSeccion()
        }
        items(productos) { producto ->
            ProductoCard(producto)
        }
    }
}

@Composable
fun IconoRedondo(
    icon: ImageVector,
    onClick: () -> Unit,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    iconTint: Color = Color.White,
    size: Dp = 40.dp,
    contentDescription: String? = null
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconTint
        )
    }
}

@Composable
fun EncabezadoSeccion() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                color = Color.LightGray.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Productos Disponibles",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.weight(1f)
        )
        IconoRedondo(
            icon = Icons.Default.Add,
            onClick = { /* Acción agregar */ },
            contentDescription = "Agregar Producto"
        )
    }
}

@Composable
fun ProductoCard(producto: Producto) {
    val imagenBitmap = producto.imagen?.let {
        BitmapFactory.decodeByteArray(it, 0, it.size).asImageBitmap()
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
                    onClick = { /* Acción editar */ },
                    contentDescription = "Editar"
                )
                Spacer(modifier = Modifier.height(8.dp))
                IconoRedondo(
                    icon = Icons.Default.Delete,
                    onClick = { /* Acción eliminar */ },
                    backgroundColor = MaterialTheme.colorScheme.error,
                    contentDescription = "Eliminar"
                )
            }

        }
    }
}
