package com.application.mapa.feature.password.data

sealed class PasswordDataState {
    object SavingSuccess : PasswordDataState()
    object SavingError : PasswordDataState()
}