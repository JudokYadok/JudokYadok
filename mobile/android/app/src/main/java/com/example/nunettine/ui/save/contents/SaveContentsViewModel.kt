package com.example.nunettine.ui.save.contents

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nunettine.data.local.NewTextReq
import com.example.nunettine.data.remote.dto.BasicRes
import com.example.nunettine.data.remote.dto.library.TextList
import com.example.nunettine.data.remote.dto.library.TextListRes
import com.example.nunettine.data.remote.dto.library.TextRes
import com.example.nunettine.data.remote.service.library_study.TextService
import com.example.nunettine.data.remote.view.library.TextListView
import com.example.nunettine.data.remote.view.library.TextModifyView
import com.example.nunettine.data.remote.view.library.TextNewView
import com.example.nunettine.data.remote.view.library.TextView

class SaveContentsViewModel: ViewModel(), TextListView, TextView, TextModifyView, TextNewView {
    // 전체 조회
    val textListML = MutableLiveData<List<TextList>>()

    // 단일 조회
    val textTitleML = MutableLiveData<String>()
    val textContentsML = MutableLiveData<String>()
    val textTypeML = MutableLiveData<String>()

    init {
        textListML.value = emptyList()
        textTitleML.value = String()
        textContentsML.value = String()
        textTypeML.value = String()
    }

    fun getTextListService(user_id: Int) {
        val getTextListService = TextService()
        getTextListService.setTextListView(this@SaveContentsViewModel)
        getTextListService.getTextList(user_id)
    }

    fun setTextService(user_id: Int, newTextReq: NewTextReq) {
        val setTextService = TextService()
        setTextService.setTextNewView(this@SaveContentsViewModel)
        setTextService.setText(user_id, newTextReq)
    }

    fun getTextService(user_id: Int, text_id: Int) {
        val getTextService = TextService()
        getTextService.setTextView(this@SaveContentsViewModel)
        getTextService.getText(user_id, text_id)
    }

    fun putTextService(user_id: Int, text_id: Int, textReq: NewTextReq) {
        val putTextService = TextService()
        putTextService.setTextModifyView(this@SaveContentsViewModel)
        putTextService.putText(user_id, text_id, textReq)
    }

    override fun onGetTextListSuccess(response: List<TextList>) {
        textListML.postValue(response)
        Log.d("GET-TEXT-LIST-성공", response.toString())
    }

    override fun onGetTextListFailure(result_code: Int) {
        Log.d("GET-TEXT-LIST-오류", result_code.toString())
    }

    override fun onGetTextModifySuccess(response: BasicRes) {
        Log.d("MODIFY-TEXT-성공", response.toString())
    }

    override fun onGetTextModifyFailure(result_code: Int) {
        Log.d("MODIFY-TEXT-오류", result_code.toString())
    }

    override fun onGetTextNewSuccess(response: BasicRes) {
        Log.d("NEW-TEXT-성공", response.toString())
    }

    override fun onGetTextNewFailure(result_code: Int) {
        Log.d("NEW-TEXT-오류", result_code.toString())
    }

    override fun onGetTextSuccess(response: TextRes) {
        textTitleML.postValue(response.text_title)
        textContentsML.postValue(response.text_contents)
        Log.d("GET-TEXT-성공", response.toString())
    }

    override fun onGetTextFailure(result_code: Int) {
        Log.d("GET-TEXT-오류", result_code.toString())
    }
}