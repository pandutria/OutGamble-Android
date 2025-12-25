package com.example.outgamble_android.presentation.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.outgamble_android.MainActivity
import com.example.outgamble_android.R
import com.example.outgamble_android.data.local.FullnamePref
import com.example.outgamble_android.presentation.auth.AuthActivity
import com.example.outgamble_android.util.IntentHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            delay(3000)
            if (FullnamePref(this@SplashActivity).get() != "")
                IntentHelper.navigate(this@SplashActivity, MainActivity::class.java)
            else
                IntentHelper.navigate(this@SplashActivity, AuthActivity::class.java)

            finish()
        }
    }
}