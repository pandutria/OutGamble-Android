package com.example.outgamble_android.presentation.chatbot.chating

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.outgamble_android.R
import com.example.outgamble_android.data.gemini.repsonse.GeminiChatMessage
import com.example.outgamble_android.databinding.ActivityChatBotChatingBinding
import com.example.outgamble_android.presentation.adapter.ChatBotAdapter
import com.example.outgamble_android.util.IntentHelper
import com.example.outgamble_android.util.ToastHelper

class ChatBotChatingActivity : AppCompatActivity() {

    private var _binding: ActivityChatBotChatingBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ChatBotAdapter
    private lateinit var viewModel: ChatBotChatingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityChatBotChatingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val navInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(0, systemBars.top, 0, maxOf(imeInsets.bottom, navInsets.bottom))

            insets
        }

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        viewModel = ViewModelProvider(this)[ChatBotChatingViewModel::class.java]

        binding.btnBack.setOnClickListener {
            IntentHelper.finish(this)
        }

        adapter = ChatBotAdapter(mutableListOf())
        binding.rvChat.adapter = adapter

        viewModel.messages.observe(this) { messages ->
            val lastMessage = messages.lastOrNull() ?: return@observe
            showTypingEffect(lastMessage)
        }

        binding.btnSend.setOnClickListener {
            val message = binding.etMessage.text.toString().trim()

            if (message.isEmpty()) {
                ToastHelper.warning(this)
                return@setOnClickListener
            }

            adapter.addMessage(GeminiChatMessage(message, true))

            if (isAllowedTopic(message)) {
                viewModel.askGemini(message)
            } else {
                showTypingEffect(getRejectMessage())
            }

            binding.etMessage.text.clear()
            binding.etMessage.clearFocus()

            val imm = getSystemService(INPUT_METHOD_SERVICE)
                    as android.view.inputmethod.InputMethodManager
            imm.hideSoftInputFromWindow(binding.etMessage.windowToken, 0)
        }
    }

    private fun isAllowedTopic(message: String): Boolean {
        val keywords = listOf(
            "judi", "judol", "slot", "bet", "taruhan",
            "pencegahan", "edukasi", "bahaya judi",
            "kecanduan judi", "dampak judi",
            "hukum judi", "lapor judi", "stop judi"
        )

        val lower = message.lowercase()
        return keywords.any { lower.contains(it) }
    }

    private fun getRejectMessage(): GeminiChatMessage {
        return GeminiChatMessage(
            message = """
                Maaf 🙏
                Saya hanya dapat membantu seputar **edukasi, pencegahan, dan pemberantasan judi**.

                Silakan ajukan pertanyaan terkait:
                • Bahaya judi
                • Dampak kecanduan judi
                • Cara berhenti judi
                • Upaya pencegahan judi
                • Edukasi hukum & sosial tentang judi
            """.trimIndent(),
            isUser = false
        )
    }

    private fun showTypingEffect(fullMessage: GeminiChatMessage) {
        var index = 0
        val handler = Handler(Looper.getMainLooper())

        val emptyMsg = GeminiChatMessage(message = "", isUser = false)
        adapter.addMessage(emptyMsg)
        val position = adapter.itemCount - 1

        val runnable = object : Runnable {
            override fun run() {
                if (index <= fullMessage.message.length) {
                    val currentText = fullMessage.message.substring(0, index)
                    adapter.updateMessage(position, currentText)
                    index++
                    handler.postDelayed(this, 20)
                }
            }
        }
        handler.post(runnable)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        IntentHelper.finish(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
