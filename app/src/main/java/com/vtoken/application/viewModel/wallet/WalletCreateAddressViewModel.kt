package com.vtoken.application.viewModel.wallet

import android.content.Context
import android.content.Intent
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.core.view.PointerIconCompat
import android.text.TextUtils
import android.util.Log
import com.vtoken.application.view.activity.ValidatePwdActivity
import com.vtoken.application.viewModel.BaseViewModel
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import vdsMain.model.Address

class WalletCreateAddressViewModel(context: Context):BaseViewModel(context) {

    //f11163y
    var labelString = ObservableField<String>()

    //f11164z
    var isGeneralAddress = ObservableBoolean(true)

    //f11162x
    var createNumberString = ObservableField("1")

    init {

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == 1007) {
            addAddress(resultCode, data)
        }
    }
    fun checkPwd(){
        val number=createNumberString.get()!!.toIntOrNull()
        if(number==null){
            showToast("请输入正确数量")
        }else if(number>100){
            showToast("最多创建100个地址")
        }else{
            val intent2 = Intent(this.context, ValidatePwdActivity::class.java)
            startActivityForResult(intent2, PointerIconCompat.TYPE_CROSSHAIR)
        }
    }
    //m8988a
    private fun addAddress(i: Int, intent: Intent?) {
        if (i == -1 && intent != null) {
            val pwd = intent.getStringExtra("pwd")
            showLoadingFragment(getStringRescourcesByResName("loading_create_msg") as CharSequence)
            val label = this.labelString.get()
            io.reactivex.Observable.create(object : ObservableOnSubscribe<List<Address>> {
                override fun subscribe(observableEmitter: ObservableEmitter<List<Address>>) {
                    val number = Integer.valueOf(this@WalletCreateAddressViewModel.createNumberString.get() as String)
                    var addressList: MutableList<Address> = ArrayList<Address>()
                    if (!this@WalletCreateAddressViewModel.isGeneralAddress.get()) {
                    } else if (number.toInt() == 1) {
                        val vCashCore1 = this@WalletCreateAddressViewModel.vCashCore
                        val intValue = number.toInt()
                        val labelArr = arrayOfNulls<CharSequence>(1)
                        labelArr[0] = if (TextUtils.isEmpty(label)) "" else label
                        addressList = vCashCore1.createNormalAddress(pwd as CharSequence, intValue, *labelArr) as MutableList<Address>
                    } else {
                        val labelArr = arrayOfNulls<String>(number.toInt())
                        for (i in 0 until number.toInt()) {
                            labelArr[i] = if (TextUtils.isEmpty(label)) "" else String.format(
                                Locale.getDefault(),
                                "%s%d",
                                *arrayOf<Any>(label!!, Integer.valueOf(i))
                            )
                        }
                        addressList = this@WalletCreateAddressViewModel.vCashCore.createNormalAddress(
                            pwd as CharSequence,
                            number.toInt(),
                            *labelArr
                        ) as MutableList<Address>
                    }
                    var addressOrderIndex = this@WalletCreateAddressViewModel.vCashCore.getWalletAddressModel().getAddressAmount() - 1
                    for (address in addressList) {
                        val i2 = addressOrderIndex - 1
                        address.updateAddressOrderIndex(addressOrderIndex)
                        addressOrderIndex = i2
                    }
                    observableEmitter.onNext(addressList)
                }
            }).subscribeOn(Schedulers.io()).compose(bindDestoryEvent()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : io.reactivex.Observer<List<Address>> {
                    override fun onSubscribe(disposable: Disposable) {}

                    /* renamed from: a */
                    override fun onNext(list: List<Address>) {
                        dismissLoadingDialog()
                        if (!list.isEmpty()) {
                            val walletCreateAddressViewModel = this@WalletCreateAddressViewModel
                            walletCreateAddressViewModel.showCreateNumberToast(walletCreateAddressViewModel.isGeneralAddress.get(), list.size)
                            LocalBroadcastManager.getInstance(this@WalletCreateAddressViewModel.context.getApplicationContext())
                                .sendBroadcast(Intent("ecology.reload_address").putExtra("need_sort", true))
                        } else {
                            val walletCreateAddressViewModel = this@WalletCreateAddressViewModel
                            walletCreateAddressViewModel.showToast(walletCreateAddressViewModel.getStringRescourcesByResName("create_fail"))
                        }
                        val sb = StringBuilder()
                        sb.append("onActivityResult: ")
                        sb.append(list)
                        Log.d("CreateAddressViewModel", sb.toString())
                        finish()
                    }

                    override fun onError(th: Throwable) {
                        th.printStackTrace()
                        val walletCreateAddressViewModel = this@WalletCreateAddressViewModel
                        walletCreateAddressViewModel.showToast(walletCreateAddressViewModel.getStringRescourcesByResName("create_fail"))
                        dismissLoadingDialog()
                    }

                    override fun onComplete() {
                        Log.e("CreateAddressViewModel", "onComplete: ")
                    }
                })
        }
    }


    //m8992a
    fun showCreateNumberToast(z: Boolean, i: Int) {
        showToast(String.format(getStringRescourcesByResName("already_create_addr"),Integer.valueOf(i)))
    }
}