package com.application.mapa.feature.fingerprint.usecase

import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.application.mapa.feature.fingerprint.CryptographyManager
import com.application.mapa.feature.fingerprint.R
import com.application.mapa.feature.fingerprint.repository.CiphertextRepository
import javax.crypto.Cipher

class ShowBiometricPromptForDecryptionUseCase constructor(
    private val context: Context,
    private val cryptographyManager: CryptographyManager,
    private val createBiometricPromptUseCase: CreateBiometricPromptUseCase,
    private val createPromptInfoUseCase: CreatePromptInfoUseCase,
    private val ciphertextRepository: CiphertextRepository
) {

    fun execute(
        activity: FragmentActivity,
        onSuccess: (Cipher) -> Unit
    ) {
        ciphertextRepository.getCiphertext()?.let { textWrapper ->
            val secretKeyName = context.getString(R.string.secret_key_name)
            val cipher = cryptographyManager.getInitializedCipherForDecryption(
                secretKeyName, textWrapper.initializationVector
            )
            val biometricPrompt = createBiometricPromptUseCase.execute(
                activity = activity,
                processSuccess = {
                    it.cryptoObject?.cipher?.let { cipher ->
                        onSuccess(cipher)
                    }
                })
            val promptInfo = createPromptInfoUseCase.execute(
                CreatePromptInfoUseCase.Params(
                    titleText = context.getString(R.string.biometric_prompt_title),
                    negativeText = context.getString(R.string.enter_password)
                )
            )
            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
        }
    }
}