package com.capstone.finku.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.finku.repository.TransactionRepository
import com.capstone.finku.ui.fragment.uploadimage.UploadImageViewModel

class TransactionViewModelFactory(
    private val repository: TransactionRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(UploadImageViewModel::class.java) -> {
                UploadImageViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}