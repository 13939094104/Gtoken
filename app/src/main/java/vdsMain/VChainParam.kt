package vdsMain

import bitcoin.UInt256
import bitcoin.consensus.ArithUint256
import com.google.common.base.Ascii
import com.vc.libcommon.util.UInt64
import net.bither.bitherj.utils.Utils
import org.spongycastle.apache.bzip2.BZip2Constants
import org.spongycastle.asn1.cmc.BodyPartID
import vdsMain.block.BlockInfo
import vdsMain.block.VBlockHeader
import vdsMain.block.VBlockInfo
import vdsMain.wallet.ChainParams
import vdsMain.wallet.Wallet
import java.io.IOException

//biq
//910 VChainParam
class VChainParam(wallet:Wallet) : ChainParams(wallet){

    val f11664ae =
        ArithUint256.m463a(UInt256.uint256S("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"))

    var f11665U = UInt64(100000)

    var f11666V: UInt64

    var f11667W: UInt64

    var f11669Y: Array<String?>

    @JvmField
    var f11670Z: CFeeRate

    @JvmField
    var f11671aa: Boolean = false

    @JvmField
    var f11672ab: CFeeRate

    @JvmField
    /* renamed from: ac */
    var f11673ac: CFeeRate

    var f11674ad: Short = 0

    @JvmField
    var f11675af: ByteArray? = null

    init {
        val uInt64 = this.f11665U
        this.f11666V = uInt64
        this.f11667W = uInt64
        this.f11669Y = arrayOfNulls(4)
        this.f11670Z = CFeeRate(0)
        this.f11671aa = true
        this.f11672ab = CFeeRate(1000)
        this.f11673ac = CFeeRate(1000)
    }

    override fun getBaseParams(): Params {
        return VParams()
    }

    //f11668X
    @JvmField
    var testZcashAddressList: List<Array<String>>? = null

    fun mo42378a(i: Int): String? {
        return this.f11669Y[i]
    }

    fun getTestZcashAddressList():List<Array<String>>?{
        return testAddressList
    }

    fun setTestZcashAddressList(list:List<Array<String>>?){
        testAddressList=list
    }

    //mo42381d
    override fun initMainParams() {
        this.version = 2
        this.networkType = NETWORK_TYPE.MAIN
        this.magicBytes = byteArrayOf(36, -27, 39, 100)
        this.protocalVersion = 170010
        this.peerPort = 6533
        this.f11674ad = 133
        this.f11675af =
            StringToolkit.getBytes("04b7ecf0baa90495ceb4e4090f6b2fd37eec1e9c85fac68a487f3ce11589692e4a317479316ee814e066638e1db54e37a10689b70286e6315b1087b6615d179264")
        this.hash = UInt256(Utils.getReverseStringBytes("0804fd488d9f5787d025d8b1e9e199301b5b42bcbe779a4e875983103c6036a8"), true)
        this.genesisMerkelRootHash = UInt256(Utils.getReverseStringBytes("898ea66248eba5b44db100123c4f09c4e9fe670142268674684752a92461d133"), true)
        this.base58Prefixes_public_address = byteArrayOf(16, Ascii.FS)
        this.base58Prefixes_script_address = byteArrayOf(16, 65)
        this.f12924s = byteArrayOf(java.lang.Byte.MIN_VALUE)
        this.f12925t = "bc"
        val strArr = this.f11669Y
        strArr[0] = "vs"
        strArr[1] = "vviews"
        strArr[2] = "vivks"
        strArr[3] = "secret-extended-key-main"
        val vParams = this.params as VParams
        vParams.f11745o = false
        vParams.f11746p = BZip2Constants.baseBlockSize
        vParams.f11747q = 158000
        vParams.f11748r = 17280
        vParams.f11751u = 15
        vParams.f11749s = 0
        vParams.f11750t = 40
        vParams.f11756z = 60
        vParams.f11732A = 10
        vParams.f11733B = 211680
        this.vidLockBlockNumber = 10080
        vParams.f8383d = 750
        vParams.f8384e = 950
        vParams.f8385f = 4000
        vParams.f8386g = UInt256.uint256S("07ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff")
        vParams.f8388i = 17
        if (f11664ae.mo9509e(ArithUint256.m463a((this.params as VParams).f8386g)).mo9505c((this.params as VParams).f8388i)) {
            vParams.nPowMaxAdjustDown = 32
            vParams.nPowMaxAdjustUp = 16
            vParams.nPowTargetSpacing = 60
            vParams.f8387h = false
            vParams.f11734C = 0
            vParams.nMinerConfirmationWindow = 2016
            vParams.nBlockCountPerDay = 1440
            vParams.nWeekCount1stSeason = 3
            vParams.nWeekCountOfSeason = 1
            vParams.nBlockCountOfWeek = vParams.nBlockCountPerDay * 7
            vParams.nBlockCountOf1stSeason = vParams.nBlockCountOfWeek * vParams.nWeekCount1stSeason
            vParams.nBlockCountOfSeason = vParams.nBlockCountOfWeek * vParams.nWeekCountOfSeason
            vParams.f11752v = vParams.nBlockCountOfWeek
            vParams.f11753w = 5.0
            vParams.f11754x = 3.0
            vParams.f11755y = 1.5
            vParams.f11742K = 12
            vParams.f11743L = 12
            this.f12929x = 96
            this.f12930y = 5
            this.blockChainType = BLOCK_CHAIN_TYPE.VCASH
            return
        }
        throw IllegalArgumentException("Invalidate maxUint, powLimit and nPowAveragingWindow")
    }

