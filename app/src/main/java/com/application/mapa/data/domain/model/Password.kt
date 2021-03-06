package com.application.mapa.data.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Password(
    val id: Long = UNDEFINED_ID,
    val name: String,
    val value: String
) : Parcelable {

    companion object {
        const val UNDEFINED_ID = -1L
    }
}