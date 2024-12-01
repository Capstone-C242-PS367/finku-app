package com.capstone.finku.ui.fragment.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.dataStore
import androidx.fragment.app.Fragment
import com.capstone.finku.data.pref.UserModel
import com.capstone.finku.data.pref.UserPreference
import com.capstone.finku.data.pref.dataStore
import com.capstone.finku.databinding.FragmentProfileBinding
import com.capstone.finku.ui.activity.welcome.WelcomeActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

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
//        activity.setSupportActionBar(binding.tvToolbarTitle)
        activity.supportActionBar?.title = "Profile"
        initProfile()
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

    private fun initProfile() {
        val pref = UserPreference.getInstance(requireContext().dataStore)
        val user = runBlocking {
            pref.getSession().first()
        }

        binding.apply {
            tvUserName.text = user.name
            tvUserEmail.text = user.email
        }
    }

}