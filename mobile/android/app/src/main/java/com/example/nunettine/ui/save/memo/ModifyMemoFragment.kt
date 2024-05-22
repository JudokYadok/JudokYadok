package com.example.nunettine.ui.save.memo

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.nunettine.data.local.MemoReq
import com.example.nunettine.databinding.FragmentModifyMemoBinding

class ModifyMemoFragment: Fragment() {
    private lateinit var binding: FragmentModifyMemoBinding
    private lateinit var viewModel: SaveMemoViewModel
    private var memoId = 0
    private var user_id = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentModifyMemoBinding.inflate(layoutInflater)
        getData()
        viewModel = ViewModelProvider(this).get(SaveMemoViewModel::class.java)
        viewModel.getMemoService(memoId, user_id)
        observeMemo()
        clickListener()
        return binding.root
    }

    private fun initUI(memo_title: String, memo_contents: String) = with(binding) {
        modifyMemoNameEt.setText(memo_title)
        modifyMemoEt.setText(memo_contents)
    }

    private fun clickListener() = with(binding) {
        modifyMemoBackBtn.setOnClickListener {
            goBackFragment()
        }

        modifyMemoBtn.setOnClickListener {
            val memoReq = MemoReq(memoId, modifyMemoNameEt.text.toString(), modifyMemoEt.text.toString())
            viewModel.modifyMemoSerivce(memoId, memoReq, user_id)
            Toast.makeText(context, "메모가 수정되었습니다.", Toast.LENGTH_SHORT).show()
            goBackFragment()
        }
    }

    private fun goBackFragment() { parentFragmentManager.popBackStack() }

    private fun getData() {
        // 데이터 읽어오기
        val sharedPreferences1: SharedPreferences = requireContext().getSharedPreferences("memo", Context.MODE_PRIVATE)
        memoId = sharedPreferences1.getInt("memo_id", memoId)

        val sharedPreferences2: SharedPreferences = requireContext().getSharedPreferences("kakao", Context.MODE_PRIVATE)
        user_id = sharedPreferences2.getInt("user_id", user_id)
    }

    private fun observeMemo() {
        viewModel.memoTitleML.observe(viewLifecycleOwner) { memoTitle ->
            viewModel.memoContentsML.observe(viewLifecycleOwner) { memoContents ->
                // 데이터가 변경되었을 때 UI 업데이트
                initUI(memoTitle, memoContents)
            }
        }
    }
}