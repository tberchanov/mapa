package com.application.mapa.feature.password.list

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.mapa.data.model.Password
import com.application.mapa.data.repository.PasswordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PasswordListViewModel @ViewModelInject constructor(
    private val passwordRepository: PasswordRepository
) : ViewModel() {

    var passwordList by mutableStateOf(listOf<Password>())
        private set

    fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            passwordRepository.observePasswords().collect {
                Log.e("PasswordListViewModel", "collect: $it")
                withContext(Dispatchers.Main) {
                    passwordList = it
                }
            }
        }
    }
}