package com.example.proyecto01
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

class DBHelper(context: Context) : SQLiteOpenHelper(context, "proyecto01.db", null, 1) {
    private val context: Context

    init {
        this.context = context
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query1 = """
        CREATE TABLE producto (
            id_producto INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre TEXT NOT NULL,
            precio DOUBLE NOT NULL,
            descripcion TEXT,
            imagen BLOB
        );
    """
        db?.execSQL(query1)

        insertarProducto(db, "Taladro", "Taladro Marca Truper", 2500.00, R.drawable.taladro)
        insertarProducto(db, "Desatornillador", "Desatornillador Marca Truper", 2700.00, R.drawable.desatornillador)
        insertarProducto(db, "Rotomartillo", "Rotomartillo Marca BOSCH", 3000.00, R.drawable.rotomartillo)
        insertarProducto(db, "Desarmadores", "Kit de desarmadores Marca Milwaukee", 3000.00, R.drawable.desarmadoras)
        insertarProducto(db, "Aspiradora", "Aspiradora para uso en seco y/o mojado de 9 Galones", 2696.00, R.drawable.aspiradora)
        insertarProducto(db, "Sierra", "Sierra Marca DeWalt de 550 Watts", 2435.00, R.drawable.sierra)
        insertarProducto(db, "Impearmibilizante", "Impermeabilizante Marca Fester de 19L", 1829.00, R.drawable.impermeabilizante)
        insertarProducto(db, "Esmeriladora", "Emeriladora angular de 4 1/2' de 18V", 1595.00, R.drawable.esmeriladora)
        insertarProducto(db, "Sopladora", "Sopladora eléctrica Marca Homelite de 7 Amperes", 1499.00, R.drawable.sopladora)
        insertarProducto(db, "Pala", "Pala redonda Marca Truper", 329.00, R.drawable.pala)
        insertarProducto(db, "Cubeta", "Cubeta multiusos The Home Depot", 125.00, R.drawable.cubeta)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    private fun convertirImagenABlob(imagenResId: Int): ByteArray {
        val bitmap = BitmapFactory.decodeResource(context.resources, imagenResId)
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true) //Ajuste de redimensionado
        val outputStream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream) // Puedes ajustar el formato y la calidad
        return outputStream.toByteArray()
    }

    private fun insertarProducto(db: SQLiteDatabase?, nombre: String, descripcion: String?, precio: Double?, imagenResId: Int) {
        val values = ContentValues()
        values.put("nombre", nombre)
        values.put("descripcion", descripcion)
        values.put("precio", precio)
        values.put("imagen", convertirImagenABlob(imagenResId))
        db?.insert("producto", null, values)
    }
}
