package com.sarikaya.kotlinbasicnoteapp.model

import com.google.gson.annotations.SerializedName

data class GetMeResponse(val code: String, val data: UserData, val message: String)
data class UserData(@SerializedName("full_name") val fullName: String, val email: String)
