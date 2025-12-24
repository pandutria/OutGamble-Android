package com.example.outgamble_android.presentation.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.outgamble_android.data.model.User
import com.example.outgamble_android.data.repository.AuthRepository
import com.example.outgamble_android.data.state.ResultState

class LoginViewModel: ViewModel() {
    private val repository = AuthRepository()

    private val _loginState = MutableLiveData<ResultState<User>>()
    val loginState: LiveData<ResultState<User>> get() = _loginState

    fun login(username: String, password: String) {
        repository.login(username, password) {
            _loginState.postValue(it)
        }
    }
}