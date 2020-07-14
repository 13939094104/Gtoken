package com.vtoken.application.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.vtoken.application.ApplicationLoader
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.databinding.FragmentWalletMainBinding
import com.vtoken.application.databinding.MainLeftBinding
import com.vtoken.application.viewModel.wallet.WalletLeftMainViewModel
import com.vtoken.application.viewModel.wallet.WalletMainViewModel
import com.vtoken.vdsecology.vcash.VCashCore
import vdsMain.AddressFilter
import vdsMain.BLOCK_CHAIN_TYPE
import vdsMain.Constants
import vdsMain.block.ChainSyncStatus
import vdsMain.message.VCountMessage
import vdsMain.model.Address
import vdsMain.observer.ASyncMessageObserver
import vdsMain.tool.DeviceUtil
import vdsMain.tool.Util

class WalletFragment():BaseFragment(){

    lateinit var dataBinding: FragmentWalletMainBinding

    lateinit var mainModel:WalletMainViewModel

    lateinit var mainLeftBinding:MainLeftBinding

    lateinit var walletLeftModel:WalletLeftMainViewModel

    //f3936i
    internal var lastSendGetVCountMsgTime: Long = 0

    //f3937j
    internal var chainTypeToCurBlockNo = HashMap<BLOCK_CHAIN_TYPE, Long>(2)

    //f3934g
    internal var blockChainType = ApplicationLoader.getBlockChainType()

    internal var f3932e = false

    //f3939l
    internal var lastBlockNoUpdateTime: Long = 0

    //f3947u
    private var walletFragmentHandler: WalletFragmentHandler? = null

    //f3933f
    lateinit var vCashCore: VCashCore

