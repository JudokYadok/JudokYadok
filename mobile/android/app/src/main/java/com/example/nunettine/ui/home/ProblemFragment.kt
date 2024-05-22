package com.example.nunettine.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.nunettine.R
import com.example.nunettine.data.remote.dto.study.Question
import com.example.nunettine.data.remote.dto.study.QuizGradeRes
import com.example.nunettine.data.remote.service.library_study.QuizService
import com.example.nunettine.data.remote.view.study.QuizGradeView
import com.example.nunettine.databinding.FragmentProblemBinding
import com.example.nunettine.ui.main.MainActivity
import com.example.nunettine.ui.save.memo.SaveMemoViewModel
import com.example.nunettine.utils.LoadingDialogGrade

class ProblemFragment(private val quiz_list: List<Question>) : Fragment(), QuizGradeView {
    private lateinit var binding: FragmentProblemBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var memoViewModel: SaveMemoViewModel
    private lateinit var loadingDialog: LoadingDialogGrade
    private var timer: CountDownTimer? = null
    private var isPlaying = false
    private var pauseTime = 0L // 멈춤 시간
    private var type = ""
    private var category = ""
    private var text_id = 0
    private var text_title = ""
    private var text_contents = ""
    private var user_id = 0
    private var quiz_answer_list = listOf<Int>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProblemBinding.inflate(layoutInflater)
        getData()
        loadingDialog = LoadingDialogGrade(requireContext()) // 로딩 다이얼로그 초기화

