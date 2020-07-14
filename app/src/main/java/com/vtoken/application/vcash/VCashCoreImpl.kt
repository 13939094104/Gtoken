package com.vtoken.vdsecology.vcash

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import bitcoin.CNoDestination
import bitcoin.UInt256
import bitcoin.account.hd.HDSeed
import com.tbruyelle.rxpermissions2.RxPermissions
import com.vc.libcommon.exception.AddressFormatException
import com.vc.libcommon.util.UInt64
import com.vc.libcommon.utils.JNIUtils
import com.vtoken.application.constant.Constant
import com.vtoken.vdsecology.vcash.tools.NativeLibLoader
import com.vtoken.vdsecology.vcash.tools.ThreadToolkit
import generic.crypto.ECC
import generic.exceptions.AddressNotFoundException
import generic.exceptions.EncryptException
import generic.exceptions.SeedWordsFormatException
import generic.utils.AddressUtils
import io.reactivex.functions.Consumer
import org.apache.commons.io.IOUtils
import vcash.model.VTxModel
import vcash.network.VPeerManager
import vdsMain.*
import vdsMain.block.BlockHeader
import vdsMain.block.BlockInfo
import vdsMain.block.ChainSyncStatus
import vdsMain.db.Db
import vdsMain.message.CluePreCreateTreeMssage
import vdsMain.message.Message
import vdsMain.message.SendMsgResult
import vdsMain.model.*
import vdsMain.observer.*
import vdsMain.peer.Peer
import vdsMain.peer.PeerInfo
import vdsMain.peer.PeerManager
import vdsMain.table.WalletTable
import vdsMain.tool.SPUtils
import vdsMain.tool.SystemToolkit
import vdsMain.transaction.*
import vdsMain.wallet.ChainParams
import vdsMain.wallet.VWallet
import vdsMain.wallet.Wallet
import vdsMain.wallet.WalletType
import java.io.File
import java.lang.Exception
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.collections.ArrayList

enum class WalletBackupStatus {
    NA,
    BACKUP_WALLET,
    BACKUP_ADDRESS,
    BACKUP_CONTACT,
    RESTORE_WALLET,
    RESTORE_ADDRESS,
    RESTORE_CONTACT
}

enum class ObserverType {
    CORE,
    BLOCK,
    ACCOUNT,
    TX,
    NETWORK,
    ASYNC_MSG
}

class VCashCoreImpl:VCashCore {

    override fun getSpendUtxoList(includeFee: Boolean, list: List<Utxo>, sendValue: Long, feeValue: Long): List<Utxo> {
        checkWalletInit()
        return this.vWallet!!.getSelfTransactionModel().getSpendUtxoList(includeFee, list, sendValue, feeValue)
    }

    override fun getUtxoListByAddress(address: Address, vararg igVarArr: BLOCK_CHAIN_TYPE): List<Utxo>? {
        return if(!isInitSuccess()){
            null
        }else getWalletByChainType(*igVarArr).getSelfTransactionModel().mo40449b(address.getCTxDestination())
    }

    override fun getAccountUtxoList(account: Account, igVar: BLOCK_CHAIN_TYPE, z: Boolean): List<Utxo> {
       checkWalletInit()
        return getWalletByChainType(igVar).getSelfTransactionModel().mo44483a(account,z)
    }

    override fun getTransactionListByCTxDestination(
        cTxDestination: CTxDestination,
        transactionConfirmType: TransactionConfirmType,
        vararg blockchainTypes: BLOCK_CHAIN_TYPE
    ): List<Transaction>? {
        if(!isInitSuccess()){
            return null
        }
        if(blockchainTypes.size<2){
            return getWalletByChainType(*blockchainTypes).getSelfTransactionModel().getTransactionListByConfirmType(cTxDestination,transactionConfirmType)
        }
        val transactionList= ArrayList<Transaction>()
        for(i in blockchainTypes.indices){
            val mutableList = getWalletByChainType(blockchainTypes[i]).getSelfTransactionModel().getTransactionListByConfirmType(cTxDestination, transactionConfirmType)
            if (mutableList != null && !mutableList.isEmpty()) {
                transactionList.addAll(mutableList)
            }
        }
        return transactionList
    }


    //f12798a
    val permissionArr = arrayOf(
        "android.permission.READ_EXTERNAL_STORAGE",
        "android.permission.WRITE_EXTERNAL_STORAGE",
        "android.permission.INTERNET"
    )

    //f12814q
    //915 f13462q
    var vWallet: VWallet? = null

    private val initInfo = InitInfo()

    private val lock = ReentrantLock()

    //f12802e
    //915 f13450e
    var appFolderPath: String=""

    private val coreEventObserverLinkedHashMap = LinkedHashMap<CoreEventObserver, CoreEventObserver>()

    //f12808k
    private val accountAddressObserverLinkedHashMap = LinkedHashMap<AccountAddressObserver, AccountAddressObserver>()

    //f12807j
    private val blockChainObserverMap = LinkedHashMap<BlockChainObserver, BlockChainObserver>()

    //f12809l
    private val networkObserverMap = LinkedHashMap<NetworkObserver, NetworkObserver>()

    //f12810m
    private val transactionObserverMap = LinkedHashMap<TransactionObserver, TransactionObserver>()

    //f12813p
    private val asyncMsgObserverMap = LinkedHashMap<ASyncMessageObserver, ASyncMessageObserver>()

    //f12804g
    //915 f13452g
    var walletBackStatus = WalletBackupStatus.NA

    var application: Application

