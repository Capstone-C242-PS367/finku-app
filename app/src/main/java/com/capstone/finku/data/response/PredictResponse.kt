package com.capstone.finku.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PredictResponse(

	@field:SerializedName("data")
	val data: ResponseData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
): Parcelable

@Parcelize
data class ResponseData(

	@field:SerializedName("result")
	val result: List<ResultItem?>? = null,

	@field:SerializedName("total_credit")
	val totalCredit: Int? = null,

	@field:SerializedName("difference")
	val difference: Int? = null,

	@field:SerializedName("total_debit")
	val totalDebit: Int? = null
): Parcelable

@Parcelize
data class ResultItem(

	@field:SerializedName("date")
	var date: String? = null,

	@field:SerializedName("amount")
	var amount: String? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("type")
	var type: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("title")
	var title: String? = null
): Parcelable
