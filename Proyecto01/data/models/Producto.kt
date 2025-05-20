package com.example.proyecto01.models

data class Producto(
    val id: Int? = null,
    val nombre: String,
    val precio: Double,
    val descripcion: String?,
    val imagen: ByteArray?,
    val imagenPath: String? = null
)