package vdsMain.transaction

import android.content.ContentValues
import android.database.Cursor
import androidx.annotation.NonNull
import android.util.Log
import bitcoin.BaseBlob
import bitcoin.UInt256
import bitcoin.VariableInteger
import bitcoin.script.BaseSignatureChecker
import bitcoin.script.CScript
import bitcoin.script.Interpreter
import bitcoin.script.TransactionSignatureChecker
import com.vc.libcommon.exception.AddressFormatException
import vdsMain.WalletSerializedAbstractTableItem
import generic.exceptions.InvalidateTransactionException
import generic.exceptions.NotMatchException
import generic.io.StreamWriter
import generic.serialized.SeriableData
import net.bither.bitherj.exception.ScriptException
import vdsMain.CTxDestination
import vdsMain.StringToolkit
import vdsMain.UnsafeByteArrayOutputStream
import vdsMain.table.AbstractTable
import vdsMain.wallet.CWallet
import vdsMain.wallet.Wallet
import vdsMain.model.Address;
import java.io.IOException
import java.io.OutputStream
import java.util.*

public open class Transaction : WalletSerializedAbstractTableItem, TransactionInteface{

    override fun getWallet(): Wallet {
        return wallet;
    }


    companion object{
        //TODO 把静态变量先写死
        @JvmField
        //val f12405a =UInt.f672b.mo18933b()
        val uIntMax=4294967295L
        //f12406b
        val defaultHash = UInt256.uint256S("0000000000000000000000000000000000000000000000000000000000000001")
    }
    //f12407c
    //910 f12542c
    @JvmField
    protected var txid = UInt256()

    //f12408d
    @JvmField
    protected var version: Long = 1

    //f12409e
    @JvmField
    protected var flag: Short = 0

    //f12410f
    @JvmField
    protected var lockTime: Long = 0

    //f12411g
    //910 f12546g
    @JvmField
    protected var timeStamp: Long = 0

    //f12412h
    @JvmField
    protected var fee: Long = 0

    //f12413i
    @JvmField
    protected var satoshi: Long = 0

    //f12414j
    @JvmField
    protected var blockHash: UInt256? = null

    //f12415k
    protected var cTxDestination: CTxDestination? = null

    /* renamed from: l */
    protected var f12416l = false

    //f12417m
    @JvmField
    protected var direction = TransactionDirection.UNKNOWN

    //f12418n
    @JvmField
    protected var type = TranscationType.NORMAL

    //f12419o
    protected var txInList: MutableList<TxIn> = ArrayList()

    //f12420p
    protected var txOutList: MutableList<TxOut> = ArrayList()

    //f12421q
    protected var cWallet: CWallet?=null

    //f12422r
    @JvmField
    protected var blockNo = -1

    //C3829a
    enum class TransactionDirection {
        UNKNOWN,
        SEND,
        RECEIVE
    }

    //C3830b
    enum class TranscationType {
        NORMAL,
        COINBASE
    }

    /* renamed from: a */
    override fun getKey(): String {
        return "txid"
    }

    fun getDirectionOrdinal():Int{
        return direction.ordinal
    }

    //mo43231C_
    fun isDefaultHash(): Boolean {
        val uInt256 = this.blockHash
        return uInt256 != null && uInt256 == defaultHash
    }

    //mo43233D_
    fun initDefaultBlockHashIfNull() {
        val uInt256 = this.blockHash
        if (uInt256 == null) {
            this.blockHash = UInt256(defaultHash as BaseBlob)
        } else {
            uInt256.set(defaultHash as BaseBlob)
        }
        this.blockNo = -1
        if (this.timeStamp == -1L) {
            this.timeStamp = System.currentTimeMillis() / 1000
        }
    }

    override fun initTableItemVariable(cursor: Cursor, i: Int, i2: Int, cloumnIndex: Int) {
        when (cloumnIndex) {
            0 -> {
                this.txid.setHash(cursor.getString(cloumnIndex))
                return
            }
            1 -> {
                this.version = cursor.getLong(cloumnIndex)
                return
            }
            2 -> {
                this.flag = cursor.getShort(cloumnIndex)
                return
            }
            3 -> {
                this.lockTime = cursor.getLong(cloumnIndex)
                return
            }
            4 -> {
                this.timeStamp = cursor.getLong(cloumnIndex)
                return
            }
            5 -> {
                this.blockNo = cursor.getInt(cloumnIndex)
                return
            }
            6 -> {
                val string = cursor.getString(cloumnIndex)
                if (string == null || string.isEmpty()) {
                    this.blockHash = null
                    return
                } else {
                    this.blockHash = UInt256(string)
                    return
                }
            }
            7 -> {
                TransactionSerializer.seralizeToTxInList(this as TransactionInteface, cursor.getBlob(cloumnIndex), 0)
                return
            }
            8 -> {
                TransactionSerializer.m10981b(this, cursor.getBlob(cloumnIndex), 0)
                return
            }
            9 -> {
                TransactionSerializer.m10968a(cursor.getBlob(cloumnIndex), this)
                mo43272x()
                return
            }
            else -> return
        }
    }


