package com.example.outgamble_android.presentation.consultation.chating

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.outgamble_android.R
import com.example.outgamble_android.data.local.UserIdPref
import com.example.outgamble_android.data.state.ResultState
import com.example.outgamble_android.databinding.ActivityConsultationChatingBinding
import com.example.outgamble_android.presentation.adapter.ChatConsultationAdapter
import com.example.outgamble_android.util.ClearFocusHelper
import com.example.outgamble_android.util.IntentHelper
import com.example.outgamble_android.util.ToastHelper

class ConsultationChatingActivity : AppCompatActivity() {
    private var _binding: ActivityConsultationChatingBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ConsulationChatingViewModel
    private var consultationId: String? = null

    private lateinit var adapter: ChatConsultationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityConsultationChatingBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
//            insets
//        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val navInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(0, systemBars.top, 0, maxOf(imeInsets.bottom, navInsets.bottom))

            insets
        }

        binding.root.setOnClickListener {
            ClearFocusHelper.onEditText(this, binding.etMessage)
        }

        viewModel = ViewModelProvider(this)[ConsulationChatingViewModel::class.java]
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

        val userId = UserIdPref(this).get()
        viewModel.getConsultation(doctorId, userId)
        viewModel.getConsultationState.observe(this) {state ->
            when(state) {
                is ResultState.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.rvChat.visibility = View.GONE
                }
                is ResultState.Success -> {
                    consultationId = state.data.id
                    viewModel.getMessage(consultationId!!)
                }
                is ResultState.Error -> {
                    viewModel.createConsultation(doctorId, userId)
                }
            }
        }

        adapter = ChatConsultationAdapter(userId)

        viewModel.postConsultationState.observe(this) {state ->
            when(state) {
                is ResultState.Loading -> {

                }
                is ResultState.Success -> {
                    viewModel.getConsultation(doctorId, userId)
                }
                is ResultState.Error -> {
                    viewModel.getConsultation(doctorId, userId)
                }
            }
        }

        viewModel.getMessageState.observe(this) {state ->
            when(state) {
                is ResultState.Loading -> {

                }
                is ResultState.Success -> {
                    adapter.set(state.data.sortedBy { x -> x.createdAt })
                    binding.rvChat.adapter = adapter
                    binding.pbLoading.visibility = View.GONE
                    binding.rvChat.visibility = View.VISIBLE
                }
                is ResultState.Error -> {
                    binding.pbLoading.visibility = View.GONE
                    binding.rvChat.visibility = View.VISIBLE
                }
            }
        }

        binding.btnSend.setOnClickListener {
            if (binding.etMessage.text.toString() == "") {
                ToastHelper.warning(this)
                return@setOnClickListener
            }

            viewModel.sendMessage(consultationId!!, userId, binding.etMessage.text.toString())
        }

        viewModel.sendMessageState.observe(this) {state ->
            when(state) {
                is ResultState.Loading -> {
                    binding.pbLoadingChat.visibility = View.VISIBLE
                    binding.btnSend.visibility = View.GONE
                }
                is ResultState.Success -> {
                    binding.etMessage.text.clear()
                    binding.pbLoadingChat.visibility = View.GONE
                    binding.btnSend.visibility = View.VISIBLE
                }
                is ResultState.Error -> {
                    binding.pbLoadingChat.visibility = View.GONE
                    binding.btnSend.visibility = View.VISIBLE
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