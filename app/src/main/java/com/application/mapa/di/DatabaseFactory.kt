package com.application.mapa.di

import android.content.Context
import com.application.mapa.data.database.AppDatabase
import com.application.mapa.feature.encription.Decryptor
import dagger.hilt.android.qualifiers.ApplicationContext

class DatabaseFactory(
    @ApplicationContext
    private val context: Context,
    private val decryptor: Decryptor
) {

    private var database: AppDatabase? = null

    fun openDatabase(password: String) {
        database = AppDatabase.getInstance(
            password,
            context,
            decryptor
        )
    }

    @Throws(IllegalStateException::class)
    fun getDatabase(): AppDatabase {
        return database ?: throw IllegalStateException("Database is not opened yet!")
    }

    fun closeDatabase() {
        database?.close()
        database = null
    }
}