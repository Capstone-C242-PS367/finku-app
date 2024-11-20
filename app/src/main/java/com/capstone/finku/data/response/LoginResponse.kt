package com.capstone.finku.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("access_token")
	val accessToken: String? = null
)
