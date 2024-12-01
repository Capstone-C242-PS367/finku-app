package com.capstone.finku.ui.fragment.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.finku.data.pref.UserPreference
import com.capstone.finku.data.pref.dataStore
import com.capstone.finku.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        initHome()
    }

    private fun setupRecyclerView() {
        binding.rvRecaps.apply {
            layoutManager = LinearLayoutManager(context)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initHome() {
        val pref = UserPreference.getInstance(requireContext().dataStore)
        val user = runBlocking {
            pref.getSession().first()
        }
    Log.d("USER", user.toString())
        binding.apply {
            val name = if(user.name.isEmpty()) {
                user.email
            } else {
                user.name
            }
            tvGreeting.text = "Hi, $name"
        }
    }

}