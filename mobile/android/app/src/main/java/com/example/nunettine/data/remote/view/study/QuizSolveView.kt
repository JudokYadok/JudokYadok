package com.example.nunettine.data.remote.view.study

import com.example.nunettine.data.remote.dto.study.QuizSolveRes

interface QuizSolveView {
    fun onGetQuizSolveSuccess(response: QuizSolveRes)
    fun onGetQuizSolveFailure(result_code: Int)
}