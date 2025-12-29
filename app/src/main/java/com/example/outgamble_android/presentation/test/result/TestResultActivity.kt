package com.example.outgamble_android.presentation.test.result

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.outgamble_android.R
import com.example.outgamble_android.databinding.ActivityTestBinding
import com.example.outgamble_android.databinding.ActivityTestResultBinding
import com.example.outgamble_android.util.IntentHelper

class TestResultActivity : AppCompatActivity() {
    private var _binding: ActivityTestResultBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityTestResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBack.setOnClickListener {
            IntentHelper.finish(this)
        }

        val poin = intent.getIntExtra("poin", 0)

        binding.tvPoin.text = "Total Poin: $poin"

        when (poin) {
            in 15..30 -> {
                binding.tvRisk.text = "Tingkat Resiko: Rendah"
                binding.tvDesc.text =
                    "Kamu menunjukkan kontrol yang baik terhadap aktivitas perjudian. Saat ini belum terlihat tanda kecanduan yang signifikan."
                binding.tvRecomendation.text =
                    "Pertahankan kebiasaan positif ini. Hindari berjudi sebagai pelarian dari stres dan tetap prioritaskan kebutuhan utama serta aktivitas sehat lainnya."
            }

            in 31..45 -> {
                binding.tvRisk.text = "Tingkat Resiko: Sedang"
                binding.tvDesc.text =
                    "Terdapat beberapa tanda yang menunjukkan risiko kecanduan judi. Jika tidak dikendalikan, kondisi ini dapat berkembang menjadi masalah serius."
                binding.tvRecomendation.text =
                    "Mulailah membatasi aktivitas perjudian, kelola keuangan dengan lebih ketat, dan cari dukungan dari orang terdekat."
            }

            else -> {
                binding.tvRisk.text = "Tingkat Resiko: Tinggi"
                binding.tvDesc.text =
                    "Hasil menunjukkan risiko kecanduan judi yang tinggi. Aktivitas judi kemungkinan sudah berdampak pada keuangan, emosi, atau kehidupan sosial kamu."
                binding.tvRecomendation.text =
                    "Segera hentikan aktivitas perjudian dan cari bantuan profesional. Dukungan dari keluarga dan lingkungan sangat disarankan."
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