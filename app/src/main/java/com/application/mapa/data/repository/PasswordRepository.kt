package com.application.mapa.data.repository

import com.application.mapa.data.model.Password
import kotlinx.coroutines.flow.Flow

interface PasswordRepository {

    suspend fun savePassword(password: Password)

    suspend fun getPasswords(): List<Password>

    fun getPassword(id: Long): Flow<Password>

    suspend fun observePasswords(): Flow<List<Password>>
}