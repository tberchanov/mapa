package com.application.mapa.feature.password.master

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.mapa.di.DatabaseFactory
import com.application.mapa.feature.check.root.CheckRootUseCase
import com.application.mapa.feature.encryption.database.Encryptor
import com.application.mapa.feature.encryption.database.KeyGenerator
import com.application.mapa.feature.encryption.database.storable.StorableManager
import com.application.mapa.feature.fingerprint.repository.CiphertextRepository
import com.application.mapa.feature.fingerprint.usecase.DecryptDataFromStorageUseCase
import com.application.mapa.feature.fingerprint.usecase.ShowBiometricPromptForDecryptionUseCase
import com.application.mapa.feature.password.master.bruteforce.BruteForceManager
import com.application.mapa.feature.password.master.model.MasterPasswordScreenState
import com.application.mapa.feature.password.master.model.PasswordVerificationState.PasswordVerificationFailure
import com.application.mapa.feature.password.master.model.PasswordVerificationState.PasswordVerified
import com.application.mapa.util.ActivityProvider
import com.application.mapa.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface MasterPasswordViewModel {
    val state: LiveData<MasterPasswordScreenState>
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
    private val decryptDataFromStorageUseCase: DecryptDataFromStorageUseCase,
    private val checkRootUseCase: CheckRootUseCase,
    private val bruteForceManager: BruteForceManager
) : ViewModel(), MasterPasswordViewModel {

    override val state = MutableLiveData(
        MasterPasswordScreenState(
            false,
            null,
            !storableManager.storableEnabled(),
            false
        )
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            state.postValue(
                state.value?.copy(showRootError = checkRootUseCase.execute())
            )
        }

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
            if (bruteForceManager.canPasswordBeChecked()) {
                verifyMasterPasswordAfterBruteForceCheck(password)
            } else {
                state.postValue(
                    state.value?.copy(
                        verificationState = Event(PasswordVerificationFailure),
                        showTryLaterMessage = true
                    )
                )
            }
        }
    }

    private suspend fun verifyMasterPasswordAfterBruteForceCheck(password: String) {
        runCatching {
            if (!storableManager.storableEnabled()) {
                generateKeys(password)
            }
            databaseFactory.openDatabase(password)
        }.fold(
            onSuccess = {
                bruteForceManager.correctPassword()
                state.postValue(
                    state.value?.copy(verificationState = Event(PasswordVerified))
                )
            },
            onFailure = {
                bruteForceManager.incorrectPassword()
                state.postValue(
                    state.value?.copy(verificationState = Event(PasswordVerificationFailure))
                )
            }
        )
    }

    private fun generateKeys(password: String) {
        keyGenerator.createNewKey()
        encryptor.persistRawKey(
            keyGenerator.rawByteKey,
            password.toCharArray()
        )
    }
}