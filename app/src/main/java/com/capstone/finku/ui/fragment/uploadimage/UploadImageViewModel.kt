package com.capstone.finku.ui.fragment.uploadimage

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.finku.data.response.PredictResponse
import com.capstone.finku.repository.TransactionRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class UploadImageViewModel(
    private val repository: TransactionRepository
) : ViewModel() {
    private val _imageUri = MutableLiveData<Uri>()
    val imageUri: LiveData<Uri> = _imageUri

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _predictResult = MutableLiveData<PredictResponse?>()
    val predictResult: LiveData<PredictResponse?> = _predictResult

    fun persistImage(uri: Uri) {
        _imageUri.value = uri
    }

    fun predict(file: MultipartBody.Part) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.predict(file)

                _predictResult.value = response
                _message.value = response.message ?: ""
            } catch (e: Exception) {
                _message.value = "An unexpected error occurred: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}