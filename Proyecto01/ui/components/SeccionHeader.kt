package com.example.proyecto01.ui.components

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.pdf.PdfDocument
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.example.proyecto01.models.Producto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import android.graphics.Paint
import android.graphics.Typeface
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeccionHeader(navController: NavController, productos: List<Producto>) {
    var showMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    TopAppBar(
        title = {
            Text(
                text = "Productos Disponibles",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally)
            )
        },
        navigationIcon = {
            Box {
                IconButton(
                    onClick = { showMenu = true },
                    modifier = Modifier
                        .size(48.dp)
                        .padding(8.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Opciones",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.width(200.dp)
                ) {
                    DropdownMenuItem(
                        text = {
                            Text("Generar PDF", style = MaterialTheme.typography.bodyLarge)
                        },
                        onClick = {
                            showMenu = false
                            generarYMostrarPDF(context, productos, coroutineScope)
                        },
                        leadingIcon = {
                            Icon(Icons.Default.AccountBox, contentDescription = null)
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text("Preferencias Compartidas", style = MaterialTheme.typography.bodyLarge)
                        },
                        onClick = {
                            showMenu = false
                            navController.navigate("preferencias")
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Settings, contentDescription = null)
                        }
                    )
                }
            }
        },
        actions = {
            IconButton(
                onClick = { navController.navigate("crear") }
            ) {
                IconoRedondo(
                    icon = Icons.Default.AddCircle,
                    onClick = { navController.navigate("crear") },
                    contentDescription = "Crear"
                )
            }
        }
    )
}

fun generarYMostrarPDF(
    context: Context,
    productos: List<Producto>,
    coroutineScope: CoroutineScope
) {
    coroutineScope.launch {
        try {
            val document = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()

            var page = document.startPage(pageInfo)
            var canvas = page.canvas
            val titlePaint = Paint().apply {
                color = Color.BLACK
                textSize = 20f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }
            val bodyPaint = Paint().apply {
                color = Color.DKGRAY
                textSize = 14f
            }

            var yPos = 60f
            canvas.drawText("Lista de Productos", 50f, yPos, titlePaint)
            yPos += 40f

            // Encabezados de tabla
            canvas.drawText("Nombre", 50f, yPos, titlePaint)
            canvas.drawText("Precio", 250f, yPos, titlePaint)
            canvas.drawText("Descripción", 350f, yPos, titlePaint)
            yPos += 30f

            productos.forEach { producto ->

                // Salto de página si excede
                if (yPos > 780f) {
                    document.finishPage(page)
                    page = document.startPage(pageInfo)
                    canvas = page.canvas
                    yPos = 60f
                }

                // Dibujar datos
                canvas.drawText(producto.nombre, 50f, yPos, bodyPaint)
                canvas.drawText("$${producto.precio}", 250f, yPos, bodyPaint)
                producto.descripcion?.let {
                    canvas.drawText(it, 350f, yPos, bodyPaint)
                }
                yPos += 20f

                // Línea separadora
                canvas.drawLine(40f, yPos, 550f, yPos, bodyPaint)
                yPos += 20f
            }

            document.finishPage(page)

            val file = File(context.cacheDir, "productos_${System.currentTimeMillis()}.pdf")
            FileOutputStream(file).use {
                document.writeTo(it)
            }
            document.close()

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            }

            context.startActivity(intent)

        } catch (e: Exception) {
            Toast.makeText(context, "Error al generar PDF: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}