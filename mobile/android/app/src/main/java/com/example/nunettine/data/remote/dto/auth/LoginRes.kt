package com.example.nunettine.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class LoginRes(
    @SerializedName("user_id") val user_id: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("access_token") val access_token: String,
    @SerializedName("refresh_token") val refresh_token: String
)