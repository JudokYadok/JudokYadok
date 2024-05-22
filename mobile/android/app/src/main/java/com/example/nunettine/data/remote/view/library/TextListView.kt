package com.example.nunettine.data.remote.view.library

import com.example.nunettine.data.remote.dto.library.TextList
import com.example.nunettine.data.remote.dto.library.TextListRes

interface TextListView {
    fun onGetTextListSuccess(response: List<TextList>)
    fun onGetTextListFailure(result_code: Int)
}