    //f12817t
    //915 f13465t
    var walletObserver: WalletObserver = object : WalletObserver {

        /* renamed from: a */
        override fun mo42767a(wallet: Wallet, list: List<Address>) {
            val container = this@VCashCoreImpl.getObserverVectorContainer(ObserverType.ACCOUNT)
            for (next in container.observerList!!) {
                if (container.observerHashMap!!.containsKey(next)) {
                    (next as AccountAddressObserver).mo41743a(list)
                }
            }
        }

        /* renamed from: a */
        override fun mo42770a(izVar: Wallet, jfVar: Account) {
            val a = this@VCashCoreImpl.getObserverVectorContainer(ObserverType.ACCOUNT)
            for (next in a.observerList!!) {
                if (a.observerHashMap!!.containsKey(next)) {
                    (next as AccountAddressObserver).mo41744a(jfVar)
                }
            }
        }

        /* renamed from: b */
        override fun mo42781b(izVar: Wallet, list: List<Address>) {
            val a = this@VCashCoreImpl.getObserverVectorContainer(ObserverType.ACCOUNT)
            for (next in a.observerList!!) {
                if (a.observerHashMap!!.containsKey(next)) {
                    (next as AccountAddressObserver).mo41746b(list)
                }
            }
        }


        /* renamed from: a */
        override fun onWalletResync(wallet: Wallet, z: Boolean) {
            val container = this@VCashCoreImpl.getObserverVectorContainer(ObserverType.CORE)
            for (next in container.observerList!!) {
                if (container.observerHashMap!!.containsKey(next)) {
                    (next as CoreEventObserver).onResynched(z)
                }
            }
        }

        /* renamed from: a */
        override fun mo42765a(izVar: Wallet, uInt256: UInt256) {
            val c = izVar.getBlockChainType()
            val b = izVar.getTransactionFromAllMap(uInt256)
            val a = this@VCashCoreImpl.getObserverVectorContainer(ObserverType.TX)
            for (next in a.observerList!!) {
                if (a.observerHashMap!!.containsKey(next)) {
                    (next as TransactionObserver).onTransactionSent(c, b)
                }
            }
        }

        /* renamed from: c */
        override fun onTransactionsConfirmed(wallet: Wallet, list: List<Transaction>) {
            val blockChainType = wallet.getBlockChainType()
            val container = this@VCashCoreImpl.getObserverVectorContainer(ObserverType.TX)
            for (observer in container.observerList!!) {
                if (container.observerHashMap!!.containsKey(observer)) {
                    (observer as TransactionObserver).onTransactionsConfirmed(blockChainType, list)
                }
            }
        }

        /* renamed from: d */
        override fun mo42785d(izVar: Wallet, list: List<Transaction>) {
            val c = izVar.getBlockChainType()
            val a = this@VCashCoreImpl.getObserverVectorContainer(ObserverType.TX)
            for (next in a.observerList!!) {
                if (a.observerHashMap!!.containsKey(next)) {
                    (next as TransactionObserver).mo39605b(c, list)
                }
            }
        }

        override fun mo42766a(izVar: Wallet, dhVar: Transaction) {
            val c = izVar.getBlockChainType()
            val a = this@VCashCoreImpl.getObserverVectorContainer(ObserverType.TX)
            val vector = Vector<Transaction>(1)
            vector.add(dhVar)
            for (next in a.observerList!!) {
                if (a.observerHashMap!!.containsKey(next)) {
                    (next as TransactionObserver).onTransactionsReceived(c, vector)
                }
            }
        }


//        /* renamed from: b */
        override fun mo42780b(izVar: Wallet, dhVar: Transaction) {
            val c = izVar.getBlockChainType()
            val a = this@VCashCoreImpl.getObserverVectorContainer(ObserverType.TX)
            for (next in a.observerList!!) {
                if (a.observerHashMap!!.containsKey(next)) {
                    (next as TransactionObserver).onTransactionAbanded(c, dhVar)
                }
            }
        }

        /* renamed from: a */
        override fun OnUpdateWallTypeSuccess(izVar: Wallet, jeVar: WalletType) {
//            if (izVar !== this@VCashCoreImpl.f12815r) {
//                val a = this@VCashCoreImpl.getObserverVectorContainer(ObserverType.CORE)
//                for (next in a.observerList!!) {
//                    if (a.observerHashMap!!.containsKey(next)) {
//                        (next as CoreEventObserver).mo39598a(jeVar)
//                    }
//                }
//            }
        }

        /* renamed from: a */
        override fun onUpdateBlock(
            izVar: Wallet,
            blockInfo: BlockInfo?,
            list: List<Transaction>,
            hashMap: HashMap<CTxDestination, Address>
        ) {
            val c = izVar.getBlockChainType()
            val a = this@VCashCoreImpl.getObserverVectorContainer(ObserverType.BLOCK)
            for (next in a.observerList!!) {
                if (a.observerHashMap!!.containsKey(next)) {
                    (next as BlockChainObserver).onUpdateBlock(c, blockInfo, list, hashMap)
                }
            }
        }

        /* renamed from: a */
        override fun onCurBlockNoChange(wallet: Wallet, i: Int, cur: Int, max: Int) {
            val blockChainType = wallet.getBlockChainType()
            val observerVectorContainer = this@VCashCoreImpl.getObserverVectorContainer(ObserverType.BLOCK)
            for (next in observerVectorContainer.observerList!!) {
                if (observerVectorContainer.observerHashMap!!.containsKey(next)) {
                    (next as BlockChainObserver).onCurBlockNoChange(blockChainType, i, cur, max)
                }
            }
        }
//
//        /* renamed from: a */
        override fun onBlockNoUpdate(izVar: Wallet, ioVar: ChainSyncStatus, blockHeader: BlockHeader?, i: Int, ioVar2: ChainSyncStatus) {
            val c = izVar.getBlockChainType()
            val a = this@VCashCoreImpl.getObserverVectorContainer(ObserverType.BLOCK)
            for (next in a.observerList!!) {
                if (a.observerHashMap!!.containsKey(next)) {
                    (next as BlockChainObserver).onBlockNoUpdate(c, ioVar, blockHeader, i)
                }
            }
        }

        /* renamed from: a */
        override fun mo42768a(
            izVar: Wallet,
            list: List<BlockHeader>,
            list2: List<Transaction>,
            hashMap: HashMap<CTxDestination, Address>
        ) {
            val c = izVar.getBlockChainType()
            val a = this@VCashCoreImpl.getObserverVectorContainer(ObserverType.BLOCK)
            for (next in a.observerList!!) {
                if (a.observerHashMap!!.containsKey(next)) {
                    (next as BlockChainObserver).onTransactionsInvalid(c, list, list2, hashMap)
                }
            }
        }

//        /* renamed from: a */
        override fun mo42774a(wallet: Wallet, peerManagerStatus: PeerManager.PeerManagerStatus) {
            val container = this@VCashCoreImpl.getObserverVectorContainer(ObserverType.NETWORK)
            for (next in container.observerList!!) {
                if (container.observerHashMap!!.containsKey(next)) {
                    (next as NetworkObserver).onPeerManagerStatusChange(peerManagerStatus)
                }
            }
        }

        /* renamed from: a */
        override fun onConnectStatusChange(wallet: Wallet, peer: Peer, peerStatus: Peer.PeerStatus) {
            val container = this@VCashCoreImpl.getObserverVectorContainer(ObserverType.NETWORK)
            for (observer in container.observerList!!) {
                if (container.observerHashMap!!.containsKey(observer)) {
                    (observer as NetworkObserver).onConnectStatusChange(peer, peerStatus)
                }
            }
        }
//
//        /* renamed from: a */
        override fun onMainPeerChange(peerManager: PeerManager, peer: Peer) {
            val a = this@VCashCoreImpl.getObserverVectorContainer(ObserverType.NETWORK)
            for (next in a.observerList!!) {
                if (a.observerHashMap!!.containsKey(next)) {
                    (next as NetworkObserver).onMainPeerChange(peerManager, peer)
                }
            }
        }

        override fun onMessageReceived(wallet: Wallet, peer: Peer, message: Message) {
            val container = this@VCashCoreImpl.getObserverVectorContainer(ObserverType.ASYNC_MSG)
            for (next in container.observerList!!) {
                if (container.observerHashMap!!.containsKey(next)) {
                    (next as ASyncMessageObserver).onMessageReceived(message)
                }
            }
        }
//
//        /* renamed from: a_ */
        override fun mo43914a_() {
//            val a = this@VCashCoreImpl.getObserverVectorContainer(ObserverType.TX)
//            for (next in a.observerList) {
//                if (a.observerHashMap!!.containsKey(next)) {
//                    (next as TransactionObserver).mo39601a()
//                }
//            }
        }
    }

    override fun getAppWalletPath(): String {
        return this.appFolderPath
    }

    override fun getInitInfo(): InitInfo {
        return initInfo;
    }

    override fun getNewHDSeed(): HDSeed {
       return  HDSeed.getNewHDSeed()
    }

    //mo43801a
    @Synchronized
    override fun checkAndSetWallType(walletType: WalletType): Boolean {
        checkWalletInit()
        return this.vWallet!!.checkAndUpdateWallType(walletType, *BooleanArray(0))
    }


    //mo43786a
    override fun changeWalletPassword(charSequence: CharSequence, charSequence2: CharSequence) {
        checkWalletInit()
        if (this.vWallet!!.getSelfWalletType() !== WalletType.UNKNOWN) {
            this.vWallet!!.changeWalletPwd(charSequence, charSequence2)
            return
        }
        throw IllegalStateException("Wallet type not confirmed!")
    }

    @Synchronized
    override fun initHDAccountFromWordList(charSequence: CharSequence, strArr: Array<String>, vararg iArr: Int): HDAccount? {
        val hdAccount: HDAccount
        checkWalletInit()
        hdAccount = HDAccount(this.vWallet!!.getPersonalDB().getSelfHDAccountTable() as WalletTable, charSequence, strArr, *iArr)
        if (hdAccount.IsHDAccountInitSuccess()) {
            this.vWallet!!.getSelfAccountModel().mo44410a(hdAccount as Account, charSequence)
        } else {
            throw IllegalArgumentException("Invalidate hd account.")
        }
        return hdAccount
    }

    @Synchronized
    override fun InitHDAccount(charSequence: CharSequence, hDSeed: HDSeed, vararg iArr: Int): HDAccount {
        val hdAccount: HDAccount
        checkWalletInit()
        hdAccount = HDAccount(this.vWallet!!.getPersonalDB().getSelfHDAccountTable() as WalletTable, charSequence, hDSeed,*iArr)
        if (hdAccount.IsHDAccountInitSuccess()) {
            this.vWallet!!.getSelfAccountModel().mo44410a(hdAccount as Account, charSequence)
        } else {
            throw IllegalArgumentException("Invalidate hd account.")
        }
        return hdAccount
    }

    //mo43766a
    override fun createAnonymousAccount(charSequence: CharSequence, bArr: ByteArray, vararg charSequenceArr: CharSequence): Address {
        checkWalletInit()
        return (this.vWallet!!.getSelfAccountModel() as VAccountModel).mo42668i().createAddress(charSequence, 1, *charSequenceArr)!!.get(0)
    }

    //mo43901J
    //915 mo44213L
    fun lockWallets() {
        this.vWallet!!.lock()
        //比特币钱包
        //this.f12815r.lock()
    }

    //mo43902K
    //915 mo44214M
    fun unlockWallets() {
        this.vWallet!!.unLock()
        //this.f12815r.unLock()
    }

    //mo43868h
    override fun getCurBlockInfoByType(vararg blockChainType: BLOCK_CHAIN_TYPE): BlockInfo? {
        return if (getWalletByChainType(*blockChainType) == null || getWalletByChainType(*blockChainType).getSelfBlockChainModel() == null) {
            null
        } else getWalletByChainType(*blockChainType).getSelfBlockChainModel().getCurBlockInfo()
    }

    //mo43850e
    override fun getMaxBlockNo(vararg blockChainType: BLOCK_CHAIN_TYPE): Long {
        return getWalletByChainType(*blockChainType).getSelfBlockChainModel().getMaxBlockNo()
    }

    override fun getCurBlockNo(vararg igVarArr: BLOCK_CHAIN_TYPE): Long {
        val wallet = getWalletByChainType(*igVarArr)
        val sb = StringBuilder()
        sb.append("getBlockHeight type->")
        sb.append(igVarArr)
        sb.append(";wallet-->")
        sb.append(wallet)
        Log.d("VCWalletCore", sb.toString())
        return wallet.getSelfBlockChainModel().getCurrentBlockNo().toLong()
    }