    //mo42883b
    //mo43250a
    //910 mo42945b
    //setCWallet
    open fun setSelfCWallet(mfVar:CWallet){
        this.cWallet=mfVar
    }

    override fun getValue(): String {
        return this.txid.hashString()
    }

    constructor(@NonNull  wallet: Wallet):super(wallet){
        this.version = wallet.getChainParams().version.toLong()
    }

    constructor (transaction: Transaction?): super(transaction as WalletSerializedAbstractTableItem?) {
        if (transaction != null) {
            this.cWallet = transaction.cWallet
            this.txid.set(transaction.txid as BaseBlob)
            this.version = transaction.version
            this.flag = transaction.flag
            this.lockTime = transaction.lockTime
            this.timeStamp = transaction.timeStamp
            this.fee = transaction.fee
            this.satoshi = transaction.satoshi
            this.blockNo = transaction.blockNo
            val uInt256 = transaction.blockHash
            if (uInt256 != null) {
                this.blockHash = UInt256(uInt256 as BaseBlob)
            }
            val oVar = transaction.cTxDestination
            if (oVar != null) {
                this.cTxDestination = oVar.clone()
            }
            this.f12416l = transaction.f12416l
            this.direction = transaction.direction
            this.type = transaction.type
            val list = transaction.txInList
            if (list != null && !list!!.isEmpty()) {
                this.txInList!!.addAll(transaction.txInList!!)
            }
            val list2 = transaction.txOutList
            if (list2 != null && !list2!!.isEmpty()) {
                this.txOutList!!.addAll(transaction.txOutList!!)
            }
        }
    }

    constructor(cMutableTransaction: CMutableTransaction): super(cMutableTransaction.getSelfWallet()) {

        this.version = cMutableTransaction.version
        this.lockTime = cMutableTransaction.lockTime
        this.flag = cMutableTransaction.flag
        this.blockNo = cMutableTransaction.blockNo
        if (cMutableTransaction.txInList != null && !cMutableTransaction.txInList.isEmpty()) {
            this.txInList.addAll(cMutableTransaction.txInList)
        }
        if (cMutableTransaction.txOutList != null && !cMutableTransaction.txOutList.isEmpty()) {
            this.txOutList.addAll(cMutableTransaction.txOutList)
        }
        updateTxidByContent()
    }

    constructor(izVar: Wallet, fxVar: AbstractTable): super(izVar, fxVar) {

    }

    //mo43249a
    fun setTxInAndTxOutAndSetIndex(list: List<TxIn>?, list2: List<TxOut>?, needComputeByTxs: Boolean) {
        this.txInList = list as MutableList<TxIn>
        this.txOutList = list2 as MutableList<TxOut>
        var i = 0
        if (list != null) {
            var vinIndex = 0
            for (txIn in list) {
                val tempIndex = vinIndex + 1
                txIn.setIndex(vinIndex)
                vinIndex = tempIndex
            }
        }
        if (list2 != null) {
            for (txOut in list2) {
                val i4 = i + 1
                txOut.setIndex(i)
                i = i4
            }
        }
        if (needComputeByTxs) {
            recomputeByTxs(null)
        }
    }

    fun mo43251a_(list: List<TxIn>, z: Boolean) {
        this.txInList = list as MutableList<TxIn>
        if (z) {
            recomputeByTxs(null)
        }
    }

    //mo43247a
    fun addTxIn(txIn: TxIn, recompute: Boolean) {
        txIn.setMTxid(this.txid)
        txIn.setIndex(this.txInList!!.size)
        this.txInList.add(txIn)
        if (recompute) {
            recomputeByTxs(null)
        }
    }

    //mo43256b_
    fun clearAndSetTxOutList(list: List<TxOut>?, recompute: Boolean) {
        this.txOutList.clear()
        if (list != null && !list.isEmpty()) {
            this.txOutList.addAll(list)
        }
        if (recompute) {
            recomputeByTxs(null)
        }
    }

    /* renamed from: a */
    fun mo43248a(txOut: TxOut, z: Boolean) {
        txOut.setMTxid(this.txid)
        txOut.setIndex(this.txOutList.size)
        this.txOutList.add(txOut)
        if (z) {
            recomputeByTxs(null)
        }
    }

    //mo43262k
    fun getBlockNo():Int{
        return blockNo;
    }

    //mo43253b
    //910 mo43317b
    //setBlockNo
    fun setMBlockNo(no:Int){
        this.blockNo=no;
    }

