package com.example.nunettine.data.remote.view.study

import com.example.nunettine.data.remote.dto.study.TextList

interface StudyListView {
    fun onGetStudyListSuccess(response: List<TextList>)
    fun onGetStudyListFailure(result_code: Int)
}