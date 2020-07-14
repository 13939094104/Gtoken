package vdsMain.wallet

import android.util.LongSparseArray
import bitcoin.UInt256
import net.bither.bitherj.utils.Utils
import vdsMain.BLOCK_CHAIN_TYPE
import vdsMain.BaseChainParam
import vdsMain.NETWORK_TYPE
import vdsMain.Params
import vdsMain.block.BitcoinBlockHeader
import vdsMain.block.BlockInfo
import vdsMain.transaction.Transaction

open class ChainParams(wallet: Wallet) : BaseChainParam(wallet) {

    //f14912b
    var version: Int = 0

    @JvmField
    var f14913c: Long = 120

    //f14915e
    //915 f15588e
    var vidLockBlockNumber: Long = 70

    @JvmField
    var f14916f: Long = 0

    @JvmField
    var f14917g: Long = 0

    @JvmField
    var f14918h: Long = 0

    @JvmField
    var f14919i = LongSparseArray<UInt256>()



    override fun getBaseParams(): Params {
        return Params()
    }

    fun mo42379b(): Array<String>? {
        return null
    }

    fun mo42380c(): Array<ByteArray?> {
        return arrayOf(this.f12924s)
    }

    //mo42381d
    //915 mo42661d
    open fun initMainParams() {
        this.version = 1
        this.networkType = NETWORK_TYPE.MAIN
        this.magicBytes = byteArrayOf(-7, -66, -76, -39)
        this.protocalVersion = 70015
        this.peerPort = 8333
        this.vidLockBlockNumber = 10080
        this.hash = UInt256(Utils.getReverseStringBytes("000000000019d6689c085ae165831e934ff763ae46a2a6c172b3f1b60a8ce26f"), true)
        this.genesisMerkelRootHash = UInt256(Utils.getReverseStringBytes("4a5e1e4baab89f3a32518a88c31bc87f618f76673e2cc77ab2127b7afdeda33b"), true)
        this.base58Prefixes_public_address = byteArrayOf(0)
        this.base58Prefixes_script_address = byteArrayOf(5)
        this.f12924s = byteArrayOf(java.lang.Byte.MIN_VALUE)
        this.f12925t = "bc"
        this.blockChainType = BLOCK_CHAIN_TYPE.BITCOIN
        this.f14913c = 120
        this.f14916f = 227931
        this.f14917g = 388381
        this.f14918h = 363725
        this.f14919i.put(11111, UInt256("0x0000000069e244f73d78e8fd29ba2fd2ed618bd6fa2ee92559f542fdb26e7c1d"))
        this.f14919i.put(33333, UInt256("0x000000002dd5588a74784eaa7ab0507a18ad16a236e7b1ce69f00d7ddfb5d0a6"))
        this.f14919i.put(74000, UInt256("0x0000000000573993a3c9e41ce34471c079dcf5f52a0e824a81e7f953b8661a20"))
        this.f14919i.put(105000, UInt256("0x00000000000291ce28027faea320c8d2b054b2e0fe44a773f3eefb151d6bdc97"))
        this.f14919i.put(134444, UInt256("0x00000000000005b12ffd4cd315cd34ffd4a594f430ac814c91184a0d42d2b0fe"))
        this.f14919i.put(168000, UInt256("0x000000000000099e61ea72015e79632f216fe6cb33d7899acb35b75c8303b763"))
        this.f14919i.put(193000, UInt256("0x000000000000059f452a5f7340de6682a977387c17010ff6e6c3bd83ca8b1317"))
        this.f14919i.put(210000, UInt256("0x000000000000048b95347e83192f69cf0366076336c639f9b7228e9ba171342e"))
        this.f14919i.put(216116, UInt256("0x00000000000001b4f4b433e81ee46494af945cf96014816a4e2370f11b23df4e"))
        this.f14919i.put(225430, UInt256("0x00000000000001c108384350f74090433e7fcf79a606b8e797f065b130575932"))
        this.f14919i.put(250000, UInt256("0x000000000000003887df1f29024b06fc2200b55f8af8f35453d7be294df2d214"))
        this.f14919i.put(279000, UInt256("0x0000000000000001ae8c72a0b0c301f67e3afca10e819efa9041e458e9bd7e40"))
        this.f14919i.put(295000, UInt256("0x00000000000000004d9b4ef50f0f9d686fd69db2e03af35a100370c64632a983"))
    }


