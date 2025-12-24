package com.example.outgamble_android.presentation.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.outgamble_android.data.repository.AuthRepository
import com.example.outgamble_android.data.state.ResultState

class RegisterViewModel: ViewModel() {
    private val repository = AuthRepository()

    private val _registerState = MutableLiveData<ResultState<String>>()
    val registerState: LiveData<ResultState<String>> get() = _registerState

    fun register(fullname: String, username: String, password: String) {
        repository.register(fullname, username, password) {
            _registerState.postValue(it)
        }
    }
}