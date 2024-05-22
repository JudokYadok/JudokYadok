package com.example.nunettine.data.remote.dto.study

import com.google.gson.annotations.SerializedName

data class QuizSaveRes(
    @SerializedName("message") val message: String,
    @SerializedName("quiz_id") val quiz_id: Int
)