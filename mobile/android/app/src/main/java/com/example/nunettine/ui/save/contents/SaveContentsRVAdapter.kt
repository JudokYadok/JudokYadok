package com.example.nunettine.ui.save.contents

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
import com.example.nunettine.data.remote.dto.library.TextList
import com.example.nunettine.data.remote.service.library_study.TextService
import com.example.nunettine.data.remote.view.library.TextDelView
import com.example.nunettine.databinding.ItemContentsListBinding
import com.example.nunettine.ui.main.MainActivity
import com.example.nunettine.ui.save.memo.ModifyMemoFragment

class SaveContentsRVAdapter(private val context: Context, private val contentsList: MutableList<TextList>): RecyclerView.Adapter<SaveContentsRVAdapter.ViewHolder>(), TextDelView {
    private lateinit var binding : ItemContentsListBinding
    private var user_id = 0

    inner class ViewHolder(val binding: ItemContentsListBinding): RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
        fun bind(textList: TextList) = with(binding) {
            getData()
            itemContentsListTypeTv.text = textList.text_category
            itemContentsListNameTv.text = textList.text_title
            textScroll(itemContentsListNameTv)

            itemContentsListDelBtn.setOnClickListener {
                delTextService(textList.text_id, adapterPosition)
            }

            itemContentsListLo.setOnClickListener {
                moveFragment(ModifyContentsFragment(textList.text_category))
                saveData(textList.text_id)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemContentsListBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = contentsList.size

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(contentsList[position])

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
    private fun saveData(textId: Int) {
        // 데이터 저장
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("contents", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt("contents_id", textId)
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

    private fun delTextService(text_id: Int, position: Int) {
        val delTextService = TextService()
        delTextService.setTextDeleteView(this@SaveContentsRVAdapter)
        delTextService.deleteText(user_id, text_id, position)
    }

    override fun deleteTextSuccess(response: BasicRes, position: Int) {
        Toast.makeText(context, "지문이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
        contentsList.removeAt(position)
        notifyItemRemoved(position)
        Log.d("TEXT-DELETE-성공", response.toString())
    }

    override fun deleteTextFailure(message: String) {
        Log.d("TEXT-DELETE-오류", message)
    }
}