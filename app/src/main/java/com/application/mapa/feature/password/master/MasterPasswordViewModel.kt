package com.application.mapa.feature.password.master

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.mapa.di.DatabaseFactory
import com.application.mapa.feature.encryption.database.Encryptor
import com.application.mapa.feature.encryption.database.KeyGenerator
import com.application.mapa.feature.encryption.database.storable.StorableManager
import com.application.mapa.feature.password.master.PasswordVerificationState.PasswordVerificationFailure
import com.application.mapa.feature.password.master.PasswordVerificationState.PasswordVerified
import com.application.mapa.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MasterPasswordViewModel @ViewModelInject constructor(
    private val storableManager: StorableManager,
    private val encryptor: Encryptor,
    private val keyGenerator: KeyGenerator,
    private val databaseFactory: DatabaseFactory
) : ViewModel() {

    val verificationState = MutableLiveData<Event<PasswordVerificationState>>()

    fun verifyMasterPassword(password: String) {
        viewModelScope.launch(Dispatchers.Default) {
            runCatching {
                if (!storableManager.storableEnabled()) {
                    generateKeys(password)
                }
                databaseFactory.openDatabase(password)
            }.fold(
                onSuccess = { verificationState.postValue(Event(PasswordVerified)) },
                onFailure = {
                    Log.e("MasterPasswordViewModel", "verifyMasterPassword error", it)
                    verificationState.postValue(Event(PasswordVerificationFailure))
                }
            )
        }
    }

    private fun generateKeys(password: String) {
        keyGenerator.createNewKey()
        encryptor.persistRawKey(
            keyGenerator.rawByteKey,
            password.toCharArray()
        )
    }
}