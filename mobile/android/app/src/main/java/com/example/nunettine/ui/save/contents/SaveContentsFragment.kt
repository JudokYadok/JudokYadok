package com.example.nunettine.ui.save.contents

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nunettine.R
import com.example.nunettine.data.remote.dto.library.MemoList
import com.example.nunettine.data.remote.dto.library.TextList
import com.example.nunettine.databinding.FragmentContentsListBinding
import com.example.nunettine.ui.main.MainActivity
import com.example.nunettine.ui.save.memo.SaveMemoRVAdapter
import com.example.nunettine.ui.save.memo.SaveMemoViewModel

class SaveContentsFragment : Fragment() {
    private lateinit var binding: FragmentContentsListBinding
    private lateinit var viewModel: SaveContentsViewModel
    private var user_id: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentContentsListBinding.inflate(layoutInflater)
        getData()
        viewModel = ViewModelProvider(this).get(SaveContentsViewModel::class.java)
        viewModel.getTextListService(user_id)
        observeMemoList()
        clickListener()
        return binding.root
    }

    private fun clickListener() = with(binding) {
        contentsListBtn.setOnClickListener { moveFragment(AddContentsFragment()) }
    }

    private fun initRV(textList: List<TextList>) = with(binding) {
        val adapter = fragmentManager?.let { SaveContentsRVAdapter(requireContext(), textList.toMutableList()) }
        contentsListRv.adapter = adapter
        contentsListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun observeMemoList() {
        viewModel.textListML.observe(viewLifecycleOwner, Observer {
            it?.let { textList ->
                initRV(textList)
            }
        })
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
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("kakao", Context.MODE_PRIVATE)
        user_id = sharedPreferences.getInt("user_id", user_id)
    }
}