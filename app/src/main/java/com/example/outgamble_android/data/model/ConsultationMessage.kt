package com.example.outgamble_android.data.model

data class ConsultationMessage(
    val id: String = "",
    val consultationId: String = "",
    val senderId: String = "",
    val message: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
