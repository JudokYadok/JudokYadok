package com.example.nunettine.data.remote.retrofit

import com.example.nunettine.data.local.MemoReq
import com.example.nunettine.data.local.NewMemoReq
import com.example.nunettine.data.local.NewTextReq
import com.example.nunettine.data.remote.dto.BasicRes
import com.example.nunettine.data.remote.dto.library.MemoList
import com.example.nunettine.data.remote.dto.library.MemoRes
import com.example.nunettine.data.remote.dto.library.QuizRes
import com.example.nunettine.data.remote.dto.library.QuizSaveDetailRes
import com.example.nunettine.data.remote.dto.library.QuizSaveList
import com.example.nunettine.data.remote.dto.library.QuizSaveListRes
import com.example.nunettine.data.remote.dto.library.TextList
import com.example.nunettine.data.remote.dto.library.TextListRes
import com.example.nunettine.data.remote.dto.library.TextRes
import retrofit2.Call
import retrofit2.http.*

interface LibraryRetrofitInterface {
    @GET("/user/library/quiz/{user_id}")
    fun getQuizList(@Path("user_id") user_id: Int): Call<List<QuizSaveList>>

    @GET("/user/library/quiz/{user_id}/{quiz_id}")
    fun getQuiz(@Path("user_id") user_id: Int, @Path("quiz_id") quiz_id: Int): Call<QuizSaveDetailRes>

    @DELETE("/user/library/quiz/{user_id}/{quiz_id}")
    fun delQuiz(@Path("user_id") user_id: Int, @Path("quiz_id") quiz_id: Int): Call<BasicRes>


    @GET("/user/library/memo/{user_id}")
    fun getMemoList(@Path("user_id") user_id: Int): Call<List<MemoList>>

    @GET("/user/library/memo/{user_id}/{memo_id}")
    fun getMemo(@Path("memo_id") memo_id: Int, @Path("user_id") user_id: Int): Call<MemoRes>

    @PUT("/user/library/memo/{user_id}/{memo_id}")
    fun putMemo(@Path("memo_id") memo_id: Int, @Path("user_id") user_id: Int, @Body memoReq: MemoReq): Call<BasicRes>

    @POST("/user/library/memo/{user_id}")
    fun postMemo(@Body newMemoReq: NewMemoReq, @Path("user_id") user_id: Int): Call<BasicRes>

    @DELETE("/user/library/memo/{user_id}/{memo_id}")
    fun deleteMemo(@Path("memo_id") memo_id: Int, @Path("user_id") user_id: Int): Call<BasicRes>


    @GET("/user/library/mytext/{user_id}")
    fun getMyTextList(@Path("user_id") user_id: Int): Call<List<TextList>>

    @GET("/user/library/mytext/{user_id}/{text_id}")
    fun getMyText(@Path("user_id") user_id: Int, @Path("text_id") text_id: Int): Call<TextRes>

    @PUT("/user/library/mytext/{user_id}/{text_id}")
    fun putMyText(@Path("user_id") user_id: Int, @Path("text_id") text_id: Int, @Body myTextReq: NewTextReq): Call<BasicRes>

    @POST("/user/library/mytext/{user_id}")
    fun postMyText(@Path("user_id") user_id: Int, @Body newTextReq: NewTextReq): Call<BasicRes>

    @DELETE("/user/library/mytext/{user_id}/{text_id}")
    fun delMyText(@Path("user_id") user_id: Int, @Path("text_id") text_id: Int): Call<BasicRes>
}