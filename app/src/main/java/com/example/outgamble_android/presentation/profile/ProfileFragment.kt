package com.example.outgamble_android.presentation.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.outgamble_android.R
import com.example.outgamble_android.data.local.FullnamePref
import com.example.outgamble_android.data.local.UserIdPref
import com.example.outgamble_android.databinding.FragmentProfileBinding
import com.example.outgamble_android.presentation.auth.AuthActivity
import com.example.outgamble_android.util.IntentHelper

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(layoutInflater)

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.bg)
        val insetsController = WindowInsetsControllerCompat(requireActivity().window, requireActivity().window.decorView)
        insetsController.isAppearanceLightStatusBars = true
        insetsController.isAppearanceLightNavigationBars = false

        binding.tvFullname.text = FullnamePref(requireContext()).get()

        binding.layoutLogout.setOnClickListener {
            IntentHelper.navigate(requireActivity(), AuthActivity::class.java)
            IntentHelper.finish(requireActivity())
            UserIdPref(requireContext()).remove()
            FullnamePref(requireContext()).remove()
        }

        binding.layoutExit.setOnClickListener {
            requireActivity().finishAffinity()
            UserIdPref(requireContext()).remove()
            FullnamePref(requireContext()).remove()
        }

        return binding.root
    }
    override fun onResume() {
        super.onResume()
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.bg)
    }
}