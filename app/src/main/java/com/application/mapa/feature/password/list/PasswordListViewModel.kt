package com.application.mapa.feature.password.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.mapa.data.repository.PasswordRepository
import com.application.mapa.feature.check.root.CheckRootUseCase
import com.application.mapa.feature.password.list.model.PasswordListState
import com.application.mapa.feature.password.list.model.SelectablePassword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface PasswordListViewModel {
    val state: LiveData<PasswordListState>
    fun selectPassword(selectablePassword: SelectablePassword)
    fun disableSelection()
    fun deleteSelectedPasswords()
}

class PasswordListViewModelImpl @ViewModelInject constructor(
    private val passwordRepository: PasswordRepository,
    private val checkRootUseCase: CheckRootUseCase
) : ViewModel(), PasswordListViewModel {

    override var state = MutableLiveData(
        PasswordListState(
            passwords = emptyList(),
            selectionEnabled = false,
            false
        )
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            state.postValue(
                state.value?.copy(showRootError = checkRootUseCase.execute())
            )
        }

        observePasswords()
    }

    private fun observePasswords() {
        viewModelScope.launch(Dispatchers.IO) {
            passwordRepository.observePasswords()
                .map { passwords ->
                    passwords.map {
                        SelectablePassword(it, false)
                    }
                }
                .collect {
                    state.postValue(state.value?.copy(passwords = it))
                }
        }
    }

    override fun selectPassword(selectablePassword: SelectablePassword) {
        state.value = state.value?.let { state ->
            val passwords = selectPasswordInList(state.passwords, selectablePassword)
            val selectedPasswordQuantity = passwords.count { it.selected }
            state.copy(
                passwords = passwords,
                selectionEnabled = selectedPasswordQuantity != 0
            )
        }
    }

    private fun selectPasswordInList(
        passwords: List<SelectablePassword>,
        selectablePassword: SelectablePassword
    ): List<SelectablePassword> {
        val passwordIndex = passwords.indexOfFirst {
            it.password == selectablePassword.password
        }
        return passwords
            .toMutableList()
            .apply {
                set(
                    passwordIndex,
                    selectablePassword.copy(selected = !selectablePassword.selected)
                )
            }
    }

    override fun disableSelection() {
        state.value = state.value?.run {
            copy(
                passwords = passwords.map { it.copy(selected = false) },
                selectionEnabled = false
            )
        }
    }

    override fun deleteSelectedPasswords() {
        viewModelScope.launch(Dispatchers.IO) {
            passwordRepository.deletePasswords(getSelectedPasswordsIds())
            withContext(Dispatchers.Main) {
                disableSelection()
            }
        }
    }

    private fun getSelectedPasswordsIds() =
        state.value?.passwords
            ?.filter { it.selected }
            ?.map { it.password.id } ?: emptyList()
}