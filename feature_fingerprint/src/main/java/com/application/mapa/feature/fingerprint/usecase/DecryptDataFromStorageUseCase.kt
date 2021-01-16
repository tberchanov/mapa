package com.application.mapa.feature.fingerprint.usecase

import com.application.mapa.feature.fingerprint.CryptographyManager
import com.application.mapa.feature.fingerprint.repository.CiphertextRepository
import javax.crypto.Cipher

class DecryptDataFromStorageUseCase constructor(
    private val cryptographyManager: CryptographyManager,
    private val ciphertextRepository: CiphertextRepository
) {

    fun execute(
        cipher: Cipher,
        onSuccess: (String) -> Unit
    ) {
        ciphertextRepository.getCiphertext()?.let { textWrapper ->
            val plaintext =
                cryptographyManager.decryptData(textWrapper.ciphertext, cipher)
            onSuccess(plaintext)
        }
    }
}