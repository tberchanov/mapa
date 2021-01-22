package com.application.mapa.feature.password.data

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.mapa.data.domain.model.Password
import com.application.mapa.data.repository.PasswordRepository
import com.application.mapa.feature.password.data.model.PasswordDataScreenAction
import com.application.mapa.feature.password.data.model.PasswordDataScreenAction.*
import com.application.mapa.feature.password.data.model.PasswordDataScreenState
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

    val state = MutableLiveData(getInitialState())

    fun postAction(action: PasswordDataScreenAction) = when (action) {
        is CleanData -> processCleanDataAction()
        is LoadPassword -> processLoadPasswordAction(action)
        is SavePassword -> processSavePasswordAction()
        is CopyPassword -> processCopyPasswordAction()
        is ModifyPasswordName -> processModifyPasswordNameAction(action)
        is ModifyPasswordValue -> processModifyPasswordValueAction(action)
    }

    private fun processCleanDataAction() {
        state.value = getInitialState()
    }

    private fun getInitialState() =
        PasswordDataScreenState(
            password = null,
            showNameError = false,
            showValueError = false
        )

    private fun processModifyPasswordNameAction(action: ModifyPasswordName) {
        state.value = state.value?.run {
            copy(password = password?.copy(name = action.name))
        }
    }

    private fun processModifyPasswordValueAction(action: ModifyPasswordValue) {
        state.value = state.value?.run {
            copy(password = password?.copy(value = action.value))
        }
    }

    private fun processLoadPasswordAction(action: LoadPassword) {
        val generatedPassword = generatedPasswordDataHolder.popGeneratedPassword()

        val id = action.id
        if (id == null) {
            state.value = state.value?.copy(
                password = Password(name = "", value = generatedPassword ?: ""),
                showValueError = false,
                showNameError = false
            )
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                val password = if (state.value?.password?.id == id) {
                    state.value?.password
                } else {
                    passwordRepository.getPassword(id)
                }?.let {
                    if (generatedPassword != null) {
                        it.copy(value = generatedPassword)
                    } else {
                        it
                    }
                }
                state.postValue(
                    state.value?.copy(
                        password = password,
                        showNameError = false,
                        showValueError = false
                    )
                )
            }
        }
    }

    private fun processSavePasswordAction() {
        state.value?.password?.let { password ->
            if (validatePassword(password)) {
                viewModelScope.launch(Dispatchers.IO) {
                    passwordRepository.savePassword(password)
                    savingState.postValue(Event(PasswordDataState.SavingSuccess))
                }
            }
        }
    }

    private fun validatePassword(password: Password): Boolean {
        var isPasswordValid = true
        var state = state.value

        if (password.name.isEmpty()) {
            isPasswordValid = false
            state = state?.copy(showNameError = true)
        } else {
            state = state?.copy(showNameError = false)
        }
        if (password.value.isEmpty()) {
            isPasswordValid = false
            state = state?.copy(showValueError = true)
        } else {
            state = state?.copy(showValueError = false)
        }

        if (!isPasswordValid) {
            this.state.value = state
        }
        return isPasswordValid
    }

    private fun processCopyPasswordAction() {
        state.value?.password?.let {
            saveTextToClipboardUseCase.execute(it.value)
            vibrationUseCase.execute()
        }
    }
}