package com.example.outgamble_android

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.outgamble_android.databinding.ActivityMainBinding
import com.example.outgamble_android.presentation.consultation.list.ConsultationFragment
import com.example.outgamble_android.presentation.education.EducationFragment
import com.example.outgamble_android.presentation.home.HomeFragment
import com.example.outgamble_android.presentation.profile.ProfileFragment

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        window.navigationBarColor = ContextCompat.getColor(this, R.color.primary)

        showFragment(HomeFragment())

        binding.bottomNav.itemActiveIndex = 0

        binding.bottomNav.setOnItemSelectedListener { index ->
            when (index) {
                0 -> showFragment(HomeFragment())
                1 -> showFragment(ConsultationFragment())
                2 -> showFragment(EducationFragment())
                3 -> showFragment(ProfileFragment())
                4 -> showFragment(ProfileFragment())
            }
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, fragment)
            .commit()
    }
}