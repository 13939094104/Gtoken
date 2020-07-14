package com.vtoken.application.viewModel.wallet

import android.content.Context
import android.content.Intent
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.core.view.InputDeviceCompat
import android.text.*
import android.util.Log
import android.view.View
import com.google.zxing.integration.android.IntentIntegrator
import com.orhanobut.logger.Logger
import com.vc.libcommon.exception.AddressFormatException
import com.vc.libcommon.exception.TxSizeException
import com.vtoken.application.ApplicationLoader
import com.vtoken.application.model.ChangeAddress
import com.vtoken.application.model.MyObservableDecimal
import com.vtoken.application.util.PreferenceUtil
import com.vtoken.application.view.activity.ValidatePwdActivity
import com.vtoken.application.view.activity.wallet.WalletSendChooseUtxoActivity
import com.vtoken.application.viewModel.BaseViewModel
import com.vtoken.application.viewModel.PromptDialogViewModel
import generic.exceptions.SignatureFailedException
import generic.exceptions.UtxoAlreadySpendException
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vdsMain.AddressType
import vdsMain.BLOCK_CHAIN_TYPE
import vdsMain.Utils
import vdsMain.model.Account
import vdsMain.model.Address
import vdsMain.tool.Util
import vdsMain.transaction.*
import  java.lang.Double
import java.math.BigDecimal
import java.util.ArrayList

open class WalletSendTransactionViewModel(context: Context):BaseViewModel(context) {

    //f11244B
    var blockChainTypeName = ObservableField<String>()

    //f11250H转出地址
    var addressString = ObservableField<String>()

    //f11265W
    val oldTxid: String?

    //f11258P
    var account: Account?

    //f11264V
    val address: Address

    //f11270ab
    var receiverAddress: String?

    //f11255M
    val jumpType: Int

    var f11249G = ObservableInt(8)

    lateinit var f11251I: TextWatcher

    lateinit var f11252J: TextWatcher

    internal var f11262T: String? = null

    var requireFee=BigDecimal("0.0001")

    //f11247E
    var inculdeFee = ObservableBoolean(false)

    var f11269aa: Boolean = false

    //f11245C
    var availableBalanceString = ObservableField("0")

    //f11246D
    var reMarkString = ObservableField<String>()

    //f11276y发送数量
    var sendValueObservableDecimal = MyObservableDecimal(null, false)

    //f11243A手续费
    var feeValueObservableDecimal = MyObservableDecimal(null, false)

    //f11254L
    var changeAddressList: MutableList<ChangeAddress>? = ArrayList()

    //f11277z
    var sumBalance = ObservableField<String>()

    //f11275x接受地址
    var receiverAddressString = ObservableField<String>()

    init {
        this.blockChainTypeName.set(this.block_chain_type.name)
        //自来也null异常
        try {
            this.addressString.set(intent.getStringExtra("address"))
        }catch (e:Exception){
            this.addressString.set(PreferenceUtil.getString("username",""))
        }
        this.oldTxid =intent.getStringExtra("old_txid")
        this.address = this.vCashCore.getAddressFromAddressString(this.addressString.get() as String, this.block_chain_type)!!
        this.jumpType = intent.getIntExtra("jumpType", 3)
        this.receiverAddress =intent.getStringExtra("receiver_address")
        this.receiverAddressString.set(this.receiverAddress)
        this.account = this.vCashCore.getAccount(this.address.account)
        feeValueObservableDecimal.set("0.0001")
        Log.d("自来也参数","oldTxid:"+this.oldTxid+"addressString:"+this.addressString.toString()+"address:"+this.address.toString()+
        "jumpType:"+jumpType+"receiverAddress:"+receiverAddress)
        initSumBalance()
    }

    //m9084R
    fun getAvailableBalanceString(): String {
        return if (!this.address.isAccount() || this.address.isFlagIndentity()) {
            CAmount.toDecimalSatoshiString(java.lang.Long.valueOf(this.address.getAvailableBalance(this.block_chain_type)))
        } else
            CAmount.toDecimalSatoshiString(
                java.lang.Long.valueOf(
                    this.account!!.getSumBalance(
                        false,
                        this.block_chain_type
                    )
                )
            )
    }

