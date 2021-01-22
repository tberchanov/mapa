package com.application.mapa.feature.password.generator.model

data class CurrentPasswordArg(
    val currentPassword: String
) {
    override fun toString(): String {
        return "|$currentPassword|"
    }

    companion object {

        fun fromString(str: String): CurrentPasswordArg {
            return CurrentPasswordArg(str.replace("|", ""))
        }
    }
}