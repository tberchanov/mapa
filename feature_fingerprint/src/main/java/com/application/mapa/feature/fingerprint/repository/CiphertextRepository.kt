package com.application.mapa.feature.fingerprint.repository

import com.application.mapa.feature.fingerprint.CiphertextWrapper

interface CiphertextRepository {

    fun saveCiphertext(ciphertextWrapper: CiphertextWrapper)

    fun getCiphertext(): CiphertextWrapper?

    fun clearCiphertext()

    fun hasCiphertextSaved(): Boolean
}