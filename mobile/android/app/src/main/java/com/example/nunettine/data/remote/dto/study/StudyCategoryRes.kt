package com.example.nunettine.data.remote.dto.study

import com.google.gson.annotations.SerializedName

data class StudyCategoryRes(
    @SerializedName("category_list") val category_list: List<String>
)
