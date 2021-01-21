package com.application.mapa.feature.password.generator.model

sealed class PasswordGeneratorScreenAction {

    class ModifyGeneratedPassword(
        val value: String
    ) : PasswordGeneratorScreenAction()

    class ModifyGenerationSetting(
        val setting: PasswordGenerationSetting
    ) : PasswordGeneratorScreenAction()

    class ModifyPasswordLength(
        val length: Int
    ) : PasswordGeneratorScreenAction()

    object GeneratePassword : PasswordGeneratorScreenAction()

    object ApplyPassword : PasswordGeneratorScreenAction()
}