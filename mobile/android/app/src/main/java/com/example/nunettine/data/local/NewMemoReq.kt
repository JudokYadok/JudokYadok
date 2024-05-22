package com.example.nunettine.data.local

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("NewMemoReq")
data class NewMemoReq(
    val title: String,
    val contents: String
)
