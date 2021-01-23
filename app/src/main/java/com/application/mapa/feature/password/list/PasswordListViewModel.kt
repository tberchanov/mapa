package com.application.mapa.feature.password.list

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.mapa.data.repository.PasswordRepository
import com.application.mapa.feature.password.list.model.PasswordListState
import com.application.mapa.feature.password.list.model.SelectablePassword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface PasswordListViewModel {
    val state: PasswordListState
    fun selectPassword(selectablePassword: SelectablePassword)
    fun disableSelection()
    fun deleteSelectedPasswords()
}

class PasswordListViewModelImpl @ViewModelInject constructor(
    private val passwordRepository: PasswordRepository
) : ViewModel(), PasswordListViewModel {

    init {
        observePasswords()
    }

    override var state by mutableStateOf(
        PasswordListState(
            passwords = emptyList(),
            selectionEnabled = false
        )
    )
        private set

    private fun observePasswords() {
        viewModelScope.launch(Dispatchers.IO) {
            passwordRepository.observePasswords()
                .map { passwords ->
                    passwords.map {
                        SelectablePassword(it, false)
                    }
                }
                .collect {
                    withContext(Dispatchers.Main) {
                        state = state.copy(passwords = it)
                    }
                }
        }
    }

    override fun selectPassword(selectablePassword: SelectablePassword) {
        val passwords = selectPasswordInList(state.passwords, selectablePassword)
        val selectedPasswordQuantity = passwords.count { it.selected }
        state = state.copy(
            passwords = passwords,
            selectionEnabled = selectedPasswordQuantity != 0
        )
    }

    private fun selectPasswordInList(
        passwords: List<SelectablePassword>,
        selectablePassword: SelectablePassword
    ): List<SelectablePassword> {
        val passwordIndex = state.passwords.indexOfFirst {
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
        state = state.copy(
            passwords = state.passwords.map { it.copy(selected = false) },
            selectionEnabled = false
        )
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
        state.passwords
            .filter { it.selected }
            .map { it.password.id }
}