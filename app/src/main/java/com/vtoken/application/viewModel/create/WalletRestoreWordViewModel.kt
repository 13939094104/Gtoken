package com.vtoken.application.viewModel.create

import android.content.Context
import android.content.Intent
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import com.vtoken.application.view.activity.create.WalletRestorePwdActivity
import com.vtoken.application.view.activity.create.WalletRestoreWordActivity
import com.vtoken.application.viewModel.BaseViewModel
import java.util.*

class WalletRestoreWordViewModel(context: Context) :BaseViewModel(context) {

    companion object{
        //f9930I
        private val maxWordsLength = Integer.valueOf(12)
    }

    var f9936F: TextWatcher

    //f9931A
    var progressHintString = ObservableField<String>()

    //f9940x
    //915 f10462x
    var countWordHintString = ObservableField<String>()

    //f9941y
    var operateHintString = ObservableField<String>()

    //f9942z
    var wordInputString = ObservableField<String>()

    //f9933C
    var canInput = ObservableBoolean(true)

    //f9932B
    var isFinish = ObservableBoolean()

    //f10456D
    var f9934D = ObservableInt(4)

    //f9938H
    //f10460H
    var needClear = false

    //f9939J
    //915 f10461J
    private var wordSize = 0

    //f9937G
    //915 f10459G
    var wordsList = ArrayList<String>()

    init {

        this.f9936F = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}

            override fun afterTextChanged(editable: Editable) {
                if (!this@WalletRestoreWordViewModel.canInput.get()) {
                    return
                }
                if (editable.length > 0) {
                    this@WalletRestoreWordViewModel.isFinish.set(true)
                } else {
                    this@WalletRestoreWordViewModel.isFinish.set(false)
                }
            }
        }
    }

    fun revokeOne(){
        (getContext() as WalletRestoreWordActivity).revokeOne()
    }

    //mo39924a
    fun checkAndAddWords() {
        if (this.canInput.get()) {
            if (this.needClear) {
                this.wordsList.clear()
                (getContext() as WalletRestoreWordActivity).clearWords()
                this.needClear = false
            }
            if(isStringEmpty(this.wordInputString.get())){
                showToast(getStringRescourcesByResName("input_word_tip"))
                return
            }
            this.wordsList.add(this.wordInputString.get()!!)
            (getContext() as WalletRestoreWordActivity).addNewWord(this.wordInputString.get()!!)
            this.wordSize = this.wordsList.size
            this.countWordHintString.set(getNextCountWordHintString())
            this.wordInputString.set("")
            this.f9934D.set(0)
            return
        }
        createWalletByWords(true)
    }

    //m7657b
    private fun createWalletByWords(z: Boolean) {
        val arrayList = ArrayList<String>(this.wordsList.size)
        val it = this.wordsList.iterator()
        while (it.hasNext()) {
            arrayList.add((it.next() as String).toLowerCase())
        }
        try {
            if (!this.vCashCore.checkWords(arrayList.toTypedArray() as Array<String>)) {
                showToast(getStringRescourcesByResName("hd_seed_words_error"))
                return
            }
            val intent = Intent(this.context, WalletRestorePwdActivity::class.java)
            intent.putStringArrayListExtra("wordList", arrayList)
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            showToast(getStringRescourcesByResName("hd_seed_words_error"))
        }

    }

    //mo39929b
    //915 mo40053b
    fun getNextCountWordHintString(): String {
        this.progressHintString.set(String.format("%s/12", *arrayOf<Any>(Integer.valueOf(this.wordSize))))
        if (this.wordSize > maxWordsLength.toInt() - 1) {
            this.operateHintString.set(getStringRescourcesByResName("recovery_finish"))
            this.canInput.set(false)
            this.isFinish.set(true)
            return ""
        }
        this.operateHintString.set(getStringRescourcesByResName("recovery_next"))
        this.canInput.set(true)
        this.isFinish.set(false)
        val sb = StringBuilder()
        sb.append(this.wordSize + 1)
        sb.append("")
        return getStringRescourcesByResName("hint_recovery_count_word").replace("#", sb.toString())
    }

    //mo41656a
    fun removeWord(i: Int) {
        this.wordsList.removeAt(i)
        this.wordSize--
        this.progressHintString.set(String.format("%s/12", *arrayOf<Any>(Integer.valueOf(i))))
        this.countWordHintString.set(getNextCountWordHintString())
        if (this.wordSize > 0) {
            this.f9934D.set(0)
        } else {
            this.f9934D.set(4)
        }
    }


    //915 mo41895a
    fun getWordListFromString(str: String) {
        if (!TextUtils.isEmpty(str)) {
            this.needClear = true
            val asList = Arrays.asList(
                *str.replace("\r|\n".toRegex(), "").replace(
                    " +".toRegex(),
                    " "
                ).split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            )
            this.wordsList.clear()
            (getContext() as WalletRestoreWordActivity).clearWords()
            for (str2 in asList) {
                this.wordsList.add(str2)
                (getContext() as WalletRestoreWordActivity).addNewWord(str2)
            }
            this.wordSize = this.wordsList.size
            this.countWordHintString.set(getNextCountWordHintString())
            this.f9934D.set(0)
        }
    }

    //mo41657a
    fun replaceWordStr(i: Int, str: String) {
        this.wordsList.removeAt(i)
        this.wordsList.add(i, str)
    }
}