package com.capstone.finku.ui.fragment.ocrresult

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.finku.data.request.PostTransactionRequest
import com.capstone.finku.data.response.ResultItem
import com.capstone.finku.data.response.TransactionResponse
import com.capstone.finku.repository.TransactionRepository
import kotlinx.coroutines.launch

class OcrResultViewModel(
    private val repository: TransactionRepository
): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _transactionResult = MutableLiveData<TransactionResponse?>()
    val transactionResult: LiveData<TransactionResponse?> = _transactionResult

    fun storeTransaction(bodyTransactionRequest: PostTransactionRequest) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.storeTransaction(bodyTransactionRequest)

                _transactionResult.value = response
                _message.value = response.message ?: ""
            } catch (e: Exception) {
                _message.value = "An unexpected error occurred: ${e.message}"
                _transactionResult.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}