package com.example.outgamble_android.presentation.news

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.outgamble_android.R
import com.example.outgamble_android.databinding.ActivityMainBinding
import com.example.outgamble_android.databinding.ActivityNewsBinding
import com.example.outgamble_android.util.IntentHelper

class NewsActivity : AppCompatActivity() {
    private var _binding: ActivityNewsBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityNewsBinding.inflate(layoutInflater)
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

        if (!link.isNullOrEmpty()) {
            binding.webNews.apply {
                settings.javaScriptEnabled = true
                webViewClient = object : android.webkit.WebViewClient() {
                    override fun onPageStarted(view: android.webkit.WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                        binding.pbLoading.visibility = View.VISIBLE
                        binding.webNews.visibility = View.GONE
                    }

                    override fun onPageFinished(view: android.webkit.WebView?, url: String?) {
                        binding.pbLoading.visibility = View.GONE
                        binding.webNews.visibility = View.VISIBLE
                    }
                }
                loadUrl(link)
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