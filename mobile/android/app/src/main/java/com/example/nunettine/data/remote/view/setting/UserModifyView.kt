package com.example.nunettine.data.remote.view.setting

import com.example.nunettine.data.remote.dto.BasicRes2

interface UserModifyView {
    fun onGetUserModifySuccess(response: BasicRes2)
    fun onGetUserModifyFailure(result_code: Int)
}