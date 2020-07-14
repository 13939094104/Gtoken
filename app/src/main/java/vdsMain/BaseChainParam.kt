package vdsMain

import bitcoin.UInt256
import com.vc.libcommon.util.UInt64
import vdsMain.block.BlockInfo
import vdsMain.block.BlockSyncStatus
import vdsMain.transaction.Transaction
import vdsMain.wallet.Wallet


enum class NETWORK_TYPE {
    MAIN,
    REGTEST,
    TEST
}

abstract class BaseChainParam {

    constructor(wallet: Wallet) {
        this.wallet = wallet
        this.params = getBaseParams()
    }

    //f12898D
    @JvmField
    var minFee = UInt64(10000)

    //f12899E
    @JvmField
    var maxFee = UInt64.m856c()

    protected var wallet: Wallet

    //f12931z
    var networkType = NETWORK_TYPE.MAIN

    //f12912R
    @JvmField
    var walletPwd: String? = null

    //f12895A
    @JvmField
    var labelResAndVersionName: String? = null

    @JvmField
    //f12915j
    public var magicBytes: ByteArray? = null

    //f12928w
    protected var params: Params? = null

    //f12926u
    protected  var mBlockInfo: BlockInfo?=null

    //mo42377a
    abstract fun getBaseParams(): Params

    //f12918m
    @JvmField
    var protocalVersion: Int = 0

    //f12904J
    @JvmField
    var peerCount = 5

    //f12916k
    @JvmField
    var peerPort: Int = 0

    //f12905K
    var peerTimeout: Long = 60000

    //f12906L
    var pingTime: Long = 30000

    //f12911Q
    var testNetAddress: String? = null

    //f12917l
    var imPort: Int = 0


    protected var f12929x = 0


    /* renamed from: y */
    protected var f12930y = 0

    //f12920o
    @JvmField
    var hash: UInt256? = null

    var password: String? = null

    //f12913S
    var testAddressList: List<Array<String>>? = null

    //f12921p
    var genesisMerkelRootHash: UInt256? = null

    var f12924s: ByteArray? = null

    //f12922q
    var base58Prefixes_public_address: ByteArray? = null

    //f12923r
    var base58Prefixes_script_address: ByteArray? = null

    var f12925t: String? = null

    var blockChainType = BLOCK_CHAIN_TYPE.BITCOIN


    fun getSelfWalletPwd():String?{
        return walletPwd
    }


    //mo43966s
    fun getMaxGetDataSize(): Int {
        return 50000
    }

    //mo43964q
    //915 mo44299q
    fun getSelfNetworkType(): NETWORK_TYPE {
        return this.networkType
    }

    fun mo43963p(): Int {
        return this.f12930y
    }


    fun mo43962o(): Int {
        return this.f12929x
    }


    fun mo43961n(): Params {
        return this.params!!
    }

    abstract fun getMainnetFirstBlockInfo(): BlockInfo?

    abstract fun mo43958j(): Transaction?

    abstract fun mo42386i(): BlockInfo?

    abstract fun mo43960l(): Transaction?

    abstract fun mo42385h(): BlockInfo?

    abstract fun mo43959k(): Transaction?

    //mo43965r
    fun getFirstBlockInfo(): BlockInfo? {
        val transaction: Transaction?
        val blockInfo: BlockInfo
        val blockInfo1 = this.mBlockInfo
        if (blockInfo1 != null) {
            return blockInfo1
        }
        if (this.networkType === NETWORK_TYPE.MAIN) {
            blockInfo = getMainnetFirstBlockInfo()!!
            transaction = mo43958j()
        } else if (this.networkType === NETWORK_TYPE.REGTEST) {
            blockInfo = mo42386i()!!
            transaction = mo43960l()
        } else {
            blockInfo = mo42385h()!!
            transaction = mo43959k()
        }
        blockInfo.setBlockNoAndSetTransactionsHash(0)
        if (transaction != null) {
            blockInfo.mo44307a(transaction)
        }
        blockInfo.setBlockHeaderSynchronized(BlockSyncStatus.SYNCHED)
        this.mBlockInfo = blockInfo
        return blockInfo
    }

    fun mo43957a(ihVar: Base58Type): ByteArray? {
        when (ihVar) {
            Base58Type.PUBKEY_ADDRESS -> return this.base58Prefixes_public_address
            Base58Type.SCRIPT_ADDRESS -> return this.base58Prefixes_script_address
            else -> return null
        }
    }


}
