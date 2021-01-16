package com.application.mapa.feature.fingerprint.usecase

import androidx.biometric.BiometricPrompt

class CreatePromptInfoUseCase {

    fun execute(params: Params) =
        BiometricPrompt.PromptInfo.Builder()
            .setTitle(params.titleText)
            .setNegativeButtonText(params.negativeText)
            .setConfirmationRequired(true)
            .build()

    data class Params(
        val titleText: String,
        val negativeText: String
    )
}