package com.example.nunettine.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.nunettine.R
import com.example.nunettine.data.local.NewMemoReq
import com.example.nunettine.data.remote.dto.BasicRes
import com.example.nunettine.data.remote.service.library_study.MemoService
import com.example.nunettine.data.remote.view.library.MemoNewView
import com.example.nunettine.databinding.FragmentMemoBinding
import com.example.nunettine.ui.main.MainActivity
import com.example.nunettine.ui.save.memo.SaveMemoViewModel

class MemoFragment: Fragment() {
    private lateinit var binding: FragmentMemoBinding
    private lateinit var viewModel: SaveMemoViewModel
    private var user_id = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMemoBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(SaveMemoViewModel::class.java)
        getData()
        clickListener()
        return binding.root
    }

    private fun clickListener() = with(binding) {
        addMemoBackBtn.setOnClickListener { goBackFragment() }
        addMemoBtn.setOnClickListener {
            val newMemo = NewMemoReq(addMemoNameEt.text.toString(), addMemoEt.text.toString())
            Log.d("NEWMEMO", "${user_id}, ${newMemo.title}, ${newMemo.contents}")
            viewModel.setMemoService(user_id, newMemo)
            goBackFragment()
        }
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

    private fun goBackFragment() {
        parentFragmentManager.popBackStack()
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .commit()
    }

    private fun getData() {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("kakao", Context.MODE_PRIVATE)
        user_id = sharedPreferences.getInt("user_id", user_id)
    }
}