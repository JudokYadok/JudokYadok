package com.example.nunettine.data.remote.dto.library

import com.example.nunettine.data.local.Answer_List
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

data class QuizSaveDetailRes(
    @SerializedName("user_id") val user_id: Int,
    @SerializedName("text_id") val text_id: Int,
    @SerializedName("questions") val questions: List<String>,
    @SerializedName("answers") val answers: AnswerList,
    @SerializedName("user_answers") val user_answers: List<Int>,
    @SerializedName("correct_answers") val correct_answers: List<Int>,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("text_title") val text_title: String,
    @SerializedName("text_contents") val text_contents: String
)

data class AnswerList(
    @SerializedName("answer_list1") val answer_list1: List<String>,
    @SerializedName("answer_list2") val answer_list2: List<String>,
    @SerializedName("answer_list3") val answer_list3: List<String>
)
