package com.example.nunettine.ui.save.memo

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
import com.example.nunettine.data.remote.dto.library.MemoList
import com.example.nunettine.data.remote.service.library_study.MemoService
import com.example.nunettine.data.remote.view.library.MemoDelView
import com.example.nunettine.databinding.ItemMemoListBinding
import com.example.nunettine.ui.main.MainActivity
import java.text.SimpleDateFormat
import java.util.Locale

class SaveMemoRVAdapter(private val context: Context, private val memoList: MutableList<MemoList>): RecyclerView.Adapter<SaveMemoRVAdapter.ViewHolder>(), MemoDelView {
    private lateinit var binding :ItemMemoListBinding
    private var user_id = 0
    inner class ViewHolder(val binding: ItemMemoListBinding): RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
        fun bind(memo_list: MemoList) = with(binding) {
            getData()
            itemMemoListNameTv.text = memo_list.title
            textScroll(itemMemoListNameTv)

            itemMemoListDelBtn.setOnClickListener {
                onDeleteMemoService(memo_list.memo_id, adapterPosition)
            }

            itemMemoListLo.setOnClickListener {
                moveFragment(ModifyMemoFragment())
                saveData(memo_list.memo_id)
            }

            itemMemoListDateTv.text = timeFormat(memo_list.updatedAt)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemMemoListBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = memoList.size

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(memoList[position])

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
    private fun saveData(memoId: Int) {
        // 데이터 저장
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("memo", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt("memo_id", memoId)
        editor.apply()
    }

    private fun onDeleteMemoService(memo_id: Int, position: Int) {
        val deleteMemoService = MemoService()
        deleteMemoService.setMemoDelView(this@SaveMemoRVAdapter)
        deleteMemoService.deleteMemo(memo_id, position, user_id)
    }

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

    override fun onGetMemoDeleteSuccess(response: BasicRes, position: Int) {
        Toast.makeText(context, "메모가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
        memoList.removeAt(position)
        notifyItemRemoved(position)
        Log.d("MEMO-DELETE-성공", response.message)
    }

    override fun onGetMemoDeleteFailure(result_code: Int) {
        Log.d("MEMO-DELETE-실패", result_code.toString())
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
}