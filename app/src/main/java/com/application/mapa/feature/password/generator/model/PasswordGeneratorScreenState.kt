package com.application.mapa.feature.password.generator.model

data class PasswordGeneratorScreenState(
    val generatedPassword: String,
    val settings: List<PasswordGenerationSetting>,
    val length: Int,
    val showNoSettingEnabledError: Boolean
)