package com.example.nunettine.data.remote.view.study

import com.example.nunettine.data.remote.dto.study.StudyDetailRes

interface StudyDetailView {
    fun onGetStudyDetailSuccess(response: StudyDetailRes)
    fun onGetStudyDetailFailure(result_code: Int)
}