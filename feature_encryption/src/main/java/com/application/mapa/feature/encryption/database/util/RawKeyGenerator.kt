package com.application.mapa.feature.encryption.database.util

import android.os.Build
import java.security.SecureRandom

/**
 * Generates a random 32 byte key.
 *
 * @return a byte array containing random values
 */
internal fun generateRandomKey(): ByteArray =
    ByteArray(64).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            SecureRandom.getInstanceStrong().nextBytes(this)
        } else {
            SecureRandom().nextBytes(this)
        }
    }