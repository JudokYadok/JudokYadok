package com.example.nunettine.ui.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.nunettine.data.remote.dto.auth.LoginRes
import com.example.nunettine.data.remote.service.auth.AuthService
import com.example.nunettine.data.remote.view.auth.LoginView
import com.example.nunettine.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import java.time.LocalDateTime

class LoginActivity: AppCompatActivity(), LoginView {
    private lateinit var binding: ActivityLoginBinding
    @RequiresApi(Build.VERSION_CODES.O)
    var localTime: LocalDateTime = LocalDateTime.now()

    private var nickname : String? = ""
    private var profile :String? = ""
    private var email: String? = ""
    private var accessToken: String? = ""
    private var member_id: Int? = 0
    private var getAccessToken: String? = ""
    private var getRefreshToken: String? = ""

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginKakaoBtn.setOnClickListener {
            login_kakao()
        }
    }

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    private fun login_kakao() {
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        // 이메일 로그인 콜백
        val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "로그인 실패 $error")
            } else if (token != null) {
                Log.e(TAG, "로그인 성공 ${token.accessToken}")

                UserApiClient.instance.me { user, _ ->
                    if (user != null) {
                        nickname = user.kakaoAccount?.profile?.nickname.toString()
                        profile = user.kakaoAccount?.profile?.profileImageUrl.toString()
                        email = user.kakaoAccount?.email
                        accessToken = token.accessToken
                        Log.d("TAG", "$nickname, $profile, $email, $accessToken")
                        saveKakaoData()
                    }
                }
                loginService(token.accessToken)
//                goMainActivity()
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            // 카카오톡 로그인
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                // 로그인 실패 부분
                if (error != null) {
                    Log.e(TAG, "로그인 실패 $error")
                    // 사용자가 취소
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled ) {
                        return@loginWithKakaoTalk
                    }
                    // 다른 오류
                    else {
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = mCallback) // 카카오 이메일 로그인
                    }
                }
                // 로그인 성공 부분
                else if (token != null) {
                    Log.e(TAG, "로그인 성공 ${token.accessToken}")
                    loginService(token.accessToken)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = mCallback) // 카카오 이메일 로그인
        }
    }

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    private fun saveKakaoData() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("kakao", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("nickname", nickname!!)
        editor.putString("profile", profile!!)
        editor.putString("email", email!!)
        editor.putString("getAccessToken", getAccessToken!!)
        editor.putString("getRefreshToken", getRefreshToken!!)
        editor.putInt("user_id", member_id!!)
        editor.apply()
    }

    private fun loginService(authorization: String) {
        val loginService = AuthService()
        loginService.setLoginView(this@LoginActivity)
        loginService.setLogin(authorization)
    }

    private fun goMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    companion object {
        var TAG = "LoginActivity"
    }

    override fun onBackPressed() { }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onGetLoginSuccess(response: LoginRes) {
        Log.d("LOGIN-RESPONSE-성공", response.toString())
        member_id = response.user_id
        getAccessToken = response.access_token
        getRefreshToken = response.refresh_token
        saveKakaoData()
        if(response.createdAt != localTime.toString()) {
            Log.d("LOGIN", "기존 사용자")
        } else {
            Log.d("SIGNUP", "새로운 사용자")
        }
        goMainActivity()
    }

    override fun onGetLoginFailure(result_req: String) {
        Log.d("LOGIN-RESPONSE-오류", result_req)
    }
}