package com.example.nunettine.ui.home

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
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.nunettine.R
import com.example.nunettine.databinding.FragmentPreviewContentsBinding
import com.example.nunettine.ui.main.MainActivity

class PreviewContentsFragment: Fragment() {
    private lateinit var binding: FragmentPreviewContentsBinding
    private lateinit var viewModel: HomeViewModel
    private var type = ""
    private var category = ""
    private var text_id = 0
    private var user_id = 0

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPreviewContentsBinding.inflate(layoutInflater)
        getData()
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        if(type == "PREVTEXT") {
            viewModel.setStudyPrevDetailService(category, text_id)
            observeTextList()
        } else {
            viewModel.setStudyMyDetailService(user_id, category, text_id)
            observeTextList()
        }

        clickListener()
        return binding.root
    }

    private fun clickListener() = with(binding) {
        previewContentsBackBtn.setOnClickListener { moveFragment(ChooseFragment()) }
        previewContentsBtn.setOnClickListener { moveFragment(MergeCountFragment()) }
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
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("type", Context.MODE_PRIVATE)
        type = sharedPreferences.getString("type", type)!!
        category = sharedPreferences.getString("category", category)!!
        text_id = sharedPreferences.getInt("text_id", text_id)

        val sharedPreferences2: SharedPreferences = requireContext().getSharedPreferences("kakao", Context.MODE_PRIVATE)
        user_id = sharedPreferences2.getInt("user_id", user_id)
    }

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    private fun saveData(text_title: String, text_contents: String) {
        // 데이터 저장
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("text", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("text_title", text_title)
        editor.putString("text_contents", text_contents)
        editor.apply()
    }

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    private fun initUI(text_title: String, text_contents: String) = with(binding) {
        textScroll(previewContentsTv)
        previewContentsTv.text = text_title
        previewContentsDetailTv.text = text_contents
        saveData(text_title, text_contents)
    }

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    private fun observeTextList() {
        viewModel.textTitleML.observe(viewLifecycleOwner) { textTitle ->
            viewModel.textContentsML.observe(viewLifecycleOwner) { textContents ->
                // 데이터가 변경되었을 때 UI 업데이트
                initUI(textTitle, textContents)
            }
        }
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