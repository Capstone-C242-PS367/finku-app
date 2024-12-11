package com.capstone.finku.repository

import com.capstone.finku.data.request.PostTransactionRequest
import com.capstone.finku.data.response.PredictResponse
import com.capstone.finku.data.response.ResultItem
import com.capstone.finku.data.response.TransactionListResponse
import com.capstone.finku.data.response.TransactionResponse
import com.capstone.finku.data.retrofit.ApiService
import okhttp3.MultipartBody
import kotlin.concurrent.Volatile

class TransactionRepository private constructor(
    private val apiService: ApiService
){
    suspend fun predict(file: MultipartBody.Part): PredictResponse {
        return apiService.predict(file)
    }

    suspend fun storeTransaction(bodyTransactionRequest: PostTransactionRequest): TransactionResponse {
        return apiService.storeTransaction(bodyTransactionRequest)
    }

    suspend fun getTransactions(id: String): TransactionListResponse {
        return apiService.getTransactions(id)
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