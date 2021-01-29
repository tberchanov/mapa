package com.application.mapa.util

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.application.mapa.BuildConfig

class LogLiveData<T : Any?>(private val tag: String, value: T? = null) : MutableLiveData<T>(value) {

    override fun postValue(value: T?) {
        super.postValue(value)
        if (BuildConfig.DEBUG) {
            Log.d(tag, "postValue: $value")
        }
    }

    override fun setValue(value: T?) {
        super.setValue(value)
        if (BuildConfig.DEBUG) {
            Log.d(tag, "setValue: $value")
        }
    }
}