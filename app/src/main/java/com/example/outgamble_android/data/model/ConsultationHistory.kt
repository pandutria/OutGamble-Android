package com.example.outgamble_android.data.model

data class ConsultationHistory(
    val doctor: Doctor = Doctor(),
    val lastMessage: String = ""
)
