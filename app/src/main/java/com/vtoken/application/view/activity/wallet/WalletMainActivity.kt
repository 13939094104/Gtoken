package com.vtoken.application.view.activity.wallet

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.RingtoneManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.text.TextUtils
import android.util.Log
import com.vcwallet.core.part.PartWallet
import com.vtoken.application.ApplicationLoader
import com.vtoken.application.R
import com.vtoken.application.util.PartLoader
import com.vtoken.application.view.activity.BottomMenuBarActivity
import vdsMain.BLOCK_CHAIN_TYPE
import vdsMain.CTxDestination
import vdsMain.Constants
import vdsMain.block.BlockHeader
import vdsMain.block.BlockInfo
import vdsMain.block.ChainSyncStatus
import vdsMain.callback.SimpleSyncCallBack
import vdsMain.model.Address
import vdsMain.tool.SharedPreferencesUtil
import vdsMain.transaction.CAmount
import vdsMain.transaction.Transaction
import java.io.*
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.collections.ArrayList

class WalletMainActivity : BottomMenuBarActivity() {

    lateinit var receiver: BroadcastReceiver

    lateinit var block_chain_type: BLOCK_CHAIN_TYPE

    internal var vBlockHeight: Long = 0

    //VID 子界面
    //vFragment

    @SuppressLint("HandlerLeak")
    internal var handler: Handler = object : Handler() {
        override fun handleMessage(message: Message) {
            val i = message.what
            if (i != 10001) {
                when (i) {
                    900 -> {
                        if (this@WalletMainActivity.walletFragment != null) {
                            this@WalletMainActivity.walletFragment.sendReInitMsg()
                        }
//                        if (this@WalletMainActivity.vFragment != null) {
//                            this@WalletMainActivity.vFragment.mo41292d()
//                        }
                        if (message.arg1 == 2) {
                            Log.i(WalletMainActivity::class.java.simpleName, "handleMessage: ")
                            //播放音效
                            //this@WalletMainActivity.notifyTx(BLOCK_CHAIN_TYPE.getChainType(message.arg2))
                            return
                        }
                        return
                    }
                    901 -> {
                        //更新广告
//                        if (this@WalletMainActivity.walletFragment != null) {
//                            this@WalletMainActivity.walletFragment.mo39878h()
//                        }
//                        if (this@WalletMainActivity.findFragment != null) {
//                            this@WalletMainActivity.findFragment.mo41287c()
//                            return
//                        }
                        return
                    }
                    else -> return
                }
            } else {
                //this@WalletMainActivity.notifyBidBeOvertaken()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_wallet_main)
        super.onCreate(savedInstanceState)
        PartLoader.getSingleInstance(this as Context).setCurWallet(PartWallet(this.vCashCore.getAppWalletPath(), this.vCashCore.getWalletLabel()))
        this.block_chain_type = ApplicationLoader.getBlockChainType()
        bindSyncService()
        initReceiver()
        registerVCoreEvent()
        val intentFilter = IntentFilter()
        intentFilter.addAction("refresh_ad_list")
        intentFilter.addAction("refresh_tx")
        intentFilter.addAction("refresh_type")
        intentFilter.addAction("refresh_address")
        intentFilter.addAction("com.peer.PeerBroadcastReceiver.Disconnect")
        LocalBroadcastManager.getInstance(applicationContext).registerReceiver(this.receiver, intentFilter)

        var thread= object: Thread(){
            override fun run() {
                val activeNetworkInfo =
                    (this@WalletMainActivity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
                val ab = this@WalletMainActivity.vCashCore.getWalletByChainType(*arrayOfNulls<BLOCK_CHAIN_TYPE>(0)).mo44079ab()
                if (activeNetworkInfo != null && activeNetworkInfo.type != 0 || ab == "1") {
                    this@WalletMainActivity.vCashCore.checkAndStartPeerManagerNetwork()
                }
                this@WalletMainActivity.reComputeVidReward(true)
            }
        }
        thread.start()
    }

    fun notifyTx(blockChainType: BLOCK_CHAIN_TYPE) {
        Log.i(javaClass.simpleName, "notifyTx: ")
        if (this.vCashCore.getBlockChainSyncStatus(blockChainType) === ChainSyncStatus.SYNCHED) {
            when (blockChainType) {
                BLOCK_CHAIN_TYPE.VCASH -> playNoticeVoice(
                    SharedPreferencesUtil.getSharedPreferencesUtil().getAndroidWalletInt(
                        "entryVollarNotificationPosition",
                        0,
                        applicationContext
                    ), 6
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        walletFragment.walletLeftModel.onActivityResult(requestCode,resultCode,data)
    }

    private fun playNoticeVoice(i: Int, type: Int) {
        if (type != 3) {
            when (type) {
                5 -> {
                    if (!SharedPreferencesUtil.getSharedPreferencesUtil().getBooleanValue("messageNotification", true, this as Context)) {
                        return
                    }
                    if (i == 0) {
                        playRing("raw/entrybtc.ogg")
                        return
                    }
                    val ringtoneManager = RingtoneManager(this)
                    ringtoneManager.setType(2)
                    ringtoneManager.cursor
                    ringtoneManager.getRingtone(i - 1).play()
                    return
                }
                6 -> {
                    if (!SharedPreferencesUtil.getSharedPreferencesUtil().getBooleanValue("messageNotification", true, this as Context)) {
                        return
                    }
                    if (i == 0) {
                        playRing("raw/entryv.ogg")
                        return
                    }
                    val ringtoneManager2 = RingtoneManager(this)
                    ringtoneManager2.setType(2)
                    ringtoneManager2.cursor
                    ringtoneManager2.getRingtone(i - 1).play()
                    return
                }
                else -> return
            }
        } else if (i == 0) {
            playRing("raw/adwin.ogg")
        } else {
            val ringtoneManager3 = RingtoneManager(this)
            ringtoneManager3.setType(2)
            ringtoneManager3.cursor
            ringtoneManager3.getRingtone(i - 1).play()
        }
    }

    private fun registerVCoreEvent() {
        registerSyncCallBack(object : SimpleSyncCallBack() {
            /* renamed from: a */
            override fun onUpdateBlock(
                blockChainType: BLOCK_CHAIN_TYPE,
                blockInfo: BlockInfo,
                list: List<Transaction>,
                hashMap: HashMap<CTxDestination, Address>
            ) {
                //更新进度条
                this@WalletMainActivity.walletFragment.checkIntervalAndUpdateBlockUI(blockInfo.blockNo.toLong())
            }

            /* renamed from: a */
            override fun onBlockNoUpdate(blockChainType: BLOCK_CHAIN_TYPE, chainSyncStatus: ChainSyncStatus, blockHeader: BlockHeader, i: Int) {
                this@WalletMainActivity.walletFragment.checkLockBalanceOrUpdateUi(blockChainType, chainSyncStatus,blockHeader.blockNo.toLong(),i.toLong())
                if (chainSyncStatus === ChainSyncStatus.SYNCHED && blockChainType === BLOCK_CHAIN_TYPE.VCASH) {
                    val curBlockNo = this@WalletMainActivity.vCashCore.getCurBlockNo(BLOCK_CHAIN_TYPE.VCASH)
                    if (this@WalletMainActivity.vBlockHeight != curBlockNo) {
                        val mainActivity = this@WalletMainActivity
                        mainActivity.vBlockHeight = curBlockNo
                        mainActivity.reComputeVidReward(false)
                        //this@WalletMainActivity.checkBidFailed()
                    }
                }
            }

            /* renamed from: a */
            override fun onTransactionsConfirmed(blockChainType: BLOCK_CHAIN_TYPE, list: List<Transaction>) {
                Log.i(WalletMainActivity::class.java.simpleName, "onTransactionsConfirmed: ")
                if (this@WalletMainActivity.block_chain_type === blockChainType) {
                    this@WalletMainActivity.handler.removeMessages(900)
                    val obtainMessage = this@WalletMainActivity.handler.obtainMessage()
                    obtainMessage.what = 900
                    obtainMessage.obj = null
                    obtainMessage.arg1 = 1
                    obtainMessage.arg2 = blockChainType.getChainType()
                    this@WalletMainActivity.handler.sendMessageDelayed(obtainMessage, Constants.delay)
                }
            }

            /* renamed from: b */
            override fun onTransactionsReceived(blockChainType: BLOCK_CHAIN_TYPE, list: List<Transaction>) {
                Log.i(WalletMainActivity::class.java.simpleName, "onTransactionsReceived: ")
                if (this@WalletMainActivity.block_chain_type === blockChainType) {
                    this@WalletMainActivity.handler.removeMessages(900)
                }
                val obtainMessage = this@WalletMainActivity.handler.obtainMessage()
                obtainMessage.what = 900
                obtainMessage.obj = null
                if (this@WalletMainActivity.shouldNotify(list)) {
                    obtainMessage.arg1 = 2
                } else {
                    obtainMessage.arg1 = 1
                }
                obtainMessage.arg2 = blockChainType.getChainType()
                this@WalletMainActivity.handler.sendMessageDelayed(obtainMessage, Constants.delay)
            }

            /* renamed from: a */
            override fun onTransactionSent(blockChainType: BLOCK_CHAIN_TYPE, transaction: Transaction) {
                Log.i(WalletMainActivity::class.java.simpleName, "onTransactionSent: ")
                this@WalletMainActivity.handler.removeMessages(900)
                val obtainMessage = this@WalletMainActivity.handler.obtainMessage()
                obtainMessage.what = 900
                obtainMessage.obj = transaction
                obtainMessage.arg1 = 1
                this@WalletMainActivity.handler.sendMessageDelayed(obtainMessage, Constants.delay)
            }

            override fun OnAdUpdate() {
                this@WalletMainActivity.handler.removeMessages(901)
                this@WalletMainActivity.handler.sendEmptyMessageDelayed(901, Constants.delay)
            }

            /* renamed from: a */
            override fun onTransactionsInvalid(
                igVar: BLOCK_CHAIN_TYPE,
                list: List<BlockHeader>,
                list2: List<Transaction>,
                hashMap: HashMap<CTxDestination, Address>
            ) {
                this@WalletMainActivity.onTransactionsInvalid(list2)
            }

            /* renamed from: b */
            override fun onTransactionAbanded(igVar: BLOCK_CHAIN_TYPE, transaction: Transaction?) {
                val str = WalletMainActivity::class.java.simpleName
                val sb = StringBuilder()
                sb.append("onTransactionAbanded: ")
                sb.append(transaction?.getTxId()?.hashString())
                Log.e(str, sb.toString())
                if (transaction != null) {
                    this@WalletMainActivity.handler.removeMessages(900)
                    this@WalletMainActivity.handler.sendEmptyMessageDelayed(900, Constants.delay)
                    if (transaction.getFlag() === 1) {
                        val txInList = transaction.getSelfTxInList()
                        if (txInList != null && !txInList.isEmpty()) {
                            for (txIn in txInList) {
                                if (this@WalletMainActivity.vCashCore.getAddressFromAddressString(txIn.getAddress()!!)!= null) {
                                    this@WalletMainActivity.handler.post(Runnable {
                                        this@WalletMainActivity.toast(
                                            this@WalletMainActivity.getString(
                                                "create_v_tx_failed"
                                            )
                                        )
                                    })
                                    return
                                }
                            }
                        }
                    }
                }
            }

            /* renamed from: c */
            override fun mo39687c(igVar: BLOCK_CHAIN_TYPE, list: List<Transaction>) {
                this@WalletMainActivity.onTransactionsInvalid(list)
            }


            override fun onResynched(z: Boolean) {
                Log.i(WalletMainActivity::class.java.simpleName, "onResynched: ")
                val mainActivity = this@WalletMainActivity
                mainActivity.vBlockHeight = 0
                if (z) {
                    val addressList = ArrayList(mainActivity.vCashCore.getVidAddressList(true))
                    if (!addressList.isEmpty()) {
                        var hasVid = false
                        for (address in addressList) {
                            if (address.isAppingVid()) {
                                this@WalletMainActivity.vCashCore.initVidAddress(address)
                                hasVid = true
                            }
                        }
                        if (hasVid) {
                            LocalBroadcastManager.getInstance(this@WalletMainActivity.getApplicationContext())
                                .sendBroadcast(Intent("refresh_vid"))
                        }
                    } else {
                        LocalBroadcastManager.getInstance(this@WalletMainActivity.getApplicationContext()).sendBroadcast(Intent("ecology.reload_address"))
                        return
                    }
                }
//                if (this@WalletMainActivity.vFragment != null) {
//                    this@WalletMainActivity.vFragment.mo41290a(false)
//                }
            }
        })
    }

    fun shouldNotify(list: List<Transaction>?): Boolean {
        var z = false
        if (list == null || list.isEmpty()) {
            return false
        }
        val it = list.iterator()
        while (true) {
            if (!it.hasNext()) {
                break
            }
            val transaction = it.next()
            if (!transaction.isFromLocalAddress()) {
                z = true
                break
            }
        }
        return z
    }

    fun reComputeVidReward(z: Boolean) {
//        var i: Int
//        var cbVar: AddressModel
//        var it: Iterator<*>
//        var cbVar2: AddressModel
//        var dnVar: TxOut
//        var j: Long
//        Log.i("Main", "reComputeVidReward: ")
//        var i2 = 0
//        //this.startEndBlock = calculateSeasonStartEnd(this.vCashCore.mo44161e(arrayOfNulls<BLOCK_CHAIN_TYPE>(0)))
//        var addressModel = this.vCashCore.getWalletAddressModel()
//        var i3 = 1
//        val f = this.vCashCore.getCurBlockNo(BLOCK_CHAIN_TYPE.VCASH)
//        val j2 = this.vCashCore.getWalletChainParams(BLOCK_CHAIN_TYPE.VCASH).vidLockBlockNumber
//        val arrayList = ArrayList(addressModel.mo43374a(false))
//        val a = CAmount.m11399a(4)
//        CAmount.m11400a(java.lang.Double.valueOf(0.5))
//        var it2: Iterator<*> = arrayList.iterator()
//        while (it2.hasNext()) {
//            var jkVar = it2.next() as Address
//            val v2 = jkVar.mo44621v()
//            val a2 = this.vCashCore.mo44072a(v2, TransactionConfirmType.ALL, arrayOfNulls<BLOCK_CHAIN_TYPE>(i2))
//            var it3: Iterator<*> = it2
//            if (a2 == null || a2!!.isEmpty()) {
//                val it4 = it3
//                jkVar.mo44547a(0.0)
//                jkVar.mo44568b(0.0)
//                jkVar.mo44585d(0)
//                jkVar.mo44570b(0)
//                jkVar.mo44577c(0.0)
//                jkVar.mo44579c(0)
//                jkVar.mo44549a(0)
//                addressModel.mo43413h(jkVar)
//                it2 = it4
//                i2 = 0
//                i3 = 1
//            } else {
//                var it5: Iterator<*> = a2!!.iterator()
//                var i4 = 0
//                var j3: Long = 0
//                var j4: Long = 0
//                var j5: Long = 0
//                var j6: Long = 0
//                var j7: Long = 0
//                var j8: Long = 0
//                var j9: Long = 0
//                var i5 = 0
//                var i6 = 0
//                while (it5.hasNext()) {
//                    val dhVar = it5.next() as Transaction
//                    val d = dhVar.mo43521d()
//                    if (dhVar.mo43525h() !== i) {
//                        cbVar = addressModel
//                        it = it5
//                    } else if (dhVar.mo43534C_() || dhVar.mo43573v()) {
//                        cbVar = addressModel
//                        it = it5
//                    } else {
//                        it = it5
//                        if (this.vCashCore.mo44108a(dhVar.mo43526h_(), BLOCK_CHAIN_TYPE.VCASH)) {
//                            cbVar = addressModel
//                        } else {
//                            val k = dhVar.mo43565k() as Long
//                            val it6 = it3
//                            val q = (dhVar.mo43522e().get(0) as TxIn).mo43606q()
//                            var j10 = j7
//                            var j11 = j6
//                            var j12 = j5
//                            var j13 = j4
//                            var i7 = i4
//                            for (dnVar2 in d) {
//                                val jkVar2 = jkVar
//                                if (dnVar2.mo43589e() !== 1) {
//                                    jkVar = jkVar2
//                                } else {
//                                    val a3 = dnVar2.mo43578a()
//                                    if (dhVar.mo43574w()) {
//                                        if (!q.equals(v2) || a3 != a) {
//                                            cbVar2 = addressModel
//                                            dnVar = dnVar2
//                                        } else if (q !is CNoDestination) {
//                                            dnVar = dnVar2
//                                            cbVar2 = addressModel
//                                            addFriendAuto(
//                                                (dhVar.mo43522e().get(0) as TxIn).mo43147i(),
//                                                dnVar.mo43147i(),
//                                                2
//                                            )
//                                        } else {
//                                            cbVar2 = addressModel
//                                            dnVar = dnVar2
//                                        }
//                                        if (dnVar.mo43148l().equals(v2) && a3 == a && q !is CNoDestination) {
//                                            addFriendAuto(
//                                                dnVar.mo43147i(),
//                                                (dhVar.mo43522e().get(0) as TxIn).mo43147i(),
//                                                1
//                                            )
//                                        }
//                                    } else {
//                                        cbVar2 = addressModel
//                                        dnVar = dnVar2
//                                    }
//                                    if (dnVar.mo43148l().equals(v2)) {
//                                        if (a3 == a) {
//                                            i5++
//                                            if ((this.startEndBlock.first as Int).toInt().toLong() < k) {
//                                                i6++
//                                                j12++
//                                                j11 += a3
//                                            }
//                                        }
//                                        if (d.indexOf(dnVar) == 0) {
//                                            i7++
//                                            j = 0
//                                        } else {
//                                            j = 0
//                                        }
//                                        if (a3 == j) {
//                                            j3 += CAmount.m11400a(java.lang.Double.valueOf(0.5))
//                                        }
//                                        j13 += a3
//                                        j10++
//                                        if (f - dhVar.mo43565k() as Long < j2 && jkVar2.mo44621v().equals(dnVar.mo43148l())) {
//                                            j9 += dnVar.mo43578a()
//                                        }
//                                    }
//                                    jkVar = jkVar2
//                                    addressModel = cbVar2
//                                }
//                            }
//                            i4 = i7
//                            j4 = j13
//                            j5 = j12
//                            j6 = j11
//                            j7 = j10
//                            it5 = it
//                            it3 = it6
//                            j8 = k
//                            i = 1
//                        }
//                    }
//                    it5 = it
//                    it3 = it3
//                    jkVar = jkVar
//                    addressModel = cbVar
//                    i = 1
//                }
//                val cbVar3 = addressModel
//                val jkVar3 = jkVar
//                val it7 = it3
//                var i8 = 12
//                if (i4 <= 12) {
//                    i8 = i4
//                }
//                jkVar3.mo44605j(i8)
//                jkVar3.mo44570b(j5)
//                jkVar3.mo44577c(CAmount.m11401a(java.lang.Long.valueOf(j6)).doubleValue())
//                jkVar3.mo44547a(CAmount.m11401a(java.lang.Long.valueOf(j4)).doubleValue())
//                jkVar3.mo44568b(CAmount.m11401a(java.lang.Long.valueOf(j3)).doubleValue())
//                jkVar3.mo44585d(i5)
//                jkVar3.mo44608k(i6)
//                jkVar3.mo44579c(j7)
//                jkVar3.mo44549a(j8)
//                jkVar3.mo44586d(j9)
//                addressModel = cbVar3
//                addressModel.mo43413h(jkVar3)
//                it2 = it7
//                i2 = 0
//                i3 = 1
//            }
//        }
//        if (!z) {
//            Log.i("VViewModel", "reComputeVidReward: ")
//            LocalBroadcastManager.getInstance(ApplicationLoader.m3654a()).sendBroadcast(Intent("refresh_vid"))
//        }
        val properties = Properties()
        val sb = StringBuilder()
        sb.append(PartLoader.getSingleInstance(this as Context).getCurPartWallet().getVdsPath())
        sb.append(File.separator)
        sb.append("wallet.conf")
        val confFilePathString = sb.toString()
        try {
            properties.load(InputStreamReader(FileInputStream(confFilePathString), StandardCharsets.UTF_8))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        properties.setProperty(
            "balance",
            CAmount.toDecimalSatoshiString(java.lang.Long.valueOf(this.vCashCore.getAllAddressAvailBalance(true, BLOCK_CHAIN_TYPE.VCASH).getValue()))
        )
//        properties.setProperty(
//            "btcBalance",
//            CAmount.toDecimalSatoshiString(java.lang.Long.valueOf(this.vCashCore.mo44052a(false, BLOCK_CHAIN_TYPE.BITCOIN).mo18968b()))
//        )
        try {
            properties.store(OutputStreamWriter(FileOutputStream(confFilePathString), StandardCharsets.UTF_8), "des")
        } catch (unused: Exception) {
        }

    }

    fun onTransactionsInvalid(list: List<Transaction>?) {
        if (list != null && !list.isEmpty()) {
            val addressList = this.vCashCore.getVidAddressList(true)
            if (addressList != null && !addressList.isEmpty()) {
                val arrayList = java.util.ArrayList<String>()
                for (transaction in list) {
                    if (!transaction.isDefaultHash()) {
                        arrayList.add(transaction.getTxidHashString())
                        val str = javaClass.simpleName
                        val sb = StringBuilder()
                        sb.append("onTransactionsInvalid: ")
                        sb.append(transaction.getTxId().hashString())
                        Log.e(str, sb.toString())
                    }
                }
                var z = false
                for (str2 in arrayList) {
                    for (address in addressList) {
                        if (!TextUtils.isEmpty(str2) && TextUtils.equals(address.getClueTxid(), str2)) {
                            this.vCashCore.initVidAddress(address)
                            z = true
                        }
                    }
                }
                if (z) {
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(Intent("refresh_vid"))
                }
            } else {
                return
            }
        }
        this.handler.removeMessages(900)
        this.handler.sendEmptyMessageDelayed(900, Constants.delay)
    }

    private fun initReceiver(){
        receiver=object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent==null)
                    return;
                val action=intent.action
                when(action){
                    "refresh_address"->{
                        this@WalletMainActivity.walletFragment.sendReInitMsg()
                    }
                    "com.peer.PeerBroadcastReceiver.Disconnect"->{
                        this@WalletMainActivity.walletFragment.checkAndSetPeerStatus()
                    }
                }
            }

        }
    }




}
