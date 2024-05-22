package com.example.nunettine.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.nunettine.R
import com.example.nunettine.databinding.ItemTypeBinding
import com.example.nunettine.ui.main.MainActivity

class TypeRVAdapter(private val categoryList: List<String>, private val context: Context, private val type: String): RecyclerView.Adapter<TypeRVAdapter.ViewHolder>() {
    private lateinit var binding: ItemTypeBinding

    inner class ViewHolder(val binding: ItemTypeBinding) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
        fun bind(category: String) = with(binding) {
            typeItemTv.text = category

            typeItemLo.setOnClickListener {
                saveData(type, category)
                moveFragment(ChooseFragment())
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemTypeBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = categoryList.size

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(categoryList[position])

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

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    fun saveData(text_type: String, category: String) {
        // 데이터 저장
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("type", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("type", text_type)
        editor.putString("category", category)
        editor.apply()
    }
}