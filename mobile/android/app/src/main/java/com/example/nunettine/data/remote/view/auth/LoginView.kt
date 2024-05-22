package com.example.nunettine.data.remote.view.auth

import com.example.nunettine.data.remote.dto.auth.LoginRes

interface LoginView {
    fun onGetLoginSuccess(response: LoginRes)
    fun onGetLoginFailure(result_req: String)
}