package com.example.outgamble_android.presentation.auth.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.outgamble_android.R
import com.example.outgamble_android.databinding.FragmentRegisterBinding
import com.example.outgamble_android.util.BackPressHelper
import com.example.outgamble_android.util.ClearFocusHelper

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(layoutInflater)

        binding.root.setOnClickListener {
            ClearFocusHelper.onEditText(requireContext(), binding.etUsername, binding.etPassword, binding.etFullname)
        }

        BackPressHelper.onBackPress(this) {
            findNavController().popBackStack()
        }

        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginScreen_to_registerScreen)
        }

        return binding.root
    }
}