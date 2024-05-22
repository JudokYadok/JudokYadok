package com.example.nunettine.data.remote.view.library

import com.example.nunettine.data.remote.dto.BasicRes

interface TextDelView {
    fun deleteTextSuccess(response: BasicRes, position: Int)
    fun deleteTextFailure(message: String)
}