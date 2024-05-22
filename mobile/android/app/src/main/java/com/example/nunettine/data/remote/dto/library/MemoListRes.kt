package com.example.nunettine.data.remote.dto.library

import com.google.gson.annotations.SerializedName

data class MemoListRes(
    @SerializedName("memo_list") val memo_list: List<MemoList>
)

data class MemoList(
    @SerializedName("memo_id") val memo_id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("updatedAt") val updatedAt: String
)
