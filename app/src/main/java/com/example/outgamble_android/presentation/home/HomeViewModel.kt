package com.example.outgamble_android.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.outgamble_android.data.model.News
import com.example.outgamble_android.data.repository.NewsRepository
import com.example.outgamble_android.data.state.ResultState

class HomeViewModel:ViewModel() {
    val newsRepository = NewsRepository()

    private val _newsState = MutableLiveData<ResultState<List<News>>>()
    val newsState: LiveData<ResultState<List<News>>> get() = _newsState

    fun getNews() {
        newsRepository.get {
            _newsState.postValue(it)
        }
    }
}