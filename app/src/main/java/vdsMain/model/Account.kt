package vdsMain.model

import com.vc.libcommon.exception.AddressFormatException
import generic.exceptions.AddressExistsException
import generic.utils.AddressUtils
import vdsMain.ArrayListMap
import vdsMain.BLOCK_CHAIN_TYPE
import vdsMain.CTxDestination
import vdsMain.SubAccountInfo
import vdsMain.table.AbstractTable
import vdsMain.table.WalletTable
import vdsMain.table.WalletTableItem
import java.util.*

abstract class Account(walletTable: WalletTable) : WalletTableItem(walletTable) {

    init {
        initSubAccountInfo(this.wallet.getBlockChainType())
    }

    @JvmField
    protected var accountEvent: AccountEvent? = null

    //f13040d
    protected var isMontitored: Boolean = false

    //mo40804k
    open fun getHashCode(): Int {
        return hashCode()
    }

    //f13041e
    protected var bitCoinTxDestinationAddressMap: ArrayListMap<CTxDestination, Address> = ArrayListMap()

    //f13042f
    protected var chainTypeSubAccInfoMap: HashMap<BLOCK_CHAIN_TYPE, SubAccountInfo>? = null

    override fun getKey(): String {
        return "id"
    }

    //mo44142b
    fun initSubAccountInfo(blockChainType: BLOCK_CHAIN_TYPE): Boolean {
        val hashMap = this.chainTypeSubAccInfoMap
        if (hashMap == null) {
            this.chainTypeSubAccInfoMap = HashMap<BLOCK_CHAIN_TYPE, SubAccountInfo>()
            this.chainTypeSubAccInfoMap!!.put(blockChainType, SubAccountInfo(blockChainType, 0))
            return true
        } else if (hashMap!!.containsKey(blockChainType)) {
            return false
        } else {
            this.chainTypeSubAccInfoMap!!.put(blockChainType, SubAccountInfo(blockChainType, 0))
            return true
        }
    }

    //mo40677a
    @Throws(Exception::class)
    abstract fun createAddress(charSequence: CharSequence, i: Int, vararg charSequenceArr: CharSequence): List<Address>?

    //mo40679a
    abstract fun isBitCoinAddress(jjVar: Address): Boolean

    abstract fun mo40792b(jjVar: Address)

    abstract fun mo40799g()

    abstract override fun hashCode(): Int

    //mo40798f
    fun getALLAddressList(): List<Address> {
        return this.bitCoinTxDestinationAddressMap.getValueList()
    }

    fun mo44139a(address: Address, vararg zArr: Boolean) {
        val arrayList = ArrayList<Address>()
        arrayList.add(address)
        checkAndAddToBitCoinCtxDestAddressMap(arrayList as List<Address>, *zArr)
    }

    //mo44146m
//    fun isMontitored(): Boolean {
//        return this.isMontitored
//    }


    //mo42432e_
    override fun getValue(): String {
        return Integer.toString(hashCode())
    }

    //mo44145l
    fun getTxDesAddressList(): List<Address> {
        return this.bitCoinTxDestinationAddressMap.getValueList()
    }



