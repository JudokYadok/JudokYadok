package com.example.nunettine.data.remote.service.library_study

import android.util.Log
import com.example.nunettine.data.local.MemoReq
import com.example.nunettine.data.local.NewMemoReq
import com.example.nunettine.data.remote.dto.BasicRes
import com.example.nunettine.data.remote.dto.library.MemoList
import com.example.nunettine.data.remote.dto.library.MemoListRes
import com.example.nunettine.data.remote.dto.library.MemoRes
import com.example.nunettine.data.remote.retrofit.LibraryRetrofitInterface
import com.example.nunettine.data.remote.retrofit.StudyRetrofitInterface
import com.example.nunettine.data.remote.view.library.MemoDelView
import com.example.nunettine.data.remote.view.library.MemoListView
import com.example.nunettine.data.remote.view.library.MemoModifyView
import com.example.nunettine.data.remote.view.library.MemoNewView
import com.example.nunettine.data.remote.view.library.MemoView
import com.example.nunettine.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class MemoService {
    private lateinit var memoView: MemoView
    private lateinit var memoListView: MemoListView
    private lateinit var memoModifyView: MemoModifyView
    private lateinit var memoNewView: MemoNewView
    private lateinit var memoDelView: MemoDelView

    fun setMemoView(memoView: MemoView) {
        this.memoView = memoView
    }

    fun setMemoListView(memoListView: MemoListView) {
        this.memoListView = memoListView
    }

    fun setMemoModifyView(memoModifyView: MemoModifyView) {
        this.memoModifyView = memoModifyView
    }

    fun setMemoNewView(memoNewView: MemoNewView) {
        this.memoNewView = memoNewView
    }

    fun setMemoDelView(memoDelView: MemoDelView) {
        this.memoDelView = memoDelView
    }

    fun getMemo(memoId: Int, userId: Int) {
        val memoService = getRetrofit().create(LibraryRetrofitInterface::class.java)
        memoService.getMemo(memoId, userId).enqueue(object : Callback<MemoRes> {
            override fun onResponse(call: Call<MemoRes>, response: Response<MemoRes>) {
                if (response.isSuccessful) {
                    val resp: MemoRes? = response.body()
                    if (resp != null) {
                        memoView.onGetMemoSuccess(resp)
                    } else {
                        Log.e("MEMO-SUCCESS", "Response body is null")
                        memoView.onGetMemoFailure(response.code())
                    }
                } else {
                    Log.e("MEMO-SUCCESS", "Response not successful: ${response.code()}")
                    memoView.onGetMemoFailure(response.code())
                }
            }

            override fun onFailure(call: Call<MemoRes>, t: Throwable) {
                Log.d("MEMO-FAILURE", t.toString())
            }
        })
    }

    fun getMemoList(user_id: Int) {
        val memoListService = getRetrofit().create(LibraryRetrofitInterface::class.java)
        memoListService.getMemoList(user_id).enqueue(object : Callback<List<MemoList>> {
            override fun onResponse(call: Call<List<MemoList>>, response: Response<List<MemoList>>) {
                if (response.isSuccessful) {
                    val resp: List<MemoList>? = response.body()
                    if (resp != null) {
                        memoListView.onGetMemoListSuccess(resp)
                    } else {
                        Log.e("MEMO-LIST-SUCCESS", "Response body is null")
                        memoListView.onGetMemoListFailure(response.code())
                    }
                } else {
                    Log.e("MEMO-LIST-SUCCESS", "Response not successful: ${response.code()}")
                    memoListView.onGetMemoListFailure(response.code())
                }
            }

            override fun onFailure(call: Call<List<MemoList>>, t: Throwable) {
                Log.d("MEMO-LIST-FAILURE", t.toString())
            }
        })
    }

    fun putMemo(memoId: Int, memoReq: MemoReq, userId: Int) {
        val memoModifyService = getRetrofit().create(LibraryRetrofitInterface::class.java)
        memoModifyService.putMemo(memoId,userId, memoReq).enqueue(object : Callback<BasicRes> {
            override fun onResponse(call: Call<BasicRes>, response: Response<BasicRes>) {
                if (response.isSuccessful) {
                    val resp: BasicRes? = response.body()
                    if (resp != null) {
                        memoModifyView.onGetMemoModifySuccess(resp)
                    } else {
                        Log.e("MEMO-MODIFY-SUCCESS", "Response body is null")
                        memoModifyView.onGetMemoModifyFailure(response.code())
                    }
                } else {
                    Log.e("MEMO-MODIFY-SUCCESS", "Response not successful: ${response.code()}")
                    memoModifyView.onGetMemoModifyFailure(response.code())
                }
            }

            override fun onFailure(call: Call<BasicRes>, t: Throwable) {
                Log.d("MEMO-MODIFY-FAILURE", t.toString())
            }
        })
    }

    // 임시 설정
    fun setMemo(newMemoReq: NewMemoReq, userId: Int) {
        val memoNewService = getRetrofit().create(LibraryRetrofitInterface::class.java)
        memoNewService.postMemo(newMemoReq, userId).enqueue(object : Callback<BasicRes> {
            override fun onResponse(call: Call<BasicRes>, response: Response<BasicRes>) {
                if (response.isSuccessful) {
                    val resp: BasicRes? = response.body()
                    if (resp != null) {
                        memoNewView.onGetMemoNewSuccess(resp)
                    } else {
                        Log.e("MEMO-NEW-SUCCESS", "Response body is null")
                        memoNewView.onGetMemoNewFailure(response.code())
                    }
                } else {
                    Log.e("MEMO-NEW-SUCCESS", "Response not successful: ${response.code()}")
                    memoNewView.onGetMemoNewFailure(response.code())
                }
            }

            override fun onFailure(call: Call<BasicRes>, t: Throwable) {
                Log.d("MEMO-NEW-FAILURE", t.toString())
            }
        })
    }

    fun deleteMemo(memo_id: Int, position: Int, user_id: Int) {
        val memoDelService = getRetrofit().create(LibraryRetrofitInterface::class.java)
        memoDelService.deleteMemo(memo_id, user_id).enqueue(object : Callback<BasicRes> {
            override fun onResponse(call: Call<BasicRes>, response: Response<BasicRes>) {
                if (response.isSuccessful) {
                    val resp: BasicRes? = response.body()
                    if (resp != null) {
                        memoDelView.onGetMemoDeleteSuccess(resp, position)
                    } else {
                        Log.e("MEMO-NEW-SUCCESS", "Response body is null")
                        memoDelView.onGetMemoDeleteFailure(response.code())
                    }
                } else {
                    Log.e("MEMO-NEW-SUCCESS", "Response not successful: ${response.code()}")
                    memoDelView.onGetMemoDeleteFailure(response.code())
                }
            }

            override fun onFailure(call: Call<BasicRes>, t: Throwable) {
                Log.d("MEMO-NEW-FAILURE", t.toString())
            }
        })
    }
}