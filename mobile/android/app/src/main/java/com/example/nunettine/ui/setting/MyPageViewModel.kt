package com.example.nunettine.ui.setting

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nunettine.data.local.UserReq
import com.example.nunettine.data.remote.dto.BasicRes2
import com.example.nunettine.data.remote.dto.setting.UserRes
import com.example.nunettine.data.remote.service.auth.AuthService
import com.example.nunettine.data.remote.service.setting.UserService
import com.example.nunettine.data.remote.view.setting.UserDeleteView
import com.example.nunettine.data.remote.view.setting.UserModifyView
import com.example.nunettine.data.remote.view.setting.UserView
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class MyPageViewModel: ViewModel(), UserView, UserModifyView, UserDeleteView {
    val emailML = MutableLiveData<String>()
    val nameML = MutableLiveData<String>()
    val joinDateML = MutableLiveData<String>()
    val dDayYearML = MutableLiveData<Int>().apply { value = 2000 }
    val dDayMonthML = MutableLiveData<Int>().apply { value = 0 }
    val dDayDateML = MutableLiveData<Int>().apply { value = 0 }
    val mofifyML = MutableLiveData<Boolean>()
    val ddayML = MutableLiveData<Int>().apply { value = 0 }

    init {
        emailML.value = String()
        nameML.value = String()
        joinDateML.value = String()
    }

    fun getUserInfoService(user_id: Int) {
        val getUserService = UserService()
        getUserService.setUserView(this@MyPageViewModel)
        getUserService.getUser(user_id)
    }

    fun putUserInfoService(userReq: UserReq) {
        val putUserService = UserService()
        putUserService.setUserModifyView(this@MyPageViewModel)
        putUserService.putUser(userReq)
    }

    fun deleteUserInfoService(user_id: Int) {
        val delUserService = UserService()
        delUserService.setUserDeleteView(this@MyPageViewModel)
        delUserService.delUser(user_id)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onGetUserSuccess(response: UserRes) {
        val dDay = LocalDate.of(response.d_day.year, response.d_day.month, response.d_day.date)
        val today = LocalDate.now()
        val daysUntilDDay = ChronoUnit.DAYS.between(today, dDay)

        emailML.postValue(response.email)
        nameML.postValue(response.name)
        joinDateML.postValue(response.createdAt)
        dDayYearML.postValue(response.d_day.year)
        dDayMonthML.postValue(response.d_day.month)
        dDayDateML.postValue(response.d_day.date)
        ddayML.postValue(daysUntilDDay.toInt())
        Log.d("USER-GET-성공", response.toString())
    }

    override fun onGetUserFailure(result_code: Int) {
        Log.d("USER-GET-오류", result_code.toString())
    }

    override fun onGetUserModifySuccess(response: BasicRes2) {
        mofifyML.postValue(true)
        Log.d("USER-UPDATE-성공", response.toString())
    }

    override fun onGetUserModifyFailure(result_code: Int) {
        Log.d("USER-UPDATE-오류", result_code.toString())
    }

    override fun onGetUserDeleteSuccess(response: BasicRes2) {
        Log.d("USER-DELETE-성공", response.toString())
    }

    override fun onGetUserDeleteFailure(result_code: Int) {
        Log.d("USER-DELETE-실패", result_code.toString())
    }
}