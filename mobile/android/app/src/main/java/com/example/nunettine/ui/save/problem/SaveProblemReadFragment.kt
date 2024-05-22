package com.example.nunettine.ui.save.problem

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.nunettine.R
import com.example.nunettine.data.remote.dto.library.QuizSaveDetailRes
import com.example.nunettine.data.remote.dto.study.Question
import com.example.nunettine.databinding.FragmentSaveProblemBinding
import com.example.nunettine.ui.home.MemoFragment
import com.example.nunettine.ui.main.MainActivity

class SaveProblemReadFragment(private val title: String): Fragment() {
    private lateinit var binding: FragmentSaveProblemBinding
    private lateinit var viewModel: SaveProblemViewModel
    private var quiz_id = 0
    private var user_id = 0

    private var right = 0
    private var wrong = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSaveProblemBinding.inflate(layoutInflater)
        getData()
        viewModel = ViewModelProvider(this).get(SaveProblemViewModel::class.java)
        viewModel.getQuiz(user_id, quiz_id)
        observeQuiz()
        clickListener()
        return binding.root
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

    private fun compareAnswer(quiz_answer_list: List<Int>, quiz_right_list: List<Int>) {
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
    private fun initOMRUI(quiz_answer_list: List<Int>, quiz_right_list: List<Int>) = with(binding) {
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

    private fun initUI(quiz: QuizSaveDetailRes) = with(binding) {
        textScroll(problemTv)
        problemTv.text = title
        problemContentsTv.text = quiz.text_contents
        // 문제 설정
        problemQuiz11TitleTv.text = quiz.questions[0]
        problemQuiz22TitleTv.text = quiz.questions[1]
        problemQuiz33TitleTv.text = quiz.questions[2]

        // 문제 보기 설정 - 1
        problemQuiz11TextTv.text = "(1) " + quiz.answers.answer_list1[0]
        problemQuiz12TextTv.text = "(2) " + quiz.answers.answer_list1[1]
        problemQuiz13TextTv.text = "(3) " + quiz.answers.answer_list1[2]
        problemQuiz14TextTv.text = "(4) " + quiz.answers.answer_list1[3]
        problemQuiz15TextTv.text = "(5) " + quiz.answers.answer_list1[4]

        // 문제 보기 설정 - 2
        problemQuiz21TextTv.text = "(1) " + quiz.answers.answer_list2[0]
        problemQuiz22TextTv.text = "(2) " + quiz.answers.answer_list2[1]
        problemQuiz23TextTv.text = "(3) " + quiz.answers.answer_list2[2]
        problemQuiz24TextTv.text = "(4) " + quiz.answers.answer_list2[3]
        problemQuiz25TextTv.text = "(5) " + quiz.answers.answer_list2[4]

        // 문제 보기 설정 - 3
        problemQuiz31TextTv.text = "(1) " + quiz.answers.answer_list3[0]
        problemQuiz32TextTv.text = "(2) " + quiz.answers.answer_list3[1]
        problemQuiz33TextTv.text = "(3) " + quiz.answers.answer_list3[2]
        problemQuiz34TextTv.text = "(4) " + quiz.answers.answer_list3[3]
        problemQuiz35TextTv.text = "(5) " + quiz.answers.answer_list3[4]
    }

    private fun clickListener() = with(binding) {
        problemBackBtn.setOnClickListener { goBackFragment() }
    }

    private fun goBackFragment() { parentFragmentManager.popBackStack() }

    private fun getData() {
        // 데이터 읽어오기
        val sharedPreferences1: SharedPreferences = requireContext().getSharedPreferences("quiz_save", Context.MODE_PRIVATE)
        quiz_id = sharedPreferences1.getInt("quiz_id", quiz_id)

        val sharedPreferences2: SharedPreferences = requireContext().getSharedPreferences("kakao", Context.MODE_PRIVATE)
        user_id = sharedPreferences2.getInt("user_id", user_id)
    }

    private fun observeQuiz() {
        viewModel.userAnswersML.observe(viewLifecycleOwner) { userAnswers ->
            viewModel.correctAnswersML.observe(viewLifecycleOwner) { correctAnswers ->
                if(userAnswers.size != 0 && correctAnswers.size != 0) {
                    compareAnswer(userAnswers, correctAnswers)
                    initOMRUI(userAnswers, correctAnswers)
                }
            }
        }

        viewModel.responseML.observe(viewLifecycleOwner) { response ->
            if(response != null) {
                initUI(response)
            }
        }
    }
}