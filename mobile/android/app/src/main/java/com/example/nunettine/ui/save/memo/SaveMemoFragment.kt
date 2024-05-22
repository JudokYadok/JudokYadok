package com.example.nunettine.ui.save.memo

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nunettine.data.remote.dto.library.MemoList
import com.example.nunettine.databinding.FragmentMemoListBinding

class SaveMemoFragment: Fragment() {
    private lateinit var binding: FragmentMemoListBinding
    private lateinit var viewModel: SaveMemoViewModel
    private var user_id: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMemoListBinding.inflate(layoutInflater)
        getData()
        viewModel = ViewModelProvider(this).get(SaveMemoViewModel::class.java)
        viewModel.getMemoListService(user_id)
        observeMemoList()
        return binding.root
    }

    private fun initRV(memoList: List<MemoList>) = with(binding) {
        val adapter = fragmentManager?.let { SaveMemoRVAdapter(requireContext(), memoList.toMutableList()) }
        memoListRv.adapter = adapter
        memoListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun observeMemoList() {
        viewModel.memoListML.observe(viewLifecycleOwner, Observer {
            it?.let { memoList ->
                initRV(memoList)
            }
        })
    }

    private fun getData() {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("kakao", Context.MODE_PRIVATE)
        user_id = sharedPreferences.getInt("user_id", user_id)
    }
}