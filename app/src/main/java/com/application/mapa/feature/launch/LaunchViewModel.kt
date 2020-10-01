package com.application.mapa.feature.launch

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.mapa.feature.encription.Encryptor
import com.application.mapa.feature.encription.KeyGenerator
import com.application.mapa.feature.encription.StorableManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LaunchViewModel @ViewModelInject constructor(
    private val storableManager: StorableManager,
    private val encryptor: Encryptor,
    private val keyGenerator: KeyGenerator
) : ViewModel() {

    val state = MutableLiveData<LaunchState>()

    fun verifyEncryption(context: Context) {
        viewModelScope.launch(Dispatchers.Default) {
            if (!storableManager.storableEnabled(context)) {
                generateKeys(context)
            }
            state.postValue(LaunchState.EncryptionVerified)
        }
    }

    private fun generateKeys(context: Context) {
        keyGenerator.createNewKey()
        encryptor.persistRawKey(
            keyGenerator.rawByteKey,
            /*TODO user should enter this value*/"passcode".toCharArray(),
            context
        )

    }
}