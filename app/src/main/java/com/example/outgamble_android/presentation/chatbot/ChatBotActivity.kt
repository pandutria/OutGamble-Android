package com.example.outgamble_android.presentation.chatbot

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.outgamble_android.R
import com.example.outgamble_android.databinding.ActivityChatBotBinding
import com.example.outgamble_android.presentation.chatbot.chating.ChatBotChatingActivity
import com.example.outgamble_android.util.IntentHelper

class ChatBotActivity : AppCompatActivity() {
    private var _binding: ActivityChatBotBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityChatBotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnStart.setOnClickListener {
            IntentHelper.navigate(this, ChatBotChatingActivity::class.java)
        }

        binding.btnBack.setOnClickListener {
            IntentHelper.finish(this)
        }
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