package com.application.mapa.feature.check.root

import android.content.Context
import com.application.mapa.BuildConfig
import com.scottyab.rootbeer.RootBeer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CheckRootUseCase(
    context: Context
) {

    private val rootBeer = RootBeer(context).apply {
        setLogging(BuildConfig.DEBUG)
    }

    suspend fun execute() = withContext(Dispatchers.IO) {
        rootBeer.isRooted
    }
}