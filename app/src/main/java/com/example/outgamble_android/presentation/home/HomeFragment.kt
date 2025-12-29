package com.example.outgamble_android.presentation.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.outgamble_android.R
import com.example.outgamble_android.data.local.FullnamePref
import com.example.outgamble_android.data.local.UserIdPref
import com.example.outgamble_android.data.model.News
import com.example.outgamble_android.data.state.ResultState
import com.example.outgamble_android.databinding.FragmentHomeBinding
import com.example.outgamble_android.presentation.adapter.NewsAdapter
import com.example.outgamble_android.presentation.chatbot.ChatBotActivity
import com.example.outgamble_android.presentation.detecion.DetectionActivity
import com.example.outgamble_android.presentation.news.NewsActivity
import com.example.outgamble_android.presentation.report.ReportsActivity
import com.example.outgamble_android.util.IntentHelper

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        binding.tvFullname.text = "Hi, ${FullnamePref(requireContext()).get()}"

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.primary)

        binding.btnReport.setOnClickListener {
            IntentHelper.navigate(requireActivity(), ReportsActivity::class.java)
        }

        binding.btnAi.setOnClickListener {
            IntentHelper.navigate(requireActivity(), ChatBotActivity::class.java)
        }

        binding.btnDetection.setOnClickListener {
            IntentHelper.navigate(requireActivity(), DetectionActivity::class.java)
        }

        newsAdapter = NewsAdapter { news ->
            val bundle = Bundle().apply {
                putString("link", news.link)
            }
            IntentHelper.navigate(requireActivity(), NewsActivity::class.java, bundle)
        }

        viewModel.getNews()
        viewModel.newsState.observe(viewLifecycleOwner) {state ->
            when(state) {
                is ResultState.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.rvNews.visibility = View.GONE
                }
                is ResultState.Success -> {
                    newsAdapter.set(state.data)
                    binding.rvNews.adapter = newsAdapter

                    binding.pbLoading.visibility = View.GONE
                    binding.rvNews.visibility = View.VISIBLE
                }
                is ResultState.Error -> {
                    viewModel.getNews()
                }
            }
        }

        return binding.root
    }
}