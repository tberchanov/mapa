package com.application.mapa.feature.fingerprint.repository

import android.content.Context
import com.application.mapa.feature.fingerprint.CiphertextWrapper
import com.google.gson.Gson

class CiphertextRepositoryImpl(
    private val context: Context,
) : CiphertextRepository {

    private val gson = Gson()
    private val mode = Context.MODE_PRIVATE

    override fun saveCiphertext(ciphertextWrapper: CiphertextWrapper) {
        val json = gson.toJson(ciphertextWrapper)
        context.getSharedPreferences(SHARED_PREFS_FILENAME, mode)
            .edit()
            .putString(CIPHERTEXT_WRAPPER, json)
            .apply()
    }

    override fun getCiphertext(): CiphertextWrapper? {
        val json = context.getSharedPreferences(
            SHARED_PREFS_FILENAME,
            mode
        ).getString(CIPHERTEXT_WRAPPER, null)
        return gson.fromJson(json, CiphertextWrapper::class.java)
    }

    override fun hasCiphertextSaved(): Boolean {
        return context.getSharedPreferences(SHARED_PREFS_FILENAME, mode)
            .contains(CIPHERTEXT_WRAPPER)
    }

    companion object {
        private const val SHARED_PREFS_FILENAME = "biometric_prefs"
        private const val CIPHERTEXT_WRAPPER = "ciphertext_wrapper"
    }
}