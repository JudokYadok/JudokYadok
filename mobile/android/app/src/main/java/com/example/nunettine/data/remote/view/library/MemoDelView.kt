package com.example.nunettine.data.remote.view.library

import com.example.nunettine.data.remote.dto.BasicRes

interface MemoDelView {
    fun onGetMemoDeleteSuccess(response: BasicRes, position: Int)
    fun onGetMemoDeleteFailure(result_code: Int)
}