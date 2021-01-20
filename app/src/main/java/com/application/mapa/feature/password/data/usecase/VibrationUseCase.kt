package com.application.mapa.feature.password.data.usecase

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class VibrationUseCase @Inject constructor(
    @ApplicationContext
    private val context: Context
) {

    private val vibratorService = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator

    fun execute() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibratorService?.vibrate(
                VibrationEffect.createOneShot(
                    VIBRATION_DURATION_MILLIS,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            //deprecated in API 26
            @Suppress("DEPRECATION")
            vibratorService?.vibrate(VIBRATION_DURATION_MILLIS)
        }
    }

    companion object {
        private const val VIBRATION_DURATION_MILLIS = 51L
    }
}