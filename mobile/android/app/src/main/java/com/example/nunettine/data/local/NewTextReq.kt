package com.example.nunettine.data.local

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("NewTextReq")
data class NewTextReq(
    val category: String,
    val title: String,
    val contents: String
)
