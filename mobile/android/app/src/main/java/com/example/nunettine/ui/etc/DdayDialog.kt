package com.example.nunettine.ui.etc

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import androidx.lifecycle.ViewModelProvider
import com.example.nunettine.databinding.DialogDdayBinding
import com.example.nunettine.ui.setting.MyPageViewModel

class DdayDialog(private val context: Context, private var year: Int, private var month: Int, private var date: Int): Dialog(context) {
    private lateinit var binding: DialogDdayBinding
    private var onDdaySelectedListener: OnDdaySelectedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogDdayBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE) // 타이틀바 제거
        setCanceledOnTouchOutside(true)
        setContentView(binding.root)
        setupDday()
        setupViews()
    }

    private fun setupDday() = with(binding) {
        dialogDdayYearEt.setText(year)
        dialogDdayMonthEt.setText(month)
        dialogDdayDateEt.setText(date)
    }

    private fun setupViews() {
        binding.dialogDdayYesBtn.setOnClickListener {
            val year = binding.dialogDdayYearEt.text.toString().toInt()
            val month = binding.dialogDdayMonthEt.text.toString().toInt()
            val date = binding.dialogDdayDateEt.text.toString().toInt()
            onDdaySelectedListener?.onDdaySelected(year, month, date)
            dismiss()
        }

        binding.dialogDdayNoBtn.setOnClickListener {
            dismiss()
        }
    }

    fun setOnDdaySelectedListener(listener: OnDdaySelectedListener) {
        onDdaySelectedListener = listener
    }

    interface OnDdaySelectedListener {
        fun onDdaySelected(year: Int, month: Int, date: Int)
    }
}