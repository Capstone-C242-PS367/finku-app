package com.capstone.finku.ui.fragment.uploadimage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.finku.R
import com.capstone.finku.data.TransactionViewModelFactory
import com.capstone.finku.data.di.Injection
import com.capstone.finku.databinding.FragmentUploadImageBinding
import com.capstone.finku.utils.FileHandler
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody


class UploadImageFragment : Fragment() {

    private lateinit var binding: FragmentUploadImageBinding
    private val uploadImageViewModel: UploadImageViewModel by viewModels {
        TransactionViewModelFactory(Injection.provideTransactionRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUploadImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUploadImageBinding.bind(view)

        binding.analyze.isEnabled = false

        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(binding.toolbar)

        activity.supportActionBar?.title = "Add Recap"

        binding.apply {
            browseFile.setOnClickListener { startGallery() }
            analyze.setOnClickListener { analyzeImage() }
            loading.visibility = View.GONE
        }

        showImage()

        uploadImageViewModel.apply {
            message.observe(viewLifecycleOwner) {
                showToast(it ?: "")
            }

            isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
                disableSubmit(it)
            }
        }
    }

    private fun analyzeImage() {
        uploadImageViewModel.imageUri.observe(viewLifecycleOwner) {
            Log.d("URI", it.toString())
            val imageFile = FileHandler().uriToFile(it, requireContext())
            val mimeType = when (imageFile.extension.lowercase()) {
                "jpg", "jpeg" -> "image/jpeg"
                "png" -> "image/png"
                else -> throw IllegalArgumentException("Unsupported image type")
            }
            val requestImageFile = imageFile.asRequestBody(mimeType.toMediaTypeOrNull())  //want send /jpeg|jpg|png/
            val multipartBody = requestImageFile.let { it1 ->
                MultipartBody.Part.createFormData(
                    "file",
                    imageFile.name,
                    it1
                )
            }
            uploadImageViewModel.predict(multipartBody)
        }
//        requireActivity().supportFragmentManager.beginTransaction()
//            .replace(R.id.container, OcrResultFragment())
//            .commit()
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                uploadImageViewModel.persistImage(uri)
                binding.analyze.isEnabled = true
                binding.analyze.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.blue)
            } else {
                binding.analyze.isEnabled = false
            }
        }

    private fun showImage() {
        uploadImageViewModel.imageUri.observe(viewLifecycleOwner) { uri ->
            binding.previewImageView.setImageURI(uri)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(loading: Boolean) {
        binding.loading.visibility = if (loading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun disableSubmit(disabled: Boolean) {
        val btnDisabled = !disabled
        binding.analyze.isEnabled = btnDisabled
    }
}