    override fun getVidAddressList(z: Boolean): List<Address> {
        return this.vWallet!!.getSelfAddressModel().getVidAddress(z)
    }

    fun mo43907e(jjVar: Address): Transaction {
        checkWalletInit()
        return this.vWallet!!.getSelfTransactionModel().mo44476a(jjVar)
    }


    //C3871a
    inner class ObserverVectorContainer() {

        //observerHashMap!!
        var observerHashMap: LinkedHashMap<*, *>? = null

        //observerList
        var observerList: List<Any>? = null
    }

    //m11792a
    private fun getObserverHashMap(observerType: ObserverType): LinkedHashMap<*, *>? {
        when (observerType) {
            ObserverType.CORE -> return this.coreEventObserverLinkedHashMap
            ObserverType.ACCOUNT -> return this.accountAddressObserverLinkedHashMap
            ObserverType.BLOCK -> return this.blockChainObserverMap
            ObserverType.NETWORK -> return this.networkObserverMap
            ObserverType.TX -> return this.transactionObserverMap
            ObserverType.ASYNC_MSG -> return this.asyncMsgObserverMap
            else -> return null
        }
    }

    //m11800b
    fun getObserverVectorContainer(observerType: ObserverType): ObserverVectorContainer {
        val container = ObserverVectorContainer()
        this.lock.lock()
        container.observerHashMap = getObserverHashMap(observerType)
        container.observerList = ArrayList(container.observerHashMap!!.values)
        this.lock.unlock()
        return container
    }

    private fun m11784R() {
        val b = getObserverVectorContainer(ObserverType.CORE)
        for (next in b.observerList!!) {
            if (b.observerHashMap!!.containsKey(next)) {
                (next as CoreEventObserver).mo39596a()
            }
        }
    }

    private fun m11785S() {
        val container = getObserverVectorContainer(ObserverType.CORE)
        for (next in container.observerList!!) {
            if (container.observerHashMap!!.containsKey(next)) {
                (next as CoreEventObserver).mo39600b()
            }
        }
    }

    //mo43822b
    override fun checkAndStartPeerManagerNetwork(): Boolean {
        if (!isInitSuccess() || this.walletBackStatus !== WalletBackupStatus.NA || getWalletType() !== WalletType.HOT) {
            return false
        }
        this.vWallet?.startPeerManagerNetwork()
        return true
    }

    override fun getWalletLabel(): String? {
        return this.vWallet?.getLabel()
    }

    override fun setWalletLabel(str: String) {
        this.vWallet!!.setLabel(str)
    }

    //mo43795a
    //915 mo44106a
    override fun InitWalletIfNeed(insertOrUpdateCurrentWalletType: Boolean) {
        checkWalletInit()
        if (this.walletBackStatus === WalletBackupStatus.NA) {
            lockWallets()
            this.initInfo.initInfo = InitInfo.InitInfoEnum.DESTROYING
            m11784R()
            //比特币共振钱包
            //this.f12815r.InitWallet(insertOrUpdateCurrentWalletType)
            this.vWallet!!.clearWallet(insertOrUpdateCurrentWalletType)
            this.initInfo.initInfo = InitInfo.InitInfoEnum.INIT_SUCCESS
            unlockWallets()
            m11785S()
            return
        }
        throw IllegalStateException("Backing up wallet.")
    }

    //915 mo44037K
    override fun destoryWalletAndDB() {
        checkWalletInit()
        if (this.walletBackStatus === WalletBackupStatus.NA) {
            lockWallets()
            this.initInfo.initInfo = InitInfo.InitInfoEnum.DESTROYING
            this.initInfo.initInfo = InitInfo.InitInfoEnum.NO_INIT
            //this.f13463r.mo44383U()
            this.vWallet!!.checkAndStopPeer()
//            val Q = this.f13463r.mo44380Q()
//            if (Q != null) {
//                Q!!.close()
//            }
//            val P = this.f13463r.mo44379P()
//            if (P != null) {
//                P!!.close()
//            }
            val personalDB = this.vWallet!!.getPersonalDB()
            if (personalDB != null) {
                personalDB.close()
            }
            val transactionDB = this.vWallet!!.getSelfTransactionDB()
            if (transactionDB != null) {
                transactionDB.close()
            }
            unlockWallets()
            //this.f13463r = null
            this.vWallet = null
            return
        }
        throw IllegalStateException("Backing up wallet.")
    }

    //m11815i
    private fun checkCount(i: Int) {
        if (i <= 0) {
            val sb = StringBuilder()
            sb.append("Invalidate count: ")
            sb.append(i)
            throw IllegalArgumentException(sb.toString())
        }
    }

    //915 mo44110a
    override fun checkMatchPassword(charSequence: CharSequence): Boolean {
        return if (!isInitSuccess()) {
            false
        } else this.vWallet!!.checkMatchPassword(charSequence)
    }

    @Synchronized
    override fun createNormalAddress(charSequence: CharSequence, amount: Int, vararg labelArr: CharSequence?): List<Address> {
        checkWalletInit()
        checkCount(amount)
        return this.vWallet!!.getSelfAddressModel().createAddress(charSequence, amount, *labelArr)
    }

    //mo43866h
    override fun getHDAccountList(): List<HDAccount>? {
        return if (!isInitSuccess()) {
            null
        } else this.vWallet!!.getSelfAccountModel().getHDAccountVector().toList()
    }

    //mo43853e
    override fun isInitSuccess(): Boolean {
        return this.initInfo.initInfo === InitInfo.InitInfoEnum.INIT_SUCCESS
    }

    //mo43858f
    override fun hasSetWalletPwd(): Boolean {
        return if (!isInitSuccess()) {
            false
        } else this.vWallet!!.hasSetPwd()
    }

    //mo43763a
    override fun getWalletType(): WalletType {
        return if (!isInitSuccess()) {
            WalletType.UNKNOWN
        } else this.vWallet!!.getWalletType()
    }

    override fun getFileRootDir(): String {
        return this.vWallet!!.getFileRootDir()
    }

    override fun mo43880l(): String {
        return this.vWallet!!.getDbPath()
    }

    constructor(context: Context){
        this.application = ThreadToolkit.getApplicationContext(context)
    }

    //m11780N
    //915 m12337O
    private fun checkWalletInit() {
        if (this.initInfo.initInfo !== InitInfo.InitInfoEnum.INIT_SUCCESS) {
            throw IllegalStateException("Wallet not init yet.")
        }
    }

    override fun fixWalletFullPublicKey(pwd:String){
        checkWalletInit()
        getWalletByChainType().selfAddressModel.checkAndFixFullPubKey(pwd)
    }

    //m11797a
    fun noticeHandleInitInfoEnum(initInfoType: InitInfo.InitInfoEnum, errorCode: Int, error: String) {
        val initInfo1 = this.initInfo
        initInfo1.initInfo = initInfoType
        initInfo1.errorCode = errorCode
        initInfo1.error = error
        val container = getObserverVectorContainer(ObserverType.CORE)
        for (next in container.observerList!!) {
            if (container.observerHashMap!!.containsKey(next)) {
                (next as CoreEventObserver).handleInitInfoEnum(this.initInfo)
            }
        }
    }

    @Synchronized
    override fun importPrivateKey(privateKeyStr: String, pwd: String): List<Address>? {
        checkWalletInit()
        val arrayList = java.util.ArrayList<Address>()
        try {
            val chainParams = DumpedPrivateKeyFactory.m12067a(privateKeyStr, this.vWallet!!.getChainParams())
            if (chainParams != null) {
                if (chainParams is VChainParam) {
                    val resolvedAddressInfo = ResolvedAddressInfo()
                    if (AddressUtils.isAnonymousAddress(privateKeyStr as CharSequence, chainParams as VChainParam, resolvedAddressInfo)) {
                        if (resolvedAddressInfo.cPrivateKey != null) {
                            val address = this.vWallet!!.getSelfWalletHelper().mo42408a(resolvedAddressInfo.cPrivateKey, pwd as CharSequence)
                            this.vWallet!!.mo41187a(address, pwd as CharSequence)
                            if (address != null) {
                                address.updateCategoryStatus(1, true)
                                arrayList.add(address)
                            }
                        } else {
                            throw AddressFormatException(
                                String.format(
                                    Locale.getDefault(),
                                    "%s was not an private key",
                                    *arrayOf<Any>(privateKeyStr)
                                )
                            )
                        }
                    }
                }
                val addressList = this.vWallet!!.getSelfWalletHelper().getAddressListFromPrivateKey(DumpedPrivateKeyFactory.checkAndGetDecodedPrivateKeyInfo(chainParams, privateKeyStr).getPrivateKey(), pwd)
                if (addressList != null) {
                    for (address in addressList) {
                        if (addressList.indexOf(address) == 0) {
                            address.updateIsHide(false)
                        } else {
                            address.updateIsHide(true)
                        }
                        address.updateCategoryStatus(1, true)
                    }
                }
                this.vWallet!!.mo44048a(addressList, pwd)
                return addressList
            }
            val sb = StringBuilder()
            sb.append("Unsupported private key format: ")
            sb.append(privateKeyStr)
            throw AddressFormatException(sb.toString())
        } catch (e: EncryptException) {
            throw AddressFormatException(e as Throwable)
        }
    }