    fun switchIncludeFee(){
        inculdeFee.set(!inculdeFee.get());
    }

    //m9083Q
    fun initSumBalance() {
        this.availableBalanceString.set(getAvailableBalanceString())
        this.sumBalance.set(this.availableBalanceString.get())
        this.f11251I = object : TextWatcher {
            override fun afterTextChanged(editable: Editable) {}

            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}
        }
        this.f11252J = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}

            override fun afterTextChanged(editable: Editable) {
                    this@WalletSendTransactionViewModel.f11249G.set(8)
            }
        }
    }

    override fun initiateScan() {
        super.initiateScan()
        this.scanRequestCode = 911
    }


    fun sendAll() {
        this.sendValueObservableDecimal.set(BigDecimal(this.sumBalance.get() as String).stripTrailingZeros().toPlainString())
        this.inculdeFee.set(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1002) run {
            if (resultCode == -1) {
                val stringExtra = data.getStringExtra("pwd")
                if (this.jumpType == 2) {
                    //TODO 这里走多签
//                    mo42116s(stringExtra)
                } else {
                    checkAndSendTrsansaction(stringExtra)
                }
            }
        }
        else if (requestCode != 1105) {
            if (this.scanRequestCode == 911) {
                val parseActivityResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
                if (parseActivityResult == null) {
                    super.onActivityResult(requestCode, resultCode, data)
                } else if (parseActivityResult.contents != null) {
                    setReceiverAddressString(parseActivityResult.contents.replace("offline_", ""))
                }
                this.scanRequestCode = 0
            }
            else if (requestCode == 49374) {
                val parseActivityResult2 = IntentIntegrator.parseActivityResult(requestCode,resultCode, data)
                this.f11262T = null
                if (parseActivityResult2 != null && parseActivityResult2.contents != null) {
                    this.f11262T = parseActivityResult2.contents
                    startActivityForResult(Intent(this.context, ValidatePwdActivity::class.java), 901)
                }
            }
            else if (requestCode == 1025)
            {
                if (resultCode == -1) {
                    this.changeAddressList = data.getParcelableArrayListExtra<ChangeAddress>("utxo")
                    val utxoList = ApplicationLoader.getSingleApplicationContext().mo38399C()
                    if (!(utxoList == null || utxoList.isEmpty() || this.changeAddressList == null)) {
                        for (index in this.changeAddressList!!.indices) {
                            this.changeAddressList!!.get(index).txOut = utxoList!!.get(index) as Utxo
                        }
                    }
                } else if (resultCode == 0) {
                    val list = this.changeAddressList
                    if (list != null) {
                        list.clear()
                    }
                }
                val tempChangeAddressList = this.changeAddressList
                if (tempChangeAddressList == null || tempChangeAddressList.isEmpty()) {
                    this.availableBalanceString.set(getAvailableBalance())
                    this.sumBalance.set(this.availableBalanceString.get())
                    return
                }
                var bigDecimal = BigDecimal.ZERO
                for (changeAddress in this.changeAddressList!!) {
                    bigDecimal = bigDecimal.add(BigDecimal(changeAddress.getBalance()))
                }
                this.availableBalanceString.set(bigDecimal.toString())
                this.sumBalance.set(bigDecimal.toString())
                val parseDouble = java.lang.Double.parseDouble(this.sendValueObservableDecimal.formatSelfDecimal(true))
                if (Math.max(parseDouble, java.lang.Double.parseDouble(this.sumBalance.get() as String)) == parseDouble) {
                    this.sendValueObservableDecimal.set(BigDecimal(this.sumBalance.get() as String).stripTrailingZeros().toPlainString())
                }
            }
        }else if(resultCode==-1){

        }
    }

    //mo42113a
    fun setReceiverAddressString(str: String) {
        this.receiverAddressString.set(str)
    }

    //m9084R
    fun getAvailableBalance(): String {
        return if (!this.address.isAccount() || this.address.isFlagIndentity()) {
            CAmount.toDecimalSatoshiDouble(java.lang.Long.valueOf(this.address.getAvailableBalance(this.block_chain_type))).toString()
        } else java.lang.Double.toString(
            CAmount.toDecimalSatoshiDouble(
                java.lang.Long.valueOf(
                    this.account!!.getSumBalance(
                        false,
                        this.block_chain_type
                    )
                )
            )
        )
    }

    //mo39929b
    fun jumpToChooseUtxoActvity() {
        val intent = Intent(this.context, WalletSendChooseUtxoActivity::class.java)
        intent.putExtra("address", this.address.getAddressString(this.block_chain_type))
        startActivityForResult(intent, InputDeviceCompat.SOURCE_GAMEPAD as Int)
    }

    //m9093a
    fun getSpendUtxoList(list: List<Utxo>?): List<Utxo>? {
        var temList: List<Utxo>? = list ?: return ArrayList()
        val sendValueString = this.sendValueObservableDecimal.formatSelfDecimal(true)
        if (!TextUtils.isEmpty(sendValueString)) {
            temList = this.vCashCore.getSpendUtxoList(
                this.inculdeFee.get(),
                temList!!,
                CAmount.toSatoshiLong(java.lang.Double.valueOf(java.lang.Double.parseDouble(sendValueString))),
                CAmount.toSatoshiLong(java.lang.Double.valueOf(java.lang.Double.parseDouble(this.feeValueObservableDecimal.formatSelfDecimal(true))))
            )
        }
        return temList
    }

    //m9086T
    fun getUtxoList(): List<Utxo> {
        val list = this.changeAddressList
        if (list != null && !list.isEmpty()) {
            val utxoList = ArrayList<Utxo>()
            for (changeAddress in this.changeAddressList!!) {
                if (changeAddress.isCheck()) {
                    utxoList.add(changeAddress.getTxOut())
                }
            }
            return utxoList
        } else return if (this.address.isAccount() && !this.address.isFlagIndentity()) {
            this.vCashCore.getAccountUtxoList(this.account!!, this.block_chain_type, false)!!
        } else {
            this.vCashCore.getUtxoListByAddress(this.address, this.block_chain_type)!!
        }
    }

    //m9088V
     fun getAddressMoneyInfoList(): List<AddressMoneyInfo> {
        val arrayList = ArrayList<AddressMoneyInfo>()
        if (!TextUtils.isEmpty(this.sendValueObservableDecimal.formatSelfDecimal(true))) {
            if (this.inculdeFee.get()) {
                arrayList.add(AddressMoneyInfo(this.receiverAddressString.get(),
                    CAmount.toSatoshiLong(Double.valueOf(BigDecimal(this.sendValueObservableDecimal.formatSelfDecimal(true)).subtract(BigDecimal(this.feeValueObservableDecimal.formatSelfDecimal(true))).toDouble())))
                )
            } else {
                arrayList.add(AddressMoneyInfo(this.receiverAddressString.get(),
                    CAmount.toSatoshiLong(Double.valueOf(Double.parseDouble(this.sendValueObservableDecimal.formatSelfDecimal(true)))))
                )
            }
        }
        return arrayList
    }

    //m9112c
    fun checkFee(sendValue: String, feeValue: String): Boolean {
        if (TextUtils.isEmpty(feeValue)) {
            return false
        }
        if (this.jumpType == 1) {
            var multiple = BigDecimal("0.005")
            if (BigDecimal(this.sendValueObservableDecimal.formatSelfDecimal(true)).compareTo(BigDecimal.ONE) >= 0) {
                multiple = multiple.multiply(BigDecimal(sendValue))
            }
            return if (multiple.compareTo(BigDecimal(feeValue)) != 1) {
                true
            } else false
        }
        val spendUtxoList = getSpendUtxoList(getUtxoList())
        requireFee= BigDecimal(java.lang.Double.toString(
            CAmount.toDecimalSatoshiDouble(
                java.lang.Long.valueOf(
                    this.vCashCore.calMinFee(
                        false,
                        Utils.getCoutPointUtxoFromUtxoList(spendUtxoList),
                        getAddressMoneyInfoList(),
                        CAmount.toSatoshiLong(java.lang.Double.valueOf(feeValue)),
                        null,
                        null,
                        this.block_chain_type
                    )
                )
            )
        ))
        return if (requireFee.compareTo(BigDecimal(feeValue)) != 1) {
            true
        } else false
    }

    //mo39911N转账
    fun checkAndSendTransaction() {
        if (!Util.m7035a(this.context)) {
            showToast(getStringRescourcesByResName("toast_hot_wallet_offline_status_can_not_send"))
        } else if (!this.vCashCore.mo43798a(this.block_chain_type)) {
            showToast(getStringRescourcesByResName("toast_hot_wallet_offline_status_can_not_send"))
        } else if (isStringEmpty(this.receiverAddressString.get())) {
            showToast(getStringRescourcesByResName("toast_send_no_receive_address"))
        } else {
            val addressType = this.vCashCore.getAddressTypeByAddressString(this.receiverAddressString.get()!!)
            if (addressType === AddressType.UNKNOWN) {
                showToast(getStringRescourcesByResName("toast_send_address_error"))
                this.receiverAddressString.set("")
            } else if (addressType === AddressType.ANONYMOUS && this.block_chain_type === BLOCK_CHAIN_TYPE.BITCOIN) {
                showToast(getStringRescourcesByResName("toast_send_address_error"))
            } else if (isStringEmpty(this.sendValueObservableDecimal.get())) {
                showToast(getStringRescourcesByResName("toast_send_no_send_amount"))
            } else {
                val bigDecimal = BigDecimal(this.sumBalance.get())
                if (bigDecimal.compareTo(BigDecimal.ZERO) != 1) {
                    showToast(getStringRescourcesByResName("toast_send_total_not_enough"))
                } else if (bigDecimal.compareTo(BigDecimal(this.sumBalance.get() as String)) == 1) {
                    showToast(getStringRescourcesByResName("toast_send_amount_error"))
                    this.sendValueObservableDecimal.set("")
                } else {
                    val changeAddressList = this.changeAddressList
                    if (changeAddressList == null || changeAddressList.size == 0) {
                        val spendUtxoList = getSpendUtxoList(getUtxoList())
                        if (this.address.getSumBalance(this.block_chain_type) > 0 && (spendUtxoList == null || spendUtxoList!!.size == 0)) {
                            showToast(getStringRescourcesByResName("toast_send_a_trans_wait_for_confirm"))
                            return
                        }
                    }
                    if (isStringEmpty(this.feeValueObservableDecimal.get())) {
                        showToast(getStringRescourcesByResName("toast_send_no_fee"))
                        return
                    }
                    if (!this.inculdeFee.get()) {
                        if (BigDecimal(this.sendValueObservableDecimal.formatSelfDecimal(true)).add(BigDecimal(this.feeValueObservableDecimal.formatSelfDecimal(true))).compareTo(
                                BigDecimal(this.sumBalance.get() as String)
                            ) == 1
                        ) {
                            showToast(getStringRescourcesByResName("toast_Send_fee_should_be_include_amount"))
                            return
                        }
                    } else if (BigDecimal(this.sendValueObservableDecimal.formatSelfDecimal(true)).compareTo(BigDecimal(this.feeValueObservableDecimal.formatSelfDecimal(true))) == -1) {
                        showToast(getStringRescourcesByResName("toast_send_amount_error"))
                        return
                    }
                    showLoadingFragment(getStringRescourcesByResName("check_fee") as CharSequence)
                    io.reactivex.Observable.create(object : ObservableOnSubscribe<Boolean> {
                        override fun subscribe(observableEmitter: ObservableEmitter<Boolean>) {
                            val walletSendTransactionViewModel = this@WalletSendTransactionViewModel
                            observableEmitter.onNext(
                                java.lang.Boolean.valueOf(
                                    walletSendTransactionViewModel.checkFee(
                                        walletSendTransactionViewModel.sendValueObservableDecimal.formatSelfDecimal(true),
                                        this@WalletSendTransactionViewModel.feeValueObservableDecimal.formatSelfDecimal(true)
                                    )
                                )
                            )
                        }
                    }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<Boolean> {
                            override fun onComplete() {}

                            override fun onSubscribe(disposable: Disposable) {}

                            /* renamed from: a */
                            override fun onNext(bool: Boolean) {
                                this@WalletSendTransactionViewModel.dismissLoadingDialog()
                                if (bool) {
                                    if (!this@WalletSendTransactionViewModel.inculdeFee.get()) {
                                        if (BigDecimal(this@WalletSendTransactionViewModel.sendValueObservableDecimal.formatSelfDecimal(true)).add(
                                                BigDecimal(
                                                    this@WalletSendTransactionViewModel.feeValueObservableDecimal.formatSelfDecimal(
                                                        true
                                                    )
                                                )
                                            ).compareTo(BigDecimal(this@WalletSendTransactionViewModel.sumBalance.get() as String)) == 1
                                        ) {
                                            val walletSendTransactionViewModel = this@WalletSendTransactionViewModel
                                            walletSendTransactionViewModel.showToast(walletSendTransactionViewModel.getStringRescourcesByResName("toast_Send_fee_should_be_include_amount"))
                                            return
                                        }
                                    } else if (BigDecimal(this@WalletSendTransactionViewModel.sendValueObservableDecimal.formatSelfDecimal(true)).compareTo(
                                            BigDecimal(
                                                this@WalletSendTransactionViewModel.feeValueObservableDecimal.formatSelfDecimal(
                                                    true
                                                )
                                            )
                                        ) == -1
                                    ) {
                                        val bgt2 = this@WalletSendTransactionViewModel
                                        bgt2.showToast(bgt2.getStringRescourcesByResName("toast_send_amount_error"))
                                        return
                                    }
                                    val intent = Intent(this@WalletSendTransactionViewModel.context, ValidatePwdActivity::class.java)
                                    if (this@WalletSendTransactionViewModel.jumpType == 1) {
                                        intent.putExtra("is_anonymous", true)
                                    }
                                    if (!this@WalletSendTransactionViewModel.vCashCore.mo43798a(this@WalletSendTransactionViewModel.block_chain_type)) {
                                        val bgt3 = this@WalletSendTransactionViewModel
                                        bgt3.showToast(bgt3.getStringRescourcesByResName("toast_hot_wallet_offline_status_can_not_send"))
                                        return
                                    }
                                    this@WalletSendTransactionViewModel.startActivityForResult(intent, 1002)
                                } else {
                                    showNorConfirmDialog(getStringRescourcesByResName("prompt"),getStringRescourcesByResName("fee_invaild_info"),
                                        getStringRescourcesByResName("confirm"),getStringRescourcesByResName("cancel"),object :View.OnClickListener{
                                            override fun onClick(v: View?) {
                                                this@WalletSendTransactionViewModel.feeValueObservableDecimal.set(requireFee.toPlainString())
                                                dismissConfirmDialog()
                                            }
                                        },object :View.OnClickListener{
                                            override fun onClick(v: View?) {
                                                dismissConfirmDialog()
                                            }
                                        })
//                                    val bgt4 = this@WalletSendTransactionViewModel
//                                    bgt4.showToast(bgt4.getStringRescourcesByResName("toast_send_fee_error"))
                                }
                            }

                            override fun onError(th: Throwable) {
                                th.printStackTrace()
                                this@WalletSendTransactionViewModel.dismissLoadingDialog()
                                if (th is AddressFormatException) {
                                    val bgt = this@WalletSendTransactionViewModel
                                    bgt.showToast(bgt.getStringRescourcesByResName("toast_send_address_error"))
                                    return
                                }
                                val bgt2 = this@WalletSendTransactionViewModel
                                bgt2.showToast(bgt2.getStringRescourcesByResName("toast_send_fee_error"))
                            }
                        })
                }
            }
        }
    }

    //mo42115r
     fun checkAndSendTrsansaction(str: String) {
        val utxoList = getUtxoList()
        if (this.address.getSumBalance(this.block_chain_type) > 0 && utxoList == null || utxoList!!.size == 0) {
            showToast(getStringRescourcesByResName("toast_send_a_trans_wait_for_confirm"))
        } else if (utxoList == null || utxoList!!.size == 0) {
            showToast(getStringRescourcesByResName("toast_send_utxo_is_empty"))
        } else {
            val addressMoneyInfoList = getAddressMoneyInfoList()
            showLoadingFragment(getStringRescourcesByResName("loading_create_tx_msg") as CharSequence)
            if (this.jumpType == 5) {
                io.reactivex.Observable.create(object : ObservableOnSubscribe<OfflineTransaction> {
                    override fun subscribe(observableEmitter: ObservableEmitter<OfflineTransaction>) {
                        val hcVar = this@WalletSendTransactionViewModel.vCashCore
                        val offlineTransaction = TransactionUtils.m13294a(
                            hcVar,
                            utxoList,
                            this@WalletSendTransactionViewModel.getSpendUtxoList(utxoList),
                            addressMoneyInfoList,
                            CAmount.toSatoshiLong(
                                java.lang.Double.valueOf(
                                    java.lang.Double.parseDouble(
                                        this@WalletSendTransactionViewModel.feeValueObservableDecimal.formatSelfDecimal(true)
                                    )
                                )
                            ),
                            this@WalletSendTransactionViewModel.inculdeFee.get(),
                            str,
                            this@WalletSendTransactionViewModel.block_chain_type
                        )
                        if (offlineTransaction.f13338a != null) {
                            this@WalletSendTransactionViewModel.vCashCore.replaceToTxMarkTable(
                                offlineTransaction.f13338a.getTxId(),
                                this@WalletSendTransactionViewModel.reMarkString.get() as String,
                                this@WalletSendTransactionViewModel.block_chain_type
                            )
                        }
                        observableEmitter.onNext(offlineTransaction)
                    }
                }).compose(bindDestoryEvent()).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<OfflineTransaction> {
                        override fun onComplete() {}

                        override fun onSubscribe(disposable: Disposable) {}

                        /* renamed from: a */
                        override fun onNext(offlineTransaction: OfflineTransaction) {
                            this@WalletSendTransactionViewModel.dismissLoadingDialog()
                            val b = offlineTransaction.mo44667b()
                            val WalletSendTransactionViewModel = this@WalletSendTransactionViewModel
//                            WalletSendTransactionViewModel.mo41345a(
//                                WalletSendTransactionViewModel.getStringRescourcesByResName("trans_code"),
//                                this@WalletSendTransactionViewModel.getStringRescourcesByResName("scan_by_offline_wallet"),
//                                b,
//                                200,
//                                1105
//                            )
                            LocalBroadcastManager.getInstance(this@WalletSendTransactionViewModel.context.getApplicationContext())
                                .sendBroadcast(Intent("refresh_address"))
                        }

                        override fun onError(th: Throwable) {
                            this@WalletSendTransactionViewModel.dismissLoadingDialog()
                            th.printStackTrace()
                            this@WalletSendTransactionViewModel.excuteThrowable(th)
                        }
                    })
            } else if (!this.vCashCore.mo43798a(this.block_chain_type)) {
                //没有节点
                //m9087U()
            } else {
                m9098a(str, utxoList, addressMoneyInfoList)
            }
        }
    }
        //自来也
       private fun m9098a(str:String, selectUtxoList:List<Utxo>, addressMoneyInfoList:List<AddressMoneyInfo> ) {
        io.reactivex.Observable.create(object : ObservableOnSubscribe<TxSignatureResult> {
            override fun subscribe(observableEmitter:ObservableEmitter<TxSignatureResult> ) {
                val z = false
                val vCashCore1 = this@WalletSendTransactionViewModel.vCashCore;
                val list = selectUtxoList;
                val contractInfo=ContractCallInfo(250000,"transfer", arrayListOf("address","uint256"),
                    arrayOf("d3f9f42c50c06c628d3d0719aa3aa8880d073d1e",10000).toList(), arrayListOf("bool"),"595d74e52b4add65ed01e3381914dfe26708dc8b")
                //test contract
//                observableEmitter.onNext(TransactionUtils.getCallContractTxSignatureResult(z, vCashCore1, list,
//                    this@WalletSendTransactionViewModel.getSpendUtxoList(list), contractInfo,
//                    CAmount.toSatoshiLong(Double.valueOf(Double.parseDouble(this@WalletSendTransactionViewModel.feeValueObservableDecimal.formatSelfDecimal(true)))),
//                    this@WalletSendTransactionViewModel.inculdeFee.get(), str, this@WalletSendTransactionViewModel.block_chain_type));


                observableEmitter.onNext(TransactionUtils.getTxSignatureResult(z, vCashCore1, list,
                    this@WalletSendTransactionViewModel.getSpendUtxoList(list), addressMoneyInfoList,
                    CAmount.toSatoshiLong(Double.valueOf(Double.parseDouble(this@WalletSendTransactionViewModel.feeValueObservableDecimal.formatSelfDecimal(true)))),
                    this@WalletSendTransactionViewModel.inculdeFee.get(), str, this@WalletSendTransactionViewModel.block_chain_type));
            }
        }).compose(bindDestoryEvent()).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe( object: Observer<TxSignatureResult> {
            override fun onSubscribe(disposable:Disposable) {

            }

            /* renamed from: a */
            override fun onNext(txSignatureResult:TxSignatureResult ) {
                this@WalletSendTransactionViewModel.dismissLoadingDialog();
                if (!txSignatureResult.isSuccess) {
                    val walletSendTransactionViewModel = this@WalletSendTransactionViewModel;
                    walletSendTransactionViewModel.showToast(walletSendTransactionViewModel.getStringRescourcesByResName("sign_fail"));
                    return;
                }
                Logger.d(txSignatureResult.transaction.getTxid().hashString())
                Logger.d(txSignatureResult.transaction.getTransactionHexString())
                Logger.d(txSignatureResult.transaction.getTxIn(0)?.scriptSig?.hexString())
                Logger.d(txSignatureResult.transaction.getTxOut(0)?.getScript()?.hexString())
                Logger.d(txSignatureResult.transaction.getTxOut(1)?.getScript()?.hexString())
                this@WalletSendTransactionViewModel.showSendTxConfirmDialog(txSignatureResult.txInfo, this@WalletSendTransactionViewModel.block_chain_type,  object:
                    conFirmDialogEvent {
                    /* renamed from: b */
                    override fun onCancel() {

                    }

                    /* renamed from: a */
                    override fun onConfirm() {
                        Log.d("自来也参数1"," "+txSignatureResult.transaction+":::"+str)
                        this@WalletSendTransactionViewModel.checkAndSendTransactionToPeers(txSignatureResult.transaction, str);
                    }
                });
            }

            override fun onError(th:Throwable ) {
                this@WalletSendTransactionViewModel.dismissLoadingDialog();
                th.printStackTrace();
                this@WalletSendTransactionViewModel.excuteThrowable(th);
            }

            override fun onComplete() {
                Log.e("SendViewModel", "onComplete: ");
            }
        });
    }

    //mo42112a
    fun checkAndSendTransactionToPeers(transaction: Transaction, str: String) {
        this.confirmDialog = null
        showLoadingFragment("" as CharSequence)
        io.reactivex.Observable.create(object : ObservableOnSubscribe<TxSendResult> {
            override fun subscribe(observableEmitter: ObservableEmitter<TxSendResult>) {
                if (this@WalletSendTransactionViewModel.oldTxid != null) {
                    this@WalletSendTransactionViewModel.vCashCore.mo43836c(this@WalletSendTransactionViewModel.oldTxid, this@WalletSendTransactionViewModel.block_chain_type)
                }
                var reMark=this@WalletSendTransactionViewModel.reMarkString.get()
                if(reMark==null){
                    reMark=""
                }
                this@WalletSendTransactionViewModel.vCashCore.replaceToTxMarkTable(transaction.getTxId(),reMark, this@WalletSendTransactionViewModel.block_chain_type)
                observableEmitter.onNext(
                    this@WalletSendTransactionViewModel.vCashCore.checkAndSendTransactionToPeers(
                        transaction,
                        str as CharSequence,
                        this@WalletSendTransactionViewModel.block_chain_type
                    )
                )
            }
        }).compose(bindDestoryEvent()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<TxSendResult> {
                override fun onComplete() {}

                override fun onSubscribe(disposable: Disposable) {}

                /* renamed from: a */
                override fun onNext(txSendResult: TxSendResult) {
                    this@WalletSendTransactionViewModel.dismissLoadingDialog()
                    this@WalletSendTransactionViewModel.showFinishDialogAndResetByResult(txSendResult.isSuccess)
                    LocalBroadcastManager.getInstance(this@WalletSendTransactionViewModel.context.getApplicationContext())
                        .sendBroadcast(Intent("refresh_address"))
                }

                override fun onError(th: Throwable) {
                    this@WalletSendTransactionViewModel.dismissLoadingDialog()
                    th.printStackTrace()
                    this@WalletSendTransactionViewModel.excuteThrowable(th)
                }
            })
    }

    //m9107b
    fun showFinishDialogAndResetByResult(isSuccess: Boolean) {
        this.confirmDialog = null
        val promptDialogViewModel = PromptDialogViewModel(this.context)
        promptDialogViewModel.setPromptString(getStringRescourcesByResName("notification"))
        promptDialogViewModel.setSpanned(SpannableString(getStringRescourcesByResName(if (isSuccess) "send_success" else "send_fail")) as Spanned)
        promptDialogViewModel.setCancelString(getStringRescourcesByResName("back"))
        createConfirmView(promptDialogViewModel, {
            this@WalletSendTransactionViewModel.dismissConfirmDialog()
            this@WalletSendTransactionViewModel.confirmDialog = null
            this@WalletSendTransactionViewModel.initSumBalance()
            this@WalletSendTransactionViewModel.availableBalanceString.set(this@WalletSendTransactionViewModel.getAvailableBalance())
            this@WalletSendTransactionViewModel.sendValueObservableDecimal.set("")
            this@WalletSendTransactionViewModel.receiverAddressString.set("")
            if (this@WalletSendTransactionViewModel.changeAddressList != null) {
                this@WalletSendTransactionViewModel.changeAddressList!!.clear()
            }
            this@WalletSendTransactionViewModel.finish()
        }, {
            this@WalletSendTransactionViewModel.dismissConfirmDialog()
            this@WalletSendTransactionViewModel.confirmDialog = null
            this@WalletSendTransactionViewModel.finish()
        })
        showConfirmDialog("sendResult", true)
    }

    //mo42114a
    fun excuteThrowable(th: Throwable) {
        if (th is TxSizeException) {
            showToast(getStringRescourcesByResName("toast_utxo_too_many"))
        } else if (th is SignatureFailedException) {
            showToast(getStringRescourcesByResName("sign_fail"))
        } else if (th is UtxoAlreadySpendException) {
            showToast(getStringRescourcesByResName("toast_utxo_is_pay"))
        } else {
            showToast(getStringRescourcesByResName("send_fail"))
        }
    }
}