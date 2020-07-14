package com.vtoken.application.viewModel.wallet

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.view.LayoutInflater
import com.google.gson.JsonParser
import com.vtoken.application.ApplicationLoader
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.util.AppUtil
import com.vtoken.application.util.PartLoader
import com.vtoken.application.util.okHttpRequest.*
import com.vtoken.application.view.activity.ValidatePwdActivity
import com.vtoken.application.view.activity.wallet.ChainResyncActivity
import com.vtoken.application.view.activity.wallet.ChooseAccountActivity
import com.vtoken.application.view.activity.wallet.WalletImportKeyActivity
import com.vtoken.application.view.activity.wallet.WalletPwdActivity
import com.vtoken.application.viewModel.BaseViewModel
import com.vtoken.application.viewModel.PromptDialogViewModel
import vdsMain.ActivityManager
import vdsMain.model.HDAccount
import vdsMain.observer.NetworkObserver
import vdsMain.peer.Peer
import vdsMain.peer.PeerManager
import java.io.*
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.regex.Pattern

class WalletLeftMainViewModel(context: Context) : BaseViewModel(context) {

    var hdAccount: HDAccount? = null

    var versionName:ObservableField<String> = ObservableField("")

    var peerVersion:ObservableField<String> = ObservableField("")

    var hasPeerInfo:ObservableField<Boolean> = ObservableField(false)

    var walletLabel:ObservableField<String> = ObservableField("")


    companion object  {
        var havedUpdate:ObservableField<Int> = ObservableField(0)




    }

    init {
        val hdAccountList = this.vCashCore.getHDAccountList()
        if (hdAccountList != null && hdAccountList.size > 0) {
            this.hdAccount = hdAccountList.get(0) as HDAccount
        }
        walletLabel.set(this.vCashCore.getWalletLabel())
        versionName.set(AppUtil.getVersionName(ApplicationLoader.getSingleApplicationContext()))
        this.vCashCore.addObserver(object :NetworkObserver{
            override fun onConnectStatusChange(peer: Peer?, peerStatus: Peer.PeerStatus?) {

            }

            override fun onPeerManagerStatusChange(peerManagerStatus: PeerManager.PeerManagerStatus?) {

            }

            override fun onMainPeerChange(peerManager: PeerManager?, peer: Peer?) {
                if(peer!=null){
                    val info=peer.peerInfo
                    val m=Pattern.compile("^/MagicBean:(.*)/$").matcher(info.subVersion)
                    if(m.find()){
                        hasPeerInfo.set(true)
                        peerVersion.set(m.group(1))
                    }
                }
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(data!=null){
            if(requestCode==1005&&resultCode==Activity.RESULT_OK){
                checkAndShowQrCodeDialog(data.getStringExtra("pwd"))
            }
        }
    }

    fun changeWalletLabel(){
        showLabelChangeConfirmDialog(walletLabel.get(), false)
    }

    override fun onAddressLabelConfirm(str: String?) {
        changeWalletLabel(str!!)
    }

    fun changeWalletLabel(label: String) {
        val partWallet = PartLoader.getSingleInstance(this.context).getCurPartWallet()
        val sb = StringBuilder()
        sb.append(partWallet.getVdsPath())
        sb.append(File.separator)
        sb.append("wallet.conf")
        val confPath = sb.toString()
        val properties = Properties()
        try {
            properties.load(InputStreamReader(FileInputStream(confPath), StandardCharsets.UTF_8))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e2: IOException) {
            e2.printStackTrace()
        }

        properties.setProperty("label", if (TextUtils.isEmpty(label)) "" else label)
        var z = false
        try {
            properties.store(
                OutputStreamWriter(FileOutputStream(confPath), StandardCharsets.UTF_8),
                "des"
            )
            z = true
        } catch (e3: Exception) {
            e3.printStackTrace()
        }

        if (z) {
            this.walletLabel.set(label)
            this.vCashCore.setWalletLabel(label)
        }
    }

    fun showReSyncChainActivity(){
        val intent=Intent(context,ChainResyncActivity::class.java)
        startActivity(intent)
    }

    fun jumpWalletPwdActivity(){
        startActivity(Intent(context,WalletPwdActivity::class.java))
    }




    //mo41761j
    fun toChangeAccout() {
        val viewModel = PromptDialogViewModel(this.context)
        viewModel.setSpanned(SpannableString(getStringRescourcesByResName("switch_account_prompt")) as Spanned)
        createConfirmView(viewModel, {
            this@WalletLeftMainViewModel.dismissConfirmDialog()
            val mainViewModel = this@WalletLeftMainViewModel
            mainViewModel.startActivity(
                Intent(mainViewModel.context, ChooseAccountActivity::class.java).putExtra(
                    "part_wallet",
                    PartLoader.getSingleInstance(this@WalletLeftMainViewModel.context).getPartWalletList()
                ).putExtra("is_switch", true)
            )
            ActivityManager.getInstance().removeAndFinishAll(ChooseAccountActivity::class.java)
        }, { this@WalletLeftMainViewModel.dismissConfirmDialog() })
        showConfirmDialog("SwitchAccount", true)
    }

    fun exportMemWords(){
        startActivityForResult(Intent(context, ValidatePwdActivity::class.java), 1005)
    }

    private fun checkAndShowQrCodeDialog(pwd: String) {
        val hdAccount = this.hdAccount
        if (hdAccount != null) {
            try {
                val wordsString = hdAccount.getMnemonicWords(pwd as CharSequence)
                val info = getStringRescourcesByResName("hd_account_seed_qr")
                showQrCodeDialog(wordsString, "VDS",info, true,info,object :
                    QrOptionDialogEvent {
                    override fun onCopyClick() {}

                    override fun onSaveClick() {}

                    override fun mo41443c() {}
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
    //区块同步数量
    fun refreshNetwork(){
        showLoadingFragment(getStringRescourcesByResName("setting_reload_net"))
        this.vCashCore.startThreadToRestartPeer()
        //LocalBroadcastManager.getInstance(ApplicationLoader.getSingleApplicationContext()).sendBroadcast(Intent("ecology.reload_address"))
        this.mainHandler.postDelayed(object:Runnable{
            override fun run() {
                this@WalletLeftMainViewModel.dismissLoadingDialog()
            }

        }, 3000)
    }


}