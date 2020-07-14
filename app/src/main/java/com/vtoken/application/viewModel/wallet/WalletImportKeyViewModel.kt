package com.vtoken.application.viewModel.wallet

import android.content.Context
import android.content.Intent
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.vtoken.application.view.activity.ValidatePwdActivity
import com.vtoken.application.view.activity.wallet.ChainResyncActivity
import com.vtoken.application.viewModel.BaseViewModel
import generic.exceptions.AddressExistsException
import generic.utils.AddressUtils
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vdsMain.HeaderType
import vdsMain.StringToolkit
import vdsMain.model.Address
import vdsMain.model.VAnonymousAddress
import vdsMain.tool.AddressPackUtil

//bgo
class WalletImportKeyViewModel(context: Context):BaseViewModel(context) {

    //f11202B
    var privateKeyString = ObservableField<String>()

    //f11203C
    var addressList: List<Address>? = null

    //f11204x
    var bip38PwdString = ObservableField<String>()

    //f11205y
    var isBip38 = ObservableBoolean(false)

    //0:vds 1:eth
    var type:Int=0

    init {
        type=intent.getIntExtra("type",0)
    }

    //mo42096a
    fun setPrivateKeyString(str: String) {
        this.privateKeyString.set(str)
    }

    //mo39924a
    fun confirmImportKey() {
        if (!isStringEmpty(this.privateKeyString.get())) {
            startActivityForResult(Intent(this.context, ValidatePwdActivity::class.java).putExtra("type",1)
                .putExtra("chainType",type).putExtra("actionType",0), 1002)
        }else{
            showToast("请输入私钥")
        }
    }

    //mo42097r
    fun startImportKey(pwd: String) {
        showLoadingFragment(getStringRescourcesByResName("is_import_pri_key") as CharSequence)
        Observable.create(object : ObservableOnSubscribe<Address> {
            override fun subscribe(observableEmitter: ObservableEmitter<Address>) {
                val walletImportKeyViewModel = this@WalletImportKeyViewModel
                var vdsPrivateKey=this@WalletImportKeyViewModel.privateKeyString.get()!!
                if(type==1){
                    if(vdsPrivateKey.substring(0,2)=="0x"){
                        vdsPrivateKey=vdsPrivateKey.substring(2)
                    }
                    val orginBytes=StringToolkit.getBytes(vdsPrivateKey)
//                    val newBytes=ByteArray(33)
//                    System.arraycopy(orginBytes,0,newBytes,0,32)
//                    newBytes[32]=1
                    vdsPrivateKey=AddressUtils.getBase58String(vCashCore.getWalletChainParams(),HeaderType.PRIVATE_KEY,orginBytes)
                }
                if (!this@WalletImportKeyViewModel.isBip38.get()) {
                    walletImportKeyViewModel.addressList = walletImportKeyViewModel.vCashCore.importPrivateKey(vdsPrivateKey, pwd)
                } else {
                    if (walletImportKeyViewModel.isStringEmpty(walletImportKeyViewModel.bip38PwdString.get() as String)) {
                        walletImportKeyViewModel.addressList = walletImportKeyViewModel.vCashCore.importPrivateKey(vdsPrivateKey, pwd)
                    } else {
                        walletImportKeyViewModel.addressList = walletImportKeyViewModel.vCashCore.importBIP38PrivateKey(
                            vdsPrivateKey,
                            this@WalletImportKeyViewModel.bip38PwdString.get() as String,
                            pwd
                        )
                    }
                }
                if (this@WalletImportKeyViewModel.addressList == null || this@WalletImportKeyViewModel.addressList!!.isEmpty()) {
                    observableEmitter.onError(IllegalArgumentException("import failed"))
                } else {
                    observableEmitter.onNext(this@WalletImportKeyViewModel.addressList!!.get(0) as Address)
                }
            }
        }).subscribeOn(Schedulers.io()).compose(bindDestoryEvent()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Address> {
                override fun onComplete() {}

                override fun onSubscribe(disposable: Disposable) {}

                /* renamed from: a */
                override fun onNext(address: Address) {
                    this@WalletImportKeyViewModel.dismissLoadingDialog()
                    if (address is VAnonymousAddress) {
                        this@WalletImportKeyViewModel.sendReloadAddressBroadcast()
                        return
                    }
                    val addressList = AddressPackUtil.m6840a(this@WalletImportKeyViewModel.vCashCore, address)
                    sendReloadAddressBroadcast()
                    this@WalletImportKeyViewModel.showToast(getStringRescourcesByResName("import_private_key_success"))
                    val intent=Intent(context, ChainResyncActivity::class.java)
                    startActivity(intent)
                }

                override fun onError(th: Throwable) {
                    this@WalletImportKeyViewModel.dismissLoadingDialog()
                    if (th is AddressExistsException) {
                        this@WalletImportKeyViewModel.showToast(this@WalletImportKeyViewModel.getStringRescourcesByResName("address_already_exist"))
                    } else {
                        this@WalletImportKeyViewModel.showToast(this@WalletImportKeyViewModel.getStringRescourcesByResName("import_private_key_failed"))
                    }
                    th.printStackTrace()
                }
            })
    }

    private fun m9032O() {
        showConfirmDialog("ImportSuccess", true)
    }

    override fun initiateScan(){
        super.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            this.confirmDialog = null
            m9032O()
        }
    }

    //m9031N
    fun sendReloadAddressBroadcast() {
        LocalBroadcastManager.getInstance(this.context).sendBroadcast(Intent("ecology.reload_address"))
    }
}