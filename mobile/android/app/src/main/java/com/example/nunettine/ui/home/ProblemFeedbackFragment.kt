package com.example.nunettine.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.nunettine.R
import com.example.nunettine.data.local.FeedbackReq
import com.example.nunettine.databinding.FragmentSettingFeedbackBinding
import com.example.nunettine.ui.main.MainActivity

class ProblemFeedbackFragment(private val text_title: String): Fragment() {
    private lateinit var binding: FragmentSettingFeedbackBinding
    private lateinit var viewModel: HomeViewModel
    private var user_id = 0
    private var quiz_id = 0 // spf 저장 필요

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingFeedbackBinding.inflate(layoutInflater)
        getData()
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding.feedbackTv.setText(text_title)
        clickListener()
        return binding.root
    }

    private fun clickListener() = with(binding) {
        feedbackBackBtn.setOnClickListener { goBackFragment() }
        feedbackWriteYesBtn.setOnClickListener {
            val feedbackContents = FeedbackReq(feedbackWriteEt.text.toString())
            viewModel.setStudyFeedbackService(user_id, quiz_id, feedbackContents)
            Toast.makeText(context, "피드백이 전달되었습니다.", Toast.LENGTH_SHORT).show()
        }
        feedbackWriteNoBtn.setOnClickListener { goBackFragment() }
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

        val sharedPreferences2: SharedPreferences = requireContext().getSharedPreferences("quiz_id", Context.MODE_PRIVATE)
        quiz_id = sharedPreferences2.getInt("quiz_id", quiz_id)
    }
}