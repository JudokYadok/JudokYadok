package com.example.nunettine.ui.save.contents

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.nunettine.R
import com.example.nunettine.data.local.NewTextReq
import com.example.nunettine.databinding.FragmentModifyContentsBinding
import com.example.nunettine.ui.save.memo.SaveMemoViewModel

class ModifyContentsFragment(private val text_type: String): Fragment() {
    private lateinit var binding: FragmentModifyContentsBinding
    private lateinit var viewModel: SaveContentsViewModel
    private var textId = 0
    private var user_id = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentModifyContentsBinding.inflate(layoutInflater)
        getData()
        Log.d("TEXTID", textId.toString())
        // Spinner
        val yearSpinner = binding.spinnerType
        val yearAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.type, android.R.layout.simple_spinner_item)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearSpinner.adapter = yearAdapter

        viewModel = ViewModelProvider(this).get(SaveContentsViewModel::class.java)
        viewModel.getTextService(user_id, textId)

        observeContents()
        clickListener()
        return binding.root
    }

    private fun clickListener() = with(binding) {
        addContentsBackBtn.setOnClickListener {
            goBackFragment()
        }

        addContentsBtn.setOnClickListener {
            val type = spinnerType.selectedItem.toString()
            val modifyText = NewTextReq(type, addContentsNameEt.text.toString(), addContentsEt.text.toString())
            viewModel.putTextService(user_id, textId, modifyText)
            Toast.makeText(context, "수정되었습니다.", Toast.LENGTH_SHORT).show()
            super.onDestroy()
        }
    }

    private fun getData() {
        // 데이터 읽어오기
        val sharedPreferences1: SharedPreferences = requireContext().getSharedPreferences("contents", Context.MODE_PRIVATE)
        textId = sharedPreferences1.getInt("contents_id", textId)

        val sharedPreferences2: SharedPreferences = requireContext().getSharedPreferences("kakao", Context.MODE_PRIVATE)
        user_id = sharedPreferences2.getInt("user_id", user_id)
    }

    private fun initUI(text_title: String, text_contents: String) = with(binding) {
        addContentsNameEt.setText(text_title)
        addContentsEt.setText(text_contents)

        // Spinner 초기 값 설정
        val yearSpinner = binding.spinnerType
        val yearAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.type, android.R.layout.simple_spinner_item)
        val position = (yearAdapter as ArrayAdapter<String>).getPosition(text_type)
        yearSpinner.setSelection(position)
    }

    private fun observeContents() {
        viewModel.textTitleML.observe(viewLifecycleOwner) { textTitle ->
            viewModel.textContentsML.observe(viewLifecycleOwner) { textContents ->
                initUI(textTitle, textContents)
            }
        }
    }

    private fun goBackFragment() { parentFragmentManager.popBackStack() }
}
