package com.example.outgamble_android.presentation.test.input

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.outgamble_android.R
import com.example.outgamble_android.data.model.Test
import com.example.outgamble_android.data.state.ResultState
import com.example.outgamble_android.databinding.ActivityTestInputBinding
import com.example.outgamble_android.presentation.adapter.TestAdapter
import com.example.outgamble_android.presentation.test.TestActivity
import com.example.outgamble_android.presentation.test.result.TestResultActivity
import com.example.outgamble_android.util.IntentHelper
import com.example.outgamble_android.util.ToastHelper
import kotlinx.serialization.builtins.serializer
import okhttp3.internal.wait

class TestInputActivity : AppCompatActivity() {
    private var _binding: ActivityTestInputBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: TestInputViewModel

    private lateinit var adapter: TestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityTestInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel = ViewModelProvider(this)[TestInputViewModel::class.java]

        binding.btnBack.setOnClickListener {
            IntentHelper.finish(this)
        }

        adapter = TestAdapter(this)

        viewModel.get()
        viewModel.getState.observe(this){state ->
            when(state) {
                is ResultState.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.rvTest.visibility = View.GONE
                }
                is ResultState.Success -> {
                    adapter.setData(state.data)
                    binding.rvTest.adapter = adapter

                    binding.pbLoading.visibility = View.GONE
                    binding.rvTest.visibility = View.VISIBLE
                }
                is ResultState.Error -> {

                }
            }
        }

        binding.btnSend.setOnClickListener {
            for(i in adapter.list) {
                if (i.poin == 0) {
                    ToastHelper.warning(this)
                    return@setOnClickListener
                }
            }

            val bundle = Bundle().apply {
                putInt("poin", countTotal())
            }

            IntentHelper.navigate(this, TestResultActivity::class.java, bundle)
            IntentHelper.finish(this)
        }
    }

    fun countTotal(): Int {
        var totalPoint = 0
        for (i in adapter.list) {
            totalPoint += i.poin
        }
        return totalPoint
    }

    override fun onBackPressed() {
        super.onBackPressed()
        IntentHelper.finish(this)
    }
}