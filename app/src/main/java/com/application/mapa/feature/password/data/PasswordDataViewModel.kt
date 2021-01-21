package com.application.mapa.feature.password.data

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.mapa.data.domain.model.Password
import com.application.mapa.data.repository.PasswordRepository
import com.application.mapa.feature.password.data.usecase.SaveTextToClipboardUseCase
import com.application.mapa.feature.password.data.usecase.VibrationUseCase
import com.application.mapa.feature.password.generator.GeneratedPasswordDataHolder
import com.application.mapa.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PasswordDataViewModel @ViewModelInject constructor(
    private val passwordRepository: PasswordRepository,
    private val saveTextToClipboardUseCase: SaveTextToClipboardUseCase,
    private val vibrationUseCase: VibrationUseCase,
    private val generatedPasswordDataHolder: GeneratedPasswordDataHolder
) : ViewModel() {

    val savingState = MutableLiveData<Event<PasswordDataState>>()

    val state = MutableLiveData<Password>()

    fun savePassword(password: Password) {
        viewModelScope.launch(Dispatchers.IO) {
            passwordRepository.savePassword(password)
            savingState.postValue(Event(PasswordDataState.SavingSuccess))
        }
    }

    fun loadPassword(id: Long?) {
        if (id != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val password = passwordRepository.getPassword(id)?.let {
                    val generatedPassword = generatedPasswordDataHolder.popGeneratedPassword()
                    if (generatedPassword != null) {
                        it.copy(value = generatedPassword)
                    } else {
                        it
                    }
                }
                state.postValue(password)
            }
        }
    }

    fun onCopyClicked(text: String) {
        saveTextToClipboardUseCase.execute(text)
        vibrationUseCase.execute()
    }
}