    override fun mo43828c(address: Address): ComplexBitcoinAddress {
        return (getWalletByChainType(BLOCK_CHAIN_TYPE.VCASH).getSelfAddressModel() as VAddressModel).mo42675m(address)
    }

    override fun importBIP38PrivateKey(str: String, str2: String, pwd: String): List<Address> {
        checkWalletInit()
        return this.vWallet!!.mo44046a(str, str2, pwd)
    }

    override fun getAddressBIP38PrivateKey(address: Address, str: String, str2: String): String {
        return this.vWallet!!.getAddressPrivateKey(address.getCTxDestination(), str, str2)
    }



    override fun getAddressOrginPrivateKey(addressString: String, pwd: String): String {
        val address = getAddressFromAddressString(addressString)
        if (address != null) {
            return address.getOrginPrivateKey(pwd as CharSequence)
        }
        val sb = StringBuilder()
        sb.append("Can not found ")
        sb.append(addressString)
        sb.append(" for wallet.")
        throw AddressNotFoundException(sb.toString())
    }

    override fun getAddressPrivateKey(addressString: String, pwd: String, vararg igVarArr: BLOCK_CHAIN_TYPE): String {
        val address = getAddressFromAddressString(addressString)
        if (address != null) {
            return address.getBase58PrivateKey(pwd as CharSequence, *igVarArr)
        }
        val sb = StringBuilder()
        sb.append("Can not found ")
        sb.append(addressString)
        sb.append(" for wallet.")
        throw AddressNotFoundException(sb.toString())
    }

    override fun getWalletChainParams(vararg igVarArr: BLOCK_CHAIN_TYPE): ChainParams {
        return getWalletByChainType(*igVarArr).getChainParams()
    }

    //mo43843d
    override fun getBlockChainSyncStatus(vararg igVarArr: BLOCK_CHAIN_TYPE?): ChainSyncStatus {
        return getWalletByChainType(*igVarArr).getChainSyncStatus()
    }

    override fun signMessageWithAddress(addressString: CharSequence, message: CharSequence, pwd: CharSequence): String {
        checkWalletInit()
        val address = this.vWallet!!.getAddressFromUsingAddressMap(addressString)
        if (address != null) {
            return address.getSignMessageBase64(message, pwd)
        }
        val sb = StringBuilder()
        sb.append(addressString)
        sb.append(" not found!")
        throw AddressNotFoundException(sb.toString())
    }

    //mo43734a
    override fun calMinFee(
        checkMaxFee: Boolean,
        list: List<COutPoint>,
        list2: List<AddressMoneyInfo>,
        j: Long,
        vOutList: VOutList?,
        lvVar: ScriptList?,
        vararg igVarArr: BLOCK_CHAIN_TYPE
    ): Long {
        return getWalletByChainType(*igVarArr).getSelfTransactionModel().calMinFee(checkMaxFee, list, list2, j, vOutList, lvVar)
    }

    //mo43886o
    override fun isValidAddress(addressString: String): Boolean {
        var z = false
        if (TextUtils.isEmpty(addressString)) {
            return false
        }
        if (!AddressUtils.m955c(addressString, getWalletByChainType().getChainParams())) {
        }
        z = true
        return z
    }

    override fun getAddressTypeByAddressString(str: String): AddressType {
        var addressType = AddressType.UNKNOWN
        try {
            addressType = AddressUtils.m952b(str as CharSequence, this.vWallet as Wallet).addressType
        } catch (e2: AddressFormatException) {
            e2.printStackTrace()
        }

        return addressType
    }

    //mo43905a
    //915 mo44217a
    fun InitWallet(wallet: Wallet?) {
        val sb = StringBuilder()
        sb.append("start init wallet: ")
        sb.append(wallet!!.javaClass.getName())
        Log.i("wallet", sb.toString())
        val currentTimeMillis = System.currentTimeMillis()
        if (wallet != null) {
            if (wallet === this.vWallet) {
                // wallet!!.initAppFileDirectory(this.appFolderPath, this.f12815r, this.walletObserver)
                wallet!!.initAppFileDirectory(this.appFolderPath,this.walletObserver)
            } else {
                wallet!!.initAppFileDirectory(this.appFolderPath, this.walletObserver)
            }
        }
        val currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis
        val sb2 = StringBuilder()
        sb2.append("wallet init used: ")
        sb2.append(currentTimeMillis2)
        Log.i("wallet", sb2.toString())
        val j = 0 + currentTimeMillis2
        val currentTimeMillis3 = System.currentTimeMillis()
        wallet!!.initWalletAndData()
        val currentTimeMillis4 = System.currentTimeMillis() - currentTimeMillis3
        val sb3 = StringBuilder()
        sb3.append("wallet load used: ")
        sb3.append(currentTimeMillis4)
        Log.i("wallet", sb3.toString())
        val j2 = j + currentTimeMillis4
        val sb4 = StringBuilder()
        sb4.append("wallet total init used: ")
        sb4.append(j2)
        Log.i("wallet", sb4.toString())
    }

    //915 m12378u
    fun initAppConfigAndSettingWithPath(path: String) {
        val sb = StringBuilder()
        sb.append("beginInit: ")
        sb.append(path)
        Log.i("VCWalletCore", sb.toString())
        object : Thread() {
            override fun run() {
                var versionCode: Int
                try {
                    ECC.init()
                    Db.initPubKeybytes()
                    val packageName = this@VCashCoreImpl.application.packageName
                    this@VCashCoreImpl.appFolderPath = path
                    if (!FileToolkit.checkDirectory(this@VCashCoreImpl.appFolderPath, false)) {
                        val coreImpl = this@VCashCoreImpl
                        val initInfoType = InitInfo.InitInfoEnum.INIT_FAILED
                        val sb2 = StringBuilder()
                        sb2.append("create application folder [")
                        sb2.append(this@VCashCoreImpl.appFolderPath)
                        sb2.append("] failed, do you have the permission for read wirte storage?")
                        coreImpl.noticeHandleInitInfoEnum(initInfoType, 1, sb2.toString())
                        return
                    }
                    val sb2 = StringBuilder()
                    sb2.append(this@VCashCoreImpl.appFolderPath)
                    sb2.append("/logs")
                    val sb3 = sb2.toString()
                    FileToolkit.checkDirectory(sb3, false)
                    val sb4 = StringBuilder()
                    sb4.append(sb3)
                    sb4.append("/appLog.log")
                    vdsMain.Log.createLogFile(this@VCashCoreImpl.application as Context, sb4.toString())
                    if (this@VCashCoreImpl.vWallet == null) {
                        this@VCashCoreImpl.vWallet = VWallet(this@VCashCoreImpl.application)
                    }
//                    if (this@VCashCoreImpl.f13463r == null) {
//                        this@VCashCoreImpl.f13463r = bsc(this@VCashCoreImpl.f13449d, this@VCashCoreImpl.f13462q)
//                    }
                    val spUtils = SPUtils.getSPUtils()
                    spUtils.startSPEdit("config", this@VCashCoreImpl.application as Context)
                    val banVer = spUtils.getInt("ban_ver", 0)
                    try {
                        versionCode = this@VCashCoreImpl.application.getPackageManager().getPackageInfo(packageName, 0).versionCode
                    } catch (e: PackageManager.NameNotFoundException) {
                        e.printStackTrace()
                        versionCode = 0
                    }

                    if (banVer != versionCode) {
                        val sb5 = StringBuilder()
                        sb5.append(this@VCashCoreImpl.appFolderPath)
                        sb5.append(File.separator)
                        sb5.append("bitcoin")
                        sb5.append(File.separator)
                        sb5.append("banlist")
                        FileUtils.isFileExist(sb5.toString())
                        val sb6 = StringBuilder()
                        sb6.append(this@VCashCoreImpl.appFolderPath)
                        sb6.append(File.separator)
                        sb6.append("vds")
                        sb6.append(File.separator)
                        sb6.append("banlist")
                        FileUtils.isFileExist(sb6.toString())
                        spUtils.putInt("ban_ver", versionCode)
                    }
                    this@VCashCoreImpl.InitWallet(this@VCashCoreImpl.vWallet as Wallet)
                    //this@VCashCoreImpl.mo44217a(this@VCashCoreImpl.f13463r as Wallet)
                    this@VCashCoreImpl.vWallet!!.updateWalletVid()
                    this@VCashCoreImpl.vWallet!!.addWalletObserver(this@VCashCoreImpl.walletObserver)
                    //this@VCashCoreImpl.f13462q.mo42673al().mo42954a(this@VCashCoreImpl.f13466u)
                    vdsMain.Log.info("VCWalletCore", "app init success")
                    this@VCashCoreImpl.noticeHandleInitInfoEnum(InitInfo.InitInfoEnum.INIT_SUCCESS, 0, "")
                } catch (e2: Exception) {
                    e2.printStackTrace()
                    vdsMain.Log.LogObjError(this@VCashCoreImpl.application as Any, "init app failed: ", e2 as Throwable)
                    this@VCashCoreImpl.noticeHandleInitInfoEnum(InitInfo.InitInfoEnum.INIT_FAILED, 0, StringToolkit.m11523a(e2 as Throwable))
                }

            }
        }.start()
    }