        textScroll(binding.problemTv)
        binding.problemTv.text = text_title
        binding.problemContentsTv.text = text_contents
        clickListener()
        settingMedia()
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        memoViewModel = ViewModelProvider(this).get(SaveMemoViewModel::class.java)
        initUI(quiz_list)
        return binding.root
    }

    private fun initUI(quizList: List<Question>) = with(binding) {
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

    private fun settingMedia() = with(binding) {
        problemPlayBtn.setOnClickListener {
            Log.d("MEDIA-PLAY", problemMediaTv.text.toString())

            // 정지 상태일 때만 실행
            if(!isPlaying) {
                startChronometer()
            }
        }

        problemTempoBtn.setOnClickListener {
            Log.d("MEDIA-PAUSE", problemMediaTv.text.toString())

            // 실행 상태일 때만 실행
            if(isPlaying) {
                pauseChronometer()
            }
        }

        problemStopBtn.setOnClickListener {
            Log.d("MEDIA-STOP", problemMediaTv.text.toString())

            stopChronometer()
        }
    }

    private fun startChronometer() = with(binding) {
        problemMediaTv.base = SystemClock.elapsedRealtime() - pauseTime
        problemMediaTv.start()
        problemPlayBtn.visibility = View.GONE
        problemTempoBtn.visibility = View.VISIBLE
        isPlaying = true
    }

    private fun stopChronometer() = with(binding) {
        problemMediaTv.base = SystemClock.elapsedRealtime()
        pauseTime = 0L
        problemMediaTv.stop()
        problemPlayBtn.visibility = View.VISIBLE
        problemTempoBtn.visibility = View.GONE
        isPlaying = false
    }

    private fun pauseChronometer() = with(binding) {
        problemMediaTv.stop()
        pauseTime = SystemClock.elapsedRealtime() - problemMediaTv.base
        problemPlayBtn.visibility = View.VISIBLE
        problemTempoBtn.visibility = View.GONE
        isPlaying = false
    }

    private fun clickListener() = with(binding) {
        problemBackBtn.setOnClickListener {
            moveFragment(MergeCountFragment())
            stopChronometer()
        }

        problemMemoBtn.setOnClickListener {
            moveFragment(MemoFragment())
            pauseChronometer()
        }

        problemCheckBtn.setOnClickListener {
            if(type =="PREVTEXT") {
                setPrevQuizGradeService(category, text_id)
                loadingDialog.show() // 로딩 다이얼로그 표시
            } else {
                setMyQuizGradeService(user_id, category, text_id)
                loadingDialog.show()
            }
        }

        // 1번 문제
        problemOmr11btn.setOnClickListener {
            viewModel.quizUserAnswer1ML.value = 0
            observeQuizUserAnswer()
            clickButton1(problemOmr11btn)
        }
        problemOmr12btn.setOnClickListener {
            viewModel.quizUserAnswer1ML.value = 1
            observeQuizUserAnswer()
            clickButton1(problemOmr12btn)
        }
        problemOmr13btn.setOnClickListener {
            viewModel.quizUserAnswer1ML.value = 2
            observeQuizUserAnswer()
            clickButton1(problemOmr13btn)
        }
        problemOmr14btn.setOnClickListener {
            viewModel.quizUserAnswer1ML.value = 3
            observeQuizUserAnswer()
            clickButton1(problemOmr14btn)
        }
        problemOmr15btn.setOnClickListener {
            viewModel.quizUserAnswer1ML.value = 4
            observeQuizUserAnswer()
            clickButton1(problemOmr15btn)
        }

        // 2번 문제
        problemOmr21btn.setOnClickListener {
            viewModel.quizUserAnswer2ML.value = 0
            observeQuizUserAnswer()
            clickButton2(problemOmr21btn)
        }
        problemOmr22btn.setOnClickListener {
            viewModel.quizUserAnswer2ML.value = 1
            observeQuizUserAnswer()
            clickButton2(problemOmr22btn)
        }
        problemOmr23btn.setOnClickListener {
            viewModel.quizUserAnswer2ML.value = 2
            observeQuizUserAnswer()
            clickButton2(problemOmr23btn)
        }
        problemOmr24btn.setOnClickListener {
            viewModel.quizUserAnswer2ML.value = 3
            observeQuizUserAnswer()
            clickButton2(problemOmr24btn)
        }
        problemOmr25btn.setOnClickListener {
            viewModel.quizUserAnswer2ML.value = 4
            observeQuizUserAnswer()
            clickButton2(problemOmr25btn)
        }

        // 3번 문제
        problemOmr31btn.setOnClickListener {
            viewModel.quizUserAnswer3ML.value = 0
            observeQuizUserAnswer()
            clickButton3(problemOmr31btn)
        }
        problemOmr32btn.setOnClickListener {
            viewModel.quizUserAnswer3ML.value = 1
            observeQuizUserAnswer()
            clickButton3(problemOmr32btn)
        }
        problemOmr33btn.setOnClickListener {
            viewModel.quizUserAnswer3ML.value = 2
            observeQuizUserAnswer()
            clickButton3(problemOmr33btn)
        }
        problemOmr34btn.setOnClickListener {
            viewModel.quizUserAnswer3ML.value = 3
            observeQuizUserAnswer()
            clickButton3(problemOmr34btn)
        }
        problemOmr35btn.setOnClickListener {
            viewModel.quizUserAnswer3ML.value = 4
            observeQuizUserAnswer()
            clickButton3(problemOmr35btn)
        }
    }

    private fun observeQuizUserAnswer(){
        viewModel.quizUserAnswer1ML.observe(viewLifecycleOwner) { quizAnswer ->
            quiz_answer_list = listOf(quizAnswer)
        }

        viewModel.quizUserAnswer1ML.observe(viewLifecycleOwner) { quizAnswer1 ->
            viewModel.quizUserAnswer2ML.observe(viewLifecycleOwner) { quizAnswer2 ->
                viewModel.quizUserAnswer3ML.observe(viewLifecycleOwner) { quizAnswer3 ->
                    quiz_answer_list = listOf(quizAnswer1, quizAnswer2, quizAnswer3)
                }
            }
        }
    }

    private fun clickButton1(clickedButton: Button) = with(binding) {
        val button1 = listOf(
            problemOmr11btn,
            problemOmr12btn,
            problemOmr13btn,
            problemOmr14btn,
            problemOmr15btn
        )

        button1.forEach { button ->
            button.isSelected = (button == clickedButton)
        }
        problemCheckBtn.isEnabled = true
    }

    private fun clickButton2(clickedButton: Button) = with(binding) {
        val button2 = listOf(
            problemOmr21btn,
            problemOmr22btn,
            problemOmr23btn,
            problemOmr24btn,
            problemOmr25btn
        )

        button2.forEach { button ->
            button.isSelected = (button == clickedButton)
        }
        problemCheckBtn.isEnabled = true
    }

    private fun clickButton3(clickedButton: Button) = with(binding) {
        val button3 = listOf(
            problemOmr31btn,
            problemOmr32btn,
            problemOmr33btn,
            problemOmr34btn,
            problemOmr35btn
        )

        button3.forEach { button ->
            button.isSelected = (button == clickedButton)
        }
        problemCheckBtn.isEnabled = true
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

    private fun setPrevQuizGradeService(category: String, text_id: Int) {
        val setPrevQuizGradeService = QuizService()
        setPrevQuizGradeService.setQuizGradeView(this@ProblemFragment)
        setPrevQuizGradeService.setGradePrevQuiz(category, text_id)
    }

    private fun setMyQuizGradeService(user_id: Int, category: String, text_id: Int) {
        val setMyQuizGradeService = QuizService()
        setMyQuizGradeService.setQuizGradeView(this@ProblemFragment)
        setMyQuizGradeService.setGradeMyQuiz(user_id, category, text_id)
    }

    override fun onGetQuizGradeSuccess(response: QuizGradeRes) {
        val quiz_right_list = listOf(quiz_list[0].answers.indexOfFirst { it.correct }, quiz_list[1].answers.indexOfFirst { it.correct }, quiz_list[2].answers.indexOfFirst { it.correct })
        moveFragment(CheckFragment(quiz_list, quiz_answer_list, quiz_right_list, response.content))
        timer?.cancel()
        loadingDialog.dismiss()
        Log.d("TEXT-SUMMARY-성공", response.toString())
    }

    override fun onGetQuizGradeFailure(result_code: Int) {
        timer?.cancel()
        loadingDialog.dismiss()
        Log.d("TEXT-SUMMARY-오류", result_code.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Fragment가 제거될 때 타이머를 취소합니다.
        timer?.cancel()
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
}