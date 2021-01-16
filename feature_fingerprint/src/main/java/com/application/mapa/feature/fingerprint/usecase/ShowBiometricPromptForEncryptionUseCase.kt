package com.application.mapa.feature.fingerprint.usecase

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import com.application.mapa.feature.fingerprint.CryptographyManager
import com.application.mapa.feature.fingerprint.R

class ShowBiometricPromptForEncryptionUseCase (
    private val context: Context,
    private val cryptographyManager: CryptographyManager,
    private val createBiometricPromptUseCase: CreateBiometricPromptUseCase,
    private val createPromptInfoUseCase: CreatePromptInfoUseCase,
) {

    fun execute(
        activity: AppCompatActivity,
        onSuccess: (BiometricPrompt.AuthenticationResult) -> Unit
    ) {
        val canAuthenticate = BiometricManager.from(context).canAuthenticate()
        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
            val secretKeyName = context.getString(R.string.secret_key_name)
            val cipher = cryptographyManager.getInitializedCipherForEncryption(secretKeyName)
            val biometricPrompt =
                createBiometricPromptUseCase.execute(activity, onSuccess)
            val promptInfo = createPromptInfoUseCase.execute()
            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
        }
    }
}