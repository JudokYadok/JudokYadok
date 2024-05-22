package com.example.nunettine.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.nunettine.R
import com.example.nunettine.data.local.Answer_List
import com.example.nunettine.data.local.QuizSaveReq
import com.example.nunettine.data.local.Quiz_List
import com.example.nunettine.data.remote.dto.study.Question
import com.example.nunettine.data.remote.dto.study.QuizSaveRes
import com.example.nunettine.data.remote.service.library_study.QuizService
import com.example.nunettine.data.remote.view.study.QuizSaveView
import com.example.nunettine.databinding.FragmentCheckBinding
import com.example.nunettine.ui.main.MainActivity

class CheckFragment(private val quiz_list: List<Question>, private val quiz_answer_list: List<Int>, private val quiz_right_list: List<Int>, private val quiz_summary: String): Fragment(), QuizSaveView {
    private lateinit var binding: FragmentCheckBinding
    private var type = ""
    private var category = ""
    private var text_id = 0
    private var text_title = ""
    private var text_contents = ""
    private var right = 0
    private var wrong = 0
    private var user_id = 0
    private var quiz_id = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCheckBinding.inflate(layoutInflater)
        getData()
        clickListener()
        initUI(quiz_list)
        compareAnswer()
        initOMRUI()
        binding.problemFeedbackBtn.isEnabled = false // 피드백 버튼 비활성화
        Log.d("ANSWER-LIST", "${quiz_answer_list}, ${quiz_right_list}")
        return binding.root
    }

    private fun compareAnswer() {
        // 1번 문제 답 비교
        if(quiz_answer_list[0] == quiz_right_list[0]) {
            right++
        } else {
            wrong++
        }

        // 2번 문제 답 비교
        if(quiz_answer_list[1] == quiz_right_list[1]) {
            right++
        } else {
            wrong++
        }

        // 3번 문제 답 비교
        if(quiz_answer_list[2] == quiz_right_list[2]) {
            right++
        } else {
            wrong++
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun initOMRUI() = with(binding) {
        problemYesResultTv.text = right.toString()
        problemNoResultTv.text = wrong.toString()

        // 1번 문제 omr 설정
        if(quiz_right_list[0] == 0) {
            problemOmr11btn.setBackgroundResource(R.drawable.btn_omr_right)
        } else if(quiz_right_list[0] == 1) {
            problemOmr12btn.setBackgroundResource(R.drawable.btn_omr_right)
        } else if(quiz_right_list[0] == 2) {
            problemOmr13btn.setBackgroundResource(R.drawable.btn_omr_right)
        } else if(quiz_right_list[0] == 3) {
            problemOmr14btn.setBackgroundResource(R.drawable.btn_omr_right)
        } else {
            problemOmr15btn.setBackgroundResource(R.drawable.btn_omr_right)
        }

        // 2번 문제 omr 설정
        if(quiz_right_list[1] == 0) {
            problemOmr21btn.setBackgroundResource(R.drawable.btn_omr_right)
        } else if(quiz_right_list[1] == 1) {
            problemOmr22btn.setBackgroundResource(R.drawable.btn_omr_right)
        } else if(quiz_right_list[1] == 2) {
            problemOmr23btn.setBackgroundResource(R.drawable.btn_omr_right)
        } else if(quiz_right_list[1] == 3) {
            problemOmr24btn.setBackgroundResource(R.drawable.btn_omr_right)
        } else {
            problemOmr25btn.setBackgroundResource(R.drawable.btn_omr_right)
        }

        // 3번 문제 omr 설정
        if(quiz_right_list[2] == 0) {
            problemOmr31btn.setBackgroundResource(R.drawable.btn_omr_right)
        } else if(quiz_right_list[2] == 1) {
            problemOmr32btn.setBackgroundResource(R.drawable.btn_omr_right)
        } else if(quiz_right_list[2] == 2) {
            problemOmr33btn.setBackgroundResource(R.drawable.btn_omr_right)
        } else if(quiz_right_list[2] == 3) {
            problemOmr34btn.setBackgroundResource(R.drawable.btn_omr_right)
        } else {
            problemOmr35btn.setBackgroundResource(R.drawable.btn_omr_right)
        }

        // 오답 omr 설정
        if(quiz_answer_list[0] != quiz_right_list[0]) {
            if(quiz_answer_list[0] == 0) {
                problemOmr11btn.setBackgroundResource(R.drawable.btn_omr_error)
            } else if(quiz_answer_list[0] == 1) {
                problemOmr12btn.setBackgroundResource(R.drawable.btn_omr_error)
            } else if(quiz_answer_list[0] == 2) {
                problemOmr13btn.setBackgroundResource(R.drawable.btn_omr_error)
            } else if(quiz_answer_list[0] == 3) {
                problemOmr14btn.setBackgroundResource(R.drawable.btn_omr_error)
            } else {
                problemOmr15btn.setBackgroundResource(R.drawable.btn_omr_error)
            }
        }

        if(quiz_answer_list[1] != quiz_right_list[1]) {
            if(quiz_answer_list[1] == 0) {
                problemOmr21btn.setBackgroundResource(R.drawable.btn_omr_error)
            } else if(quiz_answer_list[1] == 1) {
                problemOmr22btn.setBackgroundResource(R.drawable.btn_omr_error)
            } else if(quiz_answer_list[1] == 2) {
                problemOmr23btn.setBackgroundResource(R.drawable.btn_omr_error)
            } else if(quiz_answer_list[1] == 3) {
                problemOmr24btn.setBackgroundResource(R.drawable.btn_omr_error)
            } else {
                problemOmr25btn.setBackgroundResource(R.drawable.btn_omr_error)
            }
        }

        if(quiz_answer_list[2] != quiz_right_list[2]) {
            if(quiz_answer_list[2] == 0) {
                problemOmr31btn.setBackgroundResource(R.drawable.btn_omr_error)
            } else if(quiz_answer_list[2] == 1) {
                problemOmr32btn.setBackgroundResource(R.drawable.btn_omr_error)
            } else if(quiz_answer_list[2] == 2) {
                problemOmr33btn.setBackgroundResource(R.drawable.btn_omr_error)
            } else if(quiz_answer_list[2] == 3) {
                problemOmr34btn.setBackgroundResource(R.drawable.btn_omr_error)
            } else {
                problemOmr35btn.setBackgroundResource(R.drawable.btn_omr_error)
            }
        }
    }

    private fun initUI(quizList: List<Question>) = with(binding) {
        textScroll(problemTv)
        problemTv.text = text_title
        problemContentsTv.text = text_contents
        problemSummaryTv.text = quiz_summary
        // 문제 설정
        problemQuiz11TitleTv.text = quizList[0].question
        problemQuiz22TitleTv.text = quizList[1].question
        problemQuiz33TitleTv.text = quizList[2].question

        // 문제 보기 설정 - 1
        problemQuiz11TextTv.text = "(1) " + quizList[0].answers[0].answer
        problemQuiz12TextTv.text = "(2) " + quizList[0].answers[1].answer
        problemQuiz13TextTv.text = "(3) " + quizList[0].answers[2].answer
        problemQuiz14TextTv.text = "(4) " + quizList[0].answers[3].answer
        problemQuiz15TextTv.text = "(5) " + quizList[0].answers[4].answer

        // 문제 보기 설정 - 2
        problemQuiz21TextTv.text = "(1) " + quizList[1].answers[0].answer
        problemQuiz22TextTv.text = "(2) " + quizList[1].answers[1].answer
        problemQuiz23TextTv.text = "(3) " + quizList[1].answers[2].answer
        problemQuiz24TextTv.text = "(4) " + quizList[1].answers[3].answer
        problemQuiz25TextTv.text = "(5) " + quizList[1].answers[4].answer

        // 문제 보기 설정 - 3
        problemQuiz31TextTv.text = "(1) " + quizList[2].answers[0].answer
        problemQuiz32TextTv.text = "(2) " + quizList[2].answers[1].answer
        problemQuiz33TextTv.text = "(3) " + quizList[2].answers[2].answer
        problemQuiz34TextTv.text = "(4) " + quizList[2].answers[3].answer
        problemQuiz35TextTv.text = "(5) " + quizList[2].answers[4].answer
    }

    private fun setPrevQuizSaveService(quizList: List<Question>) {
        val answer_list = Answer_List(listOf(quizList[0].answers[0].answer,
            quizList[0].answers[1].answer, quizList[0].answers[2].answer,
            quizList[0].answers[3].answer, quizList[0].answers[4].answer),
            listOf(quizList[1].answers[0].answer, quizList[1].answers[1].answer,
                quizList[1].answers[2].answer, quizList[1].answers[3].answer,
                quizList[1].answers[4].answer),
            listOf(quizList[2].answers[0].answer, quizList[2].answers[1].answer,
                quizList[2].answers[2].answer, quizList[2].answers[3].answer,
                quizList[2].answers[4].answer)
        )
        val quiz_list = Quiz_List(listOf(quizList[0].question, quizList[1].question, quizList[2].question), answer_list)
        val quizSaveReq = QuizSaveReq(text_id, quiz_list, quiz_answer_list, quiz_right_list)
        val quizSaveService = QuizService()
        quizSaveService.setQuizSaveView(this@CheckFragment)
        quizSaveService.setQuizSavePrev(category, text_id, user_id, quizSaveReq)
    }

    private fun setMyQuizSaveService(quizList: List<Question>) {
        val answer_list = Answer_List(listOf(quizList[0].answers[0].answer,
            quizList[0].answers[1].answer, quizList[0].answers[2].answer,
            quizList[0].answers[3].answer, quizList[0].answers[4].answer),
            listOf(quizList[1].answers[0].answer, quizList[1].answers[1].answer,
                quizList[1].answers[2].answer, quizList[1].answers[3].answer,
                quizList[1].answers[4].answer),
            listOf(quizList[2].answers[0].answer, quizList[2].answers[1].answer,
                quizList[2].answers[2].answer, quizList[2].answers[3].answer,
                quizList[2].answers[4].answer)
        )
        val quiz_list = Quiz_List(listOf(quizList[0].question, quizList[1].question, quizList[2].question), answer_list)
        val quizSaveReq = QuizSaveReq(text_id, quiz_list, quiz_answer_list, quiz_right_list)
        val quizSaveService = QuizService()
        quizSaveService.setQuizSaveView(this@CheckFragment)
        quizSaveService.setQuizSaveMy(category, text_id, user_id, quizSaveReq)
    }

    private fun clickListener() = with(binding) {
        if(problemFeedbackBtn.isEnabled == false) {
            problemFeedbackBtn.setOnClickListener {
                Toast.makeText(context, "저장이 완료되면 피드백 작성이 가능합니다.", Toast.LENGTH_SHORT).show()
            }
        } else {
            problemFeedbackBtn.setOnClickListener { moveFragment(ProblemFeedbackFragment(text_title)) }
        }
        problemSaveBtn.setOnClickListener {
            // 응시한 문제 저장 api 호출 필요
            problemFeedbackBtn.isEnabled = true // feedback 버튼 활성화
            if(type == "PREVTEXT") {
                setPrevQuizSaveService(quiz_list)
            } else {
                setMyQuizSaveService(quiz_list)
            }

        }
        problemMemoBtn.setOnClickListener { moveFragment(MemoFragment()) }
    }

    private fun moveFragment(fragment: Fragment) {
        val mainActivity = context as MainActivity
        val mainFrmLayout = mainActivity.findViewById<FrameLayout>(R.id.main_frm) as FrameLayout?
        if (mainFrmLayout != null) {
            val transaction = mainActivity.supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            transaction.replace(mainFrmLayout.id, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    private fun getData() {
        // 데이터 읽어오기
        val sharedPreferences1: SharedPreferences = requireContext().getSharedPreferences("text", Context.MODE_PRIVATE)
        text_title = sharedPreferences1.getString("text_title", text_title)!!
        text_contents = sharedPreferences1.getString("text_contents", text_contents)!!

        val sharedPreferences2: SharedPreferences = requireContext().getSharedPreferences("type", Context.MODE_PRIVATE)
        type = sharedPreferences2.getString("type", type)!!
        category = sharedPreferences2.getString("category", category)!!
        text_id = sharedPreferences2.getInt("text_id", text_id)

        val sharedPreferences3: SharedPreferences = requireContext().getSharedPreferences("kakao", Context.MODE_PRIVATE)
        user_id = sharedPreferences3.getInt("user_id", user_id)
    }

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    private fun saveData() {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("quiz_id", AppCompatActivity.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt("quiz_id", quiz_id)
        editor.apply()
    }

    private fun textScroll(textView: TextView) {
        // 텍스트가 길때 자동 스크롤
        textView.apply {
            setSingleLine()
            marqueeRepeatLimit = -1
            ellipsize = TextUtils.TruncateAt.MARQUEE
            isSelected = true
        }
    }

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    override fun setQuizSaveSuccess(response: QuizSaveRes) {
        Toast.makeText(context, "문제가 저장되었습니다.", Toast.LENGTH_SHORT).show()
        quiz_id = response.quiz_id
        saveData()
        Log.d("문제 저장 성공", response.message)
    }

    override fun setQuizSaveFailure(message: String) {
        Log.d("문제 저장 오류", message)
    }
}