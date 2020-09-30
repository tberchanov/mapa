package com.application.mapa.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Password(
    val id: String = UNDEFINED_ID,
    val name: String,
    val value: String
) : Parcelable {

    companion object {
        const val UNDEFINED_ID = "undefined"
    }
}