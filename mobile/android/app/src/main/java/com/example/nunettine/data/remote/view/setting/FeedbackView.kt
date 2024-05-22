package com.example.nunettine.data.remote.view.setting

import com.example.nunettine.data.remote.dto.BasicRes2

interface FeedbackView {
    fun onGetFeedbackSuccess(response: BasicRes2)
    fun onGetFeedbackFailure(result_code: Int)
}