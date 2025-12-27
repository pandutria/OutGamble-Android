package com.example.outgamble_android.presentation.report

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.outgamble_android.R
import com.example.outgamble_android.databinding.ActivityReportsBinding
import com.example.outgamble_android.presentation.report.location.ReportsLocationActivity
import com.example.outgamble_android.presentation.report.sites.ReportsSiteActivity
import com.example.outgamble_android.util.IntentHelper

class ReportsActivity : AppCompatActivity() {
    private var _binding: ActivityReportsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityReportsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBack.setOnClickListener {
            IntentHelper.finish(this)
        }

        binding.btnLocation.setOnClickListener {
            IntentHelper.navigate(this, ReportsLocationActivity::class.java)
        }

        binding.btnSites.setOnClickListener {
            IntentHelper.navigate(this, ReportsSiteActivity::class.java)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        IntentHelper.finish(this)
    }
}