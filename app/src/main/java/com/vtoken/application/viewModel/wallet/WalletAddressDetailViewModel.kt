package com.vtoken.application.viewModel.wallet

import android.content.Context
import android.content.Intent
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.vtoken.application.ApplicationLoader
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.adapter.RecyclerSimpleAdapter
import com.vtoken.application.util.WalletUtil
import com.vtoken.application.view.activity.ValidatePwdActivity
import com.vtoken.application.view.activity.wallet.PrivateKeyTypeActivity
import com.vtoken.application.view.activity.wallet.WalletSendTransactionActivity
import com.vtoken.application.view.activity.wallet.WalletTransactionRecordActivity
import com.vtoken.application.viewHolder.ViewHolder
import com.vtoken.application.viewModel.BaseViewModel
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import vdsMain.AddressType
import vdsMain.BLOCK_CHAIN_TYPE
import vdsMain.model.Address
import vdsMain.model.HDAccount
import vdsMain.tool.Util
import vdsMain.transaction.CAmount
import vdsMain.transaction.Transaction
import vdsMain.transaction.TransactionConfirmType
import vdsMain.transaction.VTransaction
import java.math.BigDecimal
import java.util.HashMap

class WalletAddressDetailViewModel(context: Context) :BaseViewModel(context){

    //f10958A
    var address: Address? = null

    //f10959B
    var jumpType: Int = 0

    //f10971N
    var hdAccount: HDAccount? = null

    //f10969L
    var hasLock = ObservableBoolean(false)

    var f10961D = ObservableBoolean(true)

    private var f10972O: Disposable? = null

    var f10970M = ObservableBoolean(true)

    //f10974x
    lateinit var recyclerSimpleAdapter: RecyclerSimpleAdapter<Transaction>

    val receiveList= ArrayList<Transaction>()
    val sendList= ArrayList<Transaction>()

    //f10975y
    var addressString = ObservableField<String>()


    //f10962E
    var sumBalanceString = ObservableField<String>()

    //f10966I
    var blockChainName: ObservableField<String> = ObservableField(this.block_chain_type.name)

    //f10963F
    var lockBalanceString = ObservableField<String>()

    //f10976z
    var labelString = ObservableField<String>("")

    var hasLabel = ObservableField<Boolean>(false)

    init {
        this.addressString.set(intent.getStringExtra("address"))
        this.address = this.vCashCore.getAddressFromAddressString(this.addressString.get() as String, this.block_chain_type)
        if (this.address == null) {
            finish()
        }
        this.jumpType = intent.getIntExtra("jumpType", 3)
        //更多右上角
        //initMorePopueBtn(this.address!!)
        initData()
        initTransactionAdapter()
        if (this.address == null) {
            Log.e("AddressDetailViewModel", "can not found address instance ")
            showToast(getStringRescourcesByResName("parse_address_details_error"))
            finish()
        }
        //reloadTransaction()
    }

