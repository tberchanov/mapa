package com.application.mapa.feature.password.master

sealed class PasswordVerificationState {
    object PasswordVerified : PasswordVerificationState()
    object PasswordVerificationFailure : PasswordVerificationState()
}