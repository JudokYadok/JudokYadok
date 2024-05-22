package com.example.nunettine.data.remote.service.setting

import android.util.Log
import com.example.nunettine.data.local.FeedbackReq
import com.example.nunettine.data.local.UserReq
import com.example.nunettine.data.remote.dto.BasicRes
import com.example.nunettine.data.remote.dto.BasicRes2
import com.example.nunettine.data.remote.dto.setting.UserRes
import com.example.nunettine.data.remote.retrofit.SettingRetrofitInterface
import com.example.nunettine.data.remote.view.setting.FeedbackView
import com.example.nunettine.data.remote.view.setting.UserDeleteView
import com.example.nunettine.data.remote.view.setting.UserModifyView
import com.example.nunettine.data.remote.view.setting.UserView
import com.example.nunettine.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserService {
    private lateinit var userView: UserView
    private lateinit var userModifyView: UserModifyView
    private lateinit var userDeletView: UserDeleteView
    private lateinit var feedbackView: FeedbackView

    fun setUserView(userView: UserView) {
        this.userView = userView
    }

    fun setUserModifyView(userModifyView: UserModifyView) {
        this.userModifyView = userModifyView
    }

    fun setFeedbackView(feedbackView: FeedbackView) {
        this.feedbackView = feedbackView
    }

    fun setUserDeleteView(userDeleteView: UserDeleteView) {
        this.userDeletView = userDeleteView
    }

    fun getUser(userId: Int) {
        val userService = getRetrofit().create(SettingRetrofitInterface::class.java)
        userService.getMyPage(userId).enqueue(object : Callback<UserRes> {
            override fun onResponse(call: Call<UserRes>, response: Response<UserRes>) {
                if (response.isSuccessful) {
                    val resp: UserRes? = response.body()
                    if (resp != null) {
                        userView.onGetUserSuccess(resp)
                    } else {
                        Log.e("USER-SUCCESS", "Response body is null")
                    }
                } else {
                    Log.e("USER-SUCCESS", "Response not successful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UserRes>, t: Throwable) {
                Log.d("USER-FAILURE", t.toString())
            }
        })
    }

    fun putUser(userReq: UserReq) {
        val userModifyService = getRetrofit().create(SettingRetrofitInterface::class.java)
        userModifyService.putMyPage(userReq).enqueue(object : Callback<BasicRes2> {
            override fun onResponse(call: Call<BasicRes2>, response: Response<BasicRes2>) {
                if (response.isSuccessful) {
                    val resp: BasicRes2? = response.body()
                    if (resp != null) {
                        userModifyView.onGetUserModifySuccess(resp)
                    } else {
                        Log.e("USER-MODIFY-SUCCESS", "Response body is null")
                    }
                } else {
                    Log.e("USER-MODIFY-SUCCESS", "Response not successful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<BasicRes2>, t: Throwable) {
                Log.d("USER-MODIFY-FAILURE", t.toString())
            }
        })
    }

    fun delUser(user_id: Int) {
        val userDeleteService = getRetrofit().create(SettingRetrofitInterface::class.java)
        userDeleteService.delMyPage(user_id).enqueue(object : Callback<BasicRes2> {
            override fun onResponse(call: Call<BasicRes2>, response: Response<BasicRes2>) {
                if (response.isSuccessful) {
                    val resp: BasicRes2? = response.body()
                    if (resp != null) {
                        userDeletView.onGetUserDeleteSuccess(resp)
                    } else {
                        Log.e("USER-DELETE-FAILURE", "Response body is null")
                        userDeletView.onGetUserDeleteFailure(response.code())
                    }
                } else {
                    Log.e("USER-DELETE-FAILURE", "Response not successful: ${response.code()}")
                    userDeletView.onGetUserDeleteFailure(response.code())
                }
            }

            override fun onFailure(call: Call<BasicRes2>, t: Throwable) {
                Log.d("USER-MODIFY-FAILURE", t.toString())
            }
        })
    }

    fun setFeedback(userId: Int, feedbackReq: FeedbackReq) {
        val feedbackService = getRetrofit().create(SettingRetrofitInterface::class.java)
        feedbackService.postFeedback(userId, feedbackReq).enqueue(object : Callback<BasicRes2> {
            override fun onResponse(call: Call<BasicRes2>, response: Response<BasicRes2>) {
                if (response.isSuccessful) {
                    val resp: BasicRes2? = response.body()
                    if (resp != null) {
                        feedbackView.onGetFeedbackSuccess(resp)
                    } else {
                        feedbackView.onGetFeedbackFailure(response.code())
                        Log.e("FEEDBACK-SUCCESS", "Response body is null")
                    }
                } else {
                    feedbackView.onGetFeedbackFailure(response.code())
                    Log.e("FEEDBACK-SUCCESS", "Response not successful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<BasicRes2>, t: Throwable) {
                Log.d("FEEDBACK-FAILURE", t.toString())
            }
        })
    }
}