package com.capstone.finku.data.response

import com.google.gson.annotations.SerializedName

data class PredictResponse(

	@field:SerializedName("data")
	val data: ResponseData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class ResponseData(

	@field:SerializedName("result")
	val result: List<ResultItem?>? = null,

	@field:SerializedName("total_credit")
	val totalCredit: Int? = null,

	@field:SerializedName("difference")
	val difference: Int? = null,

	@field:SerializedName("total_debit")
	val totalDebit: Int? = null
)

data class ResultItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)
