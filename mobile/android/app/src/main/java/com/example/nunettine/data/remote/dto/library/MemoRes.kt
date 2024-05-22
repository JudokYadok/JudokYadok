package com.example.nunettine.data.remote.dto.library

import com.google.gson.annotations.SerializedName

data class MemoRes(
    @SerializedName("memo_id") val memo_id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("contents") val contents: String
)
