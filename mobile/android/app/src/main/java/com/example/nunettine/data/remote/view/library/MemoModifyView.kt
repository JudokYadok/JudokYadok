package com.example.nunettine.data.remote.view.library

import com.example.nunettine.data.remote.dto.BasicRes

interface MemoModifyView {
    fun onGetMemoModifySuccess(response: BasicRes)
    fun onGetMemoModifyFailure(result_code: Int)
}