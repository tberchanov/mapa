package com.application.mapa.feature.fingerprint.usecase

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import com.application.mapa.feature.fingerprint.CryptographyManager
import com.application.mapa.feature.fingerprint.R
import com.application.mapa.feature.fingerprint.repository.CiphertextRepository

class ShowBiometricPromptForDecryptionUseCase constructor(
    private val context: Context,
    private val cryptographyManager: CryptographyManager,
    private val createBiometricPromptUseCase: CreateBiometricPromptUseCase,
    private val createPromptInfoUseCase: CreatePromptInfoUseCase,
    private val ciphertextRepository: CiphertextRepository
) {

    fun execute(
        activity: AppCompatActivity,
        onSuccess: (BiometricPrompt.AuthenticationResult) -> Unit
    ) {
        ciphertextRepository.getCiphertext()?.let { textWrapper ->
            val secretKeyName = context.getString(R.string.secret_key_name)
            val cipher = cryptographyManager.getInitializedCipherForDecryption(
                secretKeyName, textWrapper.initializationVector
            )
            val biometricPrompt =
                createBiometricPromptUseCase.execute(activity, onSuccess)
            val promptInfo = createPromptInfoUseCase.execute()
            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
        }
    }
}