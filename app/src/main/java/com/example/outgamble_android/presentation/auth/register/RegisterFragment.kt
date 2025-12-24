package com.example.outgamble_android.presentation.auth.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.outgamble_android.R
import com.example.outgamble_android.data.state.ResultState
import com.example.outgamble_android.databinding.FragmentRegisterBinding
import com.example.outgamble_android.util.BackPressHelper
import com.example.outgamble_android.util.ClearFocusHelper
import com.example.outgamble_android.util.ToastHelper

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        binding.root.setOnClickListener {
            ClearFocusHelper.onEditText(requireContext(), binding.etUsername, binding.etPassword, binding.etFullname)
        }

        BackPressHelper.onBackPress(this) {
            findNavController().popBackStack()
        }

        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerScreen_to_loginScreen)
        }

        binding.btnRegister.setOnClickListener {
            if (binding.etFullname.text.toString() == "" || binding.etUsername.text.toString() == ""
                || binding.etPassword.text.toString() == "") {
                ToastHelper.warning(requireContext())
                return@setOnClickListener
            }

            viewModel.register(binding.etFullname.text.toString(), binding.etUsername.text.toString(), binding.etPassword.text.toString())
        }

        viewModel.registerState.observe(viewLifecycleOwner) {state ->
            when(state) {
                is ResultState.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.btnRegister.visibility = View.GONE
                }
                is ResultState.Success -> {
                    binding.pbLoading.visibility = View.GONE
                    binding.btnRegister.visibility = View.VISIBLE
                    ToastHelper.success(requireContext(), "Pendaftaran Akun Berhasil", ContextCompat.getString(requireContext(), R.string.success_register))
                    findNavController().navigate(R.id.action_registerScreen_to_loginScreen)
                }
                is ResultState.Error -> {
                    binding.pbLoading.visibility = View.GONE
                    binding.btnRegister.visibility = View.VISIBLE

                    binding.etFullname.text.clear()
                    binding.etUsername.text.clear()
                    binding.etPassword.text.clear()

                    ToastHelper.error(requireContext(), "Pendaftaran Akun Gagal", ContextCompat.getString(requireContext(), R.string.error_register))
                }
            }
        }

        return binding.root
    }
}