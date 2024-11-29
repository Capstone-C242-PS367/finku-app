package com.capstone.finku.data.pref

data class UserModel(
    val userId: String,
    val name: String,
    val email: String,
    val token: String,
    val isLogin: Boolean = false,
)

