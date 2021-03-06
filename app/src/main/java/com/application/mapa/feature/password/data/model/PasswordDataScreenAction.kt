package com.application.mapa.feature.password.data.model

sealed class PasswordDataScreenAction {
    class CleanData(val id: Long?) : PasswordDataScreenAction()
    class LoadPassword(val id: Long?) : PasswordDataScreenAction()
    object SavePassword : PasswordDataScreenAction()
    object CopyPassword : PasswordDataScreenAction()
    class ModifyPasswordName(val name: String) : PasswordDataScreenAction()
    class ModifyPasswordValue(val value: String) : PasswordDataScreenAction()
}