package com.application.mapa.feature.password.master.bruteforce

import android.os.SystemClock

class BruteForceManager(
    private val bruteForceRepository: BruteForceRepository
) {

    private var attemptsCounter: Int = 0

    suspend fun incorrectPassword() {
        attemptsCounter++
        if (attemptsCounter >= MAX_ATTEMPTS_COUNT) {
            val passwordCheckProhibitionPeriod = getLastFailedUnlockMillis() + PASSWORD_CHECK_PROHIBITION_DURATION
            if (passwordCheckProhibitionPeriod - SystemClock.elapsedRealtime() < MAX_PASSWORD_CHECK_PROHIBITION_PERIOD) {
                bruteForceRepository.saveFailedUnlockDateMillis(passwordCheckProhibitionPeriod)
                attemptsCounter = 0
            }
        }
    }

    suspend fun correctPassword() {
        attemptsCounter = 0
        bruteForceRepository.saveFailedUnlockDateMillis(0)
    }

    suspend fun canPasswordBeChecked(): Boolean {
        return SystemClock.elapsedRealtime() > bruteForceRepository.getFailedUnlockDateMillis()
    }

    private suspend fun getLastFailedUnlockMillis(): Long {
        return bruteForceRepository.getFailedUnlockDateMillis().let {
            if (it == 0L) {
                SystemClock.elapsedRealtime()
            } else {
                it
            }
        }
    }

    companion object {
        private const val MAX_ATTEMPTS_COUNT = 3
        private const val PASSWORD_CHECK_PROHIBITION_DURATION = 5 * 60 * 1000L
        private const val MAX_PASSWORD_CHECK_PROHIBITION_PERIOD = 30 * 60 * 1000L
    }
}