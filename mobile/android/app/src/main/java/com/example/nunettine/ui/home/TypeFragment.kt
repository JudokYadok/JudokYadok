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
import com.example.nunettine.databinding.FragmentTypeBinding
import com.example.nunettine.ui.main.MainActivity

class TypeFragment: Fragment() {
    private lateinit var binding: FragmentTypeBinding
    private lateinit var viewModel: HomeViewModel
    private var user_id = 0
    private var type = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTypeBinding.inflate(layoutInflater)
        getData()
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        if(type == "PREVTEXT") {
            viewModel.setPrevTypeService()
            observeCategoryList()
        } else {
            viewModel.setMyTypeService(user_id)
            observeCategoryList()
        }

        clickListener()
        return binding.root
    }

    private fun initRV(categoryList: List<String>) = with(binding){
        val typeRVAdapter = TypeRVAdapter(categoryList, requireContext(), type)
        // RecyclerView 어댑터 설정
        typeRv.layoutManager = LinearLayoutManager(requireContext())
        // RecyclerView 레이아웃 매니저 설정
        typeRv.adapter = typeRVAdapter
    }

    private fun clickListener() = with(binding) {
        typeBackBtn.setOnClickListener { moveFragment(HomeFragment()) }
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

        val sharedPreferences2: SharedPreferences = requireContext().getSharedPreferences("kakao", Context.MODE_PRIVATE)
        user_id = sharedPreferences2.getInt("user_id", user_id)
    }

    private fun observeCategoryList() {
        viewModel.categoryListML.observe(viewLifecycleOwner, Observer {
            it?.let { categoryList ->
                initRV(categoryList)
            }
        })
    }
}