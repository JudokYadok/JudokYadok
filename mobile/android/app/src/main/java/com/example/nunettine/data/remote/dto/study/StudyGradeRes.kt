package com.example.nunettine.data.remote.dto.study

import com.google.gson.annotations.SerializedName

data class StudyGradeRes(
    @SerializedName("quiz_answer") val quiz_answer: ArrayList<Int>,
    @SerializedName("text_summary") val text_summary: String
)