package com.example.outgamble_android.presentation.report.location

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.UploadCallback
import com.example.outgamble_android.R
import com.example.outgamble_android.data.local.UserIdPref
import com.example.outgamble_android.data.state.ResultState
import com.example.outgamble_android.databinding.ActivityReportsLocationBinding
import com.example.outgamble_android.util.ClearFocusHelper
import com.example.outgamble_android.util.IntentHelper
import com.example.outgamble_android.util.ToastHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

class ReportsLocationActivity : AppCompatActivity() {
    private var _binding: ActivityReportsLocationBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ReportsLocationViewModel

    var imageUri: String? = null
    var imageUrl: String? = null
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityReportsLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel = ViewModelProvider(this)[ReportsLocationViewModel::class.java]

        binding.btnBack.setOnClickListener {
            IntentHelper.finish(this)
        }

        binding.etDate.isFocusable = false
        binding.etDate.isClickable = true

        binding.etDate.setOnClickListener {
            showDatePicker()
        }

        binding.root.setOnClickListener {
            ClearFocusHelper.onEditText(this, binding.etDesc, binding.etLocation, binding.etDate)
        }

        pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK && result.data != null) {
                    val uri = result.data?.data
                    if (uri != null) {
                        contentResolver.takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )

                        imageUri = uri.toString()
                        Glide.with(this)
                            .load(imageUri)
                            .into(binding.imgImage)

                        binding.etPath.setText(imageUri)

                        binding.layoutUpload.visibility = View.GONE
                        binding.cardImage.visibility = View.VISIBLE
                    }
                }
            }

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    openImagePicker()
                    getCurrentLocation()
                }
                else Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT)
                    .show()
            }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()

        binding.layoutUpload.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            pickImageLauncher.launch(intent)
        }

        binding.btnSend.setOnClickListener {
            if (binding.etLocation.text.toString() == "" || binding.etDesc.text.toString() == ""
                || binding.etDate.text.toString() == "" || binding.etPath.text.toString() == "") {
                ToastHelper.warning(this)
                return@setOnClickListener
            }

            binding.pbLoading.visibility = View.VISIBLE
            binding.btnSend.visibility = View.GONE
            uploadImage(imageUri!!)
        }

        viewModel.reportState.observe(this) {state ->
            when(state) {
                is ResultState.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.btnSend.visibility = View.GONE
                }
                is ResultState.Success -> {
                    ToastHelper.success(this, "Berhasil Membuat Laporan", "Deskripsi berhasil disimpan dan telah tercatat dalam sistem.")
                    IntentHelper.finish(this)
                }
                is ResultState.Error -> {

                }
            }
        }
    }

    private fun uploadImage(uriString: String) {
        val uri = Uri.parse(uriString)
        val file = uriToFile(uri)
        MediaManager.get().upload(file.absolutePath)
            .unsigned("outgamble")
            .callback(object : UploadCallback {

                override fun onStart(requestId: String) {}

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}

                override fun onSuccess(
                    requestId: String,
                    resultData: Map<*, *>
                ) {
                    val url = resultData["secure_url"] as String
                    imageUrl = url
                    val userId = UserIdPref(this@ReportsLocationActivity).get()
                    viewModel.create(imageUrl!!, binding.etLocation.text.toString(), binding.etDate.text.toString(), binding.etDesc.text.toString(), userId)

                }

                override fun onError(
                    requestId: String,
                    error: com.cloudinary.android.callback.ErrorInfo
                ) {
                    error(error.description)
                }

                override fun onReschedule(requestId: String, error: com.cloudinary.android.callback.ErrorInfo) {}
            })
            .dispatch()
    }


    private fun getCurrentLocation() {
        if (
            checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
            != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val lat = location.latitude
                    val lng = location.longitude

                    binding.etLocation.setText("$lat, $lng")
                } else {
                    Toast.makeText(this, "Lokasi tidak tersedia", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->

                val formattedDate = String.format(
                    "%02d-%02d-%04d",
                    selectedDay,
                    selectedMonth + 1,
                    selectedYear
                )

                binding.etDate.setText(formattedDate)
            },
            year,
            month,
            day
        )

        datePicker.show()
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream = contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Cannot open input stream")

        val file = File(cacheDir, "upload_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(file)

        inputStream.copyTo(outputStream)

        inputStream.close()
        outputStream.close()

        return file
    }


    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        pickImageLauncher.launch(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        IntentHelper.finish(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}