package com.example.outgamble_android.util

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

object BackPressHelper {
    fun onBackPress(
        fragment: Fragment,
        enabled: Boolean = true,
        onBack: () -> Unit
    ) {
        fragment.requireActivity()
            .onBackPressedDispatcher
            .addCallback(fragment.viewLifecycleOwner, object : OnBackPressedCallback(enabled) {
                override fun handleOnBackPressed() {
                    onBack()
                }
            })
    }
}