    //mo43052a
   open fun recomputeByTxs(list: MutableList<CTxDestination>?): Boolean {
        val j: Long
        Log.d("Transaction", "recomputeByTxs")
        list?.clear()
        val hashMap = HashMap<CTxDestination,CTxDestination>()
        this.fee = 0
        this.satoshi = 0
        var destination: CTxDestination? = null
        this.cTxDestination = null
        var j2: Long = 0
        var oVar2: CTxDestination? = null
        var oVar3: CTxDestination? = null
        for (txIn in this.txInList) {
            val value = txIn.getSatoshi()
            this.fee += value
            val cTxDestination = txIn.getCTxDestination()
            if (cTxDestination != null) {
                if (this.wallet.isUsingDesAddressMapHasKey(cTxDestination) || this.wallet.isUnuseDesAddressMapHasKey(cTxDestination)) {
                    j2 += value
                    if (oVar2 == null) {
                        oVar2 = cTxDestination
                    }
                    if (list != null && !hashMap.containsKey(cTxDestination)) {
                        hashMap.put(cTxDestination, cTxDestination)
                        list.add(cTxDestination)
                    }
                } else {
                    oVar3 = cTxDestination
                }
            }
        }
        var j3: Long = 0
        var oVar4: CTxDestination? = null
        for (txOut in this.txOutList) {
            val a2 = txOut.getSatoshi()
            this.fee -= a2
            val l = txOut.getScriptCTxDestination()
            if (l != null) {
                if (this.wallet.isUsingDesAddressMapHasKey(l) || this.wallet.isUnuseDesAddressMapHasKey(l)) {
                    if (a2 > 0) {
                        j3 += a2
                    }
                    if (oVar4 == null) {
                        oVar4 = l
                    }
                    if (list != null && !hashMap.containsKey(l)) {
                        hashMap.put(l, l)
                        list.add(l)
                    }
                } else if (destination == null) {
                    destination = l
                }
            }
        }
        if (isRelatedToLocalAddress()) {
            this.satoshi = j3 - j2
            j = 0
            if (j2 > 0) {
                this.f12416l = true
            }
        } else {
            j = 0
        }
        if (this.satoshi > j) {
            this.direction = TransactionDirection.SEND
            if (isRelatedToLocalAddress()) {
                this.cTxDestination = oVar2
                if (this.cTxDestination == null) {
                    this.cTxDestination = oVar3
                }
            } else {
                this.cTxDestination = oVar3
                if (this.cTxDestination == null) {
                    this.cTxDestination = oVar2
                }
            }
        } else {
            this.direction = TransactionDirection.RECEIVE
            if (isRelatedToLocalAddress()) {
                this.cTxDestination = oVar4
                if (this.cTxDestination == null) {
                    this.cTxDestination = destination
                }
            } else {
                this.cTxDestination = destination
                if (this.cTxDestination == null) {
                    this.cTxDestination = oVar4
                }
            }
        }
        if (this.fee < 0) {
            this.fee = 0
        }
        if (this.txInList.size == 1 && (this.txInList.get(0) as TxIn).isCoinBaseVin()) {
            this.type = TranscationType.COINBASE
        }
        return true
    }

    //mo43245a
    fun getAddressVoutSumSubVinSum(address: Address): Long {
        return getVoutSumSubVinSum(address.getCTxDestination())
    }

    //mo42822a
    open fun getVoutSumSubVinSum(cTxDestination: CTxDestination): Long {
        var j: Long = 0
        for (txIn in this.txInList) {
            val cTxDestinationIn = txIn.getCTxDestination()
            if (cTxDestinationIn != null && cTxDestinationIn!!.equals(cTxDestination)) {
                j -= txIn.getSatoshi()
            }
        }
        for (txOut in this.txOutList) {
            val cTxDestinationOut = txOut.getScriptCTxDestination()
            if (cTxDestinationOut != null && cTxDestinationOut!!.equals(cTxDestination)) {
                j += txOut.getSatoshi()
            }
        }
        return j
    }

    //mo43252b
    fun getStoshiList(cTxDestination: CTxDestination): ArrayList<Long> {
        val arrayList = ArrayList<Long>()
        for (txIn in this.txInList) {
            val cTxDestination = txIn.getCTxDestination()
            if (cTxDestination != null && cTxDestination.equals(cTxDestination)) {
                arrayList.add(txIn.getSatoshi())
            }
        }
        for (dnVar in this.txOutList) {
            val l = dnVar.getScriptCTxDestination()
            if (l != null && l.equals(cTxDestination)) {
                arrayList.add(dnVar.getSatoshi())
            }
        }
        return arrayList
    }

    //910 mo43124b
    //mo43061b
    open fun fillDesListWithRelatedAddress(list: MutableList<CTxDestination>?) {
        if (list != null) {
            val hashMap = HashMap<CTxDestination,Boolean>()
            for (txIn in this.txInList) {
                if (!txIn.isCoinBaseVin()) {
                    val txInDes = txIn.getCTxDestination()
                    if (txInDes != null && (this.wallet.isUsingDesAddressMapHasKey(txInDes) || this.wallet.isUnuseDesAddressMapHasKey(txInDes)) && !hashMap.containsKey(txInDes)) {
                        hashMap.put(txInDes, true)
                    }
                }
            }
            for (txOut in this.txOutList) {
                val txOutDes = txOut.getScriptCTxDestination()
                if (txOutDes != null && (this.wallet.isUsingDesAddressMapHasKey(txOutDes) || this.wallet.isUnuseDesAddressMapHasKey(txOutDes)) && !hashMap.containsKey(txOutDes)) {
                    hashMap.put(txOutDes, java.lang.Boolean.valueOf(true))
                }
            }
            list.addAll(hashMap.keys)
        }
    }