    override fun initRegtestParams() {
        this.version = 2
        this.networkType = NETWORK_TYPE.REGTEST
        this.magicBytes = byteArrayOf(-86, -24, 63, 95)
        this.protocalVersion = 170008
        this.peerPort = 16533
        this.f11674ad = 1
        this.f11675af =
            StringToolkit.getBytes("044e7a1553392325c871c5ace5d6ad73501c66f4c185d6b0453cf45dec5a1322e705c672ac1a27ef7cdaf588c10effdf50ed5f95f85f2f54a5f6159fca394ed0c6")
        this.hash = UInt256(Utils.getReverseStringBytes("61a8f1d40cac7b7b611e4bedf8d821f98c4b1d4dbef895237e1209e50c75f5e2"), true)
        this.genesisMerkelRootHash = UInt256(Utils.getReverseStringBytes("898ea66248eba5b44db100123c4f09c4e9fe670142268674684752a92461d133"), true)
        this.base58Prefixes_public_address = byteArrayOf(Ascii.RS, 43)
        this.base58Prefixes_script_address = byteArrayOf(Ascii.RS, 85)
        this.f12924s = byteArrayOf(-17)
        val strArr = this.f11669Y
        strArr[0] = "vregtestsapling"
        strArr[1] = "vviewregtestsapling"
        strArr[2] = "vivkregtestsapling"
        strArr[3] = "secret-extended-key-regtest"
        this.f12925t = "tb"
        val bjh = this.params as VParams
        bjh.f11745o = false
        bjh.f11746p = 240
        bjh.f11747q = 350
        bjh.f11748r = 10
        bjh.f11751u = 1
        bjh.f11749s = 0
        bjh.f11750t = 40
        bjh.f11756z = 10
        bjh.f11732A = 5
        bjh.f11733B = 210
        bjh.f8383d = 750
        bjh.f8384e = 950
        bjh.f8385f = 1000
        bjh.f8386g = UInt256.uint256S("7fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff")
        bjh.f8388i = 1
        if (f11664ae.mo9509e(ArithUint256.m463a((this.params as VParams).f8386g)).mo9505c((this.params as VParams).f8388i)) {
            bjh.nPowMaxAdjustDown = 0
            bjh.nPowMaxAdjustUp = 0
            bjh.nPowTargetSpacing = 1
            bjh.f8387h = true
            bjh.f11734C = 0
            bjh.nMinerConfirmationWindow = 144
            bjh.f11744n = false
            bjh.nBlockCountPerDay = 100
            bjh.nWeekCount1stSeason = 3
            bjh.nWeekCountOfSeason = 1
            bjh.nBlockCountOfWeek = bjh.nBlockCountPerDay * 7
            bjh.nBlockCountOf1stSeason = bjh.nBlockCountOfWeek * bjh.nWeekCount1stSeason
            bjh.nBlockCountOfSeason = bjh.nBlockCountOfWeek * bjh.nWeekCountOfSeason
            bjh.f11752v = bjh.nBlockCountOfWeek
            bjh.f11753w = 5.0
            bjh.f11754x = 3.0
            bjh.f11755y = 1.5
            bjh.f11742K = 12
            bjh.f11743L = 12
            this.f12929x = 96
            this.f12930y = 5
            this.blockChainType = BLOCK_CHAIN_TYPE.VCASH
            return
        }
        throw IllegalArgumentException("Invalidate maxUint, powLimit and nPowAveragingWindow")
    }

