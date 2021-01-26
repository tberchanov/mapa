package com.application.mapa.feature.password.list.model

data class PasswordListState(
    val passwords: List<SelectablePassword>,
    val selectionEnabled: Boolean,
    val showRootError: Boolean
)