package com.capstone.finku.ui.activity.welcome

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.finku.data.ViewModelFactory
import com.capstone.finku.data.di.Injection
import com.capstone.finku.data.pref.UserPreference
import com.capstone.finku.data.pref.dataStore
import com.capstone.finku.databinding.ActivityWelcomeBinding
import com.capstone.finku.ui.activity.login.LoginActivity
import com.capstone.finku.ui.activity.login.LoginViewModel
import com.capstone.finku.ui.activity.main.MainActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var pref: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pref = UserPreference.getInstance(this.dataStore)

        binding.btnGettingStarted.setOnClickListener {
            val intent: Intent
            val session = runBlocking { pref.getSession().first() }

            if(session.isLogin) {
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}