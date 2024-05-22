package com.example.nunettine.data.local

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("FeedbackReq")
data class FeedbackReq(
    val contents: String
)