    //mo40789a
    @Synchronized
    open fun checkAndAddToBitCoinCtxDestAddressMap(list: List<Address>, vararg zArr: Boolean) {
        val addressModel = this.wallet.getSelfAddressModel()
        for (address in list) {
            if (isBitCoinAddress(address)) {
                val cTxDestination = address.getCTxDestination()
                val address1:Address? = this.bitCoinTxDestinationAddressMap.getValueSynchronized(cTxDestination)
                if (address1 == null) {
                    if (this.isMontitored) {
                        address.setFlag64(true)
                    }
                    address.setAccount(getHashCode())
                    this.bitCoinTxDestinationAddressMap.addKeyValueSynchronized(address.getCTxDestination(), address)
                } else if (address1 !== address) {
                    val sb = StringBuilder()
                    sb.append(address.getChainToAddressString())
                    sb.append(" was already exist, dest ")
                    sb.append(cTxDestination.getHash())
                    throw AddressExistsException(sb.toString())
                }
            } else {
                throw AddressFormatException(
                    String.format(
                        Locale.getDefault(),
                        "Address %s --> %s was not supported for account %s",
                        *arrayOf<Any>(
                            address.getAddressString(this.wallet.getBlockChainType()),
                            address.javaClass.getName(),
                            javaClass.getName()
                        )
                    )
                )
            }
        }
        addressModel.checkAndAddAddressToDes2AddressMap(list, true)
        if (this.accountEvent != null && (zArr.size <= 0 || zArr[0])) {
            this.accountEvent!!.mo44148a(this, list)
        }
    }

    @Synchronized
    private fun mo40796d() {
        if (this.chainTypeSubAccInfoMap != null && !this.chainTypeSubAccInfoMap!!.isEmpty()) {
            for (subAccountInfo in this.chainTypeSubAccInfoMap!!.values) {
                subAccountInfo.balance = 0
            }
        }
        if (this.accountEvent != null) {
            this.accountEvent!!.mo44149b(this, this.bitCoinTxDestinationAddressMap.getValueList())
        }
        mo40799g()
    }

    @Synchronized
    override fun deleteData(abstractTable: AbstractTable) {
        super.deleteData(abstractTable)
        mo40796d()
    }

    //mo44134a
    fun getBalance(vararg blockChainTypes: BLOCK_CHAIN_TYPE): Long {
        val subAccountInfo: SubAccountInfo?
        if (blockChainTypes.size == 0 || blockChainTypes[0] == null) {
            subAccountInfo = getSubAccountInfoByBlockChainType(this.wallet.getBlockChainType())
        } else {
            subAccountInfo = getSubAccountInfoByBlockChainType(blockChainTypes[0])
        }
        return subAccountInfo?.balance ?: 0
    }

    //mo40781a
    open fun getSumBalance(z: Boolean, vararg igVarArr: BLOCK_CHAIN_TYPE): Long {
        val blockChainType: BLOCK_CHAIN_TYPE
        val addressList = getAddressList()
        var j: Long = 0
        if (addressList == null || addressList!!.isEmpty()) {
            return 0
        }
        if (igVarArr == null) {
            blockChainType = BLOCK_CHAIN_TYPE.VCASH
        } else {
            blockChainType = igVarArr[0]
        }
        for (address in addressList!!) {
            if (z) {
                j += address.getAvailableBalance(blockChainType)
            } else {
                j += address.getSumBalance(blockChainType)
            }
        }
        return j
    }

    //mo44143c
    fun getSubAccountInfoByBlockChainType(chainType: BLOCK_CHAIN_TYPE): SubAccountInfo? {
        val hashMap = this.chainTypeSubAccInfoMap
        return if (hashMap == null || hashMap.isEmpty()) {
            null
        } else this.chainTypeSubAccInfoMap!!.get(chainType) as SubAccountInfo
    }

    //mo40790a
    open fun checkAndAddAddressBalanceToAccount(address: Address, needCal: Boolean, subAccountInfo: SubAccountInfo?) {
        if (subAccountInfo != null) {
            val subAddressInfo = address.getSubAddressInfo(subAccountInfo.blockChainType)
            if (subAddressInfo != null) {
                if (needCal) {
                    address.calSubAddressBalanceInfo(subAccountInfo.blockChainType)
                }
                subAccountInfo.balance += subAddressInfo.sumBalance
            }
        }
    }

    @Synchronized
    //mo44141a
    fun calSubAccountBalanceInfo(needCal: Boolean, blockChainType: BLOCK_CHAIN_TYPE) {
        val subAccountInfo = getSubAccountInfoByBlockChainType(blockChainType)
        if (subAccountInfo != null) {
            subAccountInfo.balance = 0
            for (address in getTxDesAddressList()) {
                checkAndAddAddressBalanceToAccount(address, needCal, subAccountInfo)
            }
        }
    }

