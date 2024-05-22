package com.example.nunettine.ui.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.nunettine.CircleTransform
import com.example.nunettine.R
import com.example.nunettine.databinding.FragmentHomeIntroBinding
import com.squareup.picasso.Picasso

class HomeIntroFragment: Fragment() {
    private lateinit var binding: FragmentHomeIntroBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeIntroBinding.inflate(layoutInflater)
        clickListener()

        Handler().postDelayed({
            moveFragment(HomeFragment())
        }, 900)

        return binding.root
    }

    private fun clickListener() = with(binding) {
    }
    private fun moveFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .replace(R.id.main_frm, fragment)
            .addToBackStack(null)
            .commit()
    }
}