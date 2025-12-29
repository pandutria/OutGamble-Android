package com.example.outgamble_android.presentation.detecion.result

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.outgamble_android.R
import com.example.outgamble_android.databinding.ActivityDetectionResultBinding
import com.example.outgamble_android.util.IntentHelper

class DetectionResultActivity : AppCompatActivity() {
    private var _binding: ActivityDetectionResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityDetectionResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBack.setOnClickListener {
            IntentHelper.finish(this)
        }

        val link = intent.getStringExtra("link")
        val status = intent.getStringExtra("status")
        val risk = intent.getStringExtra("risk")

        binding.tvLink.text = link
        val riskText = risk.toString().substring(0, 1).uppercase() + risk.toString().substring(1)
        binding.tvRisk.text = "Tingkat Resiko: ${riskText}"
        binding.tvStatus.text = status.toString().split(" ").joinToString(" ") { it.capitalize() }

        when(status) {
            "aman" -> {
                binding.tvStatus.setTextColor(ContextCompat.getColor(this, R.color.primary))
            }
            "tidak aman" -> {
                binding.tvStatus.setTextColor(Color.parseColor("#898989"))
            }
        }

        when(risk) {
            "rendah" -> {
                binding.tvDesc.text = "Berdasarkan hasil analisis AI, situs yang kamu periksa memiliki indikasi kuat terkait aktivitas perjudian online. Sistem mendeteksi pola kata kunci dan struktur halaman yang umum digunakan pada situs judi. \n" +
                        "\n" +
                        "Kami menyarankan agar kamu tidak mengakses atau membagikan situs ini, karena berpotensi mengandung konten berbahaya, risiko pencurian data, serta promosi aktivitas ilegal yang dapat merugikan pengguna.\n"
            }
            "tinggi" -> {
                binding.tvDesc.text = "Hasil analisis AI menunjukkan bahwa situs ini memiliki risiko rendah. Tidak ditemukan pola atau indikasi kuat yang mengarah pada aktivitas perjudian online. \n" +
                        "\n" +
                        "Meski demikian, tetap disarankan untuk berhati-hati saat mengakses atau membagikan informasi dari situs mana pun, terutama yang belum kamu kenal sepenuhnya.\n"
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