    //m11779M
    fun InitAppConfigAndSetting() {
        object : Thread() {
            override fun run() {
                var versionCode: Int
                try {
                    ECC.init()
                    Db.initPubKeybytes()
                    //val packageName = this@VCashCoreImpl.application.packageName
                    //不用包名，改固定路径
                    val packageName = Constant.walletConstPath
                    val coreImp = this@VCashCoreImpl
                    val sb = StringBuilder()
                    sb.append(FileToolkit.getVaildPath(Environment.getExternalStorageDirectory().absolutePath))
                    sb.append("/")
                    sb.append(packageName)
                    coreImp.appFolderPath = sb.toString()
                    if (!FileToolkit.checkDirectory(this@VCashCoreImpl.appFolderPath, false)) {
                        val coreImpl = this@VCashCoreImpl
                        val initInfoType = InitInfo.InitInfoEnum.INIT_FAILED
                        val sb2 = StringBuilder()
                        sb2.append("create application folder [")
                        sb2.append(this@VCashCoreImpl.appFolderPath)
                        sb2.append("] failed, do you have the permission for read wirte storage?")
                        coreImpl.noticeHandleInitInfoEnum(initInfoType, 1, sb2.toString())
                        return
                    }
                    val sb3 = StringBuilder()
                    sb3.append(this@VCashCoreImpl.appFolderPath)
                    sb3.append("/logs")
                    val logsPath = sb3.toString()
                    FileToolkit.checkDirectory(logsPath, false)
                    val appLogFilePath = StringBuilder()
                    appLogFilePath.append(logsPath)
                    appLogFilePath.append("/appLog.log")
                    vdsMain.Log.createLogFile(this@VCashCoreImpl.application as Context, appLogFilePath.toString())
                    if (this@VCashCoreImpl.vWallet == null) {
                        this@VCashCoreImpl.vWallet = VWallet(this@VCashCoreImpl.application)
                    }
                    //比特币钱包
//                    if (this@VCashCoreImpl.f12815r == null) {
//                        this@VCashCoreImpl.f12815r = bpv(this@VCashCoreImpl.application, this@VCashCoreImpl.vWallet)
//                    }
                    val spUtils = SPUtils.getSPUtils()
                    spUtils.startSPEdit("config", this@VCashCoreImpl.application as Context)
                    val ban_ver = spUtils.getInt("ban_ver", 0)
                    try {
                        versionCode = this@VCashCoreImpl.application.packageManager.getPackageInfo(packageName, 0).versionCode
                    } catch (e: PackageManager.NameNotFoundException) {
                        e.printStackTrace()
                        versionCode = 0
                    }

                    if (ban_ver != versionCode) {
                        val sb6 = StringBuilder()
                        sb6.append(this@VCashCoreImpl.appFolderPath)
                        sb6.append(File.separator)
                        sb6.append("bitcoin")
                        sb6.append(File.separator)
                        sb6.append("banlist")
                        FileUtils.isFileExist(sb6.toString())
                        val sb7 = StringBuilder()
                        sb7.append(this@VCashCoreImpl.appFolderPath)
                        sb7.append(File.separator)
                        sb7.append("vds")
                        sb7.append(File.separator)
                        sb7.append("banlist")
                        FileUtils.isFileExist(sb7.toString())
                        spUtils.putInt("ban_ver", versionCode)
                    }
                    this@VCashCoreImpl.InitWallet(this@VCashCoreImpl.vWallet as Wallet)
                    //初始比特币钱包
                    //this@VCashCoreImpl.InitWallet(this@VCashCoreImpl.f12815r as Wallet)
                    this@VCashCoreImpl.vWallet!!.updateWalletVid()
                    this@VCashCoreImpl.vWallet!!.addWalletObserver(this@VCashCoreImpl.walletObserver)
                    //this@VCashCoreImpl.vWallet.mo42393ac().mo42661a(this@VCashCoreImpl.f12818u)
                    vdsMain.Log.info("VCWalletCore", "app init success")
                    this@VCashCoreImpl.noticeHandleInitInfoEnum(InitInfo.InitInfoEnum.INIT_SUCCESS, 0, "")
                } catch (e2: Exception) {
                    e2.printStackTrace()
                    vdsMain.Log.LogObjError(this@VCashCoreImpl.application as Any, "init app failed: ", e2 as Throwable)
                    this@VCashCoreImpl.noticeHandleInitInfoEnum(InitInfo.InitInfoEnum.INIT_FAILED, 0, StringToolkit.m11523a(e2 as Throwable))
                }

            }
        }.start()
    }

    private fun m11817j(vararg igVarArr: BLOCK_CHAIN_TYPE): BLOCK_CHAIN_TYPE {
        return if (igVarArr.size == 0) {
            BLOCK_CHAIN_TYPE.VCASH
        } else igVarArr[0]
    }

    override fun getWalletAddressModel(): AddressModel {
        return this.vWallet!!.getSelfAddressModel()
    }

    //mo43871i
    //915 mo44182i
    override fun getWalletByChainType(vararg blockChainTypes: BLOCK_CHAIN_TYPE?): Wallet {
        val b= arrayListOf<BLOCK_CHAIN_TYPE>()
        for(blockChainType in blockChainTypes){
            if(blockChainType!=null){
                b.add(blockChainType)
            }
        }
        val arr= emptyArray<BLOCK_CHAIN_TYPE>()
        b.toArray(arr)
        when (m11817j(*arr)) {
            BLOCK_CHAIN_TYPE.VCASH,BLOCK_CHAIN_TYPE.ETH -> return this.vWallet!!
//            BLOCK_CHAIN_TYPE.BITCOIN -> return this.f12815r
            else -> return this.vWallet!!
        }
    }

    override fun getVibInfo(): VibInfo? {
        return this.vWallet!!.getSelfVibModel().getVibInfo()
    }

    //mo43719B
    override fun getVidLockBlockNumber(): Long {
        checkWalletInit()
        return this.vWallet!!.getChainParams().vidLockBlockNumber
    }

    override fun getTransactionFromAllTransactionMap(uInt256: UInt256, vararg igVarArr: BLOCK_CHAIN_TYPE): Transaction? {
        return getWalletByChainType(*igVarArr).getSelfTransactionModel().getTransactionFromAllTransactionMap(uInt256)
    }

    override fun mo43797a(uInt256: UInt256, igVar: BLOCK_CHAIN_TYPE): Boolean {
        checkWalletInit()
        return getWalletByChainType(igVar).getSelfTransactionModel().mo44532g(uInt256)
    }


    override fun getRemarkByTxid(txid: UInt256, blockChainType: BLOCK_CHAIN_TYPE): String? {
        checkWalletInit()
        return getWalletByChainType(blockChainType).getSelfTransactionModel().getRemarkByTxid(txid)
    }

    //mo43908s
    fun getCTxDestinationFromAddressString(str: String): CTxDestination? {
        var cTxDestination = AddressUtils.getDesFromAddressString(str as CharSequence, this.vWallet as Wallet)
//        if (a == null || a is CNoDestination) {
//            a = AddressUtils.m936a(str as CharSequence, this.f12815r as Wallet)
//        }
        return if (cTxDestination is CNoDestination) {
            null
        } else cTxDestination
    }

    //mo43781a
    override fun replaceToTxMarkTable(uInt256: UInt256, str: String, igVar: BLOCK_CHAIN_TYPE) {
        checkWalletInit()
        getWalletByChainType(igVar).getSelfTransactionModel().replaceToTxMarkTable(uInt256, str)
    }



    override fun checkAndSendTransactionToPeers(transaction: Transaction, charSequence: CharSequence, vararg igVarArr: BLOCK_CHAIN_TYPE): TxSendResult {
        return getWalletByChainType(*igVarArr).getSelfTransactionModel().checkPwdAndSendTransactionToPeer(transaction, charSequence)
    }

    override fun mo43836c(str: String, igVar: BLOCK_CHAIN_TYPE) {
        getWalletByChainType(igVar).getSelfTransactionModel().mo44523d(str)
    }


    override fun checkAndRecycleAddress(str: String, address: Address) {
        checkWalletInit()
        if (checkMatchPassword(str as CharSequence)) {
            getWalletByChainType(*arrayOfNulls<BLOCK_CHAIN_TYPE>(0)).getSelfAddressModel().checkAndRecycleAddress(address)
        }
    }

    override fun getLabelByAddressString(str: String): String {
        checkWalletInit()
        return this.vWallet!!.getSelfAddressModel().getLabelByAddressString(str)
    }

