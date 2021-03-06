package com.application.mapa.data.repository

import com.application.mapa.data.domain.model.Password
import kotlinx.coroutines.flow.Flow

interface PasswordRepository {

    suspend fun savePassword(password: Password)

    suspend fun getPasswords(): List<Password>

    suspend fun getPassword(id: Long): Password?

    suspend fun observePasswords(): Flow<List<Password>>

    suspend fun deletePasswords(idList: List<Long>)
}