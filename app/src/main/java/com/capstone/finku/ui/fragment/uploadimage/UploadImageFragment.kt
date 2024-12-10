package com.capstone.finku.ui.fragment.uploadimage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
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
import com.capstone.finku.ui.fragment.ocrresult.OcrResultFragment
import com.capstone.finku.utils.FileHandler
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class UploadImageFragment : Fragment() {
    private lateinit var binding: FragmentUploadImageBinding
    private val uploadImageViewModel: UploadImageViewModel by viewModels {
        TransactionViewModelFactory(Injection.provideTransactionRepository(requireContext()))
    }
    private lateinit var pdfPickerLauncher: ActivityResultLauncher<Intent>

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
            browseImage.setOnClickListener { startGallery() }
            browsePdf.setOnClickListener { openFilePicker() }
            analyze.setOnClickListener { analyzeImage() }
            loading.visibility = View.GONE
        }

        showImage()

        uploadImageViewModel.apply {
            message.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    showToast(it)
                }
            }

            isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
                disableSubmit(it)
            }

            predictResult.observe(viewLifecycleOwner) {
                binding.browseImage.isEnabled = true
                binding.browsePdf.isEnabled = true

                if (it?.data != null) {
                    val fragment = OcrResultFragment().apply {
                        arguments = Bundle().apply {
                            putParcelable(OcrResultFragment.EXTRA_PREDICT_RESULT, it)
                        }
                    }
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }

        pdfPickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    val selectedPdfUri: Uri? = result.data?.data
                    selectedPdfUri?.let { uri ->
                        binding.analyze.isEnabled = true
                        uploadImageViewModel.persistImage(uri)
                    }
                }
            }

        uploadImageViewModel.fileType.observe(viewLifecycleOwner) {
            if (it == "pdf") {
                binding.pdfView.visibility = View.VISIBLE
                binding.previewImageView.visibility = View.GONE
            } else {
                binding.pdfView.visibility = View.GONE
                binding.previewImageView.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        uploadImageViewModel.clearData()
        disableSubmit(true)
    }

    private fun analyzeImage() {
        uploadImageViewModel.imageUri.observe(viewLifecycleOwner) {
            if (it.toString().isNotEmpty()) {
                binding.browseImage.isEnabled = false
                binding.browsePdf.isEnabled = false

                val imageFile = if (uploadImageViewModel.fileType.value == "pdf") {
                    FileHandler().uriToPdfFile(it, requireContext())
                } else {
                    FileHandler().uriToFile(it, requireContext())
                }

                val mimeType = when (imageFile.extension.lowercase()) {
                    "jpg", "jpeg" -> "image/jpeg"
                    "png" -> "image/png"
                    "pdf" -> "application/pdf"
                    else -> throw IllegalArgumentException("Unsupported image type")
                }
                val requestImageFile =
                    imageFile.asRequestBody(mimeType.toMediaTypeOrNull())  //want send /jpeg|jpg|png/
                val multipartBody = requestImageFile.let { it1 ->
                    MultipartBody.Part.createFormData(
                        "file",
                        imageFile.name,
                        it1
                    )
                }
                uploadImageViewModel.predict(multipartBody)
            }
        }
    }

    private fun startGallery() {
        uploadImageViewModel.setFileType("image")
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
        uploadImageViewModel.fileType.observe(viewLifecycleOwner) {type ->
            uploadImageViewModel.imageUri.observe(viewLifecycleOwner) { uri ->
                if (uri.toString().isNotEmpty() && type == "image") {
                    binding.previewImageView.setImageURI(uri)
                }
            }
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

    private fun openFilePicker() {
        uploadImageViewModel.setFileType("pdf")
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "application/pdf"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        pdfPickerLauncher.launch(intent)
    }
}

