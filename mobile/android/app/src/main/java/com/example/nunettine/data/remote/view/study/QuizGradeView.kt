package com.example.nunettine.data.remote.view.study

import com.example.nunettine.data.remote.dto.study.QuizGradeRes

interface QuizGradeView {
    fun onGetQuizGradeSuccess(response: QuizGradeRes)
    fun onGetQuizGradeFailure(result_code: Int)
}