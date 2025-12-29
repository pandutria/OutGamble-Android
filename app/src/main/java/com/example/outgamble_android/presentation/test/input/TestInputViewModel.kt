package com.example.outgamble_android.presentation.test.input

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.outgamble_android.data.model.Test
import com.example.outgamble_android.data.repository.TestRepository
import com.example.outgamble_android.data.state.ResultState

class TestInputViewModel: ViewModel() {
    private val repository = TestRepository()

    private val _getState = MutableLiveData<ResultState<List<Test>>>()
    val getState: LiveData<ResultState<List<Test>>> get() = _getState

    fun get() {
        repository.get {
            _getState.postValue(it)
        }
    }
}