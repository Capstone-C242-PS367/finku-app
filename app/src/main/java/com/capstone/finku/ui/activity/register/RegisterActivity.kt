package com.capstone.finku.ui.activity.register


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.finku.R
import com.capstone.finku.data.ViewModelFactory
import com.capstone.finku.data.di.Injection
import com.capstone.finku.databinding.ActivityRegisterBinding
import com.capstone.finku.ui.activity.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels {
        ViewModelFactory(Injection.provideRepository(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initActions()
        observeViewModel()
    }

    private fun initActions() {
        binding.btnRegister.setOnClickListener {
            val name = binding.fullnameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (validateInput(name, email, password)) {
                binding.progressBar.visibility = View.VISIBLE
                viewModel.register(name, email, password)
            }
        }
    }

    private fun validateInput(name: String, email: String, password: String): Boolean {
        return when {
            name.isEmpty() -> {
                showToast("Name is required")
                false
            }
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
        viewModel.registrationStatus.observe(this) { result ->
            binding.progressBar.visibility = View.GONE
            result.onSuccess {
                showToast(getString(R.string.success_message))
                navigateToLogin()
            }.onFailure { error ->
                showToast(error.message ?: "An unexpected error occurred")
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
