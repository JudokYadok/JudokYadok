package com.example.nunettine.ui.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.nunettine.R
import com.example.nunettine.data.remote.dto.auth.LoginRes
import com.example.nunettine.data.remote.service.auth.AuthService
import com.example.nunettine.data.remote.view.auth.LoginView
import com.example.nunettine.databinding.ActivitySplashBinding

class SplashActivity: AppCompatActivity(), LoginView {
    private lateinit var binding: ActivitySplashBinding
    private var accessToken = ""
    private var refreshToken = ""

    private var getAccessToken: String? = ""
    private var member_id: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData()
        val animation: Animation = AnimationUtils.loadAnimation(this, R.anim.gradient_expand_animation)
        binding.splashTv.startAnimation(animation)
        binding.splashIco.startAnimation(animation)

        autoLoginService()

        // 로그인 기록이 없을 경우 -> 로그인 화면으로 이동
        if(refreshToken == "") {
            Handler().postDelayed({
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000)
        }
    }

    private fun getData() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("kakao", Context.MODE_PRIVATE)
        accessToken = sharedPreferences.getString("getAccessToken", accessToken)!!
        refreshToken = sharedPreferences.getString("getRefreshToken", refreshToken)!!
    }

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    private fun saveKakaoData() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("kakao", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("getAccessToken", getAccessToken!!)
        editor.putInt("user_id", member_id!!)
        editor.apply()
    }

    private fun autoLoginService() {
        val autoLoginService = AuthService()
        autoLoginService.setLoginView(this@SplashActivity)
        autoLoginService.setAutoLogin(accessToken, refreshToken)
    }

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    override fun onGetLoginSuccess(response: LoginRes) {
        member_id = response.user_id
        getAccessToken = response.access_token
        saveKakaoData()

        Log.d("자동 로그인 성공", response.toString())

        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }

    override fun onGetLoginFailure(result_req: String) {
        Log.d("자동 로그인 실패", result_req)
    }
}