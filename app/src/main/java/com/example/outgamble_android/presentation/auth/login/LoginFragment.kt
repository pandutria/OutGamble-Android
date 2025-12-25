package com.example.outgamble_android.presentation.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedDispatcher
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.outgamble_android.MainActivity
import com.example.outgamble_android.R
import com.example.outgamble_android.data.local.FullnamePref
import com.example.outgamble_android.data.local.UserIdPref
import com.example.outgamble_android.data.state.ResultState
import com.example.outgamble_android.databinding.FragmentLoginBinding
import com.example.outgamble_android.presentation.auth.register.RegisterViewModel
import com.example.outgamble_android.util.BackPressHelper
import com.example.outgamble_android.util.ClearFocusHelper
import com.example.outgamble_android.util.IntentHelper
import com.example.outgamble_android.util.ToastHelper


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        binding.root.setOnClickListener {
            ClearFocusHelper.onEditText(requireContext(), binding.etUsername, binding.etPassword)
        }

        BackPressHelper.onBackPress(this) {
            requireActivity().finishAffinity()
        }

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginScreen_to_registerScreen)
        }

        binding.btnLogin.setOnClickListener {
            if (binding.etUsername.text.toString() == "" || binding.etPassword.text.toString() == "") {
                ToastHelper.warning(requireContext())
                return@setOnClickListener
            }

            viewModel.login(binding.etUsername.text.toString(), binding.etPassword.text.toString())
        }

        viewModel.loginState.observe(viewLifecycleOwner) {state ->
            when(state) {
                is ResultState.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.btnLogin.visibility = View.GONE
                }
                is ResultState.Success -> {
                    binding.pbLoading.visibility = View.GONE
                    binding.btnLogin.visibility = View.VISIBLE
                    ToastHelper.success(requireContext(), "Masuk Berhasil", ContextCompat.getString(requireContext(), R.string.success_login))

                    UserIdPref(requireContext()).save(state.data.id)
                    Log.d("userId", UserIdPref(requireContext()).get())
                    FullnamePref(requireContext()).save(state.data.fullname)
                    IntentHelper.navigate(requireActivity(), MainActivity::class.java)
                    requireActivity().finish()
                }
                is ResultState.Error -> {
                    binding.pbLoading.visibility = View.GONE
                    binding.btnLogin.visibility = View.VISIBLE

                    binding.etUsername.text.clear()
                    binding.etPassword.text.clear()

                    ToastHelper.error(requireContext(), "Masuk Akun Gagal", ContextCompat.getString(requireContext(), R.string.error_login))
                }
            }
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}