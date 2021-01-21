package com.application.mapa.feature.password.data.model

import com.application.mapa.data.domain.model.Password

data class PasswordDataScreenState(
    val password: Password?,
    val showNameError: Boolean,
    val showValueError: Boolean
)