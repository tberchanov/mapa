package com.application.mapa.feature.launch

sealed class LaunchState {
    object EncryptionVerified : LaunchState()
}