    override fun initTestParams() {
        this.version = 2
        this.networkType = NETWORK_TYPE.TEST
        this.magicBytes = byteArrayOf(-6, Ascii.SUB, -7, -65)
        this.protocalVersion = 170008
        this.peerPort = 26533
        this.f11674ad = 1
        this.vidLockBlockNumber = 10080
        this.f11675af = StringToolkit.getBytes("044e7a1553392325c871c5ace5d6ad73501c66f4c185d6b0453cf45dec5a1322e705c672ac1a27ef7cdaf588c10effdf50ed5f95f85f2f54a5f6159fca394ed0c6")
        this.hash = UInt256(Utils.getReverseStringBytes("bd94031d0ba5bbb72b50eecd5f5444056e5f0f788538e24261878178cdab6a62"), true)
        this.genesisMerkelRootHash = UInt256(Utils.getReverseStringBytes("898ea66248eba5b44db100123c4f09c4e9fe670142268674684752a92461d133"), true)
        this.base58Prefixes_public_address = byteArrayOf(Ascii.RS, 43)
        this.base58Prefixes_script_address = byteArrayOf(Ascii.RS, 85)
        this.f12924s = byteArrayOf(-17)
        this.f12925t = "tb"
        val strArr = this.f11669Y
        strArr[0] = "vtestsapling"
        strArr[1] = "vviewtestsapling"
        strArr[2] = "vivktestsapling"
        strArr[3] = "secret-extended-key-test"
        val vParams = this.params as VParams
        vParams.f11746p = 1000
        vParams.f11747q = 46000
        vParams.f11748r = 576
        vParams.f8383d = 51
        vParams.f8384e = 75
        vParams.f8385f = 400
        vParams.f11733B = 2100
        vParams.f8386g = UInt256.uint256S("07ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff")
        vParams.f8388i = 17
        vParams.nPowMaxAdjustDown = 32
        vParams.nPowMaxAdjustUp = 16
        vParams.f11744n = false
        vParams.f11756z = 60
        vParams.f11732A = 10
        if (f11664ae.mo9509e(ArithUint256.m463a((this.params as VParams).f8386g)).mo9505c((this.params as VParams).f8388i)) {
            vParams.nPowTargetSpacing = 3
            vParams.f8387h = true
            vParams.f11734C = 0
            vParams.nMinerConfirmationWindow = 2016
            vParams.nBlockCountPerDay = 1440
            vParams.nWeekCount1stSeason = 3
            vParams.nWeekCountOfSeason = 1
            vParams.nBlockCountOfWeek = vParams.nBlockCountPerDay * 7
            vParams.nBlockCountOf1stSeason = vParams.nBlockCountOfWeek * vParams.nWeekCount1stSeason
            vParams.nBlockCountOfSeason = vParams.nBlockCountOfWeek * vParams.nWeekCountOfSeason
            vParams.f11752v = vParams.nBlockCountOfWeek
            vParams.f11753w = 5.0
            vParams.f11754x = 3.0
            vParams.f11755y = 1.5
            vParams.f11742K = 12
            vParams.f11743L = 12
            this.f12929x = 96
            this.f12930y = 5
            this.blockChainType = BLOCK_CHAIN_TYPE.VCASH
            return
        }
        throw IllegalArgumentException("Invalidate maxUint, powLimit and nPowAveragingWindow")
    }

