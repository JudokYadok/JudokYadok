package com.example.nunettine.data.remote.dto.study

import com.google.gson.annotations.SerializedName

data class StudyDetailRes(
    @SerializedName("title") val title: String,
    @SerializedName("year") val year: String,
    @SerializedName("contents") val contents: String
)
