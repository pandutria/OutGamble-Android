package com.example.outgamble_android.presentation.consultation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.outgamble_android.data.model.Doctor
import com.example.outgamble_android.data.repository.DoctorRepository
import com.example.outgamble_android.data.state.ResultState

class ConsultationViewModel: ViewModel() {
    private val repository = DoctorRepository()

    private val _doctorState = MutableLiveData<ResultState<List<Doctor>>>()
    val doctorState: LiveData<ResultState<List<Doctor>>> get() = _doctorState

    fun get() {
        repository.get {
            _doctorState.postValue(it)
        }
    }
}