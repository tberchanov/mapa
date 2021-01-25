package com.application.mapa.feature.password.master.model

sealed class PasswordVerificationState {
    object PasswordVerified : PasswordVerificationState()
    object PasswordVerificationFailure : PasswordVerificationState()
}