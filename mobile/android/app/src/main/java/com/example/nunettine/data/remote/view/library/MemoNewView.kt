package com.example.nunettine.data.remote.view.library

import com.example.nunettine.data.remote.dto.BasicRes

interface MemoNewView {
    fun onGetMemoNewSuccess(response: BasicRes)
    fun onGetMemoNewFailure(result_code: Int)
}