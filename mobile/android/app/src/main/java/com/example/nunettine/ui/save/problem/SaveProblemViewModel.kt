package com.example.nunettine.ui.save.problem

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nunettine.data.remote.dto.library.AnswerList
import com.example.nunettine.data.remote.dto.library.QuizSaveDetailRes
import com.example.nunettine.data.remote.dto.library.QuizSaveList
import com.example.nunettine.data.remote.service.library_study.QuizService
import com.example.nunettine.data.remote.view.library.QuizListView
import com.example.nunettine.data.remote.view.library.QuizView

class SaveProblemViewModel: ViewModel(), QuizListView, QuizView {
    val quizListML = MutableLiveData<List<QuizSaveList>>()
    val questionsML = MutableLiveData<List<String>>()
    val answersML = MutableLiveData<AnswerList>()
    val userAnswersML = MutableLiveData<List<Int>>()
    val correctAnswersML = MutableLiveData<List<Int>>()
    val createdAtML = MutableLiveData<String>()
    val responseML = MutableLiveData<QuizSaveDetailRes>()

    init {
        quizListML.value = emptyList()
        questionsML.value = emptyList()
        answersML.value = AnswerList(emptyList(), emptyList(), emptyList())
        userAnswersML.value = emptyList()
        correctAnswersML.value = emptyList()
        createdAtML.value = String()
    }

    fun getQuizList(user_id: Int) {
        val getQuizListService = QuizService()
        getQuizListService.setQuizListView(this@SaveProblemViewModel)
        getQuizListService.getQuizList(user_id)
    }

    fun getQuiz(user_id: Int, quiz_id: Int) {
        val getQuizService = QuizService()
        getQuizService.setQuizView(this@SaveProblemViewModel)
        getQuizService.getQuiz(user_id, quiz_id)
    }

    override fun onGetQuizListSuccess(response: List<QuizSaveList>) {
        quizListML.postValue(response)
        Log.d("퀴즈 저장 리스트 불러오기 성공", response.toString())
    }

    override fun onGetQuizListFailure(result_code: Int) {
        Log.d("퀴즈 저장 리스트 불러오기 오류", result_code.toString())
    }

    override fun onGetQuizSuccess(response: QuizSaveDetailRes) {
        userAnswersML.postValue(response.user_answers)
        correctAnswersML.postValue(response.correct_answers)
        answersML.postValue(response.answers)
        questionsML.postValue(response.questions)
        responseML.postValue(response)
        Log.d("퀴즈 저장 불러오기 성공", response.toString())
    }

    override fun onGetQuizFailure(result_code: Int) {
        Log.d("퀴즈 저장 불러오기 오류", result_code.toString())
    }
}