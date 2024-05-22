package com.example.nunettine.data.remote.service.library_study

import android.util.Log
import com.example.nunettine.data.local.FeedbackReq
import com.example.nunettine.data.local.QuizReq
import com.example.nunettine.data.local.QuizSaveReq
import com.example.nunettine.data.remote.dto.BasicRes
import com.example.nunettine.data.remote.dto.BasicRes2
import com.example.nunettine.data.remote.dto.library.QuizSaveDetailRes
import com.example.nunettine.data.remote.dto.library.QuizSaveList
import com.example.nunettine.data.remote.dto.library.QuizSaveListRes
import com.example.nunettine.data.remote.dto.study.QuizGradeRes
import com.example.nunettine.data.remote.dto.study.QuizSaveRes
import com.example.nunettine.data.remote.dto.study.QuizSolveRes
import com.example.nunettine.data.remote.dto.study.StudyCategoryRes
import com.example.nunettine.data.remote.dto.study.StudyDetailRes
import com.example.nunettine.data.remote.dto.study.StudyListRes
import com.example.nunettine.data.remote.dto.study.TextList
import com.example.nunettine.data.remote.retrofit.LibraryRetrofitInterface
import com.example.nunettine.data.remote.retrofit.SettingRetrofitInterface
import com.example.nunettine.data.remote.retrofit.StudyRetrofitInterface
import com.example.nunettine.data.remote.view.library.QuizDelView
import com.example.nunettine.data.remote.view.library.QuizListView
import com.example.nunettine.data.remote.view.library.QuizView
import com.example.nunettine.data.remote.view.setting.FeedbackView
import com.example.nunettine.data.remote.view.study.QuizGradeView
import com.example.nunettine.data.remote.view.study.QuizSaveView
import com.example.nunettine.data.remote.view.study.QuizSolveView
import com.example.nunettine.data.remote.view.study.StudyCategoryView
import com.example.nunettine.data.remote.view.study.StudyDetailView
import com.example.nunettine.data.remote.view.study.StudyListView
import com.example.nunettine.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class QuizService {
    private lateinit var quizView: QuizView
    private lateinit var quizSolveView:QuizSolveView
    private lateinit var quizListView: QuizListView
    private lateinit var quizGradeView: QuizGradeView
    private lateinit var quizSaveView: QuizSaveView
    private lateinit var quizDeleteView: QuizDelView

    private lateinit var studyListView: StudyListView
    private lateinit var studyDetailView: StudyDetailView
    private lateinit var studyCategoryView: StudyCategoryView

    private lateinit var feedbackView: FeedbackView

    fun setQuizView(quizView: QuizView) {
        this.quizView = quizView
    }

    fun setQuizSolveView(quizSolveView: QuizSolveView) {
        this.quizSolveView = quizSolveView
    }

    fun setQuizListView(quizListView: QuizListView) {
        this.quizListView = quizListView
    }

    fun setQuizGradeView(quizGradeView: QuizGradeView) {
        this.quizGradeView = quizGradeView
    }

    fun setQuizSaveView(quiz_saveView: QuizSaveView) {
        this.quizSaveView = quiz_saveView
    }

    fun setQuizDeleteView(quiz_deleteView: QuizDelView) {
        this.quizDeleteView = quiz_deleteView
    }

    fun getStudyDetailView(studyDetailView: StudyDetailView) {
        this.studyDetailView = studyDetailView
    }

    fun getStudyListView(studyListView: StudyListView) {
        this.studyListView = studyListView
    }

    fun getStudyCategoryView(studyCategoryView: StudyCategoryView) {
        this.studyCategoryView = studyCategoryView
    }

    fun setFeedbackView(setFeedbackView: FeedbackView) {
        this.feedbackView = setFeedbackView
    }

    fun setPrevQuizSolve(category: String, textId: Int, quizType: QuizReq) {
        val quizPrevService = getRetrofit().create(StudyRetrofitInterface::class.java)
        quizPrevService.postPrevTextQuiz(category, textId, quizType).enqueue(object : Callback<QuizSolveRes> {
            override fun onResponse(call: Call<QuizSolveRes>, response: Response<QuizSolveRes>) {
                if (response.isSuccessful) {
                    val resp: QuizSolveRes? = response.body()
                    if (resp != null) {
                        quizSolveView.onGetQuizSolveSuccess(resp)
                    } else {
                        Log.e("PREV-QUIZ-SOLVE-SUCCESS", "Response body is null")
                        quizSolveView.onGetQuizSolveFailure(response.code())
                    }
                } else {
                    Log.e("PREV-QUIZ-SOLVE-SUCCESS", "Response not successful: ${response.code()}")
                    quizSolveView.onGetQuizSolveFailure(response.code())
                }
            }

            override fun onFailure(call: Call<QuizSolveRes>, t: Throwable) {
                Log.d("PREV-QUIZ-SOLVE-FAILURE", t.toString())
            }
        })
    }

    fun setMyQuizSolve(user_id: Int, category: String, textId: Int, quizType: QuizReq) {
        val quizMyService = getRetrofit().create(StudyRetrofitInterface::class.java)
        quizMyService.postMyTextQuiz(user_id, category, textId, quizType).enqueue(object : Callback<QuizSolveRes> {
            override fun onResponse(call: Call<QuizSolveRes>, response: Response<QuizSolveRes>) {
                if (response.isSuccessful) {
                    val resp: QuizSolveRes? = response.body()
                    if (resp != null) {
                        quizSolveView.onGetQuizSolveSuccess(resp)
                    } else {
                        Log.e("MY-QUIZ-SOLVE-SUCCESS", "Response body is null")
                        quizSolveView.onGetQuizSolveFailure(response.code())
                    }
                } else {
                    Log.e("MY-QUIZ-SOLVE-SUCCESS", "Response not successful: ${response.code()}")
                    quizSolveView.onGetQuizSolveFailure(response.code())
                }
            }

            override fun onFailure(call: Call<QuizSolveRes>, t: Throwable) {
                Log.d("MY-QUIZ-SOLVE-FAILURE", t.toString())
            }
        })
    }

    fun getPrevText(category: String, textId: Int) {
        val quizPrevService = getRetrofit().create(StudyRetrofitInterface::class.java)
        quizPrevService.getPrevText(category, textId).enqueue(object : Callback<StudyDetailRes> {
            override fun onResponse(call: Call<StudyDetailRes>, response: Response<StudyDetailRes>) {
                if (response.isSuccessful) {
                    val resp: StudyDetailRes? = response.body()
                    if (resp != null) {
                        studyDetailView.onGetStudyDetailSuccess(resp)
                    } else {
                        Log.e("PREV-TEXT-GET-SUCCESS", "Response body is null")
                        studyDetailView.onGetStudyDetailFailure(response.code())
                    }
                } else {
                    Log.e("PREV-TEXT-GET-SUCCESS", "Response not successful: ${response.code()}")
                    studyDetailView.onGetStudyDetailFailure(response.code())
                }
            }

            override fun onFailure(call: Call<StudyDetailRes>, t: Throwable) {
                Log.d("PREV-TEXT-GET-FAILURE", t.toString())
            }
        })
    }

    fun getMyText(user_id: Int, category: String, textId: Int) {
        val quizMyService = getRetrofit().create(StudyRetrofitInterface::class.java)
        quizMyService.getMyText(user_id, category, textId).enqueue(object : Callback<StudyDetailRes> {
            override fun onResponse(call: Call<StudyDetailRes>, response: Response<StudyDetailRes>) {
                if (response.isSuccessful) {
                    val resp: StudyDetailRes? = response.body()
                    if (resp != null) {
                        studyDetailView.onGetStudyDetailSuccess(resp)
                    } else {
                        Log.e("MY-TEXT-GET-SUCCESS", "Response body is null")
                        studyDetailView.onGetStudyDetailFailure(response.code())
                    }
                } else {
                    Log.e("MY-TEXT-GET-SUCCESS", "Response not successful: ${response.code()}")
                    studyDetailView.onGetStudyDetailFailure(response.code())
                }
            }

            override fun onFailure(call: Call<StudyDetailRes>, t: Throwable) {
                Log.d("MY-TEXT-GET-FAILURE", t.toString())
            }
        })
    }

    fun getMyTextCategory(user_id: Int) {
        val myTextCategoryService = getRetrofit().create(StudyRetrofitInterface::class.java)
        myTextCategoryService.getMyTextType(user_id).enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    val resp: List<String>? = response.body()
                    if (resp != null) {
                        studyCategoryView.onGetStudyCategorySuccess(resp)
                    } else {
                        Log.e("TEXT-TYPE-GET-SUCCESS", "Response body is null")
                        studyCategoryView.onGetStudyCategoryFailure(response.code())
                    }
                } else {
                    Log.e("TEXT-TYPE-GET-SUCCESS", "Response not successful: ${response.code()}")
                    studyCategoryView.onGetStudyCategoryFailure(response.code())
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.d("TEXT-TYPE-GET-FAILURE", t.toString())
            }
        })
    }

    fun getPrevTextCategory() {
        val prevTextCategoryService = getRetrofit().create(StudyRetrofitInterface::class.java)
        prevTextCategoryService.getPrevTextType().enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    val resp: List<String>? = response.body()
                    if (resp != null) {
                        studyCategoryView.onGetStudyCategorySuccess(resp)
                    } else {
                        Log.e("TEXT-TYPE-GET-SUCCESS", "Response body is null")
                        studyCategoryView.onGetStudyCategoryFailure(response.code())
                    }
                } else {
                    Log.e("TEXT-TYPE-GET-SUCCESS", "Response not successful: ${response.code()}")
                    studyCategoryView.onGetStudyCategoryFailure(response.code())
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.d("TEXT-TYPE-GET-FAILURE", t.toString())
            }
        })
    }

    fun getMyTextList(user_id: Int, category: String) {
        val textListService = getRetrofit().create(StudyRetrofitInterface::class.java)
        textListService.getMyTextList(user_id, category).enqueue(object : Callback<List<TextList>> {
            override fun onResponse(call: Call<List<TextList>>, response: Response<List<TextList>>) {
                if (response.isSuccessful) {
                    val resp: List<TextList>? = response.body()
                    if (resp != null) {
                        studyListView.onGetStudyListSuccess(resp)
                    } else {
                        Log.e("TEXT-LIST-GET-SUCCESS", "Response body is null")
                        studyListView.onGetStudyListFailure(response.code())
                    }
                } else {
                    Log.e("TEXT-LIST-GET-SUCCESS", "Response not successful: ${response.code()}")
                    studyListView.onGetStudyListFailure(response.code())
                }
            }

            override fun onFailure(call: Call<List<TextList>>, t: Throwable) {
                Log.d("TEXT-LIST-GET-FAILURE", t.toString())
            }
        })
    }

    fun getPrevTextList(category: String) {
        val textListService = getRetrofit().create(StudyRetrofitInterface::class.java)
        textListService.getPrevTextList(category).enqueue(object : Callback<List<TextList>> {
            override fun onResponse(call: Call<List<TextList>>, response: Response<List<TextList>>) {
                if (response.isSuccessful) {
                    val resp: List<TextList>? = response.body()
                    if (resp != null) {
                        studyListView.onGetStudyListSuccess(resp)
                    } else {
                        Log.e("TEXT-LIST-GET-SUCCESS", "Response body is null")
                        studyListView.onGetStudyListFailure(response.code())
                    }
                } else {
                    Log.e("TEXT-LIST-GET-SUCCESS", "Response not successful: ${response.code()}")
                    studyListView.onGetStudyListFailure(response.code())
                }
            }

            override fun onFailure(call: Call<List<TextList>>, t: Throwable) {
                Log.d("TEXT-LIST-GET-FAILURE", t.toString())
            }
        })
    }

    fun getQuizList(user_id: Int) {
        val quizListService = getRetrofit().create(LibraryRetrofitInterface::class.java)
        quizListService.getQuizList(user_id).enqueue(object : Callback<List<QuizSaveList>> {
            override fun onResponse(call: Call<List<QuizSaveList>>, response: Response<List<QuizSaveList>>) {
                if (response.isSuccessful) {
                    val resp: List<QuizSaveList>? = response.body()
                    if (resp != null) {
                        quizListView.onGetQuizListSuccess(resp)
                    } else {
                        Log.e("QUIZ-LIST-GET-SUCCESS", "Response body is null")
                        quizListView.onGetQuizListFailure(response.code())
                    }
                } else {
                    Log.e("QUIZ-LIST-GET-SUCCESS", "Response not successful: ${response.code()}")
                    quizListView.onGetQuizListFailure(response.code())
                }
            }

            override fun onFailure(call: Call<List<QuizSaveList>>, t: Throwable) {
                Log.d("QUIZ-LIST-GET-FAILURE", t.toString())
            }
        })
    }

    fun getQuiz(user_id: Int, quiz_id: Int) {
        val quizService = getRetrofit().create(LibraryRetrofitInterface::class.java)
        quizService.getQuiz(user_id, quiz_id).enqueue(object : Callback<QuizSaveDetailRes> {
            override fun onResponse(call: Call<QuizSaveDetailRes>, response: Response<QuizSaveDetailRes>) {
                if (response.isSuccessful) {
                    val resp: QuizSaveDetailRes? = response.body()
                    if (resp != null) {
                        quizView.onGetQuizSuccess(resp)
                    } else {
                        Log.e("QUIZ-GET-SUCCESS", "Response body is null")
                        quizView.onGetQuizFailure(response.code())
                    }
                } else {
                    Log.e("QUIZ-GET-SUCCESS", "Response not successful: ${response.code()}")
                    quizView.onGetQuizFailure(response.code())
                }
            }

            override fun onFailure(call: Call<QuizSaveDetailRes>, t: Throwable) {
                Log.d("QUIZ-GET-FAILURE", t.toString())
            }
        })
    }

    fun setGradePrevQuiz(category: String, textId: Int) {
        val quizPrevGradeService = getRetrofit().create(StudyRetrofitInterface::class.java)
        quizPrevGradeService.postPrevTextCheckQuiz(category, textId).enqueue(object : Callback<QuizGradeRes> {
            override fun onResponse(call: Call<QuizGradeRes>, response: Response<QuizGradeRes>) {
                if (response.isSuccessful) {
                    val resp: QuizGradeRes? = response.body()
                    if (resp != null) {
                        quizGradeView.onGetQuizGradeSuccess(resp)
                    } else {
                        Log.e("PREV-QUIZ-GRADE-SUCCESS", "Response body is null")
                        quizGradeView.onGetQuizGradeFailure(response.code())
                    }
                } else {
                    Log.e("PREV-QUIZ-GRADE-SUCCESS", "Response not successful: ${response.code()}")
                    quizGradeView.onGetQuizGradeFailure(response.code())
                }
            }

            override fun onFailure(call: Call<QuizGradeRes>, t: Throwable) {
                Log.d("PREV-QUIZ-GRADE-FAILURE", t.toString())
            }
        })
    }

    fun setGradeMyQuiz(user_id: Int, category: String, textId: Int) {
        val quizPrevGradeService = getRetrofit().create(StudyRetrofitInterface::class.java)
        quizPrevGradeService.postMyTextCheckQuiz(user_id, category, textId).enqueue(object : Callback<QuizGradeRes> {
            override fun onResponse(call: Call<QuizGradeRes>, response: Response<QuizGradeRes>) {
                if (response.isSuccessful) {
                    val resp: QuizGradeRes? = response.body()
                    if (resp != null) {
                        quizGradeView.onGetQuizGradeSuccess(resp)
                    } else {
                        Log.e("MY-QUIZ-GRADE-SUCCESS", "Response body is null")
                        quizGradeView.onGetQuizGradeFailure(response.code())
                    }
                } else {
                    Log.e("MY-QUIZ-GRADE-SUCCESS", "Response not successful: ${response.code()}")
                    quizGradeView.onGetQuizGradeFailure(response.code())
                }
            }

            override fun onFailure(call: Call<QuizGradeRes>, t: Throwable) {
                Log.d("MY-QUIZ-GRADE-FAILURE", t.toString())
            }
        })
    }

    fun setQuizFeedback(userId: Int, quizId: Int, contents: FeedbackReq) {
        val feedbackService = getRetrofit().create(StudyRetrofitInterface::class.java)
        feedbackService.postQuizFeedback(userId, quizId, contents).enqueue(object : Callback<BasicRes2> {
            override fun onResponse(call: Call<BasicRes2>, response: Response<BasicRes2>) {
                if (response.isSuccessful) {
                    val resp: BasicRes2? = response.body()
                    if (resp != null) {
                        feedbackView.onGetFeedbackSuccess(resp)
                    } else {
                        Log.e("QUIZ-FEEDBACK-SUCCESS", "Response body is null")
                    }
                } else {
                    Log.e("QUIZ-FEEDBACK-SUCCESS", "Response not successful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<BasicRes2>, t: Throwable) {
                Log.d("QUIZ-FEEDBACK-FAILURE", t.toString())
            }
        })
    }

    fun setQuizSavePrev(category: String, text_id: Int, user_id: Int, quizSaveReq: QuizSaveReq) {
        val prevQuizSaveService = getRetrofit().create(StudyRetrofitInterface::class.java)
        prevQuizSaveService.postQuizPrevTextSave(category, text_id, user_id, quizSaveReq).enqueue(object : Callback<QuizSaveRes> {
            override fun onResponse(call: Call<QuizSaveRes>, response: Response<QuizSaveRes>) {
                if (response.isSuccessful) {
                    val resp: QuizSaveRes? = response.body()
                    if (resp != null) {
                        quizSaveView.setQuizSaveSuccess(resp)
                    } else {
                        Log.e("PREV-QUIZ-SAVE-SUCCESS", "Response body is null")
                    }
                } else {
                    Log.e("PREV-QUIZ-SAVE-SUCCESS", "Response not successful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<QuizSaveRes>, t: Throwable) {
                Log.d("PREV-QUIZ-SAVE-FAILURE", t.toString())
            }
        })
    }

    fun setQuizSaveMy(category: String, text_id: Int, user_id: Int, quizSaveReq: QuizSaveReq) {
        val myQuizSaveService = getRetrofit().create(StudyRetrofitInterface::class.java)
        myQuizSaveService.postQuizMyTextSave(category, text_id, user_id, quizSaveReq).enqueue(object : Callback<QuizSaveRes> {
            override fun onResponse(call: Call<QuizSaveRes>, response: Response<QuizSaveRes>) {
                if (response.isSuccessful) {
                    val resp: QuizSaveRes? = response.body()
                    if (resp != null) {
                        quizSaveView.setQuizSaveSuccess(resp)
                    } else {
                        Log.e("MY-QUIZ-SAVE-SUCCESS", "Response body is null")
                    }
                } else {
                    Log.e("MY-QUIZ-SAVE-SUCCESS", "Response not successful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<QuizSaveRes>, t: Throwable) {
                Log.d("MY-QUIZ-SAVE-FAILURE", t.toString())
            }
        })
    }

    fun delSaveQuiz(user_id: Int, quiz_id: Int, position: Int) {
        val delQuizSaveService = getRetrofit().create(LibraryRetrofitInterface::class.java)
        delQuizSaveService.delQuiz(user_id, quiz_id).enqueue(object : Callback<BasicRes> {
            override fun onResponse(call: Call<BasicRes>, response: Response<BasicRes>) {
                if (response.isSuccessful) {
                    val resp: BasicRes? = response.body()
                    if (resp != null) {
                        quizDeleteView.deleteQuizSuccess(resp, position)
                    } else {
                        Log.e("QUIZ-DELETE-SUCCESS", "Response body is null")
                    }
                } else {
                    Log.e("QUIZ-DELETE-SUCCESS", "Response not successful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<BasicRes>, t: Throwable) {
                Log.d("QUIZ-DELETE-FAILURE", t.toString())
            }
        })
    }
}