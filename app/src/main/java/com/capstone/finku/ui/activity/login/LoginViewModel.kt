package com.capstone.finku.ui.activity.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.finku.data.pref.UserModel
import com.capstone.finku.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginStatus = MutableLiveData<Result<Boolean>>()
    val loginStatus: LiveData<Result<Boolean>> = _loginStatus

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginStatus.value = try {
                val response = repository.login(email, password)
                val token = response.accessToken
                val nameData = response.name ?: ""
                val emailData = response.email ?: ""

                if (token != null) {
                    repository.saveSession(
                        UserModel(
                            userId = "",
                            name = nameData,
                            email = emailData,
                            token = token,
                            isLogin = true
                        )
                    )
                    Result.success(true)
                } else {
                    Result.failure(Exception("Login failed: Invalid response"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}

