package com.capstone.finku.data.response

import com.google.gson.annotations.SerializedName

data class TransactionDetailResponse(

	@field:SerializedName("short_description")
	val shortDescription: String? = null,

	@field:SerializedName("total_debt")
	val totalDebt: String? = null,

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("total_credit")
	val totalCredit: String? = null,

	@field:SerializedName("difference")
	val difference: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)

data class DataItem(

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("category")
	val category: String? = null
)
