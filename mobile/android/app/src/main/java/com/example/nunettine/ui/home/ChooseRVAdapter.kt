package com.example.nunettine.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.nunettine.R
import com.example.nunettine.data.remote.dto.study.TextList
import com.example.nunettine.databinding.ItemChooseBinding
import com.example.nunettine.ui.main.MainActivity

class ChooseRVAdapter(private val textList: List<TextList>, private val context: Context, private val type: String, private val category: String): RecyclerView.Adapter<ChooseRVAdapter.ViewHolder>() {
    private lateinit var binding: ItemChooseBinding
    inner class ViewHolder(val binding: ItemChooseBinding): RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
        fun bind(text_list: TextList) = with(binding) {
            // 텍스트가 길때 자동 스크롤
            textScroll(chooseItemTv)

            chooseItemTv.text = text_list.title

            chooseItemLo.setOnClickListener {
                saveData(type, category, text_list.text_id)
                moveFragment(PreviewContentsFragment())
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemChooseBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = textList.size

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(textList[position])

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
    fun saveData(text_type: String, category: String, text_id: Int) {
        // 데이터 저장
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("type", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("type", text_type)
        editor.putString("category", category)
        editor.putInt("text_id", text_id)
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
}