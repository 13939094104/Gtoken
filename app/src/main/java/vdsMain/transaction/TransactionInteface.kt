package vdsMain.transaction

import bitcoin.UInt256
import vdsMain.wallet.Wallet

interface TransactionInteface {
    //mo43274R
    fun getWallet(): Wallet

    /* renamed from: a */
    fun setFlag(i: Int)

    /* renamed from: a */
    fun setVersion(j: Long)

    /* renamed from: b */
    fun checkAndSetLockTime(j: Long)

    /* renamed from: d */
    fun getSelfTxOutList(): List<TxOut>

    /* renamed from: e */
    fun getSelfTxInList(): List<TxIn>

    /* renamed from: f */
    fun getVersion(): Long

    /* renamed from: g */
    fun getLockTime(): Long

    /* renamed from: h */
    fun getFlag(): Int

    /* renamed from: h_ */
    fun getTxId(): UInt256

    /* renamed from: i_ */
    fun updateTxidByContent(): UInt256
}