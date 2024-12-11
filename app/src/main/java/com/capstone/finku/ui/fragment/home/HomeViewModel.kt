package com.capstone.finku.ui.fragment.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.finku.data.response.TransactionListResponse
import com.capstone.finku.repository.TransactionRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel(
    private val repository: TransactionRepository
): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _transactionList = MutableLiveData<TransactionListResponse>()
    val transactionList: LiveData<TransactionListResponse> = _transactionList

    private val _totalCredit = MutableLiveData<Int>()
    val totalCredit: LiveData<Int> = _totalCredit

    private val _totalDebt = MutableLiveData<Int>()
    val totalDebt: LiveData<Int> = _totalDebt

    fun getTransactions(id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getTransactions(id)

                _transactionList.value = response

                // Sum total credit
                _totalCredit.value = response.data
                    ?.filter { it?.type == "CR" }
                    ?.sumOf { it?.amount?.toInt() ?: 0 }
                    ?: 0

                // Sum total debt
                _totalDebt.value = response.data
                    ?.filter { it?.type == "DB" }
                    ?.sumOf { it?.amount?.toInt() ?: 0 }
                    ?: 0

            } catch (e: HttpException) {
                _message.value = e.message
                _transactionList.value = TransactionListResponse(
                    data = null,
                    message = null,
                    status = null
                )
            } finally {
                _isLoading.value = false
            }
        }
    }
}