    //mo44140a
    fun calSubAccountBalanceInfoByWalletChain(needCal: Boolean) {
        calSubAccountBalanceInfo(needCal, this.wallet.getBlockChainType())
    }


    @Synchronized
    fun mo40788a(charSequence: CharSequence, address: Address?) {
        this.wallet.checkWalletPassword(charSequence)
        if (address != null) {
            val address1 = this.bitCoinTxDestinationAddressMap.removeAndGet(address.getCTxDestination()) as Address
            if (address1 != null) {
                this.bitCoinTxDestinationAddressMap.removeAndGet(address1.getCTxDestination())
                if (this.accountEvent != null) {
                    val arrayList = ArrayList<Address>()
                    arrayList.add(address1)
                    this.accountEvent!!.mo44149b(this, arrayList)
                }
                calSubAccountBalanceInfoByWalletChain(false)
                val I = address1.getSubAddressInfoCollecetion()
                if (I != null) {
                    for (subAddressInfo in I!!) {
                        checkAndAddAddressBalanceToAccount(address1, false, getSubAccountInfoByBlockChainType(subAddressInfo.getBlockChainType()))
                    }
                }
                mo40792b(address1)
            }
        }
    }

    fun mo44135a(str: String): Address? {
        try {
            val a = AddressUtils.m937a(str, getWallet()) ?: return null
            return this.bitCoinTxDestinationAddressMap.getValueSynchronized(a)
        } catch (e: AddressFormatException) {
            e.printStackTrace()
            return null
        }

    }

    fun mo44136a(blockChainType: BLOCK_CHAIN_TYPE) {
        calSubAccountBalanceInfo(true, blockChainType)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("[ ")
        sb.append(super.toString())
        val stringBuffer = StringBuffer(sb.toString())
        val sb2 = StringBuilder()
        sb2.append("\nhash: ")
        sb2.append(hashCode())
        stringBuffer.append(sb2.toString())
        val sb3 = StringBuilder()
        sb3.append("\nmBalance: ")
        sb3.append(getBalance(this.wallet.getBlockChainType()))
        stringBuffer.append(sb3.toString())
        val sb4 = StringBuilder()
        sb4.append("\nmIsMontitored: ")
        sb4.append(this.isMontitored)
        stringBuffer.append(sb4.toString())
        addAddressesToString(stringBuffer)
        stringBuffer.append("\n]")
        return stringBuffer.toString()
    }

    //mo44137a
    fun addAddressesToString(stringBuffer: StringBuffer) {
        stringBuffer.append("\naddresses:[\n")
        var i = 0
        for (address in this.bitCoinTxDestinationAddressMap.getValueList()) {
            val sb = StringBuilder()
            sb.append("[ address ")
            sb.append(i)
            sb.append(" : ")
            sb.append(address)
            sb.append(" ]")
            stringBuffer.append(sb.toString())
            i++
        }
    }

    fun mo44144d(igVar: BLOCK_CHAIN_TYPE): Long {
        val arrayList = ArrayList(getAddressList())
        var j: Long = 0
        if (arrayList.isEmpty()) {
            return 0
        }
        for (address in arrayList) {
            j += address.mo44222f(igVar).getValue()
        }
        return j
    }

    //C3883a
    interface AccountEvent {
        /* renamed from: a */
        fun mo44148a(account: Account, list: List<Address>)

        /* renamed from: b */
        fun mo44149b(account: Account, list: List<Address>)
    }

    //mo44138a
    fun setSelfAccountEvent(aVar: AccountEvent) {
        this.accountEvent = aVar
    }

    //getAddressVector
    fun getAddressList(): List<Address> {
        return this.bitCoinTxDestinationAddressMap.getValueList()
    }
}
