package com.application.mapa.feature.password.list.model

import com.application.mapa.data.domain.model.Password

data class SelectablePassword(
    val password: Password,
    val selected: Boolean
)