package com.example.outgamble_android.presentation.detecion

import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.outgamble_android.R
import com.example.outgamble_android.databinding.ActivityDetectionBinding
import com.example.outgamble_android.presentation.detecion.input.DetectionInputActivity
import com.example.outgamble_android.presentation.detecion.input.DetectionInputViewModel
import com.example.outgamble_android.presentation.detecion.result.DetectionResultActivity
import com.example.outgamble_android.util.IntentHelper

class DetectionActivity : AppCompatActivity() {
    private var _binding: ActivityDetectionBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBack.setOnClickListener {
            IntentHelper.finish(this)
        }

        binding.btnStart.setOnClickListener {
            IntentHelper.navigate(this, DetectionInputActivity::class.java)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        IntentHelper.finish(this)
    }
}