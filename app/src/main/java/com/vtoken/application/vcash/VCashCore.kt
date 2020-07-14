package com.vtoken.vdsecology.vcash

import android.app.Activity
import bitcoin.UInt256
import bitcoin.account.hd.HDSeed
import com.vc.libcommon.exception.TxSizeException
import com.vc.libcommon.util.UInt64
import generic.exceptions.InvalidateTransactionException
import generic.exceptions.UtxoAlreadySpendException
import generic.exceptions.UtxoNotFoundException
import vdsMain.*
import vdsMain.block.BlockInfo
import vdsMain.block.ChainSyncStatus
import vdsMain.message.CluePreCreateTreeMssage
import vdsMain.message.SendMsgResult
import vdsMain.model.*
import vdsMain.observer.ImportKeyGetVidObserver
import vdsMain.peer.Peer
import vdsMain.peer.PeerInfo
import vdsMain.transaction.*
import vdsMain.wallet.ChainParams
import vdsMain.wallet.Wallet
import vdsMain.wallet.WalletType
import java.util.ArrayList


interface VCashCore {

    //mo44188k
    abstract fun getAppWalletPath(): String

    //915 mo44090a
    abstract fun initAppWithPath(activity: Activity, str: String)

    //mo43779a
    abstract fun initApp(activity: Activity)

    //915 mo44036J
    abstract fun getWalletLabel(): String?

    //915 mo44205s
    abstract fun setWalletLabel(str: String)

    //915 mo44155d
    abstract fun isInitSuccess(): Boolean

    //mo43799a
    abstract fun checkMatchPassword(charSequence: CharSequence): Boolean

    //mo43804a
    abstract fun checkWords(strArr: Array<String>): Boolean

    abstract fun getNewHDSeed(): HDSeed

    abstract fun getInitInfo(): InitInfo

    //mo43776a
    //915 mo44087a
    fun getWalletChainParams(vararg igVarArr: BLOCK_CHAIN_TYPE): ChainParams

    abstract fun fixWalletFullPublicKey(pwd:String)

    abstract fun mo43880l(): String

    //mo43792a
    abstract fun initVidAddress(address: Address?)

    //mo43886o
    abstract fun isValidAddress(str: String): Boolean

    //mo43720C
    //915 mo44029C
    //917 mo44061E
    abstract fun getWalletAddressModel(): AddressModel

    abstract fun mo43789a(list: List<ComplexBitcoinAddress>)

    //mo44099a
    abstract fun checkAndRecycleAddress(str: String, address: Address)

    //915 mo44203r
    abstract fun getLabelByAddressString(str: String): String

    abstract fun startThreadToRestartPeer()

    abstract fun checkAndStopPeer()

    //mo43829c
    abstract fun getRemarkByTxid(txid: UInt256, blockChainType: BLOCK_CHAIN_TYPE): String?

    abstract fun mo43800a(str: String, igVar: BLOCK_CHAIN_TYPE): Boolean

    //mo43793a
    abstract fun updateAddressIsHideAndOperateSpareArr(jjVar: Address, z: Boolean)

    //mo43763a
    //915 mo44075a
    abstract fun getWalletType(): WalletType

    //mo43858f
    //915 mo44164e
    abstract fun hasSetWalletPwd(): Boolean

    abstract fun mo43798a(igVar: BLOCK_CHAIN_TYPE): Boolean

    //mo43781a
    abstract fun replaceToTxMarkTable(uInt256: UInt256, str: String, igVar: BLOCK_CHAIN_TYPE)


    abstract fun mo43836c(str: String, igVar: BLOCK_CHAIN_TYPE)

    abstract fun startThreadToResyncBlockChain(blockChainType: BLOCK_CHAIN_TYPE, startBlockNo: Long, z: Boolean)

    //mo43748a
    abstract fun getAddressBIP38PrivateKey(address: Address, str: String, str2: String): String

    abstract fun getAddressOrginPrivateKey(addressString: String, pwd: String): String

    //mo43747a
    abstract fun getAddressPrivateKey(addressString: String, pwd: String, vararg igVarArr: BLOCK_CHAIN_TYPE): String

    //mo43755a
    abstract fun importPrivateKey(str: String, str2: String): List<Address>?

    //mo43756a
    abstract fun importBIP38PrivateKey(str: String, str2: String, pwd: String): List<Address>

    //mo43787a
    //915 mo44098a
    abstract fun addObserver(obj: Any)

