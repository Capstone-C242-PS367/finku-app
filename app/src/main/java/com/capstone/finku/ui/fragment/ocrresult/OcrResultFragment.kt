package com.capstone.finku.ui.fragment.ocrresult

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.finku.databinding.FragmentOcrResultBinding

class OcrResultFragment : Fragment() {
    private lateinit var binding: FragmentOcrResultBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOcrResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.title = "Result"
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvResult.apply {
            layoutManager = LinearLayoutManager(context)
            //adapter = RecapAdapter()
        }
    }
}