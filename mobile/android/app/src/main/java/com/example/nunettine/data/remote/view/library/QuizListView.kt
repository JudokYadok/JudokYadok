package com.example.nunettine.data.remote.view.library

import com.example.nunettine.data.remote.dto.library.QuizSaveList

interface QuizListView {
    fun onGetQuizListSuccess(response: List<QuizSaveList>)
    fun onGetQuizListFailure(result_code: Int)
}