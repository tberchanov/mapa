package com.application.mapa.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.application.mapa.data.database.model.Password
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPassword(password: Password)

    @Query("SELECT * FROM password")
    fun getAllPasswords(): List<Password>

    @Query("SELECT * FROM password WHERE :id = id")
    fun getPassword(id: Long): Flow<Password?>

    @Query("SELECT * FROM password")
    fun observePasswords(): Flow<List<Password>>
}