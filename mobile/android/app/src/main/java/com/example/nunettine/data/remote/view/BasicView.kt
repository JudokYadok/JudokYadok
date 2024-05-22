package com.example.nunettine.data.remote.view

import com.example.nunettine.data.remote.dto.BasicRes

interface BasicView {
    fun onBasicSuccess(response: BasicRes)
    fun onBasicFailure(result_code: Int, result_req: String)
}