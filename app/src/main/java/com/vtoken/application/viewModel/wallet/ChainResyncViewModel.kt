package com.vtoken.application.viewModel.wallet

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import android.os.Handler
import android.os.Message
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.text.TextUtils
import com.vtoken.application.ApplicationLoader
import com.vtoken.application.viewModel.BaseViewModel
import org.apache.commons.cli.HelpFormatter
import vdsMain.BLOCK_CHAIN_TYPE
import vdsMain.wallet.Wallet
import java.text.SimpleDateFormat
import java.util.*

class ChainResyncViewModel(context: Context):BaseViewModel(context) {

    //f11055A
    var startBlockNo = ObservableField("0")

    //f11056B
    var initFinish = ObservableBoolean()

    var f11065x = ObservableBoolean()
    //f11067z
    var dateString = ObservableField<String>()

    //f11057C
    var resyncType = ObservableInt()

    //f11058D
    var day = ObservableInt()

    //f11059E
    var month = ObservableInt()

    //f11060F
    var year = ObservableInt()

    //f11061G
    @SuppressLint("SimpleDateFormat")
    internal var dateFormat = SimpleDateFormat("yyyy-MM-dd")

    //f11062H
    var curTimeMillis: Long = 0

    internal var f11063I = true

    var f11066y = ObservableField<String>()

    //f11064J
    lateinit var wallet: Wallet

