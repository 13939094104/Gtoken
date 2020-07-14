package vdsMain.transaction

import generic.io.StreamWriter
import net.bither.bitherj.exception.ScriptException
import vdsMain.CFeeRate
import vdsMain.CTxDestination
import vdsMain.wallet.Wallet
import java.io.IOException

public open class TxOut : TxBase {
    //f12441g
    @JvmField
    protected var script: Script? = null

    constructor(wallet: Wallet) : super(wallet) {}

    constructor(txOut: TxOut) : super(txOut as TxBase) {
        checkAndSetScript(txOut.script, *BooleanArray(0))
    }

    constructor(wallet: Wallet, nValueIn: Long, nFlagIn: Int, scriptPubKeyIn: Script) : super(wallet) {
        this.mSatoshi = nValueIn
        this.mFlag = nFlagIn.toShort()
        checkAndSetScript(scriptPubKeyIn, *BooleanArray(0))
        reComputeIndexAndHash()
    }

    fun mo43325m(): Int {
        var script=this.script
        if(script==null){
            return 9
        }else{
            return script.getScriptLength();
        }
    }

    @Throws(IOException::class)
    override fun writeSerialData(streamWriter: StreamWriter) {
        streamWriter.writeUInt64(this.mSatoshi)
        val scriptPubKey = this.script
        if (scriptPubKey != null) {
            val bytes = scriptPubKey.getScriptChunksBytes()
            if (bytes == null) {
                streamWriter.writeVariableInt(0)
                return
            }
            streamWriter.writeVariableInt(bytes.size.toLong())
            streamWriter.writeBytes(bytes)
            return
        }
        streamWriter.writeVariableInt(0)
    }

    fun mo43324k(): Boolean {
        return this.wallet.getSelfTransactionModel().mo44501a(this)
    }

    @Throws(IOException::class)
    override fun onDecodeSerialData() {
        this.mSatoshi = readUInt64().toLong()
        val b = readVariableInt().getIntValue()
        if (b > 0) {
            try {
                this.script = Script(readBytes(b))
            } catch (e: ScriptException) {
                throw IOException(e)
            }

        }
        reComputeIndexAndHash()
    }

    //mo42840n
    fun getCOutPoint(): COutPoint {
        return COutPoint(this.txid, this.mIndex)
    }

    //mo43323j
    fun getScript(): Script? {
        return this.script
    }

    //mo43320a
    fun checkAndSetScript(script: Script?, vararg zArr: Boolean) {
        if (script == null) {
            this.script = null
        } else if (zArr.size == 0 || zArr[0]) {
            this.script = Script(script)
        } else {
            this.script = script
        }
    }

    override fun getAddress(): String? {
        val script = this.script
        return script?.mo43152a(this.wallet.getChainParams())
    }

    fun mo43321a(hVar: CFeeRate): Boolean {
        var z = false
        if (this.script == null) {
            return false
        }
        if (this.mSatoshi < mo43322b(hVar) && this.script!!.mo43156a() && this.script!!.mo43158b()) {
            z = true
        }
        return z
    }

    fun mo43322b(cFeeRate: CFeeRate): Long {
        val script = this.script
        return if (script == null || script.isOP_RETURN()) {
            0
        } else cFeeRate.mo43715a((mo43325m() + 148).toLong()) * 3
    }

    //mo42839l
    open fun getScriptCTxDestination(): CTxDestination? {
        val script = this.script
        return if (script != null) {
            script!!.mo43169j()
        } else null
    }

    override fun getTxnOutType(): TxnOutType {
        val script = this.script
        return if (script != null) {
            script.txnOutType
        } else TxnOutType.TX_NONSTANDARD
    }

    open fun clone(): TxOut {
        return TxOut(this)
    }
}
