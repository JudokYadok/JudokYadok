package com.example.nunettine.data.local

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("UserReq")
data class UserReq(
    val name: String,
    val email: String,
    val d_day: UserDday,
    val user_id: Int
)

data class UserDday(
    val year: Int,
    val month: Int,
    val date: Int
)
