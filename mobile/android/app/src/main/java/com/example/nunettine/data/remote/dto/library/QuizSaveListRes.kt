package com.example.nunettine.data.remote.dto.library

import com.google.gson.annotations.SerializedName

data class QuizSaveListRes(
    @SerializedName("quiz_save_list") val quiz_save_list: List<QuizSaveList>
)

data class QuizSaveList(
    @SerializedName("quiz_id") val quiz_id: Int,
    @SerializedName("text_id") val text_id: Int,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("title") val title: String
)