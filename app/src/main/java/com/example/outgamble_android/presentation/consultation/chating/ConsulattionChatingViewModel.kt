package com.example.outgamble_android.presentation.consultation.chating

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.outgamble_android.data.model.Doctor
import com.example.outgamble_android.data.repository.DoctorRepository
import com.example.outgamble_android.data.state.ResultState

class ConsulattionChatingViewModel: ViewModel() {
    private val doctorRepository = DoctorRepository()

    private val _doctorState = MutableLiveData<ResultState<Doctor>>()
    val doctorState: LiveData<ResultState<Doctor>> get() = _doctorState

    fun getDoctorById(id: String) {
        doctorRepository.getById(id) {
            _doctorState.postValue(it)
        }
    }
}