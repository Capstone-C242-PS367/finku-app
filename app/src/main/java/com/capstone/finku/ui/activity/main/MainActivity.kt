package com.capstone.finku.ui.activity.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.finku.R
import com.capstone.finku.databinding.ActivityMainBinding
import com.capstone.finku.ui.fragment.home.HomeFragment
import com.capstone.finku.ui.fragment.profile.ProfileFragment
import com.capstone.finku.ui.fragment.uploadimage.UploadImageFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment())
                .commit()
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, HomeFragment())
                        .commit()
                    true
                }

                R.id.navigation_dashboard -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, UploadImageFragment())
                        .commit()
                    true
                }
                R.id.navigation_profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, ProfileFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}