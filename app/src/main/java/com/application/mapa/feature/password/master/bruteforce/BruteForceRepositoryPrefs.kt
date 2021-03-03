package com.application.mapa.feature.password.master.bruteforce

import android.content.Context

class BruteForceRepositoryPrefs(
    context: Context
) : BruteForceRepository {

    private val prefs = context.getSharedPreferences(BRUTE_FORCE_PREFS, Context.MODE_PRIVATE)

    override suspend fun getFailedUnlockDateMillis(): Long {
        return prefs.getLong(LAST_FAILED_UNLOCK_DATE_MILLIS_KEY, 0)
    }

    override suspend fun saveFailedUnlockDateMillis(millis: Long) {
        prefs.edit()
            .putLong(LAST_FAILED_UNLOCK_DATE_MILLIS_KEY, millis)
            .apply()
    }

    companion object {
        private const val BRUTE_FORCE_PREFS = "BRUTE_FORCE_PREFS"
        private const val LAST_FAILED_UNLOCK_DATE_MILLIS_KEY = "LAST_FAILED_UNLOCK_DATE_MILLIS"
    }
}