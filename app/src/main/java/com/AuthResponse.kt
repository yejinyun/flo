package com

import com.google.gson.annotations.SerializedName

data class Auth(
    @SerializedName("userInx") val userIdx: Int,
    @SerializedName("jwt") val jwt: String
    )

data class AuthResponse(
    @SerializedName("isSunccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: Auth?
    )
