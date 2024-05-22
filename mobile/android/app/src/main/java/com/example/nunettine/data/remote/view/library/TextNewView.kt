package com.example.nunettine.data.remote.view.library

import com.example.nunettine.data.remote.dto.BasicRes

interface TextNewView {
    fun onGetTextNewSuccess(response: BasicRes)
    fun onGetTextNewFailure(result_code: Int)
}