package com.example.outgamble_android.presentation.consultation.chating

import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.outgamble_android.R
import com.example.outgamble_android.data.state.ResultState
import com.example.outgamble_android.databinding.ActivityConsultationChatingBinding
import com.example.outgamble_android.util.IntentHelper

class ConsultationChatingActivity : AppCompatActivity() {
    private var _binding: ActivityConsultationChatingBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ConsulattionChatingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityConsultationChatingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel = ViewModelProvider(this)[ConsulattionChatingViewModel::class.java]
        binding.btnBack.setOnClickListener {
            IntentHelper.finish(this)
        }

        val doctorId = intent.getStringExtra("id")
        viewModel.getDoctorById(doctorId!!)
        viewModel.doctorState.observe(this) {state ->
            when(state) {
                is ResultState.Loading -> {
                    binding.tvName.text = intent.getStringExtra("name")

                    Glide.with(this)
                        .load(intent.getStringExtra("image"))
                        .into(binding.image)
                }
                is ResultState.Success -> {
                    binding.tvName.text = state.data.name

                    Glide.with(this)
                        .load(state.data.image)
                        .into(binding.image)
                }
                is ResultState.Error -> {
                    viewModel.getDoctorById(doctorId!!)
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        IntentHelper.finish(this)
    }
}