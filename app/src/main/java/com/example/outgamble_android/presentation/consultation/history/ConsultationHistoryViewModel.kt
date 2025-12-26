package com.example.outgamble_android.presentation.consultation.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.outgamble_android.data.model.ConsultationHistory
import com.example.outgamble_android.data.repository.ConsultationRepository
import com.example.outgamble_android.data.state.ResultState

class ConsultationHistoryViewModel: ViewModel() {
    val repository = ConsultationRepository()

    private val _historyState = MutableLiveData<ResultState<List<ConsultationHistory>>>()
    val historyState: LiveData<ResultState<List<ConsultationHistory>>> get() = _historyState

    fun getHistory(userId: String) {
        repository.getHistory(userId) {
            _historyState.postValue(it)
        }
    }
}