    internal var f3935h: ASyncMessageObserver? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding=DataBindingUtil.inflate<FragmentWalletMainBinding>(inflater!!, R.layout.fragment_wallet_main,container,false)
//        dataBinding.mainScrollView.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
//            Logger.d(scrollY);
//        }
        setRecyclerView(dataBinding.addressRecyclerView)
        mainModel= WalletMainViewModel(this.activity,this)
        dataBinding.setVariable(BR.walletMainModel,mainModel)
        this.vCashCore = ApplicationLoader.getVcashCore()
        initLeftView()
        initWalletHandler()
        val intentFilter = IntentFilter()
        intentFilter.addAction("ecology.reload_address")
        intentFilter.addAction("import_pri_key")
        intentFilter.addAction("errorBar")
        this.chainTypeToCurBlockNo[BLOCK_CHAIN_TYPE.VCASH] = java.lang.Long.valueOf(this.vCashCore.getCurBlockNo(BLOCK_CHAIN_TYPE.VCASH))
        LocalBroadcastManager.getInstance(this.context.getApplicationContext())
            .registerReceiver(this.walletReceiver, intentFilter)
        updateBlockInfoUI()
        return dataBinding.root
    }

    private fun initLeftView() {
        this.mainLeftBinding = DataBindingUtil.inflate<MainLeftBinding>(LayoutInflater.from(context), R.layout.main_left, null, false)
        walletLeftModel= WalletLeftMainViewModel(context)
        this.mainLeftBinding.setVariable(BR.walletLeftMainModel,walletLeftModel)
        this.mainLeftBinding.getRoot().setLayoutParams(ViewGroup.LayoutParams(-1, DeviceUtil.getActivityHeightPixels(context as Activity)))
        this.dataBinding.leftMain.addHeaderView(this.mainLeftBinding.getRoot())
    }

    override fun onDestroy() {
        mainModel.onDestroy()
        super.onDestroy()
    }

    fun showLeftMenu(){
        this.dataBinding.drawerLayout.openDrawer(GravityCompat.START)
        //this.dataBinding.leftMain.showContextMenu()
    }

    //f3927A
    private val walletReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            var c: Char
            val action = intent.action
            val hashCode = action!!.hashCode()
            if (hashCode == -1876919763) {
                if (action == "import_pri_key") {
                    c = 1.toChar()
                }
            } else if (action == "ecology.reload_address") {
                c = 0.toChar()
                when (c) {
                    0.toChar() -> {
                        this@WalletFragment.sendReInitMsg()
                        return
                    }
                    1.toChar() -> {
                        if (!TextUtils.isEmpty(intent.getStringExtra("new_address"))) {
                            this@WalletFragment.f3932e = true
                        }
                        if (this@WalletFragment.mainModel != null) {
                            this@WalletFragment.mainModel.initAddressListByAddressFilter()
                            return
                        }
                        return
                    }
                    else -> return
                }
            }else if(action == "errorBar"){
                mainModel.showErrorBar.set(true)
                mainModel.errorBarMsg.set(intent.getStringExtra("msg"))
            }
        }
    }

    //mo39876f
    fun checkAndSetPeerStatus() {
        val mainViewModel = this.mainModel
            if (mainViewModel != null) {
                mainViewModel.setPeerStatus()
            }
    }

    //C2769a
    open class WalletFragmentHandler : Handler()

    fun mo39872b(address: Address) {
        //BindUtil.m6880a(this.dataBinding.f8011n, address)
    }

    //mo39874d
    fun sendReInitMsg() {
        this.walletFragmentHandler?.removeMessages(10000)
        this.walletFragmentHandler?.sendEmptyMessageDelayed(10000, Constants.delay)
    }

    //mo39877g
    fun checkIntervalAndUpdateBlockUI(curBlock:Long?=null) {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - this.lastBlockNoUpdateTime > 1000) {
            this.lastBlockNoUpdateTime = currentTimeMillis
           //更新ui
            updateBlockInfoUI(curBlock)
            LocalBroadcastManager.getInstance(ApplicationLoader.getSingleApplicationContext()).sendBroadcast(Intent("ecology.reload_address"));
        }
    }

    fun m4145a(checkSynched: Boolean) {
        if (checkSynched) {
            if (this.vCashCore.getBlockChainSyncStatus(BLOCK_CHAIN_TYPE.VCASH) !== ChainSyncStatus.SYNCHED) {
                return
            }
        }
        if (this.lastSendGetVCountMsgTime == 0L || System.currentTimeMillis() - this.lastSendGetVCountMsgTime >= 60000) {
            val vCashCore1 = ApplicationLoader.getVcashCore()
            if (this.f3935h == null) {
                this.f3935h = object : ASyncMessageObserver {
                    override fun onMessageReceived(message: vdsMain.message.Message?) {
                        if (message is VCountMessage) {
                            ApplicationLoader.f3257a.set(
                                Util.m7023a(
                                    java.lang.Long.toString((message as VCountMessage).f11927b.toLong()),
                                    0
                                )
                            )
                        }
                    }
                }
                vCashCore1!!.addObserver(this.f3935h as Any)
            }
            if (vCashCore1 != null) {
                vCashCore1.checkAndSendGetVCountMessage()
                this.lastSendGetVCountMsgTime = System.currentTimeMillis()
            }
        }
    }

    //mo39869a
    fun checkLockBalanceOrUpdateUi(chainType: BLOCK_CHAIN_TYPE, chainSyncStatus: ChainSyncStatus,curBLockNo:Long?=null,maxBlockNo:Long?=null) {
        if (chainSyncStatus === ChainSyncStatus.SYNCHED) {
            val addressList = this.vCashCore.getWalletAddressModel().getAllUsingTxDesMapAddress()
            if (addressList != null && !addressList.isEmpty()) {
                for (address in addressList) {
                    if (address.getSumBalance(chainType) > address.getAvailableBalance(chainType)) {
                        address.calSubAddressBalanceInfo(chainType)
                    }
                }
            }
            this.walletFragmentHandler!!.sendEmptyMessage(1)
        } else if (chainSyncStatus === ChainSyncStatus.SYNCHING) {
            //更新ui
            updateBlockInfoUI(curBLockNo,maxBlockNo)
        }
    }

    //m4172s
    fun updateBlockInfoUI(curBlock:Long?=null,maxBlockNo:Long?=null) {
        this.walletFragmentHandler!!.post(Runnable {
//            if (this@WalletFragment.vCashCore.getBlockChainSyncStatus(this@WalletFragment.blockChainType) == ChainSyncStatus.SYNCHED) {
//                this@WalletFragment.mainLeftBinding.loadingProgressBar.setVisibility(View.GONE)
//                return@Runnable
//            }
            val curBlockNo = if(curBlock!=null) curBlock else this@WalletFragment.mainModel.vCashCore.getCurBlockNo(this@WalletFragment.mainModel.block_chain_type)
            val tempMaxBlockNo = if(maxBlockNo!=null) maxBlockNo else this@WalletFragment.mainModel.vCashCore.getMaxBlockNo(this@WalletFragment.mainModel.block_chain_type)
            if (curBlockNo != tempMaxBlockNo) {
                this@WalletFragment.mainLeftBinding.loadingProgressBar.setVisibility(View.VISIBLE)
            } else {
                this@WalletFragment.mainLeftBinding.loadingProgressBar.setVisibility(View.GONE)
            }
            this@WalletFragment.mainLeftBinding.curBlock.setText(curBlockNo.toString())
            this@WalletFragment.mainLeftBinding.maxBlock.setText(tempMaxBlockNo.toString())
//            //自来也
            if(curBlockNo.toString()!=tempMaxBlockNo.toString()){
                this@WalletFragment.mainLeftBinding.curBlock!!.setTextColor(resources.getColor(R.color.red))
                this@WalletFragment.mainLeftBinding.syncProgress.setProgressDrawable(resources.getDrawable(R.drawable.progress_sync1))
            }else{
                this@WalletFragment.mainLeftBinding.curBlock!!.setTextColor(resources.getColor(R.color.resync_back_selected))
                this@WalletFragment.mainLeftBinding.syncProgress.setProgressDrawable(resources.getDrawable(R.drawable.progress_sync))
            }
            this@WalletFragment.mainLeftBinding.syncProgress.setMax(100)
            if (tempMaxBlockNo != 0L) {
                this@WalletFragment.mainLeftBinding.syncProgress.setProgress((curBlockNo.toDouble() / tempMaxBlockNo.toDouble() * 100.0).toInt())
            }
        })
    }
    
    //m4166n
    @SuppressLint("HandlerLeak")
    private fun initWalletHandler() {
        this.walletFragmentHandler = object : WalletFragmentHandler() {
            override fun handleMessage(message: Message) {
                val i = message.what
                if (i == 1) {
                    if (ChainSyncStatus.SYNCHED.equals(
                            ApplicationLoader.getVcashCore().getBlockChainSyncStatus(
                                ApplicationLoader.getBlockChainType()))
                    ) {
                        val preBlockNo =
                            this@WalletFragment.chainTypeToCurBlockNo.get(this@WalletFragment.blockChainType) as Long
                        val nowBlockNo = this@WalletFragment.vCashCore.getCurBlockNo(this@WalletFragment.blockChainType)
                        if (nowBlockNo != preBlockNo) {
                            this@WalletFragment.chainTypeToCurBlockNo.put(
                                this@WalletFragment.blockChainType,
                                java.lang.Long.valueOf(nowBlockNo)
                            )
                            removeMessages(3)
                            val msg = Message.obtain()
                            msg.what = 3
                            msg.arg1 = this@WalletFragment.blockChainType.getChainType()
                            msg.what = 3
                            sendMessageDelayed(msg, 500)
                        } else {
                            return
                        }
                    }
                    Log.i("HotWalletFragment", "handleMessage: MSG_SYNCED")
                } else if (i == 10000) {
                    this@WalletFragment.reInitDataAndAddress()
                }else{
                    when(i){
                        3->{
                            //隐藏进度条
                            if (BLOCK_CHAIN_TYPE.getChainType(message.arg1) === this@WalletFragment.blockChainType) {
                                this@WalletFragment.mainLeftBinding.loadingProgressBar.setVisibility(View.GONE)
                            }
                            this@WalletFragment.m4161l()
                            if (this@WalletFragment.mainModel != null) {
                                val addressList = ArrayList(this@WalletFragment.mainModel.vCashCore.getAddressListByFilter(AddressFilter.GENERAL))
                                if (!addressList.isEmpty()) {
                                    for (address in addressList) {
                                        if (address.mo44173S()) {
                                            address.updateCategoryStatus(8, true)
                                        }
                                    }
                                }
                                this@WalletFragment.sendReInitMsg()
                            }
                            this@WalletFragment.m4145a(true)
                        }
                    }
                }
            }
        }
    }

    //VID 有关
    fun m4161l() {
        val core = ApplicationLoader.getVcashCore()
//        if (core != null) {
//                if (core.getCurBlockNo(BLOCK_CHAIN_TYPE.VCASH) > 231840) {
//                    val vidAddressList = core.mo43811b(false)
//                    if (vidAddressList != null && !vidAddressList!!.isEmpty()) {
//                        for (address in vidAddressList!!) {
//                            if (address.getIsDirectSignVid() && address.getDirectInvNo() < 1) {
//                                core.mo43792a(address)
//                            }
//                        }
//                    }
//                } else {
//                    core.mo43784a(this as ImportKeyGetVidObserver)
//                }
//        }
    }

    //mo39875e
    fun reInitDataAndAddress() {
        val mainViewModel = this.mainModel
        mainViewModel.initData()
        this.mainModel.initAddressListByAddressFilter()
    }


    //mo39879i
    fun getMoreImageButton(): ImageView {
        return this.dataBinding.addImage
    }
}

