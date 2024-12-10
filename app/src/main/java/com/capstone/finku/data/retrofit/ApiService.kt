package com.capstone.finku.data.retrofit

import com.capstone.finku.data.request.PostTransactionRequest
import com.capstone.finku.data.response.LoginResponse
import com.capstone.finku.data.response.PredictResponse
import com.capstone.finku.data.response.RegisterResponse
import com.capstone.finku.data.response.TransactionResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @FormUrlEncoded
    @POST("users")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("/auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @Multipart
    @POST("/predict")
    suspend fun predict(
        @Part file: MultipartBody.Part
    ): PredictResponse

    @POST("/transactions")
    suspend fun storeTransaction(
        @Body requestBody: PostTransactionRequest
    ): TransactionResponse
}