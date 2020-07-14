package com.vtoken.application.viewModel.wallet

import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import android.text.TextUtils
import android.util.Log
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.adapter.RecyclerSimpleAdapter
import com.vtoken.application.model.TransactionRecord
import com.vtoken.application.viewHolder.ViewHolder
import com.vtoken.application.viewModel.BaseViewModel
import vdsMain.BLOCK_CHAIN_TYPE
import vdsMain.tool.Util
import vdsMain.transaction.*
import java.util.ArrayList
import vdsMain.model.Address

class WalletTransactionDetailViewModel(context: Context):BaseViewModel(context) {

    //f11317A
    var transaction: Transaction?

    //f11326x
    var txIdString = ObservableField<String>()

    var f11319C = ObservableBoolean(false)

    //f11324H
    var errorReason = ObservableField<String>()

    var address:Address?=null

    var remarkShow = ObservableField(false)

    //f11327y
    var remarkString = ObservableField<String>("")

    var f11318B = ObservableBoolean(false)

    var f11321E = ObservableBoolean(false)

    var f11322F = ObservableBoolean(false)

    var f11323G = ObservableBoolean(false)

    var f11325I: Boolean = false

    //f11328z
    var recyclerSimpleAdapter: RecyclerSimpleAdapter<TransactionRecord>

    init {
        this.txIdString.set(intent.getStringExtra("trans_ID"))
        this.address=vCashCore.getAddressFromAddressString(intent.getStringExtra("address"))
        this.recyclerSimpleAdapter = RecyclerSimpleAdapter(R.layout.item_transaction_detail, BR.transactionRecord)
        this.recyclerSimpleAdapter.setRecyclerBindEvent(object : RecyclerSimpleAdapter.RecyclerSimpleBindEvent {
            override fun onBind(viewHolder: ViewHolder, i: Int) {
                viewHolder.getViewDataBinding().setVariable(BR.walletTransactionDetailModel, this@WalletTransactionDetailViewModel)
            }
        })
        this.recyclerSimpleAdapter.setOnItemClickListener(object:
            RecyclerSimpleAdapter.RecyclerSimpleAdapterClickEvent {
            override fun onClick(position: Int) {
                Util.copyAndShowToast(recyclerSimpleAdapter.dataList[position].address)
            }

            override fun onLongClick(position: Int) {

            }

        })
        this.recyclerSimpleAdapter.mo38515a(true)
        this.transaction = this.vCashCore.mo43809b(this.txIdString.get()!!, this.block_chain_type)
        if(this.transaction!=null)
        {
            mo42138a(this.txIdString.get()!!)
            if (this.transaction!!.isCoinBaseTransaction()) {
                this.f11319C.set(true)
            } else {
                this.f11319C.set(false)
            }
            val sb = StringBuilder()
            sb.append("TransactionRecordViewModel: ")
            sb.append(this.txIdString.get())
            Log.e("", sb.toString())
            if (this.transaction!!.isDefaultHash()) {
                val rejectTx = this.vCashCore.mo43807b(this.transaction!!.getTxId(), this.block_chain_type)
                if (rejectTx != null) {
                    this.errorReason.set(rejectTx.getErrorReson())
                }
            } else if (this.transaction!!.isNotConfirmed()) {
                this.errorReason.set(getString(R.string.tx_confrim_time_out as Int))
            }
            val remark=this.vCashCore.getRemarkByTxid(this.transaction!!.getTxId(), this.block_chain_type)
            this.remarkString.set(remark)
            if(!TextUtils.isEmpty(remark)){
                remarkShow.set(true)
            }

        }
    }

    fun mo42138a(str: String) {
        if (TextUtils.equals(str, this.txIdString.get() as CharSequence?)) {
            if (this.transaction!!.isDefaultHash() || this.transaction!!.isNotConfirmed()) {
                this.f11321E.set(true)
                this.f11322F.set(m9168a(this.transaction!!))
                if (this.vCashCore.mo43800a(this.txIdString.get()!!, this.block_chain_type)) {
                    this.f11323G.set(true)
                } else {
                    this.f11323G.set(false)
                }
            } else {
                this.f11321E.set(false)
            }
        }
    }

    fun showTxidQrCodeDialog(){
        showQrCodeDialog(txIdString.get(),"VDS","VDS事务ID",true,txIdString.get(),object:QrOptionDialogEvent{
            override fun onCopyClick() {
            }

            override fun onSaveClick() {
            }
            override fun mo41443c() {

            }

        })
    }

    fun getTransferAmount():String{
        return getTransactionBalanceStringByAddress(this.transaction,this.address)
    }

    fun getTxStatusString():String{
        if (transaction!!.isNotConfirmed() || transaction!!.isDefaultHash()) {
            return "转账失败"
        } else if (transaction!!.isConfirmed()) {
            return "转账已确认"
        } else {
            return "转账待确认"
        }
    }

    private fun m9168a(transaction: Transaction): Boolean {
        if (transaction.getFlag() !== 0 || transaction.isCoinBaseTransaction()) {
            return false
        }
        return if (transaction !is VTransaction || !(transaction as VTransaction).isSpendDesciptionListAndOuputDescriptionListNotEmpty()) {
            true
        } else false
    }

