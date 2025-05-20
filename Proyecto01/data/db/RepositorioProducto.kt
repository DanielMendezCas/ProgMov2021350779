package com.example.proyecto01.data.db

import android.content.ContentValues
import android.content.Context
import androidx.core.database.getBlobOrNull
import androidx.core.database.getDoubleOrNull
import androidx.core.database.getStringOrNull
import com.example.proyecto01.models.Producto

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

fun eliminarProducto(context: Context, idProducto: Int) {
    val dbHelper = DBHelper(context)
    val db = dbHelper.writableDatabase
    db.delete("producto", "id_producto = ?", arrayOf(idProducto.toString()))
    db.close()
}

fun obtenerProductoPorId(context: Context, id: Int): Producto? {
    val dbHelper = DBHelper(context)
    val db = dbHelper.readableDatabase
    val cursor = db.rawQuery("SELECT * FROM producto WHERE id_producto = ?", arrayOf(id.toString()))

    var producto: Producto? = null
    if (cursor.moveToFirst()) {
        producto = Producto(
            id = id,
            nombre = cursor.getStringOrNull(cursor.getColumnIndexOrThrow("nombre")) ?: "",
            precio = cursor.getDoubleOrNull(cursor.getColumnIndexOrThrow("precio")) ?: 0.0,
            descripcion = cursor.getStringOrNull(cursor.getColumnIndexOrThrow("descripcion")),
            imagen = cursor.getBlobOrNull(cursor.getColumnIndexOrThrow("imagen"))
        )
    }

    cursor.close()
    db.close()
    return producto
}

fun actualizarProducto(context: Context, producto: Producto) {
    val dbHelper = DBHelper(context)
    val db = dbHelper.writableDatabase

    val values = ContentValues().apply {
        put("nombre", producto.nombre)
        put("precio", producto.precio)
        put("descripcion", producto.descripcion)
        put("imagen", producto.imagen)
    }

    db.update("producto", values, "id_producto = ?", arrayOf(producto.id.toString()))
    db.close()
}

fun insertarProducto(context: Context, producto: Producto){
    val dbHelper = DBHelper(context)
    val db = dbHelper.writableDatabase
    val values = ContentValues().apply {
        put("nombre", producto.nombre)
        put("precio", producto.precio)
        put("descripcion", producto.descripcion)
        put("imagen", producto.imagen)
    }
    db.insert("producto", null, values)
    db.close()
}