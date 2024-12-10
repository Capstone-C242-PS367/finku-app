package com.capstone.finku.data.request

import com.capstone.finku.data.response.ResultItem
import com.google.gson.annotations.SerializedName

data class PostTransactionRequest(

	@field:SerializedName("data")
	val data: List<ResultItem?>? = null,

	@field:SerializedName("user_id")
	val userId: String? = null
)
