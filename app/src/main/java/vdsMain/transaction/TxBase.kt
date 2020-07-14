package vdsMain.transaction

import bitcoin.UInt256
import net.bither.bitherj.utils.Utils
import vdsMain.DataTypeToolkit
import vdsMain.WalletSeriablData
import vdsMain.wallet.Wallet

import java.util.Arrays

enum class TxnOutType private constructor(/* renamed from: o */
    //f12393o
    private val value: Int
) {
    TX_NONSTANDARD(0),
    TX_PUBKEY(1),
    TX_PUBKEYHASH(2),
    TX_SCRIPTHASH(3),
    TX_MULTISIG(4),
    TX_NULL_DATA(5),
    TX_CREATE(6),
    TX_CALL(7),
    TX_WITNESS_V0_SCRIPTHASH(8),
    TX_WITNESS_V0_KEYHASH(9),
    TX_WITNESS_UNKNOWN(10),
    TX_CONTRACT_CREATE(11),
    TX_CONTRACT_CALL(12),
    TX_CONTRACT_SPENT(13);

    //mo43211a
    fun getValue(): Int {
        return this.value
    }

    companion object {
        //m10858a
        fun getTxOutType(i: Int): TxnOutType {
            when (i) {
                0 -> return TX_NONSTANDARD
                1 -> return TX_PUBKEY
                2 -> return TX_PUBKEYHASH
                3 -> return TX_SCRIPTHASH
                4 -> return TX_MULTISIG
                5 -> return TX_NULL_DATA
                6 -> return TX_CREATE
                7 -> return TX_CALL
                8 -> return TX_WITNESS_V0_SCRIPTHASH
                9 -> return TX_WITNESS_V0_KEYHASH
                10 -> return TX_WITNESS_UNKNOWN
                11 -> return TX_CONTRACT_CREATE
                12 -> return TX_CONTRACT_CALL
                13 -> return TX_CONTRACT_SPENT
                else -> return TX_NONSTANDARD
            }
        }
    }
}

abstract class TxBase : WalletSeriablData {

    //f12430a
    @JvmField
    var txid: UInt256? = null

    //f12431b
    @JvmField
    protected var mSatoshi: Long = 0

    //f12432c
    @JvmField
    var mIndex = 0

    //f12433d
    @JvmField
    protected var mFlag: Short = 0

    /* renamed from: e */
    protected var f12434e = -1

    /* renamed from: f */
    protected var f12435f = 0

    //mo43289h
    abstract fun getTxnOutType(): TxnOutType

    //mo42838i
    abstract fun getAddress(): String?

    constructor(izVar: Wallet) : super(izVar) {}

    constructor(dkVar: TxBase) : super(dkVar as WalletSeriablData) {
        this.txid = dkVar.txid
        this.mSatoshi = dkVar.mSatoshi
        this.mIndex = dkVar.mIndex
        this.mFlag = dkVar.mFlag
        this.f12434e = dkVar.f12434e
    }

    //mo43277a
    fun setSatoshiValue(j: Long) {
        this.mSatoshi = j
    }

    /* renamed from: b */
    fun mo43282b(j: Long) {
        this.mSatoshi += j
    }

    /* renamed from: c */
    fun mo43284c(j: Long) {
        this.mSatoshi -= j
    }

    //mo43275a
    open fun getSatoshi(): Long {
        return this.mSatoshi
    }

    /* renamed from: a */
    fun mo43279a(str: String) {
        val uInt256 = this.txid
        if (uInt256 == null) {
            this.txid = UInt256(str)
        } else {
            uInt256.hex = str
        }
        reComputeIndexAndHash()
    }

    //mo43278a
    fun setMTxid(uInt256: UInt256) {
        this.txid = uInt256
        reComputeIndexAndHash()
    }

    //mo43280b
    //getTxid
    fun getMTxid(): UInt256? {
        return txid;
    }

    //mo43283c
    fun getTxidString(): String {
        val uInt256 = this.txid ?: return "00000000000000000000000000000000"
        return uInt256.hashString()
    }

    //mo43276a
    fun setIndex(i: Int) {
        this.mIndex = i
        reComputeIndexAndHash()
    }

    //mo43285d
    fun getIndex(): Int {
        return this.mIndex
    }

    //mo43286e
    fun getFlag(): Short {
        return this.mFlag
    }

    //mo43281b
    fun setFlag(i: Int) {
        this.mFlag = i.toShort()
    }

    /* renamed from: f */
    fun mo43287f(): Boolean {
        return this.wallet.getCurrentBlockNo() > this.f12434e.toLong()
    }

    override fun toString(): String {
        val stringBuffer = StringBuffer()
        stringBuffer.append(super.toString())
        stringBuffer.append("\ntxid: ")
        stringBuffer.append(getTxidString())
        stringBuffer.append("\ntype: ")
        stringBuffer.append(getTxnOutType().name)
        stringBuffer.append("\naddress: ")
        stringBuffer.append(getAddress())
        val sb = StringBuilder()
        sb.append("\nmSatoshi: ")
        sb.append(this.mSatoshi)
        stringBuffer.append(sb.toString())
        val sb2 = StringBuilder()
        sb2.append("\nmIndex: ")
        sb2.append(this.mIndex)
        stringBuffer.append(sb2.toString())
        val sb3 = StringBuilder()
        sb3.append("\nmFlag: ")
        sb3.append(this.mFlag.toInt())
        stringBuffer.append(sb3.toString())
        return stringBuffer.toString()
    }

    override fun hashCode(): Int {
        return this.f12435f
    }

    override fun equals(obj: Any?): Boolean {
        var z = true
        if (obj === this) {
            return true
        }
        if (obj !is TxBase) {
            return false
        }
        val dkVar = obj as TxBase?
        if (!DataTypeToolkit.m11497a(dkVar!!.txid as Any?, this.txid as Any?)) {
            return false
        }
        if (this.mIndex != dkVar.mIndex) {
            z = false
        }
        return z
    }

    //mo43288g
    fun reComputeIndexAndHash() {
        val bArr = ByteArray(36)
        val uInt256 = this.txid
        if (uInt256 != null) {
            System.arraycopy(uInt256.data(), 0, bArr, 0, 32)
        }
        Utils.uint32ToByteArrayLE(this.mIndex.toLong(), bArr, 32)
        this.f12435f = Arrays.hashCode(bArr)
    }
}
