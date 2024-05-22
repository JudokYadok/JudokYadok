package com.example.nunettine.data.remote.dto.setting

import com.google.gson.annotations.SerializedName

data class UserRes(
    @SerializedName("user_id") val user_id: Int,
    @SerializedName("kakao_id") val kakao_id: String,
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("d_day") val d_day: DDay
)

data class DDay(
    @SerializedName("year") val year: Int,
    @SerializedName("month") val month: Int,
    @SerializedName("date") val date: Int
)
