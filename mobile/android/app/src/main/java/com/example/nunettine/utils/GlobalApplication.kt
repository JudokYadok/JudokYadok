package com.example.nunettine.utils

import android.app.Application
import com.example.nunettine.R
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Kakao Sdk 초기화
        KakaoSdk.init(this, getString(R.string.kakao_appkey))
    }
}