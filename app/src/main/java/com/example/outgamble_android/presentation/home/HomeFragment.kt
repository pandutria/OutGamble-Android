package com.example.outgamble_android.presentation.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.checkSelfPermission
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
import com.example.outgamble_android.presentation.test.TestActivity
import com.example.outgamble_android.util.IntentHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel
    private lateinit var newsAdapter: NewsAdapter


    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var lat = 0.0
    private var lng = 0.0

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

        binding.btnTest.setOnClickListener {
            IntentHelper.navigate(requireActivity(), TestActivity::class.java)
        }

        binding.mapPreviewWebView.isEnabled = false

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

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    getCurrentLocation()
                }
                else Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT)
                    .show()
            }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getCurrentLocation()

        return binding.root
    }

    private fun showMap() {
        val html = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                html, body {
                    margin: 0;
                    padding: 0;
                    width: 100%;
                    height: 100%;
                    overflow: hidden;
                }

                .map-container {
                    position: relative;
                    width: 100%;
                    height: 100%;
                    overflow: hidden;
                    pointer-events: none;
                }

                iframe {
                    position: absolute;
                    top: -40px;     
                    left: 0;
                    width: 100%;
                    height: calc(100% + 60px); 
                    border: 0;
                }
                .map-overlay {
                    position: absolute;
                    bottom: 0;
                    left: 0;
                    width: 100%;
                    height: 40px; /* sesuaikan */
                    background: #fff; /* samain warna background */
                    z-index: 10;
                }
            </style>
        </head>
        <body>
            <div class="map-container">
                <iframe
                    src="https://www.google.com/maps?q=$lat,$lng&z=15&output=embed"
                    loading="lazy">
                </iframe>
            </div>
        </body>
        </html>
    """.trimIndent()

        binding.mapPreviewWebView.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true

            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()

            loadDataWithBaseURL(
                "https://www.google.com",
                html,
                "text/html",
                "UTF-8",
                null
            )
        }
    }

    override fun onResume() {
        super.onResume()
        getCurrentLocation()
    }

    private fun getCurrentLocation() {
        if (
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val lats = location.latitude
                    val lngs = location.longitude

                    lat = lats
                    lng = lngs
                    showMap()
                } else {
                    Toast.makeText(requireContext(), "Lokasi tidak tersedia", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding =   null
    }
}