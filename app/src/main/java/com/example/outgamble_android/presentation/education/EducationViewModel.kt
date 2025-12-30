package com.example.outgamble_android.presentation.education

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.outgamble_android.data.model.Education
import com.example.outgamble_android.data.repository.EducationRepository
import com.example.outgamble_android.data.state.ResultState

class EducationViewModel: ViewModel() {
    private val repository = EducationRepository()

    private val _getState = MutableLiveData<ResultState<List<Education>>>()
    val getState: LiveData<ResultState<List<Education>>> get() = _getState

    fun get() {
        repository.get {
            _getState.postValue(it)
        }
    }
}