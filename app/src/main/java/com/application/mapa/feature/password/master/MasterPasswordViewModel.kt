package com.application.mapa.feature.password.master

import android.content.Context
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.mapa.di.DatabaseFactory
import com.application.mapa.feature.encription.Encryptor
import com.application.mapa.feature.encription.KeyGenerator
import com.application.mapa.feature.encription.StorableManager
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

    fun verifyMasterPassword(context: Context, password: String) {
        viewModelScope.launch(Dispatchers.Default) {
            runCatching {
                if (storableManager.storableEnabled(context)) {
                    databaseFactory.openDatabase(password)
                } else {
                    generateKeys(context, password)
                }
            }.fold(
                onSuccess = { verificationState.postValue(Event(PasswordVerified)) },
                onFailure = {
                    Log.e("MasterPasswordViewModel", "verifyMasterPassword error", it)
                    verificationState.postValue(Event(PasswordVerificationFailure))
                }
            )
        }
    }

    private fun generateKeys(context: Context, password: String) {
        keyGenerator.createNewKey()
        encryptor.persistRawKey(
            keyGenerator.rawByteKey,
            password.toCharArray(),
            context
        )
    }
}