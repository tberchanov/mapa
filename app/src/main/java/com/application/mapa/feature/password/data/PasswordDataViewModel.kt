package com.application.mapa.feature.password.data

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.mapa.data.model.Password
import com.application.mapa.data.repository.PasswordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PasswordDataViewModel @ViewModelInject constructor(
    private val passwordRepository: PasswordRepository
) : ViewModel() {

    var state = MutableLiveData<PasswordDataState>()

    fun savePassword(password: Password) {
        viewModelScope.launch(Dispatchers.IO) {
            passwordRepository.savePassword(password)
            state.postValue(PasswordDataState.SavingSuccess)
        }
    }
}