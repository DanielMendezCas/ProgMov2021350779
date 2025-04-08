package com.example.proyecto01

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val descripcion: String?,
    val imagen: ByteArray?
)