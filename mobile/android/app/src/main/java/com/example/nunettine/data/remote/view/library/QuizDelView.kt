package com.example.nunettine.data.remote.view.library

import com.example.nunettine.data.remote.dto.BasicRes

interface QuizDelView {
    fun deleteQuizSuccess(response: BasicRes, position: Int)
    fun deleteQuizFailure(message: String)
}