    //mo43066y_
    open fun isRelatedToLocalAddress(): Boolean {
        val addressModel = this.wallet.getSelfAddressModel()
        val transactionModel = this.wallet.getSelfTransactionModel()
        var isRelated = false
        for (txOut in this.txOutList) {
            val txOutCTxDestination = txOut.getScriptCTxDestination()
            if (addressModel.isUsingDesAddressMapHasKey(txOutCTxDestination) || addressModel.isUnuseDesAddressMapHasKey(txOutCTxDestination)) {
                isRelated = true
            }
        }
        for (txIn in this.txInList) {
            val preTxOut = transactionModel.getTxOutByCOutPoint(txIn.getPrevTxOut())
            if (preTxOut != null) {
                val preTxOutCTxDestination = preTxOut!!.getScriptCTxDestination()
                if (addressModel.isUsingDesAddressMapHasKey(preTxOutCTxDestination) || addressModel.isUnuseDesAddressMapHasKey(preTxOutCTxDestination)) {
                    isRelated = true
                }
            }
        }
        return isRelated
    }

    //mo43067z_
    open fun isFromLocalAddress(): Boolean {
        var z = false
        if (this.txInList == null) {
            return false
        }
        val addressModel = this.wallet.getSelfAddressModel()
        val transactionModel = this.wallet.getSelfTransactionModel()
        val txInIterator = this.txInList.iterator()
        while (true) {
            if (!txInIterator.hasNext()) {
                break
            }
            val txOut = transactionModel.getTxOutByCOutPoint((txInIterator.next()).getPrevTxOut())
            if (txOut != null) {
                val cTxDestination = txOut.getScriptCTxDestination()
                var address = addressModel.getAddressByCTxDestinationFromUsingAddressMap(cTxDestination)
                if (address == null) {
                    address = addressModel.getAddressFromUnuseAddressMap(cTxDestination)
                }
                if (address == null) {
                    address = addressModel.getAddressFromUsingAddressShadowMap(cTxDestination)
                }
                if (address != null && !address.isWatchedFlag()) {
                    z = true
                    break
                }
            }
        }
        return z
    }

    open fun mo43064n(): Long {
        return this.satoshi
    }

    fun mo43263o(): Long {
        val e = getSelfTxInList()
        return if (e == null || e.isEmpty()) {
            0
        } else getVoutSumSubVinSum(e[0].getCTxDestination())
    }

    //mo43264p
    fun getType(): TranscationType {
        return this.type
    }

    //mo43219e
    override fun getSelfTxInList(): List<TxIn> {
        return this.txInList
    }

    //mo43257c
    fun getTxIn(i: Int): TxIn? {
        return if (i < 0 || i >= this.txInList.size) {
            null
        } else this.txInList.get(i)
    }

    //mo43259d
    fun getTxOut(i: Int): TxOut? {
        return if (i < 0 || i >= this.txOutList!!.size) {
            null
        } else this.txOutList!!.get(i)
    }

    //mo43218d
    override fun getSelfTxOutList(): List<TxOut> {
        return this.txOutList
    }

    //mo43223h_
    override fun getTxId(): UInt256 {
        return this.txid
    }

    //mo43265q
    fun getTxidHashString(): String {
        return this.txid.hashString()
    }

    //mo43246a
    fun setTxid(uInt256: UInt256) {
        this.txid.set(uInt256 as BaseBlob)
        mo43272x()
    }

    //mo43266r
    fun getFee(): Long {
        return this.fee
    }

    //mo43267s
    fun checkAndGetBlockNoFromAll(): Long {
        val blockNo = this.blockNo
        if (blockNo != -1) {
            return blockNo.toLong()
        }
        if (this.wallet == null) {
            return -1
        }
        val blockHeader = this.wallet.getSelfBlockChainModel().getBlockHeaderFromCachedAndDbByHash(this.blockHash) ?: return -1
        return blockHeader.getBlockNo().toLong()
    }

    //mo43254b
    //910 mo43318b
    fun setBlockHash(uInt256: UInt256?) {
        if (uInt256 == null) {
            this.blockHash = null
        } else {
            this.blockHash = UInt256(uInt256 as BaseBlob?)
        }
    }

    //mo43268t
    fun getBlockHash(): UInt256? {
        return this.blockHash
    }

    //mo43269u
    fun isBlockHashNotNull(): Boolean {
        val blockHash = this.blockHash
        return blockHash != null && !blockHash.isNull()
    }

    //mo43220f
    override fun getVersion(): Long {
        return this.version
    }

    //mo43213a
    override fun setVersion(j: Long) {
        this.version = j
    }

    fun setDirection(j: TransactionDirection) {
        this.direction = j
    }

    //mo43221g
    override fun getLockTime(): Long {
        return this.lockTime
    }

