package com.example.outgamble_android.presentation.education.detail

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.outgamble_android.R
import com.example.outgamble_android.databinding.ActivityEducationDetailBinding
import com.example.outgamble_android.util.IntentHelper
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class EducationDetailActivity : AppCompatActivity() {
    private var _binding: ActivityEducationDetailBinding? = null
    private val binding get() = _binding!!

    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityEducationDetailBinding.inflate(layoutInflater)
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
        val title = intent.getStringExtra("title")
        val desc = intent.getStringExtra("desc")
        val source = intent.getStringExtra("source")

        player = ExoPlayer.Builder(this).build()
        binding.pvVideo.player = player
        val mediaItem = MediaItem.fromUri(link!!)
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.play()

        binding.tvTitle.text = title
        binding.tvDesc.text = desc
        binding.tvSource.text = "Source: $source"
    }

    override fun onBackPressed() {
        super.onBackPressed()
        IntentHelper.finish(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        player?.release()
        player = null
    }
}