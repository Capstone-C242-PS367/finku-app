package com.capstone.finku.ui.fragment.uploadimage

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.capstone.finku.R
import com.capstone.finku.databinding.FragmentUploadImageBinding
import com.capstone.finku.ui.fragment.ocrresult.OcrResultFragment


class UploadImageFragment : Fragment() {

    private lateinit var binding:FragmentUploadImageBinding
    private var image: Uri? = null

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
        }
    }

    private fun analyzeImage() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, OcrResultFragment())
            .commit()
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            image = uri
            showImage()
            binding.analyze.isEnabled = true
            binding.analyze.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
        } else {
            binding.analyze.isEnabled = false
        }
    }

    private fun showImage() {
        image?.let { uri ->
            binding.previewImageView.setImageURI(uri)
        }
    }
}

