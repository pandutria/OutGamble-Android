package com.example.outgamble_android.data.gemini.request

data class GeminiRequest(
    val contents: List<Content>
) {
    companion object {
        fun fromMessage(message: String): GeminiRequest {
            return GeminiRequest(
                contents = listOf(
                    Content(
                        parts = listOf(
                            Part(text = message)
                        )
                    )
                )
            )
        }
    }
}