    //mo39911N
    fun initFromDataList() {
        if (this.transaction != null) {
            this.f11325I = true
            val arrayList = ArrayList<TransactionRecord>()
            val txInList = this.transaction!!.getSelfTxInList()
            this.f11318B.set(false)
            for (txIn in txInList) {
                val value = txIn.getSatoshi()
                val addressString = txIn.getAddress()
                if (this.block_chain_type !== BLOCK_CHAIN_TYPE.BITCOIN || value != 0L && !TextUtils.isEmpty(addressString)) {
                    val transactionRecord = TransactionRecord(addressString,
                        Util.m7023a(CAmount.toDecimalSatoshiDouble(java.lang.Long.valueOf(value)).toString(), 8),
                        this.block_chain_type.name)
                    transactionRecord.isConfirmed = txIn.mo43287f()
                    if (TextUtils.isEmpty(addressString)) {
                        transactionRecord.des = m9165a(this.transaction!!, txIn)
                    }
                    transactionRecord.isWrong = false
                    arrayList.add(transactionRecord)
                } else {
                    this.f11318B.set(true)
                }
            }
            this.recyclerSimpleAdapter.setDataList(arrayList)
            this.recyclerSimpleAdapter.notifyDataSetChanged()
        }
    }

    //mo42136O
    fun initToDataList() {
        val transaction = this.transaction
        if (transaction != null) {
            val isWrong = transaction.isNotConfirmed() || this.transaction!!.isDefaultHash()
            this.f11325I = false
            this.f11318B.set(false)
            val arrayList = ArrayList<TransactionRecord>()
            var j: Long = 0
            for (txOut in this.transaction!!.getSelfTxOutList()) {
                val value = txOut.getSatoshi()
                if (this.block_chain_type !== BLOCK_CHAIN_TYPE.BITCOIN || value != 0L) {
                    if (txOut.getFlag().toInt() == 1) {
                        j += value
                        if (value == 0L) {
                        }
                    }
                    val addressString = txOut.getAddress()
                    val transactionRecord = TransactionRecord(
                        addressString,
                        Util.m7023a(CAmount.toDecimalSatoshiDouble(java.lang.Long.valueOf(value)).toString(), 8),
                        this.block_chain_type.name
                    )
                    transactionRecord.isConfirmed = txOut.mo43287f() && this.transaction!!.isConfirmed()
                    if (TextUtils.isEmpty(addressString)) {
                        transactionRecord.des = m9166a(this.transaction!!, txOut)
                    }
                    transactionRecord.isWrong = isWrong
                    arrayList.add(transactionRecord)
                }
            }
            val fee = this.transaction!!.getFee()
            if (this.transaction!!.getFlag() == 1) {
                for (txIn in this.transaction!!.getSelfTxInList()) {
                    txIn.getSatoshi()
                }
                val transactionRecord2 = TransactionRecord(
                    "",
                    Util.m7017a(
                        CAmount.toDecimalSatoshiDouble(
                            java.lang.Long.valueOf(
                                CAmount.toSatoshiLong(10) - CAmount.toSatoshiLong(
                                    java.lang.Double.valueOf(
                                        0.5
                                    )
                                ) - j
                            )
                        ), 8
                    ),
                    this.block_chain_type.name
                )
                transactionRecord2.isConfirmed = this.transaction!!.isConfirmed()
                transactionRecord2.des = getStringRescourcesByResName("chat_set_run_off")
                transactionRecord2.isWrong = isWrong
                arrayList.add(transactionRecord2)
                val transactionRecord3 = TransactionRecord("", "0.5", this.block_chain_type.name)
                transactionRecord3.isConfirmed = this.transaction!!.isConfirmed()
                transactionRecord3.des = getStringRescourcesByResName("master_fee")
                transactionRecord3.isWrong = isWrong
                arrayList.add(transactionRecord3)
            } else if (fee != 0L) {
                val feeRecord = TransactionRecord(
                    "",
                    Util.m7023a(CAmount.toDecimalSatoshiDouble(java.lang.Long.valueOf(fee)).toString(), 8),
                    this.block_chain_type.name
                )
                feeRecord.isConfirmed = this.transaction!!.isConfirmed()
                feeRecord.des = getStringRescourcesByResName("master_fee")
                feeRecord.isWrong = isWrong
                arrayList.add(feeRecord)
            }
            this.recyclerSimpleAdapter.setDataList(arrayList)
            this.recyclerSimpleAdapter.notifyDataSetChanged()
        }
    }

    private fun m9165a(transaction: Transaction, dlVar: TxIn): String? {
        if (transaction.isCoinBaseTransaction()) {
            return getStringRescourcesByResName("coinbase")
        }
        var str: String? = null
        when (transaction.getFlag()) {
            0 -> str = getStringRescourcesByResName("decode_address_failed")
            1 -> str = getStringRescourcesByResName("decode_address_failed")
            6 -> str = getStringRescourcesByResName("contract_tx")
            7 -> str = getStringRescourcesByResName("vib_tx")
        }
        return str
    }

    private fun m9166a(transaction: Transaction, dnVar: TxOut): String? {
        when (transaction.getFlag()) {
            0 -> return getStringRescourcesByResName("decode_address_failed")
            1 -> {
                return if (dnVar.getSatoshi() == 0L) {
                    getStringRescourcesByResName("chat_set_run_off")
                } else getStringRescourcesByResName("decode_address_failed")
            }
            6 -> return getStringRescourcesByResName("contract_tx")
            7 -> return getStringRescourcesByResName("vib_tx")
            8 -> return getStringRescourcesByResName("ad_tx")
            else -> return null
        }
    }
    fun copytxidString(){
        Util.copyAndShowToast(txIdString.get())
    }
}