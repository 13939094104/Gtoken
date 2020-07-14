package vdsMain.model

import android.content.ContentValues
import android.database.Cursor
import androidx.annotation.NonNull
import bitcoin.CExtKey
import bitcoin.account.hd.HDSeed
import com.vc.libcommon.exception.AddressFormatException
import generic.exceptions.AddressExistsException
import generic.exceptions.SeedWordsFormatException
import generic.io.StreamWriter
import generic.serialized.SeriableData
import vdsMain.*
import vdsMain.table.WalletTable
import java.io.IOException
import java.util.*

public class HDAccount:Account{


    //f5523a
    protected var accountId: Int = 0

    //f5524b
    protected var addr: Int = 0
    //f5525g
    private var addressVector: Vector<Address>? = null

    private var f5526h: HashMap<CTxDestination, Address>? = null

    //f5527i
    private var des2AddressSizeMap: HashMap<CTxDestination, Int>? = null
    //f5529k
    private var cExtPubKey: CExtPubKey? = null

    //f5528j
    private var encryptedHDSeed: EncryptedHDSeed? = null


    override fun mo40792b(jjVar: Address) {}

    override fun mo40799g() {}

    constructor(@NonNull walletTable:WalletTable):super(walletTable){
        Init()
    }

    constructor(@NonNull walletTable:WalletTable,charSequence:CharSequence,strArr:Array<String>,vararg iArr:Int):super(walletTable){
        Init()
        initHDSeed(strArr,charSequence,*iArr)
    }

    constructor(@NonNull walletTable:WalletTable ,charSequence:CharSequence, hDSeed:HDSeed,vararg iArr:Int):super(walletTable){
        Init()
        InitHDAccountFromHDSeed(hDSeed,charSequence,*iArr)
    }

    constructor(@NonNull walletTable:WalletTable ,charSequence:CharSequence):super(walletTable){
        this.wallet.checkWalletPassword(charSequence)
        Init()
        if (StringToolkit.getCharSequenceLength(charSequence) !== 0) {
            val b = HDSeed.getNewHDSeed()
            initHdAddresses(b, charSequence)
            getHash160Code()
            b.reset()
            return
        }
        throw NullPointerException("Password can not be empty!")
    }

    fun mo40793b_(): Int {
        return this.addr
    }

    //m5362n
    fun Init(){
        addressVector=Vector();
        this.f5526h = HashMap()
        this.des2AddressSizeMap = HashMap()
    }

    // name:m5361a
    fun initHDSeed(strArr: Array<String>, charSequence: CharSequence, vararg iArr:Int) {
        var hdSeed: HDSeed? = null
        try {
            hdSeed = HDSeed.getNewHDSeed(strArr)
        } catch (e: SeedWordsFormatException) {
            e.printStackTrace()
        }
        InitHDAccountFromHDSeed(hdSeed!!, charSequence, *iArr)
        hdSeed.reset()
    }

    //m5359a
    private fun InitHDAccountFromHDSeed(hDSeed: HDSeed, charSequence: CharSequence, vararg iArr: Int) {
        this.wallet.checkWalletPassword(charSequence)
        this.encryptedHDSeed = this.wallet.getEncryptedHDSeed(hDSeed, charSequence)
        this.addr = 0
        if (iArr.size > 0 && iArr[0] > 0) {
            this.addr = iArr[0]
        }
        if (this.addr < 0) {
            this.addr = 0
        }
        initHdAddresses(hDSeed, charSequence)
    }

    //mo40803j
    fun getEncyptedHDSeed(): EncryptedHDSeed {
        return this.encryptedHDSeed!!
    }


    //mo40782a
    fun getHDSeedFormEncypted(charSequence: CharSequence): HDSeed? {
        return if (!encryptedHDSeedIsNotEmpty()) {
            null
        } else this.wallet.decryptToHDSeed(this.encryptedHDSeed, charSequence)
    }

