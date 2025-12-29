package com.example.outgamble_android.data.ai.remote

import com.example.outgamble_android.data.ai.request.PredictRequest
import com.example.outgamble_android.data.model.Prediction
import com.example.outgamble_android.data.state.ResultState
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AiApiService {
    @POST("predict")
    suspend fun prediction(@Body request: PredictRequest): Response<Prediction>
}