package com.vtoken.application.viewModel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Process
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.vtoken.vdsecology.vcash.InitInfo
import com.vtoken.application.ApplicationLoader
import com.vtoken.application.service.SyncService
import com.vtoken.application.util.PartLoader
import com.vtoken.application.view.activity.create.WalletCreateFirstActivity
import vdsMain.ActivityManager
import vdsMain.BLOCK_CHAIN_TYPE
import vdsMain.Constants
import vdsMain.observer.CoreEventObserver
import vdsMain.tool.DeviceUtil
import vdsMain.tool.PeerUtils
import vdsMain.tool.SharedPreferencesUtil
import vdsMain.wallet.WalletType

class LoadDataViewModel(context: Context):BaseViewModel(context) {

    lateinit var f10157x: CoreEventObserver

    init {
        LoadPath(intent.getStringExtra("path"))
    }

    //m7675a
    private fun LoadPath(path: String) {
        val sb = StringBuilder()
        sb.append("load: ")
        sb.append(path)
        Log.i("LoadDataViewModel", sb.toString())
        this.f10157x = object : CoreEventObserver {
            /* renamed from: a */
            override fun mo39596a() {}

            /* renamed from: a */
            override fun mo39598a(jfVar: WalletType) {}

            /* renamed from: a */
            override fun onResynched(z: Boolean) {}

            /* renamed from: b */
            override fun mo39600b() {}

            /* renamed from: a */
            override fun handleInitInfoEnum(initInfo: InitInfo) {
                when (initInfo.initInfo.ordinal) {
                    2 -> {
                        this@LoadDataViewModel.vCashCore.removeObserver(this)
                        this@LoadDataViewModel.dismissLoadingDialog()
                        this@LoadDataViewModel.initDbConfig(path)
                        this@LoadDataViewModel.m7677b()
                        return
                    }
                    3 -> {
                        Process.killProcess(Process.myPid())
                        return
                    }
                    else -> return
                }
            }
        }
        this.vCashCore.addObserver(this.f10157x)
        this.vCashCore.initAppWithPath(this.context as Activity, path)
    }

    //915 m7682r
    fun initDbConfig(str: String) {
        val sharedPreferencesUtil = SharedPreferencesUtil.getSharedPreferencesUtil()
        if (TextUtils.equals(str, PartLoader.getSingleInstance(this.context).getDefaultWalletPath())) {
            if (!this.vCashCore.getWalletByChainType(BLOCK_CHAIN_TYPE.VCASH).getSettingConfigBoolean()) {
                val expandList = this.vCashCore.getWalletByChainType(BLOCK_CHAIN_TYPE.VCASH).getSettingExpandList()
                if (!TextUtils.isEmpty(expandList)) {
                    val c:List<Any> = sharedPreferencesUtil.mo41249c(this.context, "unexpend")
                    if (c != null && !c!!.isEmpty()) {
                        this.vCashCore.getWalletByChainType(BLOCK_CHAIN_TYPE.VCASH).setSettingExpandList(Gson().toJson(expandList as Any))
                    }
                }
                this.vCashCore.getWalletByChainType(BLOCK_CHAIN_TYPE.VCASH)
                    .setSettingBoolValue("v_sort_by_balance", sharedPreferencesUtil.getBooleanValue("v_sort_by_balance", false, this.context))
                this.vCashCore.getWalletByChainType(BLOCK_CHAIN_TYPE.VCASH).setSettingConfigBoolean(true)
            }
        }
    }

    fun m7677b() {
        Constants.InitFileDir(this.vCashCore)
        startService(Intent(this.context, SyncService::class.java))
        //mo41564b(Intent(this.context, ImService::class.java))
        PeerUtils.checkPeersStatus(null, null)
        var hasHdAccount = true
        val hasWalletType = this.vCashCore.getWalletType() !== WalletType.UNKNOWN
        val hasPwd = this.vCashCore.hasSetWalletPwd()
        val hdAccountList = this.vCashCore.getHDAccountList()
        if (hdAccountList == null || hdAccountList.isEmpty()) {
            hasHdAccount = false
        }
        if (!hasWalletType || !hasPwd || !hasHdAccount) {
            Log.i("LoadDataViewModel", "start: create")
            this.vCashCore.InitWalletIfNeed(false)
            this.mainHandler.post(Runnable {
                val viewModel = this@LoadDataViewModel
                viewModel.startActivity(Intent(viewModel.context, WalletCreateFirstActivity::class.java))
                ApplicationLoader.getSingleApplicationContext().setActivityWidthPixels(DeviceUtil.getActivityWidthPixels(this@LoadDataViewModel.context as Activity))
                ApplicationLoader.getSingleApplicationContext().setActivityHeightPixels(DeviceUtil.getActivityHeightPixels(this@LoadDataViewModel.context as Activity))
                ActivityManager.getInstance().removeAndFinishAll(WalletCreateFirstActivity::class.java)
            })
            return
        }
        Log.i("LoadDataViewModel", "start: load")
        this.mainHandler.post(Runnable {
            ApplicationLoader.getSingleApplicationContext().setActivityWidthPixels(DeviceUtil.getActivityWidthPixels(this@LoadDataViewModel.context as Activity))
            ApplicationLoader.getSingleApplicationContext().setActivityHeightPixels(DeviceUtil.getActivityHeightPixels(this@LoadDataViewModel.context as Activity))
            this@LoadDataViewModel.checkWalletTypeAndEnterMain()
        })
    }

}