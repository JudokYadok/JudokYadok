package com.example.nunettine.data.remote.view.setting

import com.example.nunettine.data.remote.dto.setting.UserRes

interface UserView {
    fun onGetUserSuccess(response: UserRes)
    fun onGetUserFailure(result_code: Int)
}