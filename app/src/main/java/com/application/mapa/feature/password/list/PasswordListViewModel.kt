package com.application.mapa.feature.password.list

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.mapa.data.domain.model.Password
import com.application.mapa.data.repository.PasswordRepository
import com.application.mapa.feature.password.list.model.PasswordListState
import com.application.mapa.feature.password.list.model.SelectablePassword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PasswordListViewModel @ViewModelInject constructor(
    private val passwordRepository: PasswordRepository
) : ViewModel() {

    var state by mutableStateOf(PasswordListState(passwords = emptyList(), selectionEnabled = false))
        private set

    fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            passwordRepository.observePasswords()
                .map { passwords ->
                    passwords.map {
                        SelectablePassword(it, false)
                    }
                }
                .collect {
                    Log.e("PasswordListViewModel", "collect: $it")
                    withContext(Dispatchers.Main) {
                        state = state.copy(passwords = it)
                    }
                }
        }
    }

    fun selectPassword(selectablePassword: SelectablePassword) {

    }
}