package com.application.mapa.feature.password.data

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.application.mapa.data.domain.model.Password
import com.application.mapa.data.repository.PasswordRepository
import com.application.mapa.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PasswordDataViewModel @ViewModelInject constructor(
    private val passwordRepository: PasswordRepository
) : ViewModel() {

    val state = MutableLiveData<Event<PasswordDataState>>()

    fun savePassword(password: Password) {
        viewModelScope.launch(Dispatchers.IO) {
            passwordRepository.savePassword(password)
            state.postValue(Event(PasswordDataState.SavingSuccess))
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