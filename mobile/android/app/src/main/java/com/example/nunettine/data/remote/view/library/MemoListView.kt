package com.example.nunettine.data.remote.view.library

import com.example.nunettine.data.remote.dto.library.MemoList

interface MemoListView {
    fun onGetMemoListSuccess(response: List<MemoList>)
    fun onGetMemoListFailure(result_code: Int)
}