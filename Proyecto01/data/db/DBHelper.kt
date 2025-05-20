package com.example.proyecto01.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

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
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}