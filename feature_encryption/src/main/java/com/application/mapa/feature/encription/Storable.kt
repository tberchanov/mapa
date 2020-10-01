package com.application.mapa.feature.encription

import com.google.gson.annotations.SerializedName

/**
 * Container for everything needed for decrypting the database.
 *
 * @param iv initialization vector
 * @param key encrypted database key
 * @param salt cryptographic salt
 */
data class Storable(
    @SerializedName("value1") val iv: String,
    @SerializedName("value2") val key: String,
    @SerializedName("value3") val salt: String
)