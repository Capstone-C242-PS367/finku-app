package com.capstone.finku.ui.activity.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.finku.data.pref.UserModel
import com.capstone.finku.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginStatus = MutableLiveData<Result<Boolean>>()
    val loginStatus: LiveData<Result<Boolean>> = _loginStatus

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginStatus.value = try {
                val response = repository.login(email, password)
                val token = response.accessToken

                if (token != null) {
                    repository.saveSession(
                        UserModel(
                            userId = "",
                            name = "",
                            email = email,
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

