package com.example.nunettine.ui.setting

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.nunettine.R
import com.example.nunettine.data.local.FeedbackReq
import com.example.nunettine.data.remote.dto.BasicRes2
import com.example.nunettine.data.remote.service.setting.UserService
import com.example.nunettine.data.remote.view.setting.FeedbackView
import com.example.nunettine.databinding.FragmentSettingFeedbackBinding
import com.example.nunettine.ui.main.MainActivity

class FeedbackFragment: Fragment(), FeedbackView {
    private lateinit var binding: FragmentSettingFeedbackBinding
    private var user_id: Int = 0
    private var accessToken = ""
    private var refreshToken = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingFeedbackBinding.inflate(layoutInflater)
        getData()
        clickListener()
        return binding.root
    }

    private fun clickListener() = with(binding) {
        feedbackBackBtn.setOnClickListener { moveFragment(SettingFragment()) }
        feedbackWriteYesBtn.setOnClickListener {
            setFeedbackService()
            moveFragment(SettingFragment())
        }
        feedbackWriteNoBtn.setOnClickListener { onBackPressedCallback }
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

    private val onBackPressedCallback = object  : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.main_frm, SettingFragment())
                .commitAllowingStateLoss()
        }
    }

    private fun getData() {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("kakao", Context.MODE_PRIVATE)
        user_id = sharedPreferences.getInt("user_id", user_id)!!
    }

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    private fun saveData() {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("kakao", AppCompatActivity.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("getAccessToken", accessToken)
        editor.putString("getRefreshToken", refreshToken)
        editor.putInt("user_id", user_id!!)
        editor.apply()
    }

    private fun setFeedbackService() = with(binding) {
        val setFeedbackService = UserService()
        val feedback = FeedbackReq(feedbackWriteEt.text.toString())
        setFeedbackService.setFeedbackView(this@FeedbackFragment)
        setFeedbackService.setFeedback(user_id, feedback)
    }

    override fun onGetFeedbackSuccess(response: BasicRes2) {
        Toast.makeText(context, "피드백이 전달되었습니다.", Toast.LENGTH_SHORT).show()
        Log.d("NEW-SETTING-FEEDBACK-성공", response.toString())
    }

    override fun onGetFeedbackFailure(result_code: Int) {
        Log.d("NEW-SETTING-FEEDBACK-오류", result_code.toString())
    }
}