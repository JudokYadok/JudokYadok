package com.example.nunettine.data.remote.dto.study

import com.google.gson.annotations.SerializedName

data class StudyListRes(
    @SerializedName("text_list") val text_list: List<TextList>
)

data class TextList(
    @SerializedName("text_id") val text_id: Int,
    @SerializedName("title") val title: String
)
