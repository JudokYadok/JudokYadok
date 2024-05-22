package com.example.nunettine.data.remote.view.study

import com.example.nunettine.data.remote.dto.study.QuizSaveRes

interface QuizSaveView {
    fun setQuizSaveSuccess(response: QuizSaveRes)
    fun setQuizSaveFailure(message: String)
}