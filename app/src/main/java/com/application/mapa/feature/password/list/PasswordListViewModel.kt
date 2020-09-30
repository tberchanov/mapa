package com.application.mapa.feature.password.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.mapa.data.model.Password
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PasswordListViewModel @ViewModelInject constructor() : ViewModel() {

    var passwordList by mutableStateOf(listOf<Password>())
        private set

    fun loadPasswords() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(4000)
            passwordList = listOf(
                Password("1", "Name", "Value"),
                Password("2", "Name2", "Value2"),
                Password("3", "Name3", "Value3")
            )
        }
    }
}