    //mo43833c
    //915 mo44144c
    override fun getAddressFromAddressString(str: String, vararg igVarArr: BLOCK_CHAIN_TYPE): Address? {
        val cTxDestination: CTxDestination?
        if (!isInitSuccess()) {
            return null
        }
        val wallet = getWalletByChainType(*igVarArr)
        if (igVarArr.size == 0) {
            cTxDestination = getCTxDestinationFromAddressString(str)
        } else {
            cTxDestination = AddressUtils.m938a(str, wallet.getChainParams())
        }
        if (cTxDestination == null || cTxDestination is CNoDestination) {
            return null
        }
        var address = wallet.getAddressByCTxDestinationFromArrayMap(cTxDestination)
        if (address == null) {
            address = wallet.getAddressFromShadowMap(cTxDestination)
        }
        return address
    }

    //mo43793a
    override fun updateAddressIsHideAndOperateSpareArr(jjVar: Address, z: Boolean) {
        this.vWallet!!.getSelfAddressModel().updateAddressIsHideAndOperateSpareArr(jjVar, z)
    }

    override fun createClueTransactionAndUpdate(
        utxoList: List<Utxo>,
        spendUtxoList: List<Utxo>,
        cluePreCreateTreeMssage: CluePreCreateTreeMssage,
        address: Address,
        firstParentAddressString: String,
        label: String,
        vidGroup: VidGroup?,
        pwd: CharSequence,
        vararg blockChainTypes: BLOCK_CHAIN_TYPE
    ) {
        checkWalletInit()
        val wallet = getWalletByChainType(*blockChainTypes)
        if (wallet.getBlockChainType() === BLOCK_CHAIN_TYPE.VCASH) {
            val txSendResult = (wallet.getSelfTransactionModel() as VTxModel).createClueTransactionAndSend(utxoList, spendUtxoList, cluePreCreateTreeMssage, firstParentAddressString, pwd)
            if (txSendResult != null && txSendResult.isSuccess) {
                address.updateClueTxidByDesHash(txSendResult.transaction.getTxidHashString())
                getWalletByChainType(*blockChainTypes).getSelfAddressModel().updateAddressLabel(address, label)
                if (vidGroup == null) {
                    address.updateNullAddressGroup()
                } else {
                    address.updateAddressGroup(vidGroup.getId())
                }
            }
        } else {
            throw IllegalArgumentException(
                String.format(
                    "Can not create v transaction for %s",
                    *arrayOf<Any>(wallet.getBlockChainType().name)
                )
            )
        }
    }

    override fun createVxdTransferInTransaction(utxoList: List<Utxo>, spendUtxoList: List<Utxo>, transferAddressMoneyInfo: java.util.ArrayList<AddressMoneyInfo>, address: Address,
                                                pwd: CharSequence, blockChainTypes: BLOCK_CHAIN_TYPE, includeFee:Boolean):Transaction?
    {
        checkWalletInit()
        val wallet = getWalletByChainType(blockChainTypes)
        if (wallet.getBlockChainType() === BLOCK_CHAIN_TYPE.VCASH) {
            val transaction = (wallet.getSelfTransactionModel() as VTxModel).getVxdTransferInTransaction(utxoList, spendUtxoList, transferAddressMoneyInfo,pwd,includeFee)
            return transaction
        } else {
            throw IllegalArgumentException(
                String.format(
                    "Can not create v transaction for %s",
                    *arrayOf<Any>(wallet.getBlockChainType().name)
                )
            )
        }
    }



    private fun m11810f(blockNo: Long): Long {
        return blockNo / 1440 * 1440 + 1
    }

