package com.example.outgamble_android.presentation.chatbot.chating

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.outgamble_android.data.gemini.repsonse.GeminiChatMessage
import com.example.outgamble_android.data.repository.GeminiRepository
import kotlinx.coroutines.launch

class ChatBotChatingViewModel: ViewModel() {
    private val repository = GeminiRepository()

    private val _messages = MutableLiveData<List<GeminiChatMessage>>(emptyList())
    val messages: LiveData<List<GeminiChatMessage>> = _messages

    fun askGemini(message: String) {
        viewModelScope.launch {
            val reply = repository.sendMessage(message)

            val updated = _messages.value?.toMutableList() ?: mutableListOf()
            updated.add(GeminiChatMessage(reply, false))
            _messages.postValue(updated)
        }
    }
}