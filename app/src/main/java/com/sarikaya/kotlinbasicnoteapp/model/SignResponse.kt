package com.sarikaya.kotlinbasicnoteapp.model

data class SignResponse(
    val code: String,
    val message: String,
    val data: LoginTokenData
)
