package com.application.mapa.feature.fingerprint.usecase

import androidx.biometric.BiometricPrompt
import com.application.mapa.feature.fingerprint.CryptographyManager
import com.application.mapa.feature.fingerprint.repository.CiphertextRepository

class EncryptAndStoreDataUseCase constructor(
    private val cryptographyManager: CryptographyManager,
    private val ciphertextRepository: CiphertextRepository
) {

    fun execute(
        authResult: BiometricPrompt.AuthenticationResult,
        data: String
    ) {
        authResult.cryptoObject?.cipher?.apply {
            val encryptedServerTokenWrapper = cryptographyManager.encryptData(data, this)
            ciphertextRepository.saveCiphertext(encryptedServerTokenWrapper)
        }
    }
}