    //mo43817b
    //915 mo44128b
    abstract fun removeObserver(obj: Any)

    //mo43822b
    abstract fun checkAndStartPeerManagerNetwork(): Boolean

    fun addUnConfirmVxdTransaction(txid:UInt256,address:Address)

    fun clearVxd(address:Address)

    //mo43739a
    fun getPreCreateMessageResult(peer: Peer?, addressString: String, parentAddressString: String, vararg igVarArr: BLOCK_CHAIN_TYPE): SendMsgResult?

    //mo43766a
    abstract fun createAnonymousAccount(charSequence: CharSequence, bArr: ByteArray, vararg charSequenceArr: CharSequence): Address

    //mo43833c
    abstract fun getAddressFromAddressString(str: String, vararg igVarArr: BLOCK_CHAIN_TYPE): Address?

    //mo43751a
    abstract fun getAddressListByFilter(haVar: AddressFilter): List<Address>?

    //创建地址
    //mo43752a
    abstract fun createNormalAddress(charSequence: CharSequence, i: Int, vararg charSequenceArr: CharSequence?): List<Address>

    //mo43790a
    abstract fun createClueTransactionAndUpdate(
        utxoList: List<Utxo>,
        spendUtxoList: List<Utxo>,
        cluePreCreateTreeMssage: CluePreCreateTreeMssage,
        address: Address,
        firstParentAddressString: String,
        label: String,
        vidGroup: VidGroup?,
        pwd: CharSequence,
        vararg blockChainTypes: BLOCK_CHAIN_TYPE
    )

    @Throws(InvalidateTransactionException::class, UtxoAlreadySpendException::class,
        UtxoNotFoundException::class, TxSizeException::class
    )
    fun createVxdTransferInTransaction(utxoList: List<Utxo>, spendUtxoList: List<Utxo>, transferAddressMoneyInfo: ArrayList<AddressMoneyInfo>, address: Address,
                                       pwd: CharSequence, blockChainTypes: BLOCK_CHAIN_TYPE, includeFee:Boolean):Transaction?


    //mo43899y
    fun getVidGroupList(): List<VidGroup>

    //mo43762a
    fun getVidRewardInfo(list: List<Address>?, igVar: BLOCK_CHAIN_TYPE): Map<Int, Any>?

    //mo43795a
    //915 mo44106a
    abstract fun InitWalletIfNeed(z: Boolean)

    //915 mo44037K
    abstract fun destoryWalletAndDB()

    //mo43881m
    abstract fun getFileRootDir(): String

    //mo43866h
    //915 mo44172g
    abstract fun getHDAccountList(): List<HDAccount>?

    abstract fun checkAndSetWallType(jeVar: WalletType): Boolean

    //mo43786a
    abstract fun changeWalletPassword(charSequence: CharSequence, charSequence2: CharSequence)

    //mo43735a
    abstract fun InitHDAccount(charSequence: CharSequence, hDSeed: HDSeed, vararg iArr: Int): HDAccount

    //mo43736a
    abstract fun initHDAccountFromWordList(charSequence: CharSequence, strArr: Array<String>, vararg iArr: Int): HDAccount?

    //mo43871i
    //915 //915 mo44182i
    abstract fun getWalletByChainType(vararg igVarArr: BLOCK_CHAIN_TYPE?): Wallet

    abstract fun getCurBlockInfoByType(vararg igVarArr: BLOCK_CHAIN_TYPE): BlockInfo?

    //mo43812b
    abstract fun getNormalPeerList(vararg igVarArr: BLOCK_CHAIN_TYPE): List<Peer>

    //mo43850e
    abstract fun getMaxBlockNo(vararg igVarArr: BLOCK_CHAIN_TYPE): Long

    //mo43855f
    //915 mo44166f
    abstract fun getCurBlockNo(vararg igVarArr: BLOCK_CHAIN_TYPE): Long

    //mo43843d
    abstract fun getBlockChainSyncStatus(vararg igVarArr: BLOCK_CHAIN_TYPE?): ChainSyncStatus

    //mo43883n
    fun getVibInfo(): VibInfo?

    //mo43719B
    fun getVidLockBlockNumber(): Long

    //mo43742a
    fun getTransactionFromAllTransactionMap(uInt256: UInt256, vararg igVarArr: BLOCK_CHAIN_TYPE): Transaction?

    fun mo43797a(uInt256: UInt256, igVar: BLOCK_CHAIN_TYPE): Boolean

