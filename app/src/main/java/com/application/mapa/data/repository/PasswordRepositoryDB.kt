package com.application.mapa.data.repository

import com.application.mapa.data.database.dao.PasswordDao
import com.application.mapa.data.model.Password
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class PasswordRepositoryDB @Inject constructor(
    private val passwordDao: PasswordDao
) : PasswordRepository {

    override suspend fun savePassword(password: Password) {
        passwordDao.insertPassword(
            // TODO mapping should be moved to mappers
            com.application.mapa.data.database.model.Password(
                id = password.id.takeUnless { it == Password.UNDEFINED_ID },
                name = password.name,
                value = password.value
            )
        )
    }

    override suspend fun getPasswords(): List<Password> {
        return passwordDao.getAllPasswords()
            // TODO mapping should be moved to mappers
            .map {
                Password(
                    id = it.id ?: Password.UNDEFINED_ID,
                    name = it.name,
                    value = it.value
                )
            }
    }

    override fun getPassword(id: Long): Flow<Password> {
        return passwordDao.getPassword(id).mapNotNull {
            it?.let {
                Password(
                    id = it.id ?: Password.UNDEFINED_ID,
                    name = it.name,
                    value = it.value
                )
            }
        }
    }

    override suspend fun observePasswords(): Flow<List<Password>> {
        return passwordDao.observePasswords().map { passwords ->
            passwords.map {
                // TODO mapping should be moved to mappers
                Password(
                    id = it.id ?: Password.UNDEFINED_ID,
                    name = it.name,
                    value = it.value
                )
            }
        }
    }
}