package com.example.nunettine.data.remote.service.library_study

import android.util.Log
import com.example.nunettine.data.local.NewTextReq
import com.example.nunettine.data.remote.dto.BasicRes
import com.example.nunettine.data.remote.dto.library.TextList
import com.example.nunettine.data.remote.dto.library.TextListRes
import com.example.nunettine.data.remote.dto.library.TextRes
import com.example.nunettine.data.remote.retrofit.LibraryRetrofitInterface
import com.example.nunettine.data.remote.view.library.TextDelView
import com.example.nunettine.data.remote.view.library.TextListView
import com.example.nunettine.data.remote.view.library.TextModifyView
import com.example.nunettine.data.remote.view.library.TextNewView
import com.example.nunettine.data.remote.view.library.TextView
import com.example.nunettine.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TextService {
    private lateinit var textView: TextView
    private lateinit var textListView: TextListView
    private lateinit var textNewView: TextNewView
    private lateinit var textModifyView: TextModifyView
    private lateinit var textDeleteView: TextDelView

    fun setTextView(textView: TextView) {
        this.textView = textView
    }

    fun setTextListView(textListView: TextListView) {
        this.textListView = textListView
    }

    fun setTextNewView(textNewView: TextNewView) {
        this.textNewView = textNewView
    }

    fun setTextModifyView(textModifyView: TextModifyView) {
        this.textModifyView = textModifyView
    }

    fun setTextDeleteView(textDeleteView: TextDelView) {
        this.textDeleteView = textDeleteView
    }

    fun getText(userId: Int, textId: Int) {
        val textService = getRetrofit().create(LibraryRetrofitInterface::class.java)
        textService.getMyText(userId, textId).enqueue(object : Callback<TextRes> {
            override fun onResponse(call: Call<TextRes>, response: Response<TextRes>) {
                if (response.isSuccessful) {
                    val resp: TextRes? = response.body()
                    if (resp != null) {
                        textView.onGetTextSuccess(resp)
                    } else {
                        Log.e("TEXT-SUCCESS", "Response body is null")
                        textView.onGetTextFailure(response.code())
                    }
                } else {
                    Log.e("TEXT-SUCCESS", "Response not successful: ${response.code()}")
                    textView.onGetTextFailure(response.code())
                }
            }

            override fun onFailure(call: Call<TextRes>, t: Throwable) {
                Log.d("TEXT-FAILURE", t.toString())
            }
        })
    }

    fun getTextList(userId: Int) {
        val textListService = getRetrofit().create(LibraryRetrofitInterface::class.java)
        textListService.getMyTextList(userId).enqueue(object : Callback<List<TextList>> {
            override fun onResponse(call: Call<List<TextList>>, response: Response<List<TextList>>) {
                if (response.isSuccessful) {
                    val resp: List<TextList>? = response.body()
                    if (resp != null) {
                        textListView.onGetTextListSuccess(resp)
                    } else {
                        Log.e("TEXT-LIST-SUCCESS", "Response body is null")
                        textListView.onGetTextListFailure(response.code())
                    }
                } else {
                    Log.e("TEXT-LIST-SUCCESS", "Response not successful: ${response.code()}")
                    textListView.onGetTextListFailure(response.code())
                }
            }

            override fun onFailure(call: Call<List<TextList>>, t: Throwable) {
                Log.d("TEXT-LIST-FAILURE", t.toString())
            }
        })
    }

    fun setText(userId: Int, newTextReq: NewTextReq) {
        val textNewService = getRetrofit().create(LibraryRetrofitInterface::class.java)
        textNewService.postMyText(userId, newTextReq).enqueue(object : Callback<BasicRes> {
            override fun onResponse(call: Call<BasicRes>, response: Response<BasicRes>) {
                if (response.isSuccessful) {
                    val resp: BasicRes? = response.body()
                    if (resp != null) {
                        textNewView.onGetTextNewSuccess(resp)
                    } else {
                        Log.e("TEXT-NEW-SUCCESS", "Response body is null")
                        textNewView.onGetTextNewFailure(response.code())
                    }
                } else {
                    Log.e("TEXT-NEW-SUCCESS", "Response not successful: ${response.code()}")
                    textNewView.onGetTextNewFailure(response.code())
                }
            }

            override fun onFailure(call: Call<BasicRes>, t: Throwable) {
                Log.d("TEXT-NEW-FAILURE", t.toString())
            }
        })
    }

    fun putText(userId: Int, textId: Int, myTextReq: NewTextReq) {
        val textModifyService = getRetrofit().create(LibraryRetrofitInterface::class.java)
        textModifyService.putMyText(userId, textId, myTextReq).enqueue(object : Callback<BasicRes> {
            override fun onResponse(call: Call<BasicRes>, response: Response<BasicRes>) {
                if (response.isSuccessful) {
                    val resp: BasicRes? = response.body()
                    if (resp != null) {
                        textModifyView.onGetTextModifySuccess(resp)
                    } else {
                        Log.e("TEXT-MODIFY-SUCCESS", "Response body is null")
                        textModifyView.onGetTextModifyFailure(response.code())
                    }
                } else {
                    Log.e("TEXT-MODIFY-SUCCESS", "Response not successful: ${response.code()}")
                    textModifyView.onGetTextModifyFailure(response.code())
                }
            }

            override fun onFailure(call: Call<BasicRes>, t: Throwable) {
                Log.d("TEXT-MODIFY-FAILURE", t.toString())
            }
        })
    }

    fun deleteText(user_id: Int, text_id: Int, position: Int) {
        val textDeleteService = getRetrofit().create(LibraryRetrofitInterface::class.java)
        textDeleteService.delMyText(user_id, text_id).enqueue(object : Callback<BasicRes> {
            override fun onResponse(call: Call<BasicRes>, response: Response<BasicRes>) {
                if (response.isSuccessful) {
                    val resp: BasicRes? = response.body()
                    if (resp != null) {
                        textDeleteView.deleteTextSuccess(resp, position)
                    } else {
                        Log.e("TEXT-DELETE-SUCCESS", "Response body is null")
                        textDeleteView.deleteTextFailure(response.message())
                    }
                } else {
                    Log.e("TEXT-DELETE-SUCCESS", "Response not successful: ${response.code()}")
                    textDeleteView.deleteTextFailure(response.message())
                }
            }

            override fun onFailure(call: Call<BasicRes>, t: Throwable) {
                Log.d("TEXT-DELETE-FAILURE", t.toString())
            }
        })
    }
}