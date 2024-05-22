package com.example.nunettine.ui.setting

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.nunettine.R
import com.example.nunettine.databinding.FragmentSettingBinding
import com.example.nunettine.ui.main.LoginActivity
import com.kakao.sdk.user.UserApiClient

class SettingFragment: Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private var accessToken = ""
    private var refreshToken = ""
    private var user_id = 0

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingBinding.inflate(layoutInflater)
        clickListener()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    private fun clickListener() = with(binding) {
        settingMypageLo.setOnClickListener { moveFragment(MypageFragment()) }
        settingInfoLo.setOnClickListener { moveFragment(InfoFragment()) }
        settingFeedbackLo.setOnClickListener { moveFragment(FeedbackFragment()) }
        settingLogoutLo.setOnClickListener { logout() }
    }

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    private fun logout() {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e("KAKAO-LOGOUT-실패", "로그아웃 실패. SDK에서 토큰 삭제됨", error)
            } else {
                saveData()
                Log.i("KAKAO-LOGOUT-성공", "로그아웃 성공. SDK에서 토큰 삭제됨")
                Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
                navigateToFirstScreen()
            }
        }
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

    private fun navigateToFirstScreen() {
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun moveFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .replace(R.id.main_frm, fragment)
            .addToBackStack(null)
            .commit()
    }
}