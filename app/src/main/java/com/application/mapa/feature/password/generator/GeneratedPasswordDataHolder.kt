package com.application.mapa.feature.password.generator

class GeneratedPasswordDataHolder {

    private var password: String? = null

    fun putGeneratedPassword(password: String) {
        this.password = password
    }

    fun popGeneratedPassword(): String? {
        return password?.also {
            password = null
        }
    }
}