    //mo43216b
    override fun checkAndSetLockTime(j: Long) {
        var time = j
        if (time < 0) {
            time = time and -1
        }
        this.lockTime = time
    }

    //mo43270v
    fun isNotConfirmed(): Boolean {
        return if (!isConfirmed() && this.wallet.getCurrentBlockNo() - this.blockNo.toLong() > 100) {
            true
        } else false
    }

    //mo43271w
    //910 mo43334w
    fun isConfirmed(): Boolean {
        return getConfirms() > 0
    }

    override fun toString(): String {
        val stringBuffer = StringBuffer()
        stringBuffer.append(super.toString())
        val sb = StringBuilder()
        sb.append("\ntxId: ")
        sb.append(this.txid.hashString())
        stringBuffer.append(sb.toString())
        val sb2 = StringBuilder()
        sb2.append("\ntype: ")
        sb2.append(this.type)
        stringBuffer.append(sb2.toString())
        val sb3 = StringBuilder()
        sb3.append("\ndirection: ")
        sb3.append(this.direction)
        stringBuffer.append(sb3.toString())
        val sb4 = StringBuilder()
        sb4.append("\nversion: ")
        sb4.append(this.version)
        stringBuffer.append(sb4.toString())
        val sb5 = StringBuilder()
        sb5.append("\nflag: ")
        sb5.append(this.flag.toInt())
        stringBuffer.append(sb5.toString())
        val sb6 = StringBuilder()
        sb6.append("\nlockTime: ")
        sb6.append(this.lockTime)
        stringBuffer.append(sb6.toString())
        val sb7 = StringBuilder()
        sb7.append("\ntimeStamp: ")
        sb7.append(this.timeStamp)
        stringBuffer.append(sb7.toString())
        val sb8 = StringBuilder()
        sb8.append("\nfee: ")
        sb8.append(this.fee)
        stringBuffer.append(sb8.toString())
        val sb9 = StringBuilder()
        sb9.append("\nblockNo: ")
        sb9.append(this.blockNo)
        stringBuffer.append(sb9.toString())
        val sb10 = StringBuilder()
        sb10.append("\nblockHash: ")
        sb10.append(this.blockHash)
        stringBuffer.append(sb10.toString())
        val sb11 = StringBuilder()
        sb11.append("\nsatoshi: ")
        sb11.append(this.satoshi)
        stringBuffer.append(sb11.toString())
        val sb12 = StringBuilder()
        sb12.append("\nconfirmed: ")
        sb12.append(isConfirmed())
        stringBuffer.append(sb12.toString())
        val sb13 = StringBuilder()
        sb13.append("\nconfirms: ")
        sb13.append(getConfirms())
        stringBuffer.append(sb13.toString())
        stringBuffer.append("\ntxins: ")
        for (txIn in this.txInList) {
            stringBuffer.append("\n[\n")
            stringBuffer.append(txIn.toString())
            stringBuffer.append("\n]")
        }
        stringBuffer.append("\ntxouts: ")
        for (txOut in this.txOutList) {
            stringBuffer.append("\n[\n")
            stringBuffer.append(txOut.toString())
            stringBuffer.append("\n]")
        }
        return stringBuffer.toString()
    }

    //mo42816i_
    override fun updateTxidByContent(): UInt256 {
        Log.d("Transaction", "updateTxidByContent")
        this.txid.clear()
        try {
            val cHashWriter = CHashWriter()
            TransactionSerializer.m10970a(this as TransactionInteface, cHashWriter as StreamWriter, false)
            setTxid(cHashWriter.GetHash())
        } catch (e: IOException) {
            e.printStackTrace()
            setTxid(UInt256.empty())
        }

        return this.txid
    }

    fun mo43272x() {
        for (txIn in this.txInList) {
            txIn.setMTxid(this.txid)
        }
        for (txOut in this.txOutList) {
            txOut.setMTxid(this.txid)
        }
    }

    //mo43273y
    fun isTransactionLock(): Boolean {
        val z: Boolean
        val txInListIterator = this.txInList.iterator()
        while (true) {
            if (txInListIterator.hasNext()) {
                if (txInListIterator.next().getSequence() == -1L) {
                    z = false
                    break
                }
            } else {
                z = true
                break
            }
        }
        val offsetBlockNoAddOne = getConfirms()
        if (offsetBlockNoAddOne < 1) {
            return true
        }
        if (getType() == TranscationType.COINBASE && offsetBlockNoAddOne < 101) {
            return true
        }
        if (z) {
            return false
        }
        val lockTime = this.lockTime
        if (lockTime >= 0 && lockTime < 500000000) {
            if (this.lockTime > this.wallet.getCurrentBlockNo()) {
                return true
            }
        }
        return false
    }

    //mo43222h
    override fun getFlag(): Int {
        return this.flag.toInt()
    }

    //mo43212a
    override fun setFlag(i: Int) {
        this.flag = i.toShort()
    }

    override fun hashCode(): Int {
        val uInt256 = this.txid ?: return 0
        return uInt256!!.hashCode()
    }