    override fun getVidRewardInfo(list: List<Address>?, igVar: BLOCK_CHAIN_TYPE): Map<Int, Any>? {
        if (list == null || list.isEmpty()) {
            return null
        }
        val hashMap = HashMap<Int,Any>()
        val curBlockNo = getCurBlockNo(BLOCK_CHAIN_TYPE.VCASH)
        val f2 = m11810f(curBlockNo)
        val transactionModel = this.vWallet!!.getSelfTransactionModel()
        val rewardTxoutMap = HashMap<String,TxOut>()
        val addressIterator: Iterator<Address> = list.iterator()
        var sumNumber: Long = 0
        var lockNumber: Long = 0
        while (addressIterator.hasNext()) {
            val address = addressIterator.next() as Address
            val allTransactionList = transactionModel.getTransactionListByConfirmType(address.getCTxDestination(), TransactionConfirmType.ALL)
            if (allTransactionList != null && !allTransactionList.isEmpty()) {
                val transIterator: Iterator<Transaction> = allTransactionList.iterator()
                while (transIterator.hasNext()) {
                    val transaction = transIterator.next()
                    if (transaction.getFlag() == 1) {
                        val txoutList = transaction.getSelfTxOutList()
                        if (!txoutList.isEmpty()) {
                            val txoutIterator: Iterator<TxOut> = txoutList.iterator()
                            while (txoutIterator.hasNext()) {
                                val txOut = txoutIterator.next()
                                if (txOut.getFlag() == 1.toShort()) {
                                    if (address.getCTxDestination().equals(txOut.getScriptCTxDestination())) {
                                        val sb = StringBuilder()
                                        sb.append(transaction.getTxidHashString())
                                        sb.append(txoutList.indexOf(txOut))
                                        rewardTxoutMap.put(sb.toString(), txOut)
                                        //if (transaction.checkAndGetBlockNoFromAll() > f2) {
                                            sumNumber += txOut.getSatoshi()
                                        //}
                                    }
                                    if ((curBlockNo - transaction.getBlockNo().toLong()) < (this.vWallet!!.getChainParams().vidLockBlockNumber)
                                            && address.getCTxDestination().equals(txOut.getScriptCTxDestination())) {
                                        lockNumber += txOut.getSatoshi()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        hashMap.put(Integer.valueOf(0), java.lang.Long.valueOf(sumNumber))
        hashMap.put(Integer.valueOf(1), java.lang.Long.valueOf(lockNumber))
        hashMap.put(Integer.valueOf(2), rewardTxoutMap.values)
        return hashMap
    }

    override fun mo43789a(list: List<ComplexBitcoinAddress>) {
        (getWalletByChainType(BLOCK_CHAIN_TYPE.VCASH).getSelfAddressModel() as VAddressModel).addAllCTxDesToCAddressMapTolist(list)
    }

    //mo43835c
    override fun checkAndStopPeer() {
        this.vWallet!!.checkAndStopPeer()
    }

    override fun getConfirmedTransactionListByBlockHash(uInt256: UInt256, vararg igVarArr: BLOCK_CHAIN_TYPE): List<Transaction> {
        return getWalletByChainType(*igVarArr).getSelfTransactionModel().getConfirmedTransactionListFromAll(uInt256)
    }

    override fun mo43809b(str: String, vararg igVarArr: BLOCK_CHAIN_TYPE): Transaction? {
        return getWalletByChainType(*igVarArr).getSelfTransactionModel().getTransactionByClueTxid(str)
    }

    override fun mo43807b(uInt256: UInt256, igVar: BLOCK_CHAIN_TYPE): RejectTx {
        checkWalletInit()
        return getWalletByChainType(igVar).mo44110f(uInt256)
    }

    override fun mo43800a(str: String, igVar: BLOCK_CHAIN_TYPE): Boolean {
        checkWalletInit()
        return getWalletByChainType(igVar).getSelfTransactionModel().mo44519c(str)
    }

    fun mo43906a(address: Address?, dhVar: Transaction) {
        if (address != null) {
            checkWalletInit()
            this.vWallet!!.getSelfAddressModel().updateDirectSignVid(address, dhVar)
        }
    }

    private fun m11812f(address: Address) {
        this.vWallet!!.getSelfAddressModel().mo43114i(address)
    }

    override fun mo43784a(observer: ImportKeyGetVidObserver) {
        checkWalletInit()
        if (getBlockChainSyncStatus(BLOCK_CHAIN_TYPE.BITCOIN) === ChainSyncStatus.SYNCHED) {
            val addressList = this.vWallet!!.getSelfAddressModel().mo43112i()
            if (addressList != null && !addressList.isEmpty()) {
                for (address in addressList) {
                    val transaction = mo43907e(address)
                    if (transaction != null && transaction.getTxId() != null) {
                        mo43906a(address, transaction)
                        observer?.mo39871a(address, transaction)
                        m11812f(address)
                    } else observer?.mo39870a(address)
                }
            }
        }
    }

    override fun initVidAddress(address: Address?) {
        if (address != null) {
            address.setAndUpdateFlag(16)
            address.updateAppingVid(false)
            address.setDirectInvNo(0)
            address.setFissionTotal(0)
            address.setFissionReward(0.0)
            address.setLastBlock(getCurBlockNo(BLOCK_CHAIN_TYPE.VCASH))
            val addressModel = getWalletByChainType(*arrayOfNulls<BLOCK_CHAIN_TYPE>(0)).getSelfAddressModel()
            addressModel.removeFromSparseArrAndAddToAddressVector(16, address)
            addressModel.updateAddressInfo(address)
        }
    }


    override fun startThreadToRestartPeer() {
        Thread(object:Runnable{
            override fun run() {
                this@VCashCoreImpl.restartPeer()
            }

        }).start()
    }

    fun restartPeer() {
        try {
            Thread.sleep(10)
            checkAndStopPeer()
            checkAndStartPeerManagerNetwork()
        } catch (e2: Exception) {
            e2.printStackTrace()
        }
    }


    //mo43785a
    override fun startThreadToResyncBlockChain(blockChainType: BLOCK_CHAIN_TYPE, startBlockNo: Long, z: Boolean) {
        Thread(object:Runnable{
            override fun run() {
                this@VCashCoreImpl.stopPeerAndResyncBlockChainSinceStartBlockNo(blockChainType,startBlockNo,z)
            }

        }).start()
    }

    //m11805b
    fun stopPeerAndResyncBlockChainSinceStartBlockNo(blockChainType: BLOCK_CHAIN_TYPE, startBlockNo: Long, z: Boolean) {
        try {
            Thread.sleep(10)
            checkAndStopPeer()
            getWalletByChainType(blockChainType).reSyncWallet(startBlockNo, z)
            try {
                this.vWallet!!.reInitWallet(true)
               //this.f12815r.mo44088b(true)
                checkAndStartPeerManagerNetwork()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } catch (e2: Exception) {
            e2.printStackTrace()
        }

    }



    override fun mo43803a(oVar: CTxDestination, igVar: BLOCK_CHAIN_TYPE): Boolean {
        checkWalletInit()
        return this.vWallet!!.getSelfAddressModel().mo43123m(oVar)
    }

    override fun initAppWithPath(activity: Activity, str: String) {
        if (this.initInfo.initInfo === InitInfo.InitInfoEnum.NO_INIT) {
            Log.d("VCWalletCore", "initting app...")
            NativeLibLoader.loadLibrary()
            JNIUtils.initJNI()
            noticeHandleInitInfoEnum(InitInfo.InitInfoEnum.INITTING, 0, "")
            if (Build.VERSION.SDK_INT < 19) {
                this@VCashCoreImpl.initAppConfigAndSettingWithPath(str)
            } else if (SystemToolkit.getDenyPermissionArr(activity, permissionArr) == null) {
                this@VCashCoreImpl.initAppConfigAndSettingWithPath(str)
            } else {
                RxPermissions(activity).request(*permissionArr).subscribe(object : Consumer<Boolean> {
                    /* renamed from: a */
                    override fun accept(bool: Boolean?) {
                        val strArr: Array<String>
                        if (bool!!) {
                            this@VCashCoreImpl.initAppConfigAndSettingWithPath(str)
                            return
                        }
                        val stringBuffer =
                            StringBuffer("Permission check failed, please allow the following permissions:")
                        for (str in this@VCashCoreImpl.permissionArr) {
                            stringBuffer.append(IOUtils.LINE_SEPARATOR_UNIX)
                            stringBuffer.append(str)
                        }
                        this@VCashCoreImpl.noticeHandleInitInfoEnum(InitInfo.InitInfoEnum.INIT_FAILED, 3, stringBuffer.toString())
                    }
                })
            }
        }
    }

    //mo43779a
    override fun initApp(activity: Activity) {
        if (this.initInfo.initInfo === InitInfo.InitInfoEnum.NO_INIT) {
            Log.d("VCWalletCore", "initting app...")
            NativeLibLoader.loadLibrary()
            JNIUtils.initJNI()
            noticeHandleInitInfoEnum(InitInfo.InitInfoEnum.INITTING, 0, "")
            if (Build.VERSION.SDK_INT < 19) {
                InitAppConfigAndSetting()
            } else if (SystemToolkit.getDenyPermissionArr(activity, permissionArr) == null) {
                InitAppConfigAndSetting()
            } else {
                RxPermissions(activity).request(*permissionArr).subscribe(object : Consumer<Boolean> {
                    /* renamed from: a */
                    override fun accept(bool: Boolean?) {
                        val strArr: Array<String>
                        if (bool!!) {
                            this@VCashCoreImpl.InitAppConfigAndSetting()
                            return
                        }
                        val stringBuffer =
                            StringBuffer("Permission check failed, please allow the following permissions:")
                        for (str in this@VCashCoreImpl.permissionArr) {
                            stringBuffer.append(IOUtils.LINE_SEPARATOR_UNIX)
                            stringBuffer.append(str)
                        }
                        this@VCashCoreImpl.noticeHandleInitInfoEnum(InitInfo.InitInfoEnum.INIT_FAILED, 3, stringBuffer.toString())
                    }
                })
            }
        }
    }


    override fun checkWords(strArr: Array<String>): Boolean {
        checkWalletInit()
        try {
            var seed=HDSeed.getNewHDSeed(strArr)
            return true
        } catch (e: SeedWordsFormatException) {
            e.printStackTrace()
            return false
        }catch (e:Exception){
            e.printStackTrace()
            return false
        }
    }

    override fun mo43771a(
        charSequence: CharSequence,
        list: List<COutPoint>,
        list2: List<AddressMoneyInfo>,
        j: Long,
        vararg igVarArr: BLOCK_CHAIN_TYPE
    ): OfflineTransaction {
        return getWalletByChainType(*igVarArr).getSelfTransactionModel().mo40428a(charSequence, list, list2, j)
    }

    //mo43764a
    override fun getAccount(i: Int): Account? {
        return if (!isInitSuccess()) {
            null
        } else this.vWallet?.getAccountFromSparseArr(i)
    }


    override fun addUnConfirmVxdTransaction(txid:UInt256, address:Address){
        checkWalletInit()
        if(!txid.isNull){
            this.vWallet!!.selfTransactionModel.checkAndAddUnConfirmVxdTxid(txid)
            address.updateVxdTxidByDesHash(txid.hashString())
        }
        if(!address.isAppingVxd){
            address.updateAppingVxd(true)
        }
    }

    override fun clearVxd(address:Address){
        checkWalletInit()
        if(address.isAppingVxd)
            address.updateAppingVxd(false)
    }

    override fun getVidGroupList(): List<VidGroup> {
        checkWalletInit()
        return this.vWallet!!.mo42394ad().getVidGroupList()
    }

    //mo43739a
    override fun getPreCreateMessageResult(peer: Peer?, addressString: String, parentAddressString: String, vararg igVarArr: BLOCK_CHAIN_TYPE): SendMsgResult? {
        return checkAndGetPreCreateMessageResult(
            peer,
            AddressUtils.getDesFromAddressString(addressString as CharSequence, getWalletByChainType(*igVarArr)),
            AddressUtils.getDesFromAddressString(parentAddressString as CharSequence, getWalletByChainType(*igVarArr))
        )
    }

    //mo43903a
    fun checkAndGetPreCreateMessageResult(peer: Peer?, des: CTxDestination, parentDes: CTxDestination): SendMsgResult? {
        checkWalletInit()
        val wallet = getWalletByChainType(*arrayOfNulls<BLOCK_CHAIN_TYPE>(0))
        if (wallet.getAddressByCTxDestinationFromArrayMap(des) == null) {
            val sb = StringBuilder()
            sb.append("Address not exists: ")
            sb.append(AddressUtils.getAddressString(des, getWalletByChainType(*arrayOfNulls<BLOCK_CHAIN_TYPE>(0)).getChainParams()))
            throw AddressNotFoundException(sb.toString())
        } else if (getWalletType() !== WalletType.HOT) {
            val sb2 = StringBuilder()
            sb2.append("This wallet type is ")
            sb2.append(getWalletType().name)
            vdsMain.Log.logObjectWarning(this as Any, sb2.toString())
            return null
        } else if (!des.equals(parentDes)) {
            return (wallet.getSelfPeerManager() as VPeerManager).checkAndGetPreCreateMessageResult(peer, des, parentDes)
        } else {
            val sb3 = StringBuilder()
            sb3.append("Clue child can not same as parent")
            sb3.append(AddressUtils.getAddressString(des, getWalletByChainType(*arrayOfNulls<BLOCK_CHAIN_TYPE>(0)).getChainParams()))
            vdsMain.Log.logObjectWarning(this as Any, sb3.toString())
            throw AddressFormatException("Clue child can not same as parent ")
        }
    }

    //mo43751a
    override fun getAddressListByFilter(addressFilter: AddressFilter): List<Address>? {
        if (!isInitSuccess()) {
            return null
        }
        when (addressFilter) {
            AddressFilter.ANONYMOUS -> return this.vWallet!!.getSelfAddressModel().getAddressListByFlag(2)
            AddressFilter.MULTISIG -> return this.vWallet!!.getSelfAddressModel().getAddressListByFlag(4)
            AddressFilter.ALL -> return this.vWallet!!.getSelfAddressModel().getAllUsingTxDesMapAddress()
            AddressFilter.GENERAL -> return this.vWallet!!.getSelfAddressModel().getAddressListByFlag(1)
            AddressFilter.WATCHED -> return this.vWallet!!.getSelfAddressModel().getAddressListByFlag(64)
            AddressFilter.INDENTITY -> return this.vWallet!!.getSelfAddressModel().getAddressListByFlag(16)
            AddressFilter.CONTRACT -> return this.vWallet!!.getSelfAddressModel().getAddressListByFlag(512)
            AddressFilter.OFFLINE_ADDR -> return this.vWallet!!.getSelfAddressModel().getAddressListByFlag(1089)
            AddressFilter.RECYCLE -> return this.vWallet!!.getSelfAddressModel().getAddressListByFlag(8192)
            else -> return null
        }
    }

    override fun addObserver(obj: Any) {
        var isSuccess: Boolean
        this.lock.lock()
        if (obj is CoreEventObserver) {
            this.coreEventObserverLinkedHashMap[obj] = obj
            isSuccess = true
        } else {
            isSuccess = false
        }
        if (obj is AccountAddressObserver) {
            this.accountAddressObserverLinkedHashMap.put(obj, obj)
            isSuccess = true
        }
        if (obj is BlockChainObserver) {
            val hiVar = obj as BlockChainObserver
            this.blockChainObserverMap.put(hiVar, hiVar)
            isSuccess = true
        }
        if (obj is NetworkObserver) {
            val hmVar = obj as NetworkObserver
            this.networkObserverMap.put(hmVar, hmVar)
            isSuccess = true
        }

        if (obj is TransactionObserver) {
            val hoVar = obj as TransactionObserver
            this.transactionObserverMap.put(hoVar, hoVar)
            isSuccess = true
        }
//        if (obj is WalletBackupObserver) {
//            val hpVar = obj as WalletBackupObserver
//            this.f12811n.put(hpVar, hpVar)
//            isSuccess = true
//        }
//        if (obj is ContactEventObserver) {
//            val hjVar = obj as ContactEventObserver
//            this.f12812o.put(hjVar, hjVar)
//            isSuccess = true
//        }
        if (obj is ASyncMessageObserver) {
            val observer = obj as ASyncMessageObserver
            this.asyncMsgObserverMap.put(observer, observer)
            isSuccess = true
        }
        this.lock.unlock()
        if (!isSuccess) {
            throw IllegalArgumentException(
                String.format(
                    Locale.getDefault(),
                    "Unsupported observer type: %s",
                    *arrayOf<Any>(obj.javaClass.name)
                )
            )
        }
    }

    //mo43817b
    override fun removeObserver(obj: Any) {
        this.lock.lock()
        if (obj is CoreEventObserver) {
            this.coreEventObserverLinkedHashMap.remove(obj)
        }
        if (obj is AccountAddressObserver) {
            this.accountAddressObserverLinkedHashMap.remove(obj)
        }
        if (obj is BlockChainObserver) {
            this.blockChainObserverMap.remove(obj)
        }
        if (obj is NetworkObserver) {
            this.networkObserverMap.remove(obj)
        }
//
        if (obj is TransactionObserver) {
            this.transactionObserverMap.remove(obj)
        }
//        if (obj is WalletBackupObserver) {
//            this.f12811n.remove(obj)
//        }
//        if (obj is ContactEventObserver) {
//            this.f12811n.remove(obj)
//        }
        if (obj is ASyncMessageObserver) {
            this.asyncMsgObserverMap.remove(obj)
        }
        this.lock.unlock()
    }

    override fun getNormalPeerList(vararg igVarArr: BLOCK_CHAIN_TYPE): List<Peer> {
        return getWalletByChainType(*igVarArr).getSelfPeerManager().getNormalPeerVector()
    }

    override fun getAllAddressAvailBalance(z: Boolean, vararg blockChainTypes: BLOCK_CHAIN_TYPE): UInt64 {
        val blockChainType: BLOCK_CHAIN_TYPE
        if (!isInitSuccess()) {
            return UInt64(0)
        }
        val wallet = getWalletByChainType(*blockChainTypes)
        val uInt64 = UInt64(0)
        val addressList = wallet.getSelfAddressModel().getAllUsingTxDesMapAddress()
        if (addressList == null || addressList.isEmpty()) {
            return uInt64
        }
        if (blockChainTypes == null) {
            blockChainType = BLOCK_CHAIN_TYPE.VCASH
        } else {
            blockChainType = blockChainTypes[0]
        }
        for (address in addressList) {
            if (!address.isWatchedFlag() && !address.isRecycleFlag() && !address.getHide()) {
                if (z) {
                    uInt64.add(address.getAvailableBalance(blockChainType))
                } else if (address !is VAnonymousAddress) {
                    uInt64.add(address.getAvailableBalance(blockChainType))
                }
            }
        }
        return uInt64
    }

    //mo43806b
    override fun getAllAddressUnConfirmedBalance(z: Boolean, vararg blockChainTypes: BLOCK_CHAIN_TYPE): UInt64 {
        val wallet = getWalletByChainType(*blockChainTypes)
        val uInt64 = UInt64()
        for (address in ArrayList(getAddressListByFilter(AddressFilter.ALL)!!)) {
            if ((z || address.getAddressType() !== AddressType.ANONYMOUS) && !address.getHide() && !address.isRecycleFlag() && !address.isWatchedFlag()) {
                val cTxDestination = address.getCTxDestination()
                val mutableList = wallet.getSelfTransactionModel().getUnConfirmedTransactionList(cTxDestination)
                if (mutableList != null) {
                    for (transaction in mutableList) {
                        val voutValue = transaction.getVoutSumSubVinSum(cTxDestination)
                        if (voutValue > 0) {
                            uInt64.add(voutValue)
                        }
                    }
                }
            }
        }
        return uInt64
    }

    //mo43733a
    override fun getSumLockBalance(includeAnonymousAddress: Boolean, blockChainType: BLOCK_CHAIN_TYPE): Long {
        if (!isInitSuccess()) {
            return 0
        }
        val wallet = getWalletByChainType(blockChainType)
        val sumLock = UInt64(0)
        val addressList = wallet.getSelfAddressModel().getAllUsingTxDesMapAddress()
        if (addressList == null || addressList.isEmpty()) {
            return 0
        }
        for (address in addressList) {
            if (!address.isWatchedFlag() && !address.isRecycleFlag() && !address.getHide()) {
                if (includeAnonymousAddress) {
                    sumLock.add(address.getSumBalance(blockChainType) - address.getAvailableBalance(blockChainType))
                 }
                else if (address !is VAnonymousAddress) {
                    sumLock.add(address.getSumBalance(blockChainType) - address.getAvailableBalance(blockChainType))
                }
            }
        }
        return sumLock.getValue()
    }

    override fun mo43750a(i: Int, ixVar: TransactionConfirmType, vararg igVarArr: BLOCK_CHAIN_TYPE): List<Transaction>? {
        return if (!isInitSuccess()) {
            null
        } else getWalletByChainType(*igVarArr).getSelfTransactionModel().mo44479a(i, ixVar)
    }

    //mo43775a
    override fun signTransactionAndGetResult(
        spendUtxoList: List<Utxo>,
        addressMoneyInfo: List<AddressMoneyInfo>,
        fee: Long,
        pwd: CharSequence,
        blockChainType: BLOCK_CHAIN_TYPE
    ): TxSignatureResult {
        return getWalletByChainType(blockChainType).getSelfTransactionModel().signTransactionAndGetResult(spendUtxoList, addressMoneyInfo, fee, pwd)
    }

    override fun signContractCallTransactionAndGetResult(
        spendUtxoList: List<Utxo>,
        contractCallInfo:ContractCallInfo,
        addressMoneyInfo: List<AddressMoneyInfo>,
        fee: Long,
        pwd: CharSequence,
        blockChainType: BLOCK_CHAIN_TYPE
    ): TxSignatureResult {
        return getWalletByChainType(blockChainType).getSelfTransactionModel().signContractCallTransactionAndGetResult(spendUtxoList,contractCallInfo, addressMoneyInfo, fee, pwd)
    }

    override fun mo43741a(i: Int, ixVar: TransactionConfirmType, blockChainType: BLOCK_CHAIN_TYPE): Transaction {
        return getWalletByChainType(blockChainType).getSelfTransactionModel().mo44503b(i, ixVar)
    }

    override fun mo43798a(blockChainType: BLOCK_CHAIN_TYPE): Boolean {
        checkWalletInit()
        val peerList = getNormalPeerList(blockChainType)
        if (peerList == null || peerList!!.isEmpty()) {
            return false
        }
        for (i in peerList!!.indices) {
            if ((peerList.get(i) as Peer).getPeerStatus() === Peer.PeerStatus.Connected) {
                return true
            }
        }
        return false
    }

    override fun getMainPeerInfo():PeerInfo?{
        checkWalletInit()
        return (this.vWallet!!.getSelfPeerManager() as VPeerManager).mainPeer?.peerInfo
    }

    override fun checkAndSendGetVCountMessage() {
        checkWalletInit()
        (this.vWallet!!.getSelfPeerManager() as VPeerManager).sendGetVCountMessage()
    }

    //mo43887p
    override fun checkAndSendGetLastSeasonListMessage() {
        checkWalletInit()
        (getWalletByChainType().getSelfPeerManager() as VPeerManager).sendGetLastSeasonTopClueListMessage()
    }

    override fun checkAndSendCallContractMessage() {
        checkWalletInit()
        (getWalletByChainType().getSelfPeerManager() as VPeerManager).sendCallContractMessage()
    }
}