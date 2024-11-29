package com.capstone.finku.ui.activity.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.finku.repository.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _registrationStatus = MutableLiveData<Result<Boolean>>()
    val registrationStatus: LiveData<Result<Boolean>> = _registrationStatus

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _registrationStatus.value = try {
                val response = repository.register(name, email, password)
                if (response.status == "success") {
                    Result.success(true)
                } else {
                    Result.failure(Exception(response.message ?: "Unknown error"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}

