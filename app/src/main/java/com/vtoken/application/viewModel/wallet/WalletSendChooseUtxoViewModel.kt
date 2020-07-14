package com.vtoken.application.viewModel.wallet

import android.content.Context
import android.content.Intent
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.vtoken.application.ApplicationLoader
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.adapter.RecyclerSimpleAdapter
import com.vtoken.application.model.ChangeAddress
import vdsMain.model.Address
import com.vtoken.application.viewModel.BaseViewModel
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vdsMain.model.Account
import vdsMain.tool.Util
import vdsMain.transaction.CAmount
import vdsMain.transaction.Utxo
import java.math.BigDecimal
import java.util.*

class WalletSendChooseUtxoViewModel(context: Context):BaseViewModel(context) {
    //f11094E
    val address: Address

    //f11090A
    val account: Account

    //f11091B
    val changeChooseListener: ChangeAddress.ChangeChooseListener

    //f11093D
    var changeAddressList = ArrayList<ChangeAddress>()

    //f11092C
    var changeAddressMutableList: MutableList<ChangeAddress> = ArrayList()

    //f11095x
    var selectedUtxoTip = ObservableField<String>("0")

    var f11096y = ObservableBoolean(true)

    //f11097z
    var recyclerSimpleAdapter: RecyclerSimpleAdapter<ChangeAddress>

    init {
        this.address = this.vCashCore.getAddressFromAddressString(intent.getStringExtra("address"))!!
        this.account = this.vCashCore.getAccount(this.address.getAccount())!!
        this.recyclerSimpleAdapter = RecyclerSimpleAdapter(R.layout.item_wallet_send_choose_utxo, BR.changeAddress)
        this.recyclerSimpleAdapter.mo38515a(true)
        this.recyclerSimpleAdapter.setOnItemClickListener(object : RecyclerSimpleAdapter.RecyclerSimpleAdapterClickEvent {
            /* renamed from: b */
            override fun onLongClick(position: Int) {}

            /* renamed from: a */
            override fun onClick(position: Int) {
                val changeAddress = this@WalletSendChooseUtxoViewModel.recyclerSimpleAdapter.getDataList().get(position) as ChangeAddress
                changeAddress.setCheck(!changeAddress.isCheck())
                this@WalletSendChooseUtxoViewModel.recyclerSimpleAdapter.notifyItemChanged(position)
            }
        })
        this.changeChooseListener = object : ChangeAddress.ChangeChooseListener {
            override fun onChangeChooseStatusChanged(changeAddress: ChangeAddress, z: Boolean) {
                var bigDecimal = BigDecimal.ZERO
                this@WalletSendChooseUtxoViewModel.changeAddressList.clear()
                for (changeAddress2 in this@WalletSendChooseUtxoViewModel.changeAddressMutableList) {
                    if (changeAddress2.isCheck()) {
                        this@WalletSendChooseUtxoViewModel.changeAddressList.add(changeAddress2)
                        bigDecimal = bigDecimal.add(BigDecimal(changeAddress2.getBalance()))
                    }
                }
                this@WalletSendChooseUtxoViewModel.selectedUtxoTip.set(
                    String.format(
                        Locale.ENGLISH,
                        "%s",
                        *arrayOf<Any>(bigDecimal.toString())
                    )
                )
            }
        }
        mo39924a()
        ApplicationLoader.getSingleApplicationContext().mo38412c(null)
    }

    fun selectAll(){
        for (address in recyclerSimpleAdapter.dataList){
            address.isCheck=true
        }
        recyclerSimpleAdapter.notifyDataSetChanged()
    }

    fun mo39924a() {
        this.f11096y.set(true)
        this.changeAddressMutableList.clear()
        Observable.create(object : ObservableOnSubscribe<Boolean> {
            override fun subscribe(observableEmitter: ObservableEmitter<Boolean>) {
                val list: List<Utxo>?
                if (!this@WalletSendChooseUtxoViewModel.address.isAccount() || this@WalletSendChooseUtxoViewModel.address.isFlagIndentity()) {
                    list = this@WalletSendChooseUtxoViewModel.vCashCore.getUtxoListByAddress(this@WalletSendChooseUtxoViewModel.address, this@WalletSendChooseUtxoViewModel.block_chain_type)
                } else {
                    list = this@WalletSendChooseUtxoViewModel.vCashCore.getAccountUtxoList(this@WalletSendChooseUtxoViewModel.account, this@WalletSendChooseUtxoViewModel.block_chain_type, false)
                }
                if (list != null) {
                    Util.m7033a(list)
                    for (utxo in list) {
                        if (utxo.getValue() != 0L) {
                            val changeAddress = ChangeAddress()
                            changeAddress.type = this@WalletSendChooseUtxoViewModel.block_chain_type.name
                            changeAddress.listener = this@WalletSendChooseUtxoViewModel.changeChooseListener
                            changeAddress.balance = CAmount.toDecimalSatoshiString(java.lang.Long.valueOf(utxo.getValue()))
                            changeAddress.txOut = utxo
                            changeAddress.type = this@WalletSendChooseUtxoViewModel.block_chain_type.name
                            changeAddress.txid = utxo.getTxOutTxid().getHex()
                            this@WalletSendChooseUtxoViewModel.changeAddressMutableList.add(changeAddress)
                        }
                    }
                }
                observableEmitter.onNext(java.lang.Boolean.valueOf(true))
            }
        }).compose(bindDestoryEvent()).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Boolean> {
                override fun onComplete() {}

                override fun onSubscribe(disposable: Disposable) {}

                /* renamed from: a */
                override fun onNext(bool: Boolean) {
                    this@WalletSendChooseUtxoViewModel.f11096y.set(false)
                    this@WalletSendChooseUtxoViewModel.recyclerSimpleAdapter.setDataList(this@WalletSendChooseUtxoViewModel.changeAddressMutableList)
                    this@WalletSendChooseUtxoViewModel.recyclerSimpleAdapter.notifyDataSetChanged()
                }

                override fun onError(th: Throwable) {
                    th.printStackTrace()
                }
            })
    }

    //mo39929b
    fun successAndReturn() {
        val intent = Intent()
        intent.putParcelableArrayListExtra("utxo", this.changeAddressList)
        val arrayList = ArrayList<Utxo>()
        val it = this.changeAddressList.iterator()
        while (it.hasNext()) {
            arrayList.add(it.next().txOut)
        }
        ApplicationLoader.getSingleApplicationContext().mo38412c(arrayList as List<Utxo>)
        getActivity().setResult(-1, intent)
        finish()
    }

    //mo39911N
    fun cancelAndReturn() {
        getActivity().setResult(0)
        finish()
    }
}