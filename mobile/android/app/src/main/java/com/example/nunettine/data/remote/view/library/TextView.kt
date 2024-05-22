package com.example.nunettine.data.remote.view.library

import com.example.nunettine.data.remote.dto.library.TextRes

interface TextView {
    fun onGetTextSuccess(response: TextRes)
    fun onGetTextFailure(result_code: Int)
}