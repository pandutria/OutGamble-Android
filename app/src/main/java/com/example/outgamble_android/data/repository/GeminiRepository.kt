package com.example.outgamble_android.data.repository

import com.example.outgamble_android.data.gemini.api.GeminiRetrofitIntance
import com.example.outgamble_android.data.gemini.request.Content
import com.example.outgamble_android.data.gemini.request.GeminiRequest
import com.example.outgamble_android.data.gemini.request.Part
import java.io.IOException
import java.net.SocketTimeoutException

class GeminiRepository {
    private val apiKey = "AIzaSyB10XBGHoC_Xnfqb2eHzxQcxOLkBf9rBys"
    suspend fun sendMessage(message: String): String {
        return try {
            val request = GeminiRequest.fromMessage(message)
            val response = GeminiRetrofitIntance.gemini
                .generateContent(apiKey, request)

            if (response.isSuccessful) {
                response.body()
                    ?.candidates
                    ?.firstOrNull()
                    ?.content
                    ?.parts
                    ?.firstOrNull()
                    ?.text
                    ?: "Maaf, saya tidak dapat menjawab saat ini 🙏"
            } else {
                "Terjadi gangguan server (${response.code()})"
            }

        } catch (e: SocketTimeoutException) {
            "Koneksi terlalu lama, silakan coba lagi 🙏"
        } catch (e: IOException) {
            "Tidak ada koneksi internet"
        } catch (e: Exception) {
            "Terjadi kesalahan sistem"
        }
    }


}