package com.example.nunettine.ui.save

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.nunettine.ui.save.contents.SaveContentsFragment
import com.example.nunettine.ui.save.memo.SaveMemoFragment
import com.example.nunettine.ui.save.problem.SaveProblemFragment

class SaveVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int):Fragment {
        return when(position) {
            0 -> SaveContentsFragment()
            1 -> SaveMemoFragment()
            else -> SaveProblemFragment()
        }
    }
}