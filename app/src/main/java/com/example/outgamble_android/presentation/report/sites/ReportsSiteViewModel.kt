package com.example.outgamble_android.presentation.report.sites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.outgamble_android.data.model.Report
import com.example.outgamble_android.data.repository.ReportRepository
import com.example.outgamble_android.data.state.ResultState

class ReportsSiteViewModel: ViewModel() {
    private val repository = ReportRepository()

    private val _createState = MutableLiveData<ResultState<Report>>()
    val createState: LiveData<ResultState<Report>> get() = _createState

    fun create(site: String, date: String, desc: String, platform: String, userId: String) {
        repository.createOnlineReport(site, date, desc, platform, userId) {
            _createState.postValue(it)
        }
    }
}