    //mo42000a
    fun initMorePopueBtn(address1: Address) {
        val inflate = getLayoutInflater().inflate(R.layout.pop_up_address_detail, null, false)
        this.popupWindow = PopupWindow(inflate, -2, -2, true)
        if (address1.isAccount() && this.jumpType == 4) {
            initPopueLayoutForHdAccount(inflate)
        } else if (address1.isWatchedAddress()) {
            //m8798f(inflate)
        } else {
            val addressType = this.address!!.getAddressType()
            if (addressType === AddressType.MULTISIG || addressType === AddressType.WITNESS_V0_SCRIPT_HASH || addressType === AddressType.WITNESS_V0_SCRIPT_HASH_SCRIPT) {
               // m8804h(inflate)
            } else {
                initNormalAddressPopueLayout(inflate)
            }
        }
    }

   

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode==-1&&data!=null){
            val pwd = data.getStringExtra("pwd")
            if(requestCode!=1016){
                when(requestCode){
                    1003->{
                        showExportPrivateKeyDialog(pwd, false)
                    }
                    1005->{
                        checkAndShowQrCodeDialog(pwd)
                    }
                }
            }
        }
    }

    //mo41997S
    fun reloadData() {
        this.block_chain_type = ApplicationLoader.getBlockChainType()
        this.blockChainName.set(this.block_chain_type.name)
        initData()
        reloadTransaction()
        setPeerStatus()
    }

    fun copyAddressString(){
        Util.copyAndShowToast(addressString.get())
    }

    fun showAddressQrDialog(){
        showQrCodeDialog(addressString.get(),"VDS","VDS公链地址",true,addressString.get(),object:QrOptionDialogEvent{
            override fun onCopyClick() {

            }

            override fun onSaveClick() {

            }

            override fun mo41443c() {

            }

        })
    }

    //m8784b
    fun showExportPrivateKeyDialog(str:String, isBip38:Boolean) {
        showLoadingFragment(getStringRescourcesByResName("loading_export_private_key_msg"));
        Observable.create(object : ObservableOnSubscribe<String> {
            override fun subscribe(observableEmitter:ObservableEmitter<String> ) {
                if (!isBip38) {
                    //observableEmitter.onNext(this@WalletAddressDetailViewModel.vCashCore.getAddressOrginPrivateKey(this@WalletAddressDetailViewModel.address!!.getAddressString(), str));
                    observableEmitter.onNext(this@WalletAddressDetailViewModel.vCashCore.getAddressPrivateKey(this@WalletAddressDetailViewModel.address!!.getAddressString(), str, this@WalletAddressDetailViewModel.block_chain_type));
                } else {
                    val vCashCore = this@WalletAddressDetailViewModel.vCashCore;
                    val address = this@WalletAddressDetailViewModel.address;
                    observableEmitter.onNext(vCashCore.getAddressBIP38PrivateKey(address!!, str, str));
                }
                intent.putExtra("is_bip_38", isBip38);
            }
        }).compose(bindDestoryEvent()).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(object: Observer<String> {
            override fun onComplete() {
            }

            override fun onSubscribe(disposable:Disposable ) {
            }

            /* renamed from: a */
            override fun onNext(privateKey:String) {
                this@WalletAddressDetailViewModel.dismissLoadingDialog();
                intent.putExtra("private_key", privateKey);
                showQrCodeDialog(privateKey, "VDS",this@WalletAddressDetailViewModel.address!!.getAddressString(this@WalletAddressDetailViewModel.block_chain_type), true,privateKey,object :
                    QrOptionDialogEvent {
                    override fun onCopyClick() {}

                    override fun onSaveClick() {}

                    override fun mo41443c() {}
                })
            }

            override fun onError(th:Throwable) {
                this@WalletAddressDetailViewModel.dismissLoadingDialog();
                th.printStackTrace();
                val bfz = this@WalletAddressDetailViewModel;
                bfz.showToast(bfz.getStringRescourcesByResName("export_failed"));
            }
        });
    }


    //m8821t
    private fun checkAndShowQrCodeDialog(pwd: String) {
        val hdAccount = this.hdAccount
        if (hdAccount != null) {
            try {
                val wordsString = hdAccount.getMnemonicWords(pwd as CharSequence)
                val info = getStringRescourcesByResName("hd_account_seed_qr")
                showQrCodeDialog(wordsString, "VDS",info, true,info,object :
                    QrOptionDialogEvent {
                    override fun onCopyClick() {}

                    override fun onSaveClick() {}

                    override fun mo41443c() {}
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    //m8807i
    private fun initPopueLayoutForHdAccount(view: View) {
        val hdAccountList = this.vCashCore.getHDAccountList()
        if (hdAccountList != null && hdAccountList.size > 0) {
            this.hdAccount = hdAccountList.get(0) as HDAccount
        }
        val linearLayout = getPopupListView().findViewById(R.id.pop_list_layout) as LinearLayout
        val noteView = getTextView(linearLayout, getStringRescourcesByResName("note"), true)
        val hdQRCodeView = getTextView(linearLayout, getStringRescourcesByResName("address_detail_pop_up_hd_qr_code"), false)
        noteView.setOnClickListener {
            this@WalletAddressDetailViewModel.changeAddressNote()
            this@WalletAddressDetailViewModel.dismissPopupWindow()
        }
        hdQRCodeView.setOnClickListener(View.OnClickListener {
            val walletAddressDetailViewModel = this@WalletAddressDetailViewModel
            walletAddressDetailViewModel.startActivityForResult(Intent(walletAddressDetailViewModel.context, ValidatePwdActivity::class.java), 1005)
            this@WalletAddressDetailViewModel.dismissPopupWindow()
        })
    }

    //m8801g
    private fun initNormalAddressPopueLayout(view: View) {
        val linearLayout = getPopupListView().findViewById(R.id.pop_list_layout) as LinearLayout
        val intent = Intent(this.context, ValidatePwdActivity::class.java)
        val noteView = getTextView(linearLayout, getStringRescourcesByResName("note"), true)
        val exportPrivateKeyView = getTextView(linearLayout, getStringRescourcesByResName("export_unencrypted_private_key"), false)
        noteView.setOnClickListener {
            this@WalletAddressDetailViewModel.changeAddressNote()
            this@WalletAddressDetailViewModel.dismissPopupWindow()
        }
        exportPrivateKeyView.setOnClickListener(View.OnClickListener {
            this@WalletAddressDetailViewModel.dismissPopupWindow()
            startActivity(Intent(this.context,PrivateKeyTypeActivity::class.java).putExtra("address",this.addressString.get()).putExtra("direction",1))
            //his@WalletAddressDetailViewModel.startActivityForResult(intent, PointerIconCompat.TYPE_HELP as Int)
        })
    }

    //m8767T
    fun initData() {
        if (!this.address!!.isAccount() || this.address!!.isFlagIndentity()) {
            val sumBalance = this.address!!.getSumBalance(this.block_chain_type)
            this.sumBalanceString.set(
                Util.m7017a(
                    CAmount.toDecimalSatoshiDouble(java.lang.Long.valueOf(sumBalance)),
                    Util.m7014a(this.block_chain_type)
                )
            )
            val lockBalance = sumBalance - this.address!!.getAvailableBalance(this.block_chain_type)
            if (lockBalance > 0) {
                this.hasLock.set(true)
                this.lockBalanceString.set(
                    Util.m7025a(
                        java.lang.Double.toString(CAmount.toDecimalSatoshiDouble(java.lang.Long.valueOf(lockBalance))),
                        this.block_chain_type.name
                    )
                )
            } else {
                this.hasLock.set(false)
            }
        } else {
            val account = this.vCashCore.getAccount(this.address!!.getAccount())
            val sumBalance = account!!.getBalance(this.block_chain_type)
            val availBalance = account!!.getSumBalance(false, this.block_chain_type)
            this.sumBalanceString.set(
                Util.m7019a(
                    CAmount.toDecimalSatoshiDouble(java.lang.Long.valueOf(sumBalance)),
                    this.block_chain_type.name
                )
            )
            val lockBalance = sumBalance - availBalance
            if (lockBalance > 0) {
                this.hasLock.set(true)
                this.lockBalanceString.set(
                    Util.m7019a(
                        CAmount.toDecimalSatoshiDouble(java.lang.Long.valueOf(lockBalance)),
                        this.block_chain_type.name
                    )
                )
            } else {
                this.hasLock.set(false)
            }
        }
        this.labelString.set(this.address!!.getLabel())
        if(!isStringEmpty(this.address!!.getLabel())){
            hasLabel.set(true)
        }else{
            hasLabel.set(false)
        }
        mo39924a()
    }

    //m8771X
    fun changeAddressNote() {
        val label = this.address!!.getLabel()
        var z = true
        if (this.jumpType != 1) {
            z = false
        }
        showLabelChangeConfirmDialog(label, z)
    }


    fun mo39924a() {
        val address1 = this.address
        if (address1 != null) {
            if (address1.isCategoryStatusNotZero()) {
                this.f10961D.set(this.vCashCore.mo43803a(this.address!!.getCTxDestination(), this.block_chain_type))
            } else {
                this.f10961D.set(true)
            }
        }
    }

    override fun onAddressLabelConfirm(str: String) {
        if (TextUtils.isEmpty(this.address!!.getLabel())) {
            showToast(getStringRescourcesByResName("address_detail_remark_success"))
        } else {
            showToast(getStringRescourcesByResName("address_detail_reset_remark_success"))
        }
        this.address!!.setLabel(str, true)
        this.labelString.set(this.address!!.getLabel())
        if(!isStringEmpty(this.address!!.getLabel())){
            hasLabel.set(true)
        }else{
            hasLabel.set(false)
        }
        LocalBroadcastManager.getInstance(this.context.getApplicationContext())
            .sendBroadcast(Intent("ecology.reload_address"))
        dismissConfirmDialog()
    }


    //mo42002c
    fun getTransactionBalanceBySelfAddress(transaction: Transaction): String {
        return getTransactionBalanceStringByAddress(transaction, this.address)
    }

    //m8768U
    fun initTransactionAdapter() {
        this.recyclerSimpleAdapter = RecyclerSimpleAdapter(R.layout.item_address_detail_transaction, BR.walletAddressDetailTransaction)
        this.recyclerSimpleAdapter.setRecyclerBindEvent(object : RecyclerSimpleAdapter.RecyclerSimpleBindEvent {
            override fun onBind(viewHolder: ViewHolder, i: Int) {
                viewHolder.getViewDataBinding().setVariable(BR.walletAddressDetailModel, this@WalletAddressDetailViewModel)
            }
        } as RecyclerSimpleAdapter.RecyclerSimpleBindEvent)
        this.recyclerSimpleAdapter.setOnItemClickListener(object :
            RecyclerSimpleAdapter.RecyclerSimpleAdapterClickEvent {
            /* renamed from: b */
            override fun onLongClick(position: Int) {}

            /* renamed from: a */
            override fun onClick(position: Int) {
                val mutableList = this@WalletAddressDetailViewModel.recyclerSimpleAdapter.getDataList()
                if (mutableList != null && !mutableList.isEmpty()) {
                    val transaction = mutableList.get(position) as Transaction
                    if (transaction !is VTransaction || !(transaction as VTransaction).mo42820Z() || !transaction.isConfirmed()) {
                        this@WalletAddressDetailViewModel.JumpTransactionRecordActivity(transaction)
                        return
                    }
                    val transaction1 =
                        this@WalletAddressDetailViewModel.vCashCore.getConfirmedTransactionListByBlockHash(transaction.getBlockHash()!!, BLOCK_CHAIN_TYPE.VCASH).get(0) as Transaction
//TODO 矿工事务 特殊跳转
//                    if (transaction1.isCoinBaseTransaction()) {
//                        val intent = Intent(this@WalletAddressDetailViewModel.context, ContractTransactionRecordActivity::class.java)
//                        intent.putExtra("trans_ID", transaction.getTxId().toString())
//                        intent.putExtra("coin_base_trans_ID", transaction1.getTxId().toString())
//                        intent.putExtra("address", this@WalletAddressDetailViewModel.addressString.get() as String)
//                        this@WalletAddressDetailViewModel.startActivity(intent)
//                        return
//                    }
                    this@WalletAddressDetailViewModel.JumpTransactionRecordActivity(transaction)
                }
            }
        })
        this.recyclerSimpleAdapter.mo38515a(true)
    }

    //mo42001b
    fun JumpTransactionRecordActivity(transaction: Transaction) {
        val intent = Intent(this.context, WalletTransactionRecordActivity::class.java)
        intent.putExtra("trans_ID", transaction.getTxId().hashString())
        intent.putExtra("address", this.addressString.get())
        startActivity(intent)
    }

    //mo39929b
    fun reloadTransaction(searchType:Int=0) {
        if (f10972O != null && !f10972O!!.isDisposed()) {
            this.f10972O!!.dispose()
        }
        this.f10970M.set(true)
        this.f10972O = Observable.create(object : ObservableOnSubscribe<List<Transaction>> {
            override fun subscribe(observableEmitter: ObservableEmitter<List<Transaction>>) {
                val list: List<Transaction>?
                val hashMap = HashMap<Transaction,Boolean>()
                if (!this@WalletAddressDetailViewModel.address!!.isAccount() || this@WalletAddressDetailViewModel.address!!.isFlagIndentity()) {
                    list = this@WalletAddressDetailViewModel.vCashCore.getTransactionListByCTxDestination(
                        this@WalletAddressDetailViewModel.address!!.getCTxDestination(),
                        TransactionConfirmType.ALL,
                        this@WalletAddressDetailViewModel.block_chain_type
                    )
                } else {
                    val accountId = this@WalletAddressDetailViewModel.address!!.getAccount()
                    list = this@WalletAddressDetailViewModel.vCashCore.mo43750a(accountId, TransactionConfirmType.ALL, this@WalletAddressDetailViewModel.block_chain_type)
                }
                for (transaction in list!!) {
                    hashMap.put(transaction, java.lang.Boolean.valueOf(true))
                }
                val arrayList = ArrayList(hashMap.keys)
                WalletUtil.m7067a(arrayList as List<Transaction>)
                observableEmitter.onNext(arrayList)
            }
        }).compose(bindDestoryEvent()).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<List<Transaction>> {
                /* renamed from: a */
                override fun accept(list: List<Transaction>) {
                    if (this@WalletAddressDetailViewModel.recyclerSimpleAdapter == null) {
                        this@WalletAddressDetailViewModel.initTransactionAdapter()
                    }
                    receiveList.clear()
                    sendList.clear()
                    for (transaction in list){
                        if(this@WalletAddressDetailViewModel.getTransactionBalanceByAddress(transaction,this@WalletAddressDetailViewModel.address)>=0){
                            transaction.setDirection(Transaction.TransactionDirection.RECEIVE)
                            receiveList.add(transaction)
                        }else{
                            transaction.setDirection(Transaction.TransactionDirection.SEND)
                            sendList.add(transaction)
                        }
                    }
                    this@WalletAddressDetailViewModel.f10970M.set(false)
                    when(searchType){
                        0->this@WalletAddressDetailViewModel.recyclerSimpleAdapter.setDataList(list)
                        1->this@WalletAddressDetailViewModel.recyclerSimpleAdapter.setDataList(receiveList)
                        2->this@WalletAddressDetailViewModel.recyclerSimpleAdapter.setDataList(sendList)
                    }
                    this@WalletAddressDetailViewModel.recyclerSimpleAdapter.notifyDataSetChanged()
                    this@WalletAddressDetailViewModel.startRecyclerAnimation()
                }
            })
    }


    //mo41995Q
    fun jumpToSendActivity() {
        val str: String
        if (this.address != null) {
            if (TextUtils.isEmpty(this.sumBalanceString.get() as CharSequence)) {
                str = "0"
            } else {
                str = (this.sumBalanceString.get() as String).replace(",", "")
            }
            if (BigDecimal(str).compareTo(BigDecimal.ZERO) != 1) {
                showToast(getStringRescourcesByResName("toast_send_total_not_enough"))
                return
            }
        }
        val intent = Intent(this.context, WalletSendTransactionActivity::class.java)
        intent.putExtra("address", this.address!!.getAddressString(this.block_chain_type))
        startActivity(intent)
    }

}