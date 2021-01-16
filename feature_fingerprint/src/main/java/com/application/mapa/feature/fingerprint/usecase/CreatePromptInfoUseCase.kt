package com.application.mapa.feature.fingerprint.usecase

import androidx.biometric.BiometricPrompt

class CreatePromptInfoUseCase {

    fun execute() = createPromptInfo()

    // TODO create string resources
    private fun createPromptInfo(): BiometricPrompt.PromptInfo =
        BiometricPrompt.PromptInfo.Builder()
            .setTitle("Title")
            .setSubtitle("Subtitle")
            .setDescription("Description")
            .setConfirmationRequired(false)
            .setNegativeButtonText("Negative")
            .build()
}