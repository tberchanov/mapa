package com.application.mapa.feature.fingerprint.usecase

import androidx.biometric.BiometricPrompt
import com.application.mapa.feature.fingerprint.CryptographyManager
import com.application.mapa.feature.fingerprint.repository.CiphertextRepository

class DecryptDataFromStorageUseCase constructor(
    private val cryptographyManager: CryptographyManager,
    private val ciphertextRepository: CiphertextRepository
) {

    fun execute(
        authResult: BiometricPrompt.AuthenticationResult,
        onSuccess: (String) -> Unit
    ) {
        ciphertextRepository.getCiphertext()?.let { textWrapper ->
            authResult.cryptoObject?.cipher?.let {
                val plaintext =
                    cryptographyManager.decryptData(textWrapper.ciphertext, it)
                onSuccess(plaintext)
            }
        }
    }
}