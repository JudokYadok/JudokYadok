package com.example.nunettine.data.remote.view.study

import com.example.nunettine.data.remote.dto.study.StudyGradeRes

interface StudyGradeView {
    fun onGetStudyGradeSuccess(response: StudyGradeRes)
    fun onGetStudyGradeFailure(result_code: Int, result_req: String)
}