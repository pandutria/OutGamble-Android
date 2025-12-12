package com.example.outgamble_android.presentation.auth.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedDispatcher
import androidx.navigation.fragment.findNavController
import com.example.outgamble_android.R
import com.example.outgamble_android.databinding.FragmentLoginBinding
import com.example.outgamble_android.util.BackPressHelper
import com.example.outgamble_android.util.ClearFocusHelper


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(layoutInflater)

        binding.root.setOnClickListener {
            ClearFocusHelper.onEditText(requireContext(), binding.etUsername, binding.etPassword)
        }

        BackPressHelper.onBackPress(this) {
            requireActivity().finishAffinity()
        }

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginScreen_to_registerScreen)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}