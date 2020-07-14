package com.vtoken.application.view.activity.create

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.databinding.WalletCreateConfirmBinding
import com.vtoken.application.model.Word
import com.vtoken.application.view.activity.IntroBaseActivity
import com.vtoken.application.viewModel.create.WalletCreateConfirmViewModel
import com.vtoken.application.widget.RadioViewGroup
import java.util.*

class WalletCreateConfirmActivity : IntroBaseActivity() {

    lateinit var confirmModel:WalletCreateConfirmViewModel

    lateinit var dataBinding :WalletCreateConfirmBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding=DataBindingUtil.setContentView<WalletCreateConfirmBinding>(this,R.layout.wallet_create_confirm)
        confirmModel= WalletCreateConfirmViewModel(this)
        dataBinding.setVariable(BR.walletCreateConfirmModel,confirmModel)
        val wordList = ArrayList<Word>()
        val wordStringList = ArrayList<String>()
        wordStringList.addAll(this.confirmModel.getWordStringList())
        Collections.sort(wordStringList)
        for (i in wordStringList.indices) {
            wordList.add(Word(wordStringList.get(i) as String))
        }
        dataBinding.radioViewGroup.setWordList(wordList)
        dataBinding.radioViewGroup.setOnRadioViewGroupSelectListener(object : RadioViewGroup.RadioViewGroupEvent{
            override fun onDeleteWord(i: Int) {

            }

            override fun onNewWordConfirm(i: Int, str: String?) {

            }

            override fun onWordClicked(str: String?, isSelected: Boolean) {
                this@WalletCreateConfirmActivity.confirmModel.checkAndAddToObserverList(str!!,isSelected)
            }

        })
        confirmModel.setListener(object :WalletCreateConfirmViewModel.WalletCreateConfirmEvent{
            override fun onRevokeFinish() {
                val selectedViewList = this@WalletCreateConfirmActivity.dataBinding.radioViewGroup.getSelectedViewList()
                if (selectedViewList != null && selectedViewList.size > 0) {
                    val view = selectedViewList.get(selectedViewList.size - 1) as View
                    view.isSelected = false
                    view.visibility = View.VISIBLE
                    selectedViewList.remove(view)
                    this@WalletCreateConfirmActivity.confirmModel.checkAndAddToObserverList(view.tag as String,false)
                }
            }

        })
        dataBinding.memericWordsArea.viewTreeObserver.addOnGlobalLayoutListener {
            this@WalletCreateConfirmActivity.confirmModel.mo41631a(this@WalletCreateConfirmActivity.dataBinding.memericWordsArea.getWidth()
                    - this@WalletCreateConfirmActivity.dataBinding.memericWordsArea.getPaddingLeft()
                    - this@WalletCreateConfirmActivity.dataBinding.memericWordsArea.getPaddingRight())
        }
    }

    override fun onBackPressed() {
        confirmModel.returnStep()
    }
}
