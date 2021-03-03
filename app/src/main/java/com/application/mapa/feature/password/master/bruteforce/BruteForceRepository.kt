package com.application.mapa.feature.password.master.bruteforce

interface BruteForceRepository {

    suspend fun getFailedUnlockDateMillis(): Long

    suspend fun saveFailedUnlockDateMillis(millis: Long)
}