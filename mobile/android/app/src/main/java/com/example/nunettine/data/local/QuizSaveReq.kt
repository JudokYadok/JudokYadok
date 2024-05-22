package com.example.nunettine.data.local

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("QuizSaveReq")
data class QuizSaveReq(
    val text_id: Int,
    val quiz_list: Quiz_List,
    val user_answer_list: List<Int>,
    val correct_answer_list: List<Int>
)

data class Quiz_List(
    val question_list: List<String>,
    val answer_list: Answer_List
)

data class Answer_List(
    val answer_list1: List<String>,
    val answer_list2: List<String>,
    val answer_list3: List<String>
)