    //mo42834z
    open fun getTransactionBytesLength(): Int {
        return getTxInBytesLength() + getTxOutsBytesLength() + 4
    }

    //mo43228A
    fun getTxInBytesLength(): Int {
        val list = this.txInList
        if (list == null || list!!.isEmpty()) {
            return 1
        }
        var lengthNative = VariableInteger.getLengthNative(this.txInList.size.toLong())
        for (txIn in this.txInList) {
            lengthNative += txIn.getLength()
        }
        return lengthNative
    }

    //mo43229B
    fun getTxOutsBytesLength(): Int {
        val list = this.txOutList
        if (list == null || list!!.isEmpty()) {
            return 1
        }
        var lengthNative = VariableInteger.getLengthNative(this.txOutList.size.toLong())
        for (m in this.txOutList) {
            lengthNative += m.mo43325m()
        }
        return lengthNative
    }

    //mo43230C
    //910 mo43286h_
    fun getTxid(): UInt256 {
        return this.txid
    }

    //mo42825a
    @Throws(NotMatchException::class)
    open fun signTransaction(pwd: CharSequence): Boolean {
        Log.d("Transaction", "signTransaction")
        this.wallet.checkWalletPassword(pwd)
        val b = mo43255b(pwd)
        updateTxidByContent()
        recomputeByTxs(null)
        return b
    }
    @Throws(NotMatchException::class,AddressFormatException::class,ScriptException::class)
    open fun mo42826a(charSequence: CharSequence, mbVar: VOutList?, lvVar: ScriptList): Boolean {
        this.wallet.checkWalletPassword(charSequence)
        return if (mbVar == null || mbVar!!.mo44704a() === 0) {
            mo43255b(charSequence)
        } else signrawtransaction(charSequence, mbVar, lvVar)
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    fun mo43255b(pwd: CharSequence): Boolean {
        val transactionModel = this.wallet.getSelfTransactionModel()
        val preTxOutList = VOutList()
        for (txIn in this.txInList) {
            preTxOutList.addTxOutToList(transactionModel.getTxOutByCOutPoint(txIn.mPrevTxOut))
        }
        return signrawtransaction(pwd, preTxOutList, null)
    }

    @Throws(AddressFormatException::class, ScriptException::class)
    //mo42832b
    open fun signrawtransaction(charSequence: CharSequence, vOutList: VOutList?, scriptList: ScriptList?): Boolean {
        var dlVar: TxIn
        var i: Int
        var j: Long
        var cScript: CScript
        var czVar: SignatureData
        var sign: Sign
        if (vOutList == null || vOutList!!.isVOutListEmpty()) {
            vdsMain.Log.logObjectWarning(this as Any, "Previors vouts is empty!")
            return false
        }
        val dfVar = CMutableTransaction(this)
        val E = clone()
        val c = vOutList!!.getTxOutIterator()
        var cyVar2 = Sign()
        var i2 = 1
        var i3 = 0
        var z = true
        for (dlVar2 in this.txInList) {
            val dnVar = c.next() as TxOut
            if (dnVar == null) {
                i3++
            } else {
                val j2 = dnVar.getSatoshi()
                if (dlVar2.getSatoshi() === 0L || j2 == dlVar2.getSatoshi()) {
                    val cScript2 = CScript(dnVar.getScript() as SeriableData)
                    val czVar2 = SignatureData()
                    if (scriptList == null || i3 >= scriptList!!.getScriptListSize()) {
                        czVar = czVar2
                        cScript = cScript2
                        j = j2
                        val i4 = i3
                        val cyVar3 = cyVar2
                        val izVar = this.wallet
                        i = i4
                        dlVar = dlVar2
                        val coVar = MutableTransactionSignatureCreator(dfVar, i4, j, 1)
                        sign = cyVar3
                        cyVar3.mo43205a(
                            izVar as SigningProvider,
                            charSequence,
                            coVar as BaseSignatureCreator,
                            cScript,
                            czVar,
                            *arrayOfNulls<CScript>(0)
                        )
                    } else {
                        val izVar2 = this.wallet
                        val iArr = IntArray(i2)
                        iArr[0] = i2
                        czVar = czVar2
                        cScript = cScript2
                        j = j2
                        val coVar2 = MutableTransactionSignatureCreator(dfVar, i3, j2, *iArr)
                        val cScriptArr = arrayOfNulls<CScript>(i2)
                        cScriptArr[0] = scriptList!!.mo44671a(i3)
                        val i5 = i3
                        val cyVar5 = cyVar2
                        cyVar2.mo43205a(
                            izVar2 as SigningProvider,
                            charSequence,
                            coVar2 as BaseSignatureCreator,
                            cScript,
                            czVar,
                            *cScriptArr
                        )
                        sign = cyVar5
                        i = i5
                        dlVar = dlVar2
                    }
                    val a = sign.CombineSignatures(
                        cScript,
                        TransactionSignatureChecker(E, i, j) as BaseSignatureChecker,
                        czVar,
                        sign.DataFromTransaction(dfVar, i.toLong())
                    )
                    Sign.UpdateTransaction(dlVar, a)
                    val scriptErrorResult = ScriptErrorResult()
                    val b = sign.getSelfInterpreter()
                    val cScript4 = a.cScript
                    val m = dlVar.getCScriptWitness()
                    val transactionSignatureChecker = TransactionSignatureChecker(E, i, j)
                    if (!b.VerifyScript(
                            cScript4,
                            cScript,
                            m,
                            131039,
                            transactionSignatureChecker as BaseSignatureChecker,
                            scriptErrorResult
                        )
                    ) {
                        if (scriptErrorResult.scriptError !== ScriptError.SCRIPT_ERR_OK) {
                            vdsMain.Log.logObjectWarning(
                                this as Any,
                                String.format(
                                    Locale.getDefault(),
                                    "Sign vin %d failed %s",
                                    *arrayOf<Any>(Integer.valueOf(i), scriptErrorResult.scriptError.toString())
                                )
                            )
                            z = false
                        }
                    }
                    i3 = i + 1
                    cyVar2 = sign
                    i2 = 1
                } else {
                    val locale = Locale.getDefault()
                    val objArr = arrayOfNulls<Any>(4)
                    objArr[0] = java.lang.Long.valueOf(dlVar2.getSatoshi())
                    objArr[i2] = dnVar.txid.toString()
                    objArr[2] = Integer.valueOf(dnVar.mIndex)
                    objArr[3] = java.lang.Long.valueOf(dnVar.getSatoshi())
                    throw InvalidateTransactionException(
                        String.format(
                            locale,
                            "Amount (%ld) for txin (%s,%d) was not from utxo %ld",
                            *objArr
                        )
                    )
                }
            }
        }
        return z
    }

    //mo43232D
    fun getCloneTransaction(): Transaction {
        val transaction = clone()
        val arrayList = ArrayList<TxIn>(this.txInList.size)
        for (txIn1 in this.txInList) {
            val txIn = TxIn(txIn1)
            txIn.mo43292a(txIn1.getScriptSig().mo43170k())
            arrayList.add(txIn)
        }
        transaction.mo43251a_(arrayList, false)
        transaction.updateTxidByContent()
        return transaction
    }

    /* renamed from: E */
    open fun clone(): Transaction {
        return this.wallet.getSelfWalletHelper().mo42405a(this)
    }

    //mo43234F
    fun isCoinBaseTransaction(): Boolean {
        val list = this.txInList
        return if (list == null || list!!.size != 1 || this.txOutList == null) {
            false
        } else (this.txInList.get(0) as TxIn).isCoinBaseVin()
    }

    /* renamed from: G */
    fun mo43235G(): Int {
        var i = 0
        if (this.txInList.isEmpty()) {
            return 0
        }
        val crVar = Script()
        for (k in this.txInList) {
            crVar.mo43154a(k.getScriptSig())
            i += crVar.mo43172m()
        }
        return i
    }

    //910 mo43299H
    //mo43236H
    fun getConfirms(): Int {
        val uInt256 = this.blockHash
        if (uInt256 == null || uInt256.isNull() || this.wallet == null) {
            return 0
        }
        val R = this.wallet.getCurrentBlockNo().toInt() - this.blockNo
        return if (R <= 0) {
            1
        } else R + 1
    }

    //mo43237I
    //910 mo43300I
    fun getTimeStamp(): Long {
        return this.timeStamp
    }

    //mo43258c
    fun setTimeStamp(j: Long) {
        this.timeStamp = j
    }

    override fun equals(obj: Any?): Boolean {
        if (obj === this) {
            return true
        }
        val checkTransaction = obj as Transaction
        if (this.txid != checkTransaction.txid || this.version != checkTransaction.version || this.lockTime != checkTransaction.lockTime || this.txInList.size != checkTransaction.txInList.size || this.txOutList.size != checkTransaction.txOutList.size) {
            return false
        }
        val txInIterator = checkTransaction.txInList.iterator()
        for (txIn in this.txInList) {
            if (!txIn.equals(txInIterator.next() as TxIn)) {
                return false
            }
        }
        val txOutIterator = checkTransaction.txOutList.iterator()
        for (txOut in this.txOutList) {
            if (!txOut.equals(txOutIterator.next() as TxOut)) {
                return false
            }
        }
        return true
    }


    //mo43238J
    fun getSerialSelfByteArray(): ByteArray {
        try {
            return serialSelfToBytes()
        } catch (e: Exception) {
            e.printStackTrace()
            return ByteArray(64)
        }

    }

    //mo43239K
    fun getTransactionHexString(): String {
        return StringToolkit.bytesToString(getSerialSelfByteArray()).toLowerCase(Locale.getDefault())
    }

    /* renamed from: a */
    open fun mo43053a(vararg mbVarArr: VOutList): Boolean {
        return mo42833b(*mbVarArr)
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
   open fun mo42833b(vararg mbVarArr: VOutList): Boolean {
        if (this.txInList.isEmpty()) {
            return true
        }
        val c =
            if (mbVarArr.size <= 0 || mbVarArr[0].isVOutListEmpty() || mbVarArr[0].mo44704a() !== this.txInList.size) null else mbVarArr[0].getTxOutIterator()
        var i = 0
        for (dlVar in this.txInList) {
            if (!dlVar.mo43304r()) {
                val o = (if (c != null) c!!.next() as TxOut else dlVar.getPrevTransactionTxOut()) ?: return false
                val cScript = CScript(o.getScript() as SeriableData)
                val cuVar = ScriptErrorResult()
                try {
                    if (!Interpreter().VerifyScript(
                            CScript(dlVar.getScriptSig() as SeriableData),
                            cScript,
                            dlVar.getCScriptWitness(),
                            131039,
                            TransactionSignatureChecker(this, i, o.getSatoshi()) as BaseSignatureChecker,
                            cuVar
                        ) && cuVar.scriptError !== ScriptError.SCRIPT_ERR_OK
                    ) {
                        return false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            i++
        }
        return true
    }

    /* renamed from: L */
    fun mo43240L(): List<String> {
        val arrayList = ArrayList<String>()
        for (i in this.txInList) {
            val i2 = i.getAddress()
            if (i2 != null) {
                arrayList.add(i2)
            }
        }
        return arrayList
    }

    /* renamed from: M */
    fun mo43241M(): List<String> {
        val arrayList = ArrayList<String>()
        for (i in this.txOutList) {
            val i2 = i.getAddress()
            if (i2 != null) {
                arrayList.add(i2)
            }
        }
        return arrayList
    }

    /* renamed from: N */
    fun mo43242N(): Int {
        return getConfirms()
    }

    /* renamed from: O */
    fun mo43243O(): Int {
        return if (!isCoinBaseTransaction()) {
            0
        } else Math.max(0, 101 - mo43242N())
    }

    //910 mo43309a
    fun updateTimeStamp(time: Long, vararg updateDb: Boolean) {
        this.timeStamp = time
        if (updateDb.size > 0 && updateDb[0]) {
            val contentValues = ContentValues()
            contentValues.put("time", java.lang.Long.valueOf(this.timeStamp))
            try {
                updateData(contentValues, String.format(Locale.US, "%s=?", *arrayOf<Any>("txid")), arrayOf<String>(this.txid.toString()))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /* renamed from: c */
    override fun getContentValues(): ContentValues {
        var vinBytes: ByteArray
        var voutBytes: ByteArray
        var witnessBtyes: ByteArray
        try {
            val unsafeByteArrayOutputStream = UnsafeByteArrayOutputStream()
            TransactionSerializer.m10972a(this as TransactionInteface, unsafeByteArrayOutputStream as OutputStream)
            vinBytes = unsafeByteArrayOutputStream.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()
            vinBytes = ByteArray(0)
        }

        try {
            val abp2 = UnsafeByteArrayOutputStream()
            TransactionSerializer.m10980b(this as TransactionInteface, abp2 as OutputStream)
            voutBytes = abp2.toByteArray()
        } catch (e2: IOException) {
            e2.printStackTrace()
            voutBytes = ByteArray(0)
        }

        try {
            witnessBtyes = TransactionSerializer.m10982b(this)
        } catch (e3: IOException) {
            e3.printStackTrace()
            witnessBtyes = ByteArray(0)
        }

        val contentValues = ContentValues()
        contentValues.put("txid", this.txid.hashString())
        contentValues.put("version", java.lang.Long.valueOf(this.version))
        contentValues.put("flag", java.lang.Short.valueOf(this.flag))
        contentValues.put("locktime", java.lang.Long.valueOf(this.lockTime))
        contentValues.put("time", java.lang.Long.valueOf(this.timeStamp))
        contentValues.put("bno", Integer.valueOf(this.blockNo))
        val uInt256 = this.blockHash
        if (uInt256 != null) {
            contentValues.put("block_hash", uInt256.hexString())
        } else {
            contentValues.put("block_hash", "")
        }
        contentValues.put("tx_ins", vinBytes)
        contentValues.put("tx_outs", voutBytes)
        contentValues.put("witness", witnessBtyes)
        return contentValues
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    override fun writeSerialData(streamWriter: StreamWriter) {
        TransactionSerializer.m10970a(this as TransactionInteface, streamWriter, *BooleanArray(0))
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    override fun decodeSerialData(seriableData: SeriableData) {
        TransactionSerializer.m10985e(this, seriableData)
    }

    //mo42817P
    open fun getBlockHashAndNoContentValues(): ContentValues {
        val contentValues = ContentValues()
        contentValues.put("block_hash", this.blockHash.toString())
        contentValues.put("bno", Integer.valueOf(this.blockNo))
        return contentValues
    }

    //mo43244Q
    fun calMinFee(): Long {
        val length = (serialSelfToBytes().size.toLong()).toDouble()
        val list = this.txInList
        return Math.max(this.wallet.getSelfTransactionModel().mo44511c() * (length + if (list == null) 0.0 else list.size.toDouble() * 81.5).toLong(), 10000)
    }
}
