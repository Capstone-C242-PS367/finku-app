package com.capstone.finku.data.di

import android.content.Context
import android.util.Log
import com.capstone.finku.data.pref.UserPreference
import com.capstone.finku.data.pref.dataStore
import com.capstone.finku.data.retrofit.ApiConfig
import com.capstone.finku.repository.TransactionRepository
import com.capstone.finku.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(pref, apiService)
    }

    fun provideTransactionRepository(context: Context): TransactionRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        Log.d("TOKEN", user.token)
        Log.d("USER ID", user.id)
        return TransactionRepository.getInstance(apiService)
    }
}
