package com.capstone.finku.ui.activity.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.finku.data.ViewModelFactory
import com.capstone.finku.data.di.Injection
import com.capstone.finku.databinding.ActivityLoginBinding
import com.capstone.finku.ui.activity.main.MainActivity
import com.capstone.finku.ui.activity.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels {
        ViewModelFactory(Injection.provideRepository(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initActions()
        observeViewModel()
    }

    private fun initActions() {
        binding.apply {
            btnLogin.setOnClickListener {
                val email = emailEditText.text.toString().trim()
                val password = passwordEditText.text.toString().trim()

                if (validateInput(email, password)) {
                    showLoading(true)
                    viewModel.login(email, password)
                }
            }

            btnRegister.setOnClickListener {
                navigateToRegister()
            }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                showToast("Email is required")
                false
            }
            password.isEmpty() -> {
                showToast("Password is required")
                false
            }
            else -> true
        }
    }

    private fun observeViewModel() {
        viewModel.loginStatus.observe(this) { result ->
            showLoading(false)
            result.onSuccess {
                onLoginSuccess()
            }.onFailure { error ->
                showToast(error.message ?: "An unexpected error occurred")
            }
        }
    }

    private fun onLoginSuccess() {
        showToast("Login successful")
        navigateToMain()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}



