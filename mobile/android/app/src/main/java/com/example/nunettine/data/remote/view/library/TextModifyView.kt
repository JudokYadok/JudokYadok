package com.example.nunettine.data.remote.view.library

import com.example.nunettine.data.remote.dto.BasicRes

interface TextModifyView {
    fun onGetTextModifySuccess(response: BasicRes)
    fun onGetTextModifyFailure(result_code: Int)
}