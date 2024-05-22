package com.example.nunettine.data.remote.dto.library

import com.google.gson.annotations.SerializedName

data class TextListRes(
    @SerializedName("text_list") val text_list: List<TextList>
)

data class TextList(
    @SerializedName("category") val text_category: String,
    @SerializedName("title") val text_title: String,
    @SerializedName("text_id") val text_id: Int
)
