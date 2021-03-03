package com.application.mapa.feature.password.master.model

import com.application.mapa.util.Event

data class MasterPasswordScreenState(
    val showRootError: Boolean,
    val verificationState: Event<PasswordVerificationState>?,
    val showWelcomeMessage: Boolean,
    val showTryLaterMessage: Boolean
)