    init {
        this.f11065x.set(intent.getBooleanExtra("is_vds", true))
        if (this.f11065x.get()) {
            this.wallet = this.vCashCore.getWalletByChainType(BLOCK_CHAIN_TYPE.VCASH)
        }
        this.mainHandler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(message: Message) {
                super.handleMessage(message)
                if (System.currentTimeMillis() - this@ChainResyncViewModel.curTimeMillis > 400 && !TextUtils.isEmpty(this@ChainResyncViewModel.startBlockNo.get() as CharSequence)) {
                    try {
                        val timeStamp = this@ChainResyncViewModel.wallet.getTimeStampByBlockNo(java.lang.Long.valueOf(this@ChainResyncViewModel.startBlockNo.get() as String).toLong())
                        if (timeStamp != 0L) {
                            this@ChainResyncViewModel.dateString.set(this@ChainResyncViewModel.dateFormat.format(Date(timeStamp * 1000)))
                        } else {
                            this@ChainResyncViewModel.dateString.set("")
                        }
                    } catch (unused: Exception) {
                    }
                }
            }
        }
        this.startBlockNo.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {

            //f11069a
            var inputBlockNo: String? = null

            override fun onPropertyChanged(observable: Observable, i: Int) {
                this@ChainResyncViewModel.setNeedInit()
                val blockNo = this.inputBlockNo
                if (blockNo == null || blockNo != (observable as ObservableField<*>).get()) {
                    this.inputBlockNo = (observable as ObservableField<*>).get() as String?
                    if (!this@ChainResyncViewModel.f11063I) {
                        this@ChainResyncViewModel.f11063I = true
                        return
                    }
                    this@ChainResyncViewModel.curTimeMillis = System.currentTimeMillis()
                    if (!TextUtils.isEmpty(this@ChainResyncViewModel.startBlockNo.get() as CharSequence)) {
                        try {
                            if (java.lang.Long.valueOf(this@ChainResyncViewModel.startBlockNo.get() as String).toLong() > this@ChainResyncViewModel.wallet.getCurBlockNo()) {
                                val curBlockNo = this@ChainResyncViewModel.wallet.getCurBlockNo()
                                val observableField = this@ChainResyncViewModel.startBlockNo
                                val sb = StringBuilder()
                                sb.append("")
                                sb.append(curBlockNo)
                                observableField.set(sb.toString())
                            } else {
                                this@ChainResyncViewModel.dateString.set("")
                            }
                        } catch (unused: Exception) {
                            this@ChainResyncViewModel.startBlockNo.set("0")
                            this@ChainResyncViewModel.dateString.set("")
                        }
                    } else {
                        this@ChainResyncViewModel.dateString.set("")
                    }
                    this@ChainResyncViewModel.mainHandler.sendEmptyMessageDelayed(0, 450)
                }
            }
        })
        this.startBlockNo.set("1")
    }

    //mo39924a
        //交易数据重载
    fun confirmResyncChain() {
        val instance = Calendar.getInstance()
        var startBlockNo: Long = 1
        when (this.resyncType.get()) {
            1 -> {
                instance.add(Calendar.YEAR, -1)
                startBlockNo = this.wallet.getSelfBlockChainModel().getFirstBlockHeaderAfterTime(instance.timeInMillis / 1000).toLong()
            }
            2 -> {
                instance.add(Calendar.MONTH, -1)
                startBlockNo = this.wallet.getSelfBlockChainModel().getFirstBlockHeaderAfterTime(instance.timeInMillis / 1000).toLong()
            }
            3 -> if (!TextUtils.isEmpty(this.startBlockNo.get() as CharSequence)) {
                startBlockNo = java.lang.Long.valueOf(this.startBlockNo.get() as String).toLong()
            } else {
                return
            }
        }
        showLoadingFragment(getStringRescourcesByResName("setting_reload_trans_data"))
        if (this.f11065x.get()) {
            this.vCashCore.startThreadToResyncBlockChain(BLOCK_CHAIN_TYPE.VCASH, startBlockNo, true)
        } else {
            this.vCashCore.startThreadToResyncBlockChain(BLOCK_CHAIN_TYPE.BITCOIN, startBlockNo, true)
        }
        LocalBroadcastManager.getInstance(ApplicationLoader.getSingleApplicationContext()).sendBroadcast(Intent("errorBar").putExtra("msg","区块正在重载，重载过程中请勿关闭应用。"))
        this.mainHandler.postDelayed(object:Runnable{
            override fun run() {
                this@ChainResyncViewModel.successFinish()
            }
        }, 1000)
    }

    //m8890Q
    fun successFinish() {
        dismissLoadingDialog()
        activity.setResult(-1)
        finish()
    }

    //mo39911N
    fun initDateVlaue() {
        if (!this.initFinish.get()) {
            if (TextUtils.isEmpty(this.dateString.get() as CharSequence)) {
                val calendar = Calendar.getInstance()
                this.day.set(calendar.get(Calendar.DATE))
                this.month.set(calendar.get(Calendar.MONTH) + 1)
                this.year.set(calendar.get(Calendar.YEAR))
            } else {
                val split = (this.dateString.get()!!).split(HelpFormatter.DEFAULT_OPT_PREFIX)
                this.month.set(Integer.valueOf(split[0]).toInt())
                this.day.set(Integer.valueOf(split[1]).toInt())
                this.year.set(Integer.valueOf(split[2]).toInt())
            }
            this.initFinish.set(true)
        }
    }

    //mo42044O
    fun setNeedInit() {
        this.initFinish.set(false)
    }

    //mo42045P
    fun confirmDatePick() {
//        this.dateString.set(
//            String.format(
//                Locale.US,
//                "%02d-%02d-%d",
//                *arrayOf<Any>(
//                    Integer.valueOf(this.month.get()),
//                    Integer.valueOf(this.day.get()),
//                    Integer.valueOf(this.year.get())
//                )
//            )
//        )
        this.dateString.set(
            String.format(
                Locale.US,
                "%d-%02d-%02d",
                *arrayOf<Any>(
                    Integer.valueOf(this.year.get()),
                    Integer.valueOf(this.month.get()),
                    Integer.valueOf(this.day.get())
                )
            )
        )
        val instance = Calendar.getInstance()
        instance.set(this.year.get(), this.month.get() - 1, this.day.get(), 0, 0, 0)
        this.f11063I = false
        val startBlockNo = this.startBlockNo
        val sb = StringBuilder()
        sb.append("")
        sb.append(this.wallet.getSelfBlockChainModel().getFirstBlockHeaderAfterTime(instance.timeInMillis / 1000))
        startBlockNo.set(sb.toString())
        this.initFinish.set(false)
    }

    //mo42047a
    fun setDateValue(day: Int, month: Int, year: Int) {
        this.day.set(day)
        this.month.set(month)
        this.year.set(year)
    }

    //mo42046a
    fun setResyncType(i: Int) {
        this.resyncType.set(i)
    }
}