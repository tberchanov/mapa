package com.application.mapa.feature.password.generator.usecase

import com.application.mapa.feature.password.generator.manager.PasswordManager

class GetPasswordLengthRangeUseCase {

    fun execute() = MIN_PASSWORD_LENGTH..PasswordManager.MAX_PASSWORD_LENGTH

    companion object {
        private const val MIN_PASSWORD_LENGTH = 1F
    }
}