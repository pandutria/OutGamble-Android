package com.example.outgamble_android.presentation.consultation.history

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.outgamble_android.R
import com.example.outgamble_android.data.local.UserIdPref
import com.example.outgamble_android.data.state.ResultState
import com.example.outgamble_android.databinding.ActivityConsultationHistoryBinding
import com.example.outgamble_android.presentation.adapter.ConsultationHistoryAdapter
import com.example.outgamble_android.presentation.consultation.chating.ConsultationChatingActivity
import com.example.outgamble_android.util.IntentHelper

class ConsultationHistoryActivity : AppCompatActivity() {
    private var _binding: ActivityConsultationHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ConsultationHistoryViewModel

    private lateinit var adapter: ConsultationHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityConsultationHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel = ViewModelProvider(this)[ConsultationHistoryViewModel::class.java]

        binding.btnBack.setOnClickListener {
            IntentHelper.finish(this)
        }

        adapter = ConsultationHistoryAdapter{history ->
            val bundle = Bundle().apply {
                putString("id", history.doctor.id)
                putString("name", history.doctor.name)
                putString("image", history.doctor.image)
            }
            IntentHelper.navigate(this, ConsultationChatingActivity::class.java, bundle)
        }

        val userId = UserIdPref(this).get()
        viewModel.getHistory(userId)
        viewModel.historyState.observe(this) {state ->
            when(state) {
                is ResultState.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.rvHistory.visibility = View.GONE
                }
                is ResultState.Success -> {
                    adapter.set(state.data)
                    binding.rvHistory.adapter = adapter
                    binding.pbLoading.visibility = View.GONE
                    binding.rvHistory.visibility = View.VISIBLE
                }
                is ResultState.Error -> {

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val userId = UserIdPref(this).get()
        viewModel.getHistory(userId)
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