package com.capstone.finku.ui.fragment.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.finku.data.response.DataProfile
import com.capstone.finku.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {
    private val _profileDetail = MutableLiveData<DataProfile?>()
    val detailProfile: LiveData<DataProfile?> = _profileDetail

    fun getDetailProfile(id: String) {
        viewModelScope.launch {
            try {
                val result = repository.getProfile(id)
                if (result != null) {
                    Log.d("ProfileViewModel", "Data fetched successfully: ,${result.name}, ${result.email}, ${result.id}")
                    _profileDetail.value = result
                } else {
                    _profileDetail.value = null
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error fetch profile", e)
                _profileDetail.value = null
            }
        }
    }

}
