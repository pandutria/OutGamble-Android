package com.example.outgamble_android.presentation.report.sites

import android.app.DatePickerDialog
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
import com.example.outgamble_android.databinding.ActivityReportsSiteBinding
import com.example.outgamble_android.presentation.report.location.ReportsLocationViewModel
import com.example.outgamble_android.util.ClearFocusHelper
import com.example.outgamble_android.util.IntentHelper
import com.example.outgamble_android.util.ToastHelper
import java.util.Calendar

class ReportsSiteActivity : AppCompatActivity() {
    private var _binding: ActivityReportsSiteBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ReportsSiteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityReportsSiteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel = ViewModelProvider(this)[ReportsSiteViewModel::class.java]

        binding.root.setOnClickListener {
            ClearFocusHelper.onEditText(this, binding.etDesc, binding.etPlatform, binding.etLink, binding.etDate)
        }

        binding.etDate.isFocusable = false
        binding.etDate.isClickable = true

        binding.etDate.setOnClickListener {
            showDatePicker()
        }

        binding.btnBack.setOnClickListener {
            IntentHelper.finish(this)
        }

        binding.btnSend.setOnClickListener {
            if (binding.etLink.toString() == "" || binding.etDate.text.toString() == ""
                || binding.etDesc.text.toString() == "" || binding.etPlatform.text.toString() == "") {
                ToastHelper.warning(this)
                return@setOnClickListener
            }

            val userId = UserIdPref(this).get()
            viewModel.create(binding.etLink.text.toString(), binding.etDate.text.toString(),
                binding.etDesc.text.toString(), binding.etPlatform.text.toString(), userId)
        }

        viewModel.createState.observe(this) {state ->
            when(state) {
                is ResultState.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.btnSend.visibility = View.GONE
                }
                is ResultState.Success -> {
                    ToastHelper.success(this, "Berhasil Membuat Laporan", "Deskripsi berhasil disimpan dan telah tercatat dalam sistem.")
                    IntentHelper.finish(this)
                }
                is ResultState.Error -> {

                }
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->

                val formattedDate = String.format(
                    "%02d-%02d-%04d",
                    selectedDay,
                    selectedMonth + 1,
                    selectedYear
                )

                binding.etDate.setText(formattedDate)
            },
            year,
            month,
            day
        )

        datePicker.show()
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