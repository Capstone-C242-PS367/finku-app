package com.capstone.finku.repository

import android.util.Log
import com.capstone.finku.data.pref.UserModel
import com.capstone.finku.data.pref.UserPreference
import com.capstone.finku.data.response.DataProfile
import com.capstone.finku.data.response.LoginResponse
import com.capstone.finku.data.response.RegisterResponse
import com.capstone.finku.data.retrofit.ApiService

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun getProfile(id: String): DataProfile? {
        val response = apiService.getUserProfile(id)
        if (response.status == "success") {
            Log.d("UserRepository", "fetch profile: ${response.data}")
            return response.data
        } else {
            Log.e("UserRepository", "Failed fetch profil: ${response.message}")
            return null
        }
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}
