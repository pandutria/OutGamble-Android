package com.example.outgamble_android.data.repository

import com.example.outgamble_android.data.ai.remote.AiRetrofitInstance
import com.example.outgamble_android.data.ai.request.PredictRequest
import com.example.outgamble_android.data.model.Prediction
import com.example.outgamble_android.data.state.ResultState
import retrofit2.Response

class AiRepository {
    suspend fun prediction(link: String): Response<Prediction> {
        val response = AiRetrofitInstance.api.prediction(PredictRequest(link))
        return response
    }
}