package com.capstone.finku.data.response


data class ProfileResponse(
    val data: DataProfile? = null,
    val message: String? = null,
    val status: String? = null
)

data class DataProfile(
    val password: String? = null,
    val updatedAt: String? = null,
    val name: String? = null,
    val createdAt: String? = null,
    val id: String? = null,
    val email: String? = null
)
