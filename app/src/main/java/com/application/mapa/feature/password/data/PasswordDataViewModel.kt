package com.application.mapa.feature.password.data

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.application.mapa.data.domain.model.Password
import com.application.mapa.data.repository.PasswordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PasswordDataViewModel @ViewModelInject constructor(
    private val passwordRepository: PasswordRepository
) : ViewModel() {

    val state = MutableLiveData<PasswordDataState>()

    fun savePassword(password: Password) {
        viewModelScope.launch(Dispatchers.IO) {
            passwordRepository.savePassword(password)
            state.postValue(PasswordDataState.SavingSuccess)
        }
    }

    fun getPassword(id: Long?): LiveData<Password> {
        return if (id == null) {
            MutableLiveData()
        } else {
            passwordRepository.getPassword(id).asLiveData()
        }
    }
}