    fun mo42388u(): Long {
        return this.f11674ad.toLong() and BodyPartID.bodyIdMax
    }

    //mo42384g
    override fun getMainnetFirstBlockInfo(): BlockInfo? {
        try {
            val vBlockInfo = VBlockInfo(VBlockHeader(this.wallet))
            vBlockInfo.mo44655a(
                "04000000000000000000000000000000000000000000000000000000000000000000000033d16124a9524768748626420167fee9c4094f3c1200b14db4a5eb4862a68e89fbc2f4300c01f0b7820d00e3347c8da4ee614674376cbc45359daa54f9b5493ef56596133a0e1900acdf375cffff0720e965ffd002cd6ad0e2dc402b8044de833e06b23127ea8c3d80aec9141077149556e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421c1000000000000000000000000000000000000000000000000000000000000004408bc9767284a389bf0db4ff042d3c18c7d398b9dede5781b75f4a5deec7d51ad92301ae2c96f9e7f3671f3d4cf1b519f88eeab1d31d1c98c82f09fab020e0cdf4ffbb305010100000000010000000000000000000000000000000000000000000000000000000000000000ffffffff4f04ffff071f010447426974636f696e30303030303030303030303030303030303030303164303731313439393763326662643565323737616531396438356236616462623165303064336439326232ffffffff010b9a6f9653350600001976a914b8512034ee84547b9929b961e5582f2b3a7e6ab188ac00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
            )
            return vBlockInfo
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

    override fun mo42386i(): BlockInfo? {
        try {
            val bjf = VBlockInfo(VBlockHeader(this.wallet))
            bjf.mo44655a(
                "04000000000000000000000000000000000000000000000000000000000000000000000033d16124a9524768748626420167fee9c4094f3c1200b14db4a5eb4862a68e89fbc2f4300c01f0b7820d00e3347c8da4ee614674376cbc45359daa54f9b5493ef56596133a0e1900acdf375cffff7f20e965ffd002cd6ad0e2dc402b8044de833e06b23127ea8c3d80aec9141077149556e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421aa000000000000000000000000000000000000000000000000000000000000004405301f3bc8725d28e321b3959ae31572a21c06e9535dd8b0b665950d8949b10d3ee60ed2e2fca3ec7630e20fa5e1d6feabf89d1c185dc6157cb9d0029c0f05f50ac3f439010100000000010000000000000000000000000000000000000000000000000000000000000000ffffffff4f04ffff071f010447426974636f696e30303030303030303030303030303030303030303164303731313439393763326662643565323737616531396438356236616462623165303064336439326232ffffffff010b9a6f9653350600001976a914b8512034ee84547b9929b961e5582f2b3a7e6ab188ac00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
            )
            return bjf
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

    }

    override fun mo42385h(): BlockInfo? {
        try {
            val bjf = VBlockInfo(VBlockHeader(this.wallet))
            bjf.mo44655a(
                "04000000000000000000000000000000000000000000000000000000000000000000000033d16124a9524768748626420167fee9c4094f3c1200b14db4a5eb4862a68e89fbc2f4300c01f0b7820d00e3347c8da4ee614674376cbc45359daa54f9b5493ef56596133a0e1900acdf375cffff0720e965ffd002cd6ad0e2dc402b8044de833e06b23127ea8c3d80aec9141077149556e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b4210100000000000000000000000000000000000000000000000000000000000000440c12ac1006b7febbb2d90b909f1565c99e16a1f5544ecab24512662c0604aba4e33819d928b1a38b59f986a835ad764231c31724c6961b19993c3c65ea740e95c87f36b9010100000000010000000000000000000000000000000000000000000000000000000000000000ffffffff4f04ffff071f010447426974636f696e30303030303030303030303030303030303030303164303731313439393763326662643565323737616531396438356236616462623165303064336439326232ffffffff010b9a6f9653350600001976a914b8512034ee84547b9929b961e5582f2b3a7e6ab188ac00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
            )
            return bjf
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

    }

}
