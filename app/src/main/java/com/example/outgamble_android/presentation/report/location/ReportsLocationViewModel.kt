package com.example.outgamble_android.presentation.report.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.outgamble_android.data.model.Report
import com.example.outgamble_android.data.repository.ReportRepository
import com.example.outgamble_android.data.state.ResultState

class ReportsLocationViewModel: ViewModel() {
    private val repository = ReportRepository()

    private val _reportState = MutableLiveData<ResultState<Report>>()
    val reportState: LiveData<ResultState<Report>> get() = _reportState

    fun create(imageUrl: String, location: String, date: String, desc: String, userId: String) {
        repository.createOfflineReport(imageUrl, location, date, desc, userId) {
            _reportState.postValue(it)
        }
    }
}