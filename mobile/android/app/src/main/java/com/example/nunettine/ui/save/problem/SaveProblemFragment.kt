package com.example.nunettine.ui.save.problem

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
import com.example.nunettine.data.remote.dto.library.QuizSaveList
import com.example.nunettine.databinding.FragmentProblemListBinding

class SaveProblemFragment: Fragment() {
    private lateinit var binding: FragmentProblemListBinding
    private lateinit var viewModel: SaveProblemViewModel
    private var user_id: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProblemListBinding.inflate(layoutInflater)
        getData()
        viewModel = ViewModelProvider(this).get(SaveProblemViewModel::class.java)
        viewModel.getQuizList(user_id)
        observeMemoList()
        return binding.root
    }

    private fun initRV(quiz_list: List<QuizSaveList>) = with(binding) {
        val adapter = SaveProblemRVAdapter(requireContext(), quiz_list.toMutableList())
        problemListRv.adapter = adapter
        problemListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun observeMemoList() {
        viewModel.quizListML.observe(viewLifecycleOwner, Observer {
            it?.let { quizList ->
                initRV(quizList)
            }
        })
    }

    private fun getData() {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("kakao", Context.MODE_PRIVATE)
        user_id = sharedPreferences.getInt("user_id", user_id)
    }
}