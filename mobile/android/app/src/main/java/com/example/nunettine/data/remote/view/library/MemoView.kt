package com.example.nunettine.data.remote.view.library

import com.example.nunettine.data.remote.dto.library.MemoRes

interface MemoView {
    fun onGetMemoSuccess(response: MemoRes)
    fun onGetMemoFailure(result_code: Int)
}