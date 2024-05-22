package com.example.nunettine.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nunettine.R
import com.example.nunettine.data.remote.dto.study.TextList
import com.example.nunettine.databinding.FragmentChooseBinding
import com.example.nunettine.ui.main.MainActivity

class ChooseFragment: Fragment() {
    private lateinit var binding: FragmentChooseBinding
    private lateinit var viewModel: HomeViewModel
    private var type = ""
    private var category = ""
    private var user_id = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChooseBinding.inflate(layoutInflater)
        getData()
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        if(type == "PREVTEXT") {
            viewModel.setPrevChooseService(category)
            observeTextList()
        } else {
            viewModel.setMyChooseService(user_id, category)
            observeTextList()
        }

        clickListner()
        return binding.root
    }

    private fun initRV(text_list: List<TextList>) = with(binding){
        val chooseRVAdapter = ChooseRVAdapter(text_list, requireContext(), type, category)
        // RecyclerView 어댑터 설정
        chooseRv.layoutManager = LinearLayoutManager(requireContext())
        // RecyclerView 레이아웃 매니저 설정
        chooseRv.adapter = chooseRVAdapter
    }

    private fun clickListner() = with(binding) {
        chooseBackBtn.setOnClickListener { moveFragment(TypeFragment()) }
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

        val sharedPreferences2: SharedPreferences = requireContext().getSharedPreferences("kakao", Context.MODE_PRIVATE)
        user_id = sharedPreferences2.getInt("user_id", user_id)
    }

    private fun observeTextList() {
        viewModel.textListML.observe(viewLifecycleOwner, Observer {
            it?.let { textList ->
                initRV(textList)
            }
        })
    }
}