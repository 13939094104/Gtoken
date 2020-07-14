package com.vtoken.application.viewModel.wallet

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.vtoken.application.view.activity.ValidatePwdActivity
import com.vtoken.application.view.activity.wallet.WalletImportKeyActivity
import com.vtoken.application.viewModel.BaseViewModel
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vdsMain.BLOCK_CHAIN_TYPE


class PrivateKeyTypeViewModel(context: Context): BaseViewModel(context) {

    //0 import 1 export
    var direction:Int=0

    var chainType:Int=1

    var addressString:String=""

    init {
        direction=intent.getIntExtra("direction",0)
        if(direction==1){
            addressString = intent.getStringExtra("address")
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(data!=null){
            if(requestCode==10001&&resultCode==Activity.RESULT_OK){
                exportPrivateKey(data.getStringExtra("pwd"))
            }
        }
    }

    fun vdsSelected(){
        chainType=0
        if(direction==0){
            startActivity(Intent(this.context,WalletImportKeyActivity::class.java).putExtra("type",0))
        }else{
            startActivityForResult(Intent(this.context, ValidatePwdActivity::class.java).putExtra("type",1)
                .putExtra("chainType",0).putExtra("actionType",1),10001)
        }
    }


    fun ethSelected(){
        chainType=1
        if(direction==0){
            startActivity(Intent(this.context,WalletImportKeyActivity::class.java).putExtra("type",1))
        }else{
            startActivityForResult(Intent(this.context, ValidatePwdActivity::class.java).putExtra("type",1)
                .putExtra("chainType",1).putExtra("actionType",1),10001)
        }
    }

    fun exportPrivateKey(pwd:String){

        Observable.create(object : ObservableOnSubscribe<String> {
            override fun subscribe(observableEmitter: ObservableEmitter<String>) {
                var privateKey=""
                if(chainType==0){
                     privateKey=vCashCore.getAddressPrivateKey(addressString,pwd,BLOCK_CHAIN_TYPE.VCASH)
                }else{
                     privateKey=vCashCore.getAddressOrginPrivateKey(addressString,pwd)
                }
                observableEmitter.onNext(privateKey)
            }
        }).compose(bindDestoryEvent()).subscribeOn(Schedulers.computation()).observeOn(
            AndroidSchedulers.mainThread()).subscribe(object: Observer<String> {
            override fun onComplete() {
            }

            override fun onSubscribe(disposable: Disposable) {
            }

            /* renamed from: a */
            override fun onNext(privateKey:String) {
                showQrCodeDialog(privateKey,if(chainType==0) "VDS" else "ETH", if(chainType==0) "VDS私钥" else "ETH私钥", true,privateKey,object :
                    QrOptionDialogEvent {
                    override fun onCopyClick() {}

                    override fun onSaveClick() {}

                    override fun mo41443c() {}
                })
            }

            override fun onError(th:Throwable) {
                th.printStackTrace();
                showToast(getStringRescourcesByResName("export_failed"));
            }
        });
    }
}