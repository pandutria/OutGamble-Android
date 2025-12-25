package com.example.outgamble_android.data.model

data class ConsultationHistory(
    val consultationId: String = "",
    val doctor: Doctor = Doctor(),
    val lastMessage: String = "",
    val createdAt: Long = 0
)
