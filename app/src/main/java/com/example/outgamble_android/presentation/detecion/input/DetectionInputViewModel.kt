package com.example.outgamble_android.presentation.detecion.input

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.outgamble_android.data.model.Prediction
import com.example.outgamble_android.data.repository.AiRepository
import com.example.outgamble_android.data.state.ResultState
import kotlinx.coroutines.launch

class DetectionInputViewModel : ViewModel() {

    private val repository = AiRepository()

    private val _aiState = MutableLiveData<ResultState<Prediction>>()
    val aiState: LiveData<ResultState<Prediction>> get() = _aiState

    fun predict(link: String) {
        _aiState.value = ResultState.Loading

        viewModelScope.launch {
            try {
                val response = repository.prediction(link)

                if (response.isSuccessful && response.body() != null) {
                    _aiState.postValue(
                        ResultState.Success(response.body()!!)
                    )
                } else {
                    _aiState.postValue(
                        ResultState.Error(
                            "Layanan AI sedang tidak tersedia. Silakan coba kembali dalam beberapa saat."
                        )
                    )
                }

            } catch (e: Exception) {
                _aiState.postValue(
                    ResultState.Error(
                        "Sistem AI sedang beristirahat. Mohon coba lagi sebentar ya 🙏"
                    )
                )
            }
        }
    }
}
