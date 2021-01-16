package com.application.mapa.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.application.mapa.data.database.AppDatabase.Companion.DB_VERSION
import com.application.mapa.data.database.dao.PasswordDao
import com.application.mapa.data.database.model.Password
import com.application.mapa.feature.encryption.database.Decryptor
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Database(
    entities = [Password::class],
    version = DB_VERSION
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun passwordDao(): PasswordDao

    companion object {
        private const val DB_NAME = "mapa-db"
        const val DB_VERSION = 1

        fun getInstance(
            passcode: String,
            context: Context,
            decryptor: Decryptor
        ): AppDatabase = buildDatabase(passcode, context, decryptor)

        private fun buildDatabase(
            passcode: String,
            context: Context,
            decryptor: Decryptor
        ): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DB_NAME
            )
                .openHelperFactory(getCipherHelper(passcode, decryptor))
                .build()
        }

        private fun getCipherHelper(
            passcode: String,
            decryptor: Decryptor
        ): SupportSQLiteOpenHelper.Factory {
            val dbKey = decryptor.getCharKey(passcode.toCharArray())
            return SupportFactory(SQLiteDatabase.getBytes(dbKey))
        }
    }
}