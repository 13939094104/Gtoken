package com.vtoken.application.view.activity.wallet

import android.content.Context
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.databinding.ActivityChainResyncBinding
import com.vtoken.application.view.activity.BaseActivity
import com.vtoken.application.viewModel.wallet.ChainResyncViewModel
import com.vtoken.application.widget.DatePicker

class ChainResyncActivity : BaseActivity() {

    lateinit var viewModel:ChainResyncViewModel

    lateinit var dataBinding:ActivityChainResyncBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding=DataBindingUtil.setContentView(this, R.layout.activity_chain_resync)
        viewModel=ChainResyncViewModel(this)
        dataBinding.setVariable(BR.chainResyncModel,viewModel)
        this.dataBinding.timeEditView.setInputType(0)
        this.dataBinding.timeEditView.setOnFocusChangeListener(View.OnFocusChangeListener { view, z ->
            this@ChainResyncActivity.onTimeEditViewFocusChange(view, z)
        })
        this.dataBinding.timeEditView.setOnClickListener(View.OnClickListener { view -> this@ChainResyncActivity.initDateValue(view) })
        this.dataBinding.datePickerView.setListener(object : DatePicker.DateChangeEvent {
            override fun onDateChanged(day: Int, month: Int, year: Int) {
                this@ChainResyncActivity.setDateValue(day, month, year)
            }
        })
        this.viewModel.initDateVlaue()
    }

    //m4103a
    fun onTimeEditViewFocusChange(view: View, isFocus: Boolean) {
        if (isFocus) {
            hideSoftInputWindow()
            this.dataBinding.timeEditView.setBackground(getDrawable(R.drawable.white_yellow_radius_3))
            this.viewModel.initDateVlaue()
            return
        }
        this.dataBinding.timeEditView.setBackground(getDrawable(R.drawable.white_radius_3))
        this.viewModel.setNeedInit()
    }

    //m4102a
    fun initDateValue(view: View) {
        this.viewModel.initDateVlaue()
    }

    //m4101a
    fun setDateValue(day: Int, month: Int, year: Int) {
        this.viewModel.setDateValue(day, month, year)
    }

    //mo39838a
    fun hideSoftInputWindow() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val peekDecorView = window.peekDecorView()
        if (peekDecorView != null) {
            inputMethodManager.hideSoftInputFromWindow(peekDecorView.windowToken, 0)
        }
    }
}
