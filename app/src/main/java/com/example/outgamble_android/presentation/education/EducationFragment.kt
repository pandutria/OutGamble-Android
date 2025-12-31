package com.example.outgamble_android.presentation.education

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.outgamble_android.R
import com.example.outgamble_android.data.state.ResultState
import com.example.outgamble_android.databinding.FragmentEducationBinding
import com.example.outgamble_android.presentation.adapter.EducationAdapter
import com.example.outgamble_android.presentation.education.detail.EducationDetailActivity
import com.example.outgamble_android.util.IntentHelper

class EducationFragment : Fragment() {
    private var _binding: FragmentEducationBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EducationViewModel

    private lateinit var adapter: EducationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEducationBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[EducationViewModel::class.java]
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.bg)

        adapter = EducationAdapter {education ->
            val bundle = Bundle().apply {
                putString("title", education.title)
                putString("desc", education.desc)
                putString("link", education.link)
                putString("source", education.source)
            }
            IntentHelper.navigate(requireActivity(), EducationDetailActivity::class.java, bundle)
        }

        viewModel.get()
        viewModel.getState.observe(viewLifecycleOwner) {state ->
            when(state) {
                is ResultState.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.rvEducation.visibility = View.GONE
                }
                is ResultState.Success -> {
                    adapter.set(state.data)
                    binding.rvEducation.adapter = adapter
                    binding.pbLoading.visibility = View.GONE
                    binding.rvEducation.visibility = View.VISIBLE
                }
                is ResultState.Error -> {

                }
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.get()
    }
}