package com.application.mapa.feature.fingerprint.usecase

import com.application.mapa.feature.fingerprint.CryptographyManager
import com.application.mapa.feature.fingerprint.repository.CiphertextRepository
import javax.crypto.Cipher

class EncryptAndStoreDataUseCase constructor(
    private val cryptographyManager: CryptographyManager,
    private val ciphertextRepository: CiphertextRepository
) {

    fun execute(
        cipher: Cipher,
        data: String
    ) {
        val encryptedServerTokenWrapper = cryptographyManager.encryptData(data, cipher)
        ciphertextRepository.saveCiphertext(encryptedServerTokenWrapper)
    }
}