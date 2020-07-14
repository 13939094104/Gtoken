package com.vtoken.application.view.activity.wallet

import android.annotation.SuppressLint
import android.content.*
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.text.TextUtils
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import com.vtoken.application.ApplicationLoader
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.databinding.WalletAddressDetailBinding
import com.vtoken.application.view.activity.BaseActivity
import com.vtoken.application.viewModel.wallet.WalletAddressDetailViewModel
import vdsMain.BLOCK_CHAIN_TYPE
import vdsMain.block.BlockHeader
import vdsMain.block.ChainSyncStatus
import vdsMain.callback.SimpleSyncCallBack
import vdsMain.transaction.Transaction

class WalletAddressDetailActivity : BaseActivity() {
    lateinit var f3848a: Handler
    lateinit var myClipboard: ClipboardManager
    lateinit var dataBinding:WalletAddressDetailBinding
    lateinit var detailModel:WalletAddressDetailViewModel
    lateinit var f3860m: BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding=DataBindingUtil.setContentView(this, R.layout.wallet_address_detail)
        detailModel=WalletAddressDetailViewModel(this)
        dataBinding.setVariable(BR.walletAddressDetailModel,detailModel)
        myClipboard=getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager;
        this.f3848a = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(message: Message) {
                if (message.what == 901) {
                    this@WalletAddressDetailActivity.detailModel.reloadData()
                }
            }
        }

        registerSyncCallBack(object : SimpleSyncCallBack() {
            /* renamed from: a */
            override fun onTransactionSent(blockChainType: BLOCK_CHAIN_TYPE, transaction: Transaction) {
                if (blockChainType === ApplicationLoader.getBlockChainType()) {
                    this@WalletAddressDetailActivity.f3848a.removeMessages(901)
                    this@WalletAddressDetailActivity.f3848a.sendEmptyMessageDelayed(901, 500)
                }
            }

            /* renamed from: b */
            override fun onTransactionsReceived(igVar: BLOCK_CHAIN_TYPE, list: List<Transaction>) {
                if (igVar === ApplicationLoader.getBlockChainType()) {
                    this@WalletAddressDetailActivity.f3848a.removeMessages(901)
                    this@WalletAddressDetailActivity.f3848a.sendEmptyMessageDelayed(901, 500)
                }
            }

            /* renamed from: a */
            override fun onTransactionsConfirmed(igVar: BLOCK_CHAIN_TYPE, list: List<Transaction>) {
                if (igVar === ApplicationLoader.getBlockChainType()) {
                    this@WalletAddressDetailActivity.f3848a.removeMessages(901)
                    this@WalletAddressDetailActivity.f3848a.sendEmptyMessageDelayed(901, 500)
                }
            }

            /* renamed from: a */
            override fun onBlockNoUpdate(igVar: BLOCK_CHAIN_TYPE, chainSyncStatus: ChainSyncStatus, jtVar: BlockHeader, i: Int) {
                if (igVar === ApplicationLoader.getBlockChainType() && chainSyncStatus === ChainSyncStatus.SYNCHED) {
                    this@WalletAddressDetailActivity.f3848a.removeMessages(901)
                    this@WalletAddressDetailActivity.f3848a.sendEmptyMessageDelayed(901, 500)
                }
            }
        })
        bindSyncService()
        this.f3860m = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (TextUtils.equals(intent.action, "refresh_tx") && !TextUtils.isEmpty(intent.getStringExtra("tx_id"))
                ) {
                    this@WalletAddressDetailActivity.detailModel.reloadTransaction()
                    this@WalletAddressDetailActivity.dataBinding.setVariable(BR.walletAddressDetailModel, this@WalletAddressDetailActivity.detailModel)
                }
            }
        }
        val intentFilter = IntentFilter()
        intentFilter.addAction("refresh_tx")
        setOnCheckedChangeListener()
        this.dataBinding.radioAll.performClick()
        LocalBroadcastManager.getInstance(applicationContext).registerReceiver(this.f3860m, intentFilter)
    }

    private fun setOnCheckedChangeListener() {
        this.dataBinding.radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            if (!(radioGroup.findViewById<View>(i) as RadioButton).isChecked) {
                return@OnCheckedChangeListener
            }
            if (i == R.id.radio_all) {
                this@WalletAddressDetailActivity.detailModel.reloadTransaction(0)
            } else if (i == R.id.radio_receive) {
                this@WalletAddressDetailActivity.detailModel.reloadTransaction(1)
            } else if (i == R.id.radio_send) {
                this@WalletAddressDetailActivity.detailModel.reloadTransaction(2)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        detailModel.onActivityResult(requestCode,resultCode,data)
    }
}
