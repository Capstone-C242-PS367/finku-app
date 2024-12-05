package com.capstone.finku.repository

import com.capstone.finku.data.response.PredictResponse
import com.capstone.finku.data.retrofit.ApiService
import okhttp3.MultipartBody
import kotlin.concurrent.Volatile

class TransactionRepository private constructor(
    private val apiService: ApiService
){
    suspend fun predict(file: MultipartBody.Part): PredictResponse {
        return apiService.predict(file)
    }

    companion object {
        @Volatile
        private var instance: TransactionRepository? = null
        fun getInstance(
            apiService: ApiService
        ): TransactionRepository =
            instance ?: synchronized(this) {
                instance ?: TransactionRepository(apiService)
            }.also {
                instance = it
            }
    }
}