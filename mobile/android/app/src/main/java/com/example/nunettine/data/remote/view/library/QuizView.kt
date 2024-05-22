package com.example.nunettine.data.remote.view.library

import com.example.nunettine.data.remote.dto.library.QuizSaveDetailRes

interface QuizView {
    fun onGetQuizSuccess(response: QuizSaveDetailRes)
    fun onGetQuizFailure(result_code: Int)
}