    abstract fun mo43784a(observer: ImportKeyGetVidObserver)

    //mo43811b
    abstract fun getVidAddressList(z: Boolean): List<Address>

    //mo43740a
    //915 mo44052a
    abstract fun getAllAddressAvailBalance(z: Boolean, vararg blockChainTypes: BLOCK_CHAIN_TYPE): UInt64

    //mo43806b
    abstract fun getAllAddressUnConfirmedBalance(z: Boolean, vararg igVarArr: BLOCK_CHAIN_TYPE): UInt64

    //mo43733a
    abstract fun getSumLockBalance(includeAnonymousAddress: Boolean, blockChainType: BLOCK_CHAIN_TYPE): Long

    abstract fun mo43803a(oVar: CTxDestination, igVar: BLOCK_CHAIN_TYPE): Boolean

    //mo43764a
    abstract fun getAccount(i: Int): Account?

    //mo43814b
    abstract fun getAddressTypeByAddressString(str: String): AddressType

    abstract fun mo43828c(address: Address): ComplexBitcoinAddress

    //mo43746a
    abstract fun signMessageWithAddress(addressString: CharSequence, message: CharSequence, pwd: CharSequence): String

    //mo43734a
    abstract fun calMinFee(checkMaxFee: Boolean, list: List<COutPoint>, list2: List<AddressMoneyInfo>, j: Long, mbVar: VOutList?, lvVar: ScriptList?, vararg igVarArr: BLOCK_CHAIN_TYPE): Long

    abstract fun mo43750a(i: Int, ixVar: TransactionConfirmType, vararg igVarArr: BLOCK_CHAIN_TYPE): List<Transaction>?

    //mo43760a
    abstract fun getTransactionListByCTxDestination(cTxDestination: CTxDestination, ixVar: TransactionConfirmType, vararg igVarArr: BLOCK_CHAIN_TYPE): List<Transaction>?

    //mo43810b
    abstract fun getConfirmedTransactionListByBlockHash(uInt256: UInt256, vararg igVarArr: BLOCK_CHAIN_TYPE): List<Transaction>

    abstract fun mo43807b(uInt256: UInt256, igVar: BLOCK_CHAIN_TYPE): RejectTx

    //mo43815b
    abstract fun checkAndSendTransactionToPeers(dhVar: Transaction, charSequence: CharSequence, vararg igVarArr: BLOCK_CHAIN_TYPE): TxSendResult

    abstract fun mo43771a(
        charSequence: CharSequence,
        list: List<COutPoint>,
        list2: List<AddressMoneyInfo>,
        j: Long,
        vararg igVarArr: BLOCK_CHAIN_TYPE
    ): OfflineTransaction

    abstract fun mo43809b(str: String, vararg igVarArr: BLOCK_CHAIN_TYPE): Transaction?

    abstract fun mo43741a(i: Int, ixVar: TransactionConfirmType, igVar: BLOCK_CHAIN_TYPE): Transaction

    //mo43758a
    abstract fun getAccountUtxoList(account: Account, igVar: BLOCK_CHAIN_TYPE, z: Boolean): List<Utxo>

    //mo43759a
    abstract fun getUtxoListByAddress(address: Address, vararg igVarArr: BLOCK_CHAIN_TYPE): List<Utxo>?

    //mo43761a
    abstract fun getSpendUtxoList(includeFee: Boolean, list: List<Utxo>, sendValue: Long, feeValue: Long): List<Utxo>

    abstract fun signContractCallTransactionAndGetResult(
        spendUtxoList: List<Utxo>,
        contractCallInfo:ContractCallInfo,
        addressMoneyInfo: List<AddressMoneyInfo>,
        fee: Long,
        pwd: CharSequence,
        blockChainType: BLOCK_CHAIN_TYPE
    ): TxSignatureResult

    abstract fun signTransactionAndGetResult(
        spendUtxoList: List<Utxo>,
        addressMoneyInfo: List<AddressMoneyInfo>,
        fee: Long,
        pwd: CharSequence,
        blockChainType: BLOCK_CHAIN_TYPE
    ): TxSignatureResult

    abstract fun getMainPeerInfo(): PeerInfo?

    //mo43900z
    abstract fun checkAndSendGetVCountMessage()

    //mo43887p
    fun checkAndSendGetLastSeasonListMessage()

    fun checkAndSendCallContractMessage()
}