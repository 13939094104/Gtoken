package com.vtoken.application.view.activity.create

import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.google.zxing.integration.android.IntentIntegrator
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.databinding.WalletRestoreWordsBinding
import com.vtoken.application.view.activity.IntroBaseActivity
import com.vtoken.application.viewModel.create.WalletRestoreWordViewModel
import com.vtoken.application.widget.RadioViewGroup

class WalletRestoreWordActivity : IntroBaseActivity() {

    lateinit var dataBinding:WalletRestoreWordsBinding

    lateinit var wordModel:WalletRestoreWordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding=DataBindingUtil.setContentView(this,R.layout.wallet_restore_words)
        wordModel= WalletRestoreWordViewModel(this)
        dataBinding.setVariable(BR.walletRestoreWordModel,wordModel)
        this.dataBinding.radioViewGroup.setOnRadioViewGroupSelectListener(object : RadioViewGroup.RadioViewGroupEvent {
            override fun onDeleteWord(i: Int) {
                this@WalletRestoreWordActivity.wordModel.removeWord(i)
            }

            override fun onNewWordConfirm(i: Int, str: String?) {
                this@WalletRestoreWordActivity.wordModel.replaceWordStr(i, str!!)
            }

            override fun onWordClicked(str: String?, isSelected: Boolean) {
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val parseActivityResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (parseActivityResult == null) {
            super.onActivityResult(requestCode, resultCode, data)
        } else if (parseActivityResult.contents != null) {
            this.wordModel.getWordListFromString(parseActivityResult.contents)
        }
    }

    //mo39735a
    fun addNewWord(str: String) {
        this.dataBinding.radioViewGroup.addNewWord(str)
    }

    fun revokeOne(){
        this.dataBinding.radioViewGroup.revokeOne()
    }

    //mo39733a
    fun clearWords() {
        this.dataBinding.radioViewGroup.clearWords()
    }
}
