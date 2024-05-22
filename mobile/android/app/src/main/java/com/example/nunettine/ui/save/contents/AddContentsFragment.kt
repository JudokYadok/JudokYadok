package com.example.nunettine.ui.save.contents

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.nunettine.R
import com.example.nunettine.data.local.NewTextReq
import com.example.nunettine.databinding.FragmentAddContentsBinding
import com.example.nunettine.ui.save.SaveFragment

class AddContentsFragment: Fragment() {
    private lateinit var binding: FragmentAddContentsBinding
    private lateinit var viewModel: SaveContentsViewModel
    private var user_id = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddContentsBinding.inflate(layoutInflater)
        getData()

        val yearSpinner = binding.spinnerType
        val yearAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.type, android.R.layout.simple_spinner_item)

        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearSpinner.adapter = yearAdapter

        viewModel = ViewModelProvider(this).get(SaveContentsViewModel::class.java)
        clickListener()
        return binding.root
    }

    private fun clickListener() = with(binding) {
        addContentsBackBtn.setOnClickListener {
            goBackFragment()
        }

        addContentsBtn.setOnClickListener {
            val type = spinnerType.selectedItem.toString()
            val newText = NewTextReq(type, addContentsNameEt.text.toString(), addContentsEt.text.toString())
            viewModel.setTextService(user_id, newText)
            Toast.makeText(context, "지문이 저장되었습니다.", Toast.LENGTH_SHORT).show()
            super.onDestroy()
        }
    }

    private fun goBackFragment() { parentFragmentManager.popBackStack() }

    private fun getData() {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("kakao", Context.MODE_PRIVATE)
        user_id = sharedPreferences.getInt("user_id", user_id)
    }
}