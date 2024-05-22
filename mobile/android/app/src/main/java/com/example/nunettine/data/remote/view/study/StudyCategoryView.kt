package com.example.nunettine.data.remote.view.study

import com.example.nunettine.data.remote.dto.study.StudyCategoryRes

interface StudyCategoryView {
    fun onGetStudyCategorySuccess(response: List<String>)
    fun onGetStudyCategoryFailure(result_code: Int)
}