package com.application.mapa.feature.settings.usecase

import com.application.mapa.data.repository.PasswordRepository
import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.spec.DSAParameterSpec
import java.security.spec.ECParameterSpec
import java.security.spec.RSAKeyGenParameterSpec

class ExportDataUseCase(
    private val passwordRepository: PasswordRepository
) {

    suspend fun execute() {
        val passwords = passwordRepository.getPasswords()

//        KeyPairGenerator.getInstance("").initialize(RSAKeyGenParameterSpec())
    }
}