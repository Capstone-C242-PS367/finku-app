package com.capstone.finku.ui.fragment.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.capstone.finku.data.ViewModelFactory
import com.capstone.finku.data.di.Injection
import com.capstone.finku.data.pref.UserPreference
import com.capstone.finku.data.pref.dataStore
import com.capstone.finku.data.response.DataProfile
import com.capstone.finku.databinding.FragmentProfileBinding
import com.capstone.finku.ui.activity.welcome.WelcomeActivity
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.title = "Profile"

        setupData()
        binding.btnLogout.setOnClickListener {
            runBlocking {
                onLogout()
            }
        }
    }

    private suspend fun onLogout() {
        val userPreference = context?.let { UserPreference.getInstance(it.dataStore) }
        userPreference?.logout()
        activity.let {
            val intent = Intent(it, WelcomeActivity::class.java)
            it?.startActivity(intent)
            it?.finish()
        }
    }
    private fun setupData() {
        val userPreference = UserPreference.getInstance(requireContext().dataStore)
        lifecycleScope.launch {
            val user = userPreference.getSession().firstOrNull()
            if (user != null) {
                viewModel.getDetailProfile(user.id)

                viewModel.detailProfile.observe(viewLifecycleOwner) { profile ->
                    if (profile != null) {
                        Log.d("ProfileFragment", "Profile data: ${profile.name}, ${profile.email}")
                        showProfileDetail(profile)
                    } else {
                        Toast.makeText(requireContext(), "Failed load data", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showProfileDetail(profile: DataProfile) {
        binding.apply {
            tvUserName.text = profile.name
            tvUserEmail.text = profile.email
        }
    }

}
