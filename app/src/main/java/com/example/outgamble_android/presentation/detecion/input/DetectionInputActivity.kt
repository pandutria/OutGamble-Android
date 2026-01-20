package com.example.outgamble_android.presentation.detecion.input

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.outgamble_android.R
import com.example.outgamble_android.data.state.ResultState
import com.example.outgamble_android.databinding.ActivityDetectionInputBinding
import com.example.outgamble_android.presentation.detecion.result.DetectionResultActivity
import com.example.outgamble_android.util.ClearFocusHelper
import com.example.outgamble_android.util.IntentHelper

class DetectionInputActivity : AppCompatActivity() {
    private var _binding: ActivityDetectionInputBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DetectionInputViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityDetectionInputBinding.inflate(layoutInflater)
        setContentView(binding.root )
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel = ViewModelProvider(this)[DetectionInputViewModel::class.java]

        binding.btnBack.setOnClickListener {
            IntentHelper.finish(this)
        }

        ClearFocusHelper.onEditText(this, binding.etLink)

        binding.btnSend.setOnClickListener {
            viewModel.predict(binding.etLink.text.toString())
        }

        viewModel.aiState.observe(this) { state ->
            when (state) {
                is ResultState.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.btnSend.visibility = View.GONE
                }
                is ResultState.Success -> {
                    binding.pbLoading.visibility = View.GONE
                    binding.btnSend.visibility = View.VISIBLE
                    val data = state.data
                    val bundle = Bundle().apply {
                        putString("link", data.link)
                        putString("status", data.status)
                        putString("risk", data.tingkat_resiko)
                    }
                    IntentHelper.navigate(this, DetectionResultActivity::class.java, bundle)
                    IntentHelper.finish(this)
                }
                is ResultState.Error -> {
                    binding.pbLoading.visibility = View.GONE
                    binding.btnSend.visibility = View.VISIBLE
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
            }
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