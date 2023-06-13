package com.sarikaya.kotlinbasicnoteapp.model

import com.google.gson.annotations.SerializedName

data class LoginTokenData(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String
)
