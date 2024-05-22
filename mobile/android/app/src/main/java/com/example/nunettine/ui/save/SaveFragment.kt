package com.example.nunettine.ui.save

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nunettine.databinding.FragmentSaveBinding
import com.google.android.material.tabs.TabLayoutMediator

class SaveFragment: Fragment() {
    private lateinit var binding: FragmentSaveBinding
    private val information = arrayListOf("지문", "메모", "문제")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSaveBinding.inflate(layoutInflater)

        var saveAdapter = SaveVPAdapter(this@SaveFragment)
        binding.saveVp.adapter = saveAdapter

        // TabLayout을 ViewPager2와 연결해주는 중재자
        TabLayoutMediator(binding.saveTlo, binding.saveVp) { tab, position ->
            tab.text = information[position]
        }.attach()

        return binding.root
    }
}