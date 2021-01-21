package com.application.mapa.feature.password.generator.model

import androidx.annotation.StringRes

const val UPPER_CASE_SYMBOLS = 0
const val LOW_CASE_SYMBOLS = 1
const val NUMBERS = 2
const val SPECIAL_SYMBOLS = 3

data class PasswordGenerationSetting(
    @StringRes val nameRes: Int,
    val id: Int,
    val enabled: Boolean
)