package com.example.nunettine.data.remote.retrofit

import com.example.nunettine.data.local.FeedbackReq
import com.example.nunettine.data.local.UserReq
import com.example.nunettine.data.remote.dto.BasicRes
import com.example.nunettine.data.remote.dto.BasicRes2
import com.example.nunettine.data.remote.dto.setting.UserRes
import retrofit2.Call
import retrofit2.http.*

interface SettingRetrofitInterface {
    @GET("/user/setting/mypage/{user_id}")
    fun getMyPage(@Path("user_id") user_id: Int): Call<UserRes>

    @PUT("/user/setting/mypage")
    fun putMyPage(@Body userReq: UserReq): Call<BasicRes2>

    @POST("/user/setting/feedback/{user_id}")
    fun postFeedback(@Path("user_id") user_id: Int, @Body feedbackReq: FeedbackReq): Call<BasicRes2>

    @DELETE("/user/setting/mypage/{user_id}")
    fun delMyPage(@Path("user_id") user_id: Int): Call<BasicRes2>
}