    //m5358a
    private fun initHdAddresses(hDSeed: HDSeed, charSequence: CharSequence) {
        this.addr = 0
        val cExtKey = hDSeed.getCExtKey(HDSeed.InteralType.EXTERNAL)
        if (this.cExtPubKey == null) {
            this.cExtPubKey = cExtKey.getcExtPubKey()
        }
        try {
            //TODO 先改成20个
            checkAndAddToBitCoinCtxDestAddressMap(getHdAddressList(hDSeed, 0, 20, charSequence), false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        hDSeed.reset()
    }

    //m5357a
    private fun getHdAddressList(hDSeed: HDSeed, start: Int, end: Int, charSequence: CharSequence): List<Address> {
        val addressTable = getWallet().getPersonalDB().getSelfAddressTable()
        val extKey = hDSeed.getCExtKey(HDSeed.InteralType.EXTERNAL)
        if (this.cExtPubKey == null) {
            this.cExtPubKey = extKey.getcExtPubKey()
        }
        this.encryptedHDSeed = this.wallet.getEncryptedHDSeed(hDSeed, charSequence)
        getHash160Code()
        val arrayList = ArrayList<BitCoinAddress>()
        var startIndex = start
        var i4 = 0
        while (i4 < end) {
            val cExtKey = CExtKey()
            extKey.mo9392a(cExtKey, startIndex.toLong() or 2147483648L)
            val bitCoinAddress = BitCoinAddress(addressTable, cExtKey.cKey.getPubKey().byteArr,cExtKey.cKey.unCompressedPubKey.byteArr ,cExtKey.cKey.getCopyBytes(), charSequence)
            cExtKey.resetBytes()
            bitCoinAddress.setAccount(this.accountId)
            bitCoinAddress.setFlag128(false)
            if (startIndex <= this.addr) {
                bitCoinAddress.setFlag8(false)
            } else {
                bitCoinAddress.setFlag8(true)
            }
            bitCoinAddress.mo44229h(false)
            arrayList.add(bitCoinAddress)
            i4++
            startIndex++
        }
        return arrayList
    }

    override fun initTableItemVariable(cursor: Cursor?, i: Int, i2: Int, columnIndex: Int) {
        when (columnIndex) {
            1 -> {
                if (cursor != null) {
                    this.addr = cursor.getInt(columnIndex)
                }
                if (this.addr < 0) {
                    this.addr = 0
                    return
                }
                return
            }
            2 -> {
                if (this.encryptedHDSeed == null) {
                    this.encryptedHDSeed = EncryptedHDSeed()
                }
                this.encryptedHDSeed!!.mo40756a(cursor?.getBlob(columnIndex))
                return
            }
            3 -> {
                if (this.cExtPubKey == null) {
                    this.cExtPubKey = CExtPubKey()
                }
                this.cExtPubKey!!.decodeSerialStream(cursor?.getBlob(columnIndex), 0)
                getHash160Code()
                return
            }
            4 -> {
                this.isMontitored = DataTypeToolkit.m11496a(cursor!!.getInt(columnIndex).toLong())
                return
            }
            else -> return
        }
    }

    //mo40791b
    fun getMnemonicWords(charSequence: CharSequence): String? {
        if (!encryptedHDSeedIsNotEmpty()) {
            return null
        }
        val hdSeed = this.wallet.decryptToHDSeed(this.encryptedHDSeed, charSequence)
        val mnemonicWords = hdSeed.getMnemonicWords()
        hdSeed.reset()
        return mnemonicWords
    }

    override fun getContentValues(): ContentValues {
        var bArr: ByteArray?
        val contentValues = ContentValues()
        try {
            bArr = this.cExtPubKey?.serialToStream()
        } catch (e: IOException) {
            e.printStackTrace()
            bArr = null
        }

        contentValues.put("id", Integer.valueOf(this.accountId))
        contentValues.put("addr", Integer.valueOf(this.addr))
        contentValues.put("seed", this.encryptedHDSeed!!.bytes)
        contentValues.put("i_pub", bArr)
        contentValues.put("monitor", Integer.valueOf(DataTypeToolkit.m11490a(this.isMontitored)))
        return contentValues
    }

    fun mo40784a(i: Int, list: MutableList<Address>) {
        if (i > 0) {
            val addressModel = this.wallet.getSelfAddressModel()
            this.addr++
            val listIterator = this.addressVector!!.listIterator(this.addr)
            var i2 = 0
            while (i2 < i) {
                val address = listIterator.next() as Address
                address.setFlag8(false)
                address.setAddressOrderIndex(this.addr)
                address.mo44229h(false)
                list.add(address)
                i2++
                this.addr++
            }
            this.addr--
            try {
                addressModel.mo43082a(list)
                updateAddress(this.addr)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    override fun createAddress(charSequence: CharSequence, i: Int, vararg charSequenceArr: CharSequence): List<Address>? {
        val i2 = this.addr + i
        val arrayList = ArrayList<Address>()
        val addressModel = this.wallet.getSelfAddressModel()
        this.wallet.checkWalletPassword(charSequence)
        if (i2 >= this.addressVector!!.size) {
            mo40784a(this.addressVector!!.size - this.addr - 1, arrayList)
            try {
                val hdSeed = this.encryptedHDSeed!!.decryptBytesToHDSeed(this.wallet.getSelfKeyCryptor(), charSequence.toString())
                val cExtKey = hdSeed.getCExtKey(HDSeed.InteralType.EXTERNAL)
                if (this.cExtPubKey == null) {
                    this.cExtPubKey = cExtKey.getcExtPubKey()
                }
                val list = getHdAddressList(hdSeed, this.addressVector!!.size, i, charSequence.toString() as CharSequence)
                hdSeed.reset()
                var i3 = this.addr + 1
                for (address in list) {
                    address.setFlag8(false)
                    address.setFlag128(false)
                    address.setAddressOrderIndex(i3)
                    arrayList.add(address)
                    i3++
                }
                m5360a(true, list, true)
                this.addr = i2
                updateAddress(this.addr)
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }

        } else {
            arrayList.addAll(this.addressVector!!.subList(this.addr, i2 + 1))
            try {
                addressModel.mo43082a(arrayList as List<Address>)
                m5360a(false, arrayList as List<Address>, true)
                if (this.addr != i2) {
                    this.addr = i2
                    updateAddress(this.addr)
                }
            } catch (e2: Exception) {
                e2.printStackTrace()
            }

        }
        return arrayList
    }

    //mo40795c_
    fun encryptedHDSeedIsNotEmpty(): Boolean {
        val encryptedHDSeed1 = this.encryptedHDSeed
        return encryptedHDSeed1 != null && !encryptedHDSeed1.mo40758b()
    }

    //mo40802i
    fun IsHDAccountInitSuccess(): Boolean {
        return if (this.accountId != 0 && !this.addressVector!!.isEmpty() && this.addressVector!!.size == this.f5526h!!.size) {
            encryptedHDSeedIsNotEmpty()
        } else false
    }



    override fun checkAndAddToBitCoinCtxDestAddressMap(list: List<Address>, vararg zArr: Boolean) {
        m5360a(false, list, *zArr)
    }
    //mo40800h
    fun getHash160Code() {
        this.accountId = CHash160.encodeToUInt160(this.cExtPubKey!!.cPubKey.getByteArr()).hashCode()
    }



    //getHashCode
    fun getId(): Int {
        return this.accountId
    }

    @Synchronized
    private fun m5360a(z: Boolean, list: List<Address>, vararg zArr: Boolean) {
        val addressModel = getWallet().getSelfAddressModel()
        var needUpdate = false
        for (address in list) {
            if (isBitCoinAddress(address)) {
                val cTxDestination = address.getCTxDestination()
                val address1:Address? = this.bitCoinTxDestinationAddressMap.getValueSynchronized(cTxDestination)
                if (address1 == null) {
                    if (this.isMontitored) {
                        address.setFlag64(true)
                    }
                    address.setFlag256(true)
                    address.setAccount(getId())
                    this.addressVector?.add(address)
                    this.f5526h?.put(address.getCTxDestination(), address)
                    this.des2AddressSizeMap?.put(address.getCTxDestination(), Integer.valueOf(this.addressVector!!.size - 1))
                    this.bitCoinTxDestinationAddressMap.addKeyValueSynchronized(address.getCTxDestination(), address)
                } else if (address1 === address) {
                    val num = this.des2AddressSizeMap!!.get(cTxDestination) as Int
                    if (this.addr < num) {
                        this.addr = num
                        needUpdate = true
                    }
                } else {
                    val sb = StringBuilder()
                    sb.append(address.getChainToAddressString())
                    sb.append(" was already exist!")
                    throw AddressExistsException(sb.toString())
                }
            } else {
                throw AddressFormatException(
                    String.format(
                        Locale.US,
                        "Address %s --> %s was not supported for account %s",
                        *arrayOf<Any>(
                            address.getAddressString(this.wallet.getBlockChainType()),
                            address.javaClass.getName(),
                            this.javaClass.getName()
                        )
                    )
                )
            }
        }
        if (z) {
            addressModel.checkAndAddAddressToDes2AddressMap(list, true, true)
        }
        if (needUpdate) {
            updateAddress(this.addr)
        }
        if (this.accountEvent != null && (zArr.size <= 0 || zArr[0])) {
            this.accountEvent!!.mo44148a(this, list)
        }
    }

    //mo40783a
    fun updateAddress(addr: Int) {
        val abstractTable = table
        abstractTable.dbWritableDataHelper.execSQL(
            String.format(
                Locale.US,
                "UPDATE %s SET %s=%d WHERE %s=%d",
                *arrayOf<Any>(abstractTable.tableName, "addr", Integer.valueOf(addr), "id", Integer.valueOf(getId()))
            )
        )
    }

    //mo40798f
    fun getAddressVector(): List<Address> {
        return (if (this.addressVector!!.isEmpty()) {
            this.addressVector
        } else this.addressVector!!.subList(0, this.addr + 1))!!
    }

    //mo40796d
    fun getAddrAddress(): Address? {
        val list = getAddressVector()
        if (list != null && !list.isEmpty()) {
            val size = list.size
            val i = this.addr
            if (size >= i) {
                return list.get(i)
            }
        }
        return null
    }

    //mo40797e
    fun getAddrIndex(): Int {
        return this.addr
    }

    override fun hashCode(): Int {
        return this.accountId
    }

    //mo40679a
    override fun isBitCoinAddress(address: Address): Boolean {
        return address is BitCoinAddress
    }

    override fun getHashCode(): Int {
        return this.accountId
    }

    override fun checkAndAddAddressBalanceToAccount(address: Address, needCal: Boolean, subAccountInfo: SubAccountInfo?) {
        if (subAccountInfo != null) {
            val subAddressInfo = address.getSubAddressInfo(subAccountInfo.blockChainType)
            if (subAddressInfo != null) {
                address.calSubAddressBalanceInfo(subAccountInfo.blockChainType)
                if (!address.isFlagIndentity()) {
                    subAccountInfo.balance += subAddressInfo.sumBalance
                }
            }
        }
    }

    fun mo40786a(streamWriter: StreamWriter) {
        streamWriter.writeUInt32T(hashCode().toLong())
        streamWriter.writeUInt32T(this.addr.toLong())
        this.encryptedHDSeed!!.serialToStream(streamWriter)
        this.cExtPubKey!!.serialToStream(streamWriter)
        streamWriter.writeBoolean(this.isMontitored)
    }

    fun mo40787a(seriableData: SeriableData) {
        this.accountId = seriableData.readUInt32().toInt()
        this.addr = seriableData.readUInt32().toInt()
        this.encryptedHDSeed = EncryptedHDSeed()
        this.encryptedHDSeed!!.decodeSerialStream(seriableData)
        this.cExtPubKey = CExtPubKey()
        this.cExtPubKey!!.decodeSerialStream(seriableData)
        this.isMontitored = seriableData.readBoolean()
    }

    //mo40781a
    override fun getSumBalance(includeLock: Boolean, vararg blockChainTypes: BLOCK_CHAIN_TYPE): Long {
        val blockChainType: BLOCK_CHAIN_TYPE
        val addressList = getAddressVector()
        var balance: Long = 0
        if (addressList == null || addressList.isEmpty()) {
            return 0
        }
        if (blockChainTypes == null) {
            blockChainType = BLOCK_CHAIN_TYPE.VCASH
        } else {
            blockChainType = blockChainTypes[0]
        }
        for (address in addressList) {
            if (!address.isFlagIndentity()) {
                if (includeLock) {
                    balance += address.getSumBalance(blockChainType)
                } else {
                    balance += address.getAvailableBalance(blockChainType)
                }
            }
        }
        return balance
    }

    //mo40780a
    fun getAddressSize(des: CTxDestination): Int {
        return (this.des2AddressSizeMap!!.get(des) as Int).toInt()
    }

}