    //mo42383f
    open fun initRegtestParams() {
        this.version = 1
        this.networkType = NETWORK_TYPE.REGTEST
        this.magicBytes = byteArrayOf(-6, -65, -75, -38)
        this.protocalVersion = 70002
        this.peerPort = 18000
        this.vidLockBlockNumber = 70
        this.hash = UInt256(Utils.getReverseStringBytes("0f9188f13cb7b2c71f2a335e3a4fc328bf5beb436012afca590b1a11466e2206"), true)
        this.genesisMerkelRootHash = UInt256(Utils.getReverseStringBytes("4a5e1e4baab89f3a32518a88c31bc87f618f76673e2cc77ab2127b7afdeda33b"), true)
        this.base58Prefixes_public_address = byteArrayOf(111)
        this.base58Prefixes_script_address = byteArrayOf(-60)
        this.f12924s=byteArrayOf(-17)
        this.f12925t = "bcrt"
        this.blockChainType = BLOCK_CHAIN_TYPE.BITCOIN
        this.f14913c = 120
        this.f14916f = 100000000
        this.f14917g = 1351
        this.f14918h = 1251
        this.f14919i.put(0, UInt256("0f9188f13cb7b2c71f2a335e3a4fc328bf5beb436012afca590b1a11466e2206"))
    }

    //mo42382e
    open fun initTestParams() {
        this.version = 1
        this.networkType = NETWORK_TYPE.TEST
        this.magicBytes = byteArrayOf(11, 17, 9, 7)
        this.protocalVersion = 70002
        this.peerPort = 18333
        this.vidLockBlockNumber = 10080
        this.hash = UInt256(Utils.getReverseStringBytes("000000000933ea01ad0ee984209779baaec3ced90fa3f408719526f8d77f4943"), true)
        this.genesisMerkelRootHash = UInt256(Utils.getReverseStringBytes("4a5e1e4baab89f3a32518a88c31bc87f618f76673e2cc77ab2127b7afdeda33b"), true)
        this.base58Prefixes_public_address = byteArrayOf(111)
        this.base58Prefixes_script_address = byteArrayOf(-60)
        this.f12924s = byteArrayOf(-17)
        this.f12925t = "tb"
        this.blockChainType = BLOCK_CHAIN_TYPE.BITCOIN
        this.f14913c = 120
        this.f14916f = 21111
        this.f14917g = 581885
        this.f14918h = 330776
        this.f14919i.put(546, UInt256("000000002a936ca763904c3c35fce2f3556c559c0214345d31b1bcebf76acb70"))
    }

    fun mo42387m(): Array<String> {
        return arrayOf(
            "seed.bitcoin.sipa.be",
            "dnsseed.bluematt.me",
            "seed.bitcoinstats.com",
            "bitseed.xf2.org",
            "seed.bitcoinstats.com",
            "seed.bitnodes.io"
        )
    }

    override fun getMainnetFirstBlockInfo(): BlockInfo? {
        val a = this.wallet.getSelfWalletHelper().mo42410a(0, 1, this.hash!!, UInt256(), this.genesisMerkelRootHash!!, 486604799)
        val j = a.getBlockHeader()
        j.mo44291b(1231006505)
        if (j is BitcoinBlockHeader) {
            j.mo40987a(2083236893)
        }
        return a
    }

    override fun mo43958j(): Transaction? {
        return null
    }

    override fun mo42386i(): BlockInfo? {
        val a = this.wallet.getSelfWalletHelper().mo42410a(0, 1, this.hash!!, UInt256(), this.genesisMerkelRootHash!!, 545259519)
        val j = a.getBlockHeader()
        j.mo44291b(1296688602)
        if (j is BitcoinBlockHeader) {
            j.mo40987a(2)
        }
        return a
    }

    override fun mo43960l(): Transaction? {
        return null
    }

    override fun mo42385h(): BlockInfo? {
        val a = this.wallet.getSelfWalletHelper().mo42410a(0, 1, this.hash!!, UInt256(), this.genesisMerkelRootHash!!, 486604799)
        val j = a.getBlockHeader()
        j.mo44291b(1296688602)
        if (j is BitcoinBlockHeader) {
            (j).mo40987a(414098458)
        }
        return a
    }

    override fun mo43959k(): Transaction? {
        return null
    }
}
