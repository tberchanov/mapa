package com.application.mapa.feature.password.master

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.mapa.di.DatabaseFactory
import com.application.mapa.feature.encryption.database.Encryptor
import com.application.mapa.feature.encryption.database.KeyGenerator
import com.application.mapa.feature.encryption.database.storable.StorableManager
import com.application.mapa.feature.fingerprint.repository.CiphertextRepository
import com.application.mapa.feature.fingerprint.usecase.DecryptDataFromStorageUseCase
import com.application.mapa.feature.fingerprint.usecase.ShowBiometricPromptForDecryptionUseCase
import com.application.mapa.feature.password.master.PasswordVerificationState.PasswordVerificationFailure
import com.application.mapa.feature.password.master.PasswordVerificationState.PasswordVerified
import com.application.mapa.util.ActivityProvider
import com.application.mapa.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface MasterPasswordViewModel {
    val verificationState: LiveData<Event<PasswordVerificationState>>
    fun verifyMasterPassword(password: String)
}

class MasterPasswordViewModelImpl @ViewModelInject constructor(
    private val storableManager: StorableManager,
    private val encryptor: Encryptor,
    private val keyGenerator: KeyGenerator,
    private val databaseFactory: DatabaseFactory,
    ciphertextRepository: CiphertextRepository,
    private val showBiometricPromptForDecryptionUseCase: ShowBiometricPromptForDecryptionUseCase,
    private val activityProvider: ActivityProvider,
    private val decryptDataFromStorageUseCase: DecryptDataFromStorageUseCase
) : ViewModel(), MasterPasswordViewModel {

    override val verificationState = MutableLiveData<Event<PasswordVerificationState>>()

    init {
        if (ciphertextRepository.hasCiphertextSaved()) {
            verifyBiometricLock()
        }
    }

    private fun verifyBiometricLock() {
        activityProvider.getActivity()?.let { activity ->
            showBiometricPromptForDecryptionUseCase.execute(activity) { cipher ->
                decryptDataFromStorageUseCase.execute(cipher) {
                    verifyMasterPassword(it)
                }
            }
        }
    }

    override fun verifyMasterPassword(password: String) {
        viewModelScope.launch(Dispatchers.Default) {
            runCatching {
                if (!storableManager.storableEnabled()) {
                    generateKeys(password)
                }
                databaseFactory.openDatabase(password)
            }.fold(
                onSuccess = { verificationState.postValue(Event(PasswordVerified)) },
                onFailure = {
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