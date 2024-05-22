package com.example.nunettine.data.remote.dto.study

import com.google.gson.annotations.SerializedName

data class QuizSolveRes(
    @SerializedName("questions") val questions: List<Question>
)

data class Question(
    @SerializedName("question") val question: String,
    @SerializedName("answers") val answers: List<Answer>
)

data class Answer(
    @SerializedName("number") val number: Int,
    @SerializedName("answer") val answer: String,
    @SerializedName("correct") val correct: Boolean
)

