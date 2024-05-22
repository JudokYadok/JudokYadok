package com.example.nunettine.utils

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL = "http://ceprj.gachon.ac.kr:60023" // 임시 설정

fun getRetrofit(): Retrofit {
    val client = OkHttpClient.Builder()
        .connectTimeout(180, TimeUnit.SECONDS) // 연결 시간 초과 설정 (180초)
        .readTimeout(180, TimeUnit.SECONDS) // 읽기 시간 초과 설정 (180초)
        .build()

    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client) // OkHttpClient를 Retrofit에 설정
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}