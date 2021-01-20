package com.application.mapa.feature.password.data.usecase

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SaveTextToClipboardUseCase @Inject constructor(
    @ApplicationContext
    context: Context
) {

    private val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager

    fun execute(text: String) {
        clipboardManager?.setPrimaryClip(ClipData.newPlainText("text", text))
    }
}