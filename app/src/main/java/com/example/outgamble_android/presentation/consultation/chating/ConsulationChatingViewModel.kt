package com.example.outgamble_android.presentation.consultation.chating

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.outgamble_android.data.model.Consultation
import com.example.outgamble_android.data.model.ConsultationMessage
import com.example.outgamble_android.data.model.Doctor
import com.example.outgamble_android.data.repository.ConsultationRepository
import com.example.outgamble_android.data.repository.DoctorRepository
import com.example.outgamble_android.data.state.ResultState

class ConsulationChatingViewModel: ViewModel() {
    private val doctorRepository = DoctorRepository()
    private val consultationRepository = ConsultationRepository()

    private val _doctorState = MutableLiveData<ResultState<Doctor>>()
    val doctorState: LiveData<ResultState<Doctor>> get() = _doctorState

    private val _getConsultationState = MutableLiveData<ResultState<Consultation>>()
    val getConsultationState: LiveData<ResultState<Consultation>> get() = _getConsultationState

    private val _postConsultationState = MutableLiveData<ResultState<String>>()
    val postConsultationState: LiveData<ResultState<String>> get() = _postConsultationState

    private val _getMessageState = MutableLiveData<ResultState<List<ConsultationMessage>>>()
    val getMessageState: LiveData<ResultState<List<ConsultationMessage>>> get() = _getMessageState

    private val _sendMessageState = MutableLiveData<ResultState<String>>()
    val sendMessageState: LiveData<ResultState<String>> get() = _sendMessageState

    fun getDoctorById(id: String) {
        doctorRepository.getById(id) {
            _doctorState.postValue(it)
        }
    }

    fun getConsultation(doctorId: String, userId: String) {
        consultationRepository.get(doctorId, userId) {
            _getConsultationState.postValue(it)
        }
    }

    fun createConsultation(doctorId: String, userId: String) {
        consultationRepository.create(doctorId, userId) {
            _postConsultationState.postValue(it)
        }
    }

    fun getMessage(consultationId: String) {
        consultationRepository.getMessage(consultationId) {
            _getMessageState.postValue(it)
        }
    }

    fun sendMessage(consutationId: String, senderId: String, message: String) {
        consultationRepository.sendMessage(consutationId, senderId, message) {
            _sendMessageState.postValue(it)
        }
    }
}