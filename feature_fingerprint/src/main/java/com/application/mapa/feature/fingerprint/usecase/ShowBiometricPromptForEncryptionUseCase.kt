package com.application.mapa.feature.fingerprint.usecase

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.application.mapa.feature.fingerprint.CryptographyManager
import com.application.mapa.feature.fingerprint.R
import com.application.mapa.feature.fingerprint.usecase.CreatePromptInfoUseCase.Params
import javax.crypto.Cipher

class ShowBiometricPromptForEncryptionUseCase(
    private val context: Context,
    private val cryptographyManager: CryptographyManager,
    private val createBiometricPromptUseCase: CreateBiometricPromptUseCase,
    private val createPromptInfoUseCase: CreatePromptInfoUseCase,
) {

    fun execute(
        activity: FragmentActivity,
        onSuccess: (Cipher) -> Unit
    ) {
        val canAuthenticate = BiometricManager.from(context).canAuthenticate(BIOMETRIC_STRONG)
        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
            val secretKeyName = context.getString(R.string.secret_key_name)
            val cipher = cryptographyManager.getInitializedCipherForEncryption(secretKeyName)
            val biometricPrompt = createBiometricPromptUseCase.execute(
                activity = activity,
                processSuccess = {
                    it.cryptoObject?.cipher?.let { cipher ->
                        onSuccess(cipher)
                    }
                }
            )
            val promptInfo = createPromptInfoUseCase.execute(
                Params(
                    titleText = context.getString(R.string.biometric_prompt_title),
                    negativeText = context.getString(R.string.cancel)
                )
            )
            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
        }
    }
}