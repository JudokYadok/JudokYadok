package com.example.nunettine.ui.save.problem

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nunettine.R
import com.example.nunettine.data.remote.dto.BasicRes
import com.example.nunettine.data.remote.dto.library.QuizSaveList
import com.example.nunettine.data.remote.service.library_study.QuizService
import com.example.nunettine.data.remote.view.library.QuizDelView
import com.example.nunettine.databinding.ItemProblemListBinding
import com.example.nunettine.ui.main.MainActivity
import com.example.nunettine.ui.save.memo.ModifyMemoFragment
import java.text.SimpleDateFormat
import java.util.Locale

class SaveProblemRVAdapter(private val context: Context, private var quiz_list: MutableList<QuizSaveList>): RecyclerView.Adapter<SaveProblemRVAdapter.ViewHolder>(), QuizDelView {
    private lateinit var binding: ItemProblemListBinding
    private var user_id = 0

    inner class ViewHolder(val binding: ItemProblemListBinding): RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
        fun bind(quizList: QuizSaveList) = with(binding) {
            getData()
            itemProblemListNameTv.text = quizList.title
            textScroll(itemProblemListNameTv)

            itemProblemListDelBtn.setOnClickListener {
                delQuizService(quizList.quiz_id, adapterPosition)
            }

            itemProblemListLo.setOnClickListener {
                saveData(quizList.quiz_id)
                moveFragment(SaveProblemReadFragment(quizList.title))
            }

            itemProblemListDateTv.text = timeFormat(quizList.updatedAt)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SaveProblemRVAdapter.ViewHolder {
        binding = ItemProblemListBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = quiz_list.size

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    override fun onBindViewHolder(holder: SaveProblemRVAdapter.ViewHolder, position: Int) = holder.bind(quiz_list[position])

    private fun timeFormat(originalTime: String): String {
        // 원본 문자열을 날짜로 파싱합니다.
        val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val originalDate = originalFormat.parse(originalTime)

        // 포맷을 변경하고자 하는 형식을 정의합니다.
        val targetFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        // 새로운 형식으로 포맷합니다.
        val formattedDate = targetFormat.format(originalDate)
        return formattedDate
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

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    private fun saveData(quizId: Int) {
        // 데이터 저장
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("quiz_save", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt("quiz_id", quizId)
        editor.apply()
    }

    private fun getData() {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("kakao", Context.MODE_PRIVATE)
        user_id = sharedPreferences.getInt("user_id", user_id)
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

    private fun delQuizService(quiz_id: Int, position: Int) {
        val delQuizService = QuizService()
        delQuizService.setQuizDeleteView(this@SaveProblemRVAdapter)
        delQuizService.delSaveQuiz(user_id,quiz_id, position)
    }

    override fun deleteQuizSuccess(response: BasicRes, position: Int) {
        Toast.makeText(context, "퀴즈가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
        quiz_list.removeAt(position)
        notifyItemRemoved(position)
        Log.d("QUIZ-DELETE-성공", response.toString())
    }

    override fun deleteQuizFailure(message: String) {
        Log.d("QUIZ-DELETE-오류", message)
    }
}