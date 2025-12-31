package com.example.outgamble_android.presentation.consultation.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.outgamble_android.R
import com.example.outgamble_android.data.state.ResultState
import com.example.outgamble_android.databinding.ActivityConsultationHistoryBinding
import com.example.outgamble_android.databinding.FragmentConsultationBinding
import com.example.outgamble_android.presentation.adapter.DoctorAdapter
import com.example.outgamble_android.presentation.consultation.chating.ConsultationChatingActivity
import com.example.outgamble_android.presentation.consultation.history.ConsultationHistoryActivity
import com.example.outgamble_android.util.IntentHelper

class ConsultationFragment : Fragment() {
    private var _binding: FragmentConsultationBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ConsultationViewModel
    private lateinit var adapter: DoctorAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentConsultationBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[ConsultationViewModel::class.java]

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.bg)
        val insetsController = WindowInsetsControllerCompat(requireActivity().window, requireActivity().window.decorView)
        insetsController.isAppearanceLightStatusBars = true
        insetsController.isAppearanceLightNavigationBars = false

        binding.btnHistory.setOnClickListener {
            IntentHelper.navigate(requireActivity(), ConsultationHistoryActivity::class.java)
        }

        adapter = DoctorAdapter {doctor ->
            val bundle = Bundle().apply {
                putString("id", doctor.id)
                putString("name", doctor.name)
                putString("image", doctor.image)
            }
            IntentHelper.navigate(requireActivity(), ConsultationChatingActivity::class.java, bundle)
        }

        viewModel.get()
        viewModel.doctorState.observe(viewLifecycleOwner) {state ->
            when(state) {
                is ResultState.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.rvDoctor.visibility = View.GONE
                }
                is ResultState.Success -> {
                    adapter.set(state.data)
                    binding.rvDoctor.adapter = adapter

                    binding.pbLoading.visibility = View.GONE
                    binding.rvDoctor.visibility = View.VISIBLE
                }
                is ResultState.Error -> {
                    viewModel.get()
                }
            }
        }


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.bg)
    }
}