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
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.nunettine.R
import com.example.nunettine.data.local.UserDday
import com.example.nunettine.data.local.UserReq
import com.example.nunettine.databinding.FragmentSettingMypageBinding
import com.example.nunettine.ui.etc.DdayDialog
import com.example.nunettine.ui.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Locale

class MypageFragment : Fragment() {
    private lateinit var binding: FragmentSettingMypageBinding
    private lateinit var viewModel: MyPageViewModel
    private var user_id: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingMypageBinding.inflate(layoutInflater)
        getData()
        viewModel = ViewModelProvider(this).get(MyPageViewModel::class.java)
        viewModel.getUserInfoService(user_id)
        initUI(viewModel.emailML.value!!, viewModel.nameML.value!!)
        observeUserInfo()
        clickListener()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun clickListener() = with(binding) {
        mypageBackBtn.setOnClickListener { moveFragment(SettingFragment()) }
        mypageModifyBtn.setOnClickListener {
            val dday = UserDday(
                mypageCalendarAddYearEt.text.toString().toInt(),
                mypageCalendarAddMonthEt.text.toString().toInt(),
                mypageCalendarAddDateEt.text.toString().toInt()
            )
            val userReq = UserReq(
                mypageNicknameEt.text.toString(),
                mypageEmailEt.text.toString(),
                dday,
                user_id
            )
            viewModel.putUserInfoService(userReq)
            if (viewModel.mofifyML.value == true) {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.dDayYearML.postValue(mypageCalendarAddYearEt.text.toString().toInt())
                    viewModel.dDayMonthML.postValue(mypageCalendarAddMonthEt.text.toString().toInt())
                    viewModel.dDayDateML.postValue(mypageCalendarAddDateEt.text.toString().toInt())
                }
            }
            Toast.makeText(requireContext(), "정보가 수정되었습니다.", Toast.LENGTH_SHORT).show()
            refreshUI()
        }
        mypageDeleteBtn.setOnClickListener { viewModel.deleteUserInfoService(user_id) }
        mypageCalendarLo.setOnClickListener {
            mypageCalendarAddLo.visibility = View.VISIBLE
            writeDday()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun refreshUI() = with(binding) {
        viewModel.getUserInfoService(user_id)
        initUI(viewModel.emailML.value!!, viewModel.nameML.value!!)
        observeUserInfo()
        mypageCalendarAddLo.visibility = View.GONE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun writeDday() = with(binding) {
        // d-day 날짜 기본 설정
        mypageCalendarAddYearEt.setText(viewModel.dDayYearML.value.toString())
        mypageCalendarAddMonthEt.setText(viewModel.dDayMonthML.value.toString())
        mypageCalendarAddDateEt.setText(viewModel.dDayDateML.value.toString())

        observeUserInfo()
    }

    fun moveFragment(fragment: Fragment) {
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

    private fun initUI(email: String, name: String) = with(binding) {
        mypageNicknameEt.setText(name)
        mypageEmailEt.setText(email)
    }

    private fun initJoinDateUI(join: String) = with(binding) {
        mypageJoinTv.text = timeFormat(join)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initDday(dday: Int, year: Int, month: Int, date: Int) = with(binding) {
        mypageCalendarEt.text = dday.toString()
        mypageCalendarAddYearEt.setText(year.toString())
        mypageCalendarAddMonthEt.setText(month.toString())
        mypageCalendarAddDateEt.setText(date.toString())
    }

    private fun timeFormat(originalTime: String): String {
        // 원본 문자열을 날짜로 파싱합니다.
        val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val originalDate = originalFormat.parse(originalTime)

        // 포맷을 변경하고자 하는 형식을 정의합니다.
        val targetFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())

        // 새로운 형식으로 포맷합니다.
        val formattedDate = targetFormat.format(originalDate)
        return formattedDate
    }

    private fun getData() {
        // 데이터 읽어오기
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences("kakao", Context.MODE_PRIVATE)
        user_id = sharedPreferences.getInt("user_id", user_id)!!
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeUserInfo() {
        viewModel.emailML.observe(viewLifecycleOwner) { email ->
            viewModel.nameML.observe(viewLifecycleOwner) { name ->
                initUI(email, name)
            }
        }

        viewModel.joinDateML.observe(viewLifecycleOwner) { join ->
            if (join.length > 0) {
                initJoinDateUI(join)
            }
        }
        viewModel.dDayYearML.observe(viewLifecycleOwner) { year ->
            viewModel.dDayMonthML.observe(viewLifecycleOwner) { month ->
                viewModel.dDayDateML.observe(viewLifecycleOwner) { date ->
                    viewModel.ddayML.observe(viewLifecycleOwner) { dday ->
                        if (dday != 0 && year != 2000 && month != 0 && date != 0) {
                            initDday(dday, year, month, date)
                        }
                    }
                }
            }
        }
    }
}