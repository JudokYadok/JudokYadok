package com.example.nunettine.data.remote.view.setting

import com.example.nunettine.data.remote.dto.BasicRes2

interface UserDeleteView {
    fun onGetUserDeleteSuccess(response: BasicRes2)
    fun onGetUserDeleteFailure(result_code: Int)
}