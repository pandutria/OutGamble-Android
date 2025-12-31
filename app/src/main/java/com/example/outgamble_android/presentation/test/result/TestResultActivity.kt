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
                binding.tvRisk.text = "Tingkat Risiko: Rendah"
                binding.tvDesc.text =
                    "Berdasarkan hasil penilaian yang dilakukan, kamu menunjukkan tingkat risiko kecanduan judi yang rendah. " +
                            "Hal ini menandakan bahwa aktivitas perjudian yang dilakukan masih berada dalam batas wajar dan belum menunjukkan pola perilaku kompulsif. " +
                            "Saat ini, kamu masih mampu mengendalikan frekuensi, durasi, serta pengeluaran yang berkaitan dengan perjudian. " +
                            "Tidak ditemukan indikasi kuat bahwa aktivitas tersebut berdampak negatif terhadap kondisi keuangan, kesehatan mental, " +
                            "atau hubungan sosial yang kamu miliki."

                binding.tvRecomendation.text =
                    "Meskipun tingkat risiko masih tergolong rendah, penting untuk tetap waspada dan menjaga pola perilaku yang sehat. " +
                            "Disarankan untuk menetapkan batasan yang jelas terkait waktu dan dana yang digunakan untuk berjudi. " +
                            "Hindari menjadikan perjudian sebagai sarana utama untuk mengatasi stres, tekanan emosional, atau masalah pribadi. " +
                            "Sebagai langkah pencegahan, isi waktu luang dengan aktivitas positif seperti olahraga, kegiatan kreatif, " +
                            "atau interaksi sosial yang mendukung kesejahteraan mental dan emosional kamu."

            }

            in 31..45 -> {
                binding.tvRisk.text = "Tingkat Risiko: Sedang"
                binding.tvDesc.text =
                    "Hasil evaluasi menunjukkan adanya peningkatan risiko kecanduan judi pada diri kamu. " +
                            "Beberapa indikator mengarah pada pola perilaku berjudi yang mulai sulit dikendalikan, " +
                            "baik dari segi frekuensi, durasi, maupun jumlah uang yang dikeluarkan. " +
                            "Pada tahap ini, aktivitas perjudian berpotensi mulai memengaruhi kestabilan emosi, " +
                            "pengambilan keputusan keuangan, serta hubungan dengan orang-orang terdekat. " +
                            "Jika tidak ditangani sejak dini, kondisi ini dapat berkembang menjadi kecanduan yang lebih serius."

                binding.tvRecomendation.text =
                    "Langkah pencegahan sangat dianjurkan untuk menghindari peningkatan risiko di masa mendatang. " +
                            "Mulailah dengan mengurangi frekuensi berjudi dan menetapkan batas keuangan yang ketat. " +
                            "Buatlah perencanaan keuangan yang lebih terstruktur dan transparan. " +
                            "Selain itu, penting untuk membuka diri dan berbagi kondisi ini dengan keluarga atau teman dekat " +
                            "agar kamu mendapatkan dukungan emosional dan pengawasan yang positif. " +
                            "Jika diperlukan, pertimbangkan untuk mengikuti konseling ringan atau program edukasi mengenai risiko kecanduan judi."

            }

            else -> {
                binding.tvRisk.text = "Tingkat Risiko: Tinggi"
                binding.tvDesc.text =
                    "Berdasarkan hasil analisis, kamu berada pada tingkat risiko kecanduan judi yang tinggi. " +
                            "Hal ini menunjukkan bahwa aktivitas perjudian kemungkinan telah menjadi perilaku yang bersifat kompulsif " +
                            "dan sulit untuk dikendalikan. Dampak negatif dari kebiasaan ini dapat mencakup masalah keuangan yang serius, " +
                            "tekanan emosional, gangguan kesehatan mental, serta penurunan kualitas hubungan sosial dan keluarga. " +
                            "Tanpa adanya intervensi yang tepat, risiko kerugian yang lebih besar dapat terus meningkat."

                binding.tvRecomendation.text =
                    "Disarankan untuk segera menghentikan seluruh aktivitas perjudian dan mengambil langkah pemulihan secara serius. " +
                            "Carilah bantuan profesional seperti psikolog, konselor, atau layanan rehabilitasi yang berpengalaman dalam menangani kecanduan judi. " +
                            "Dukungan dari keluarga, pasangan, dan lingkungan sekitar sangat penting dalam proses pemulihan. " +
                            "Selain itu, lakukan perubahan gaya hidup dengan menjauhi pemicu perjudian dan menggantinya dengan aktivitas yang sehat, " +
                            "produktif, serta mendukung kesejahteraan jangka panjang kamu."

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