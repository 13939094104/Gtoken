package com.vtoken.application.viewModel.wallet

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.ObservableField
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.vtoken.application.R
import com.vtoken.application.util.okHttpRequest.*
import com.vtoken.application.view.activity.ValidatePwdActivity
import com.vtoken.application.view.activity.wallet.ChooseMultipleAddressDetailActivity
import com.vtoken.application.view.activity.wallet.EthAddressDetailActivity
import com.vtoken.application.view.activity.wallet.PrivateKeyTypeActivity
import com.vtoken.application.view.activity.wallet.WalletAddressDetailActivity
import com.vtoken.application.viewModel.BaseViewModel
import etherum.web3j.Web3jManager
import vdsMain.model.Address
import vdsMain.transaction.CAmount
import org.json.JSONObject
import vdsMain.*
import vdsMain.model.HDAccount

class MultipleWalletViewModel(context: Context): BaseViewModel(context) {


    var address: Address? = null

    var jumpType: Int = 0

    var addressString = ObservableField<String>()

    var titleString = ObservableField<String>("")

    var ethAddressString = ObservableField<String>("")

    var sumVdsAvailableString = ObservableField<String>()

    var sumVdstAvailableString = ObservableField<String>("0")//自来也

    var usdtBalanceString = ObservableField<String>("0")

    var ethBalanceString = ObservableField<String>("0")

    var web3Event:Web3jManager.Web3jManagerEvent

    init {
        this.addressString.set(intent.getStringExtra("address"))
        this.jumpType = intent.getIntExtra("jumpType", 3)
        this.address = this.vCashCore.getAddressFromAddressString(this.addressString.get() as String, this.block_chain_type)
        loadLabel()
//        if (this.address!!.isAccount()) {
//            val hdAccountList = this.vCashCore.getHDAccountList()
//            val address = hdAccountList!!.get(0).getAddressVector()
//            val hd=address.get(0)
//            ethAddressString.set(hd.getAddressString(BLOCK_CHAIN_TYPE.ETH))
//        }else{
//
//        }
        initMorePopueBtn()
        ethAddressString.set(this.address!!.getAddressString(BLOCK_CHAIN_TYPE.ETH))
        getSumAvailableStringAndFormat()
        web3Event= object:Web3jManager.Web3jManagerEvent(){
            override fun onBalanceChange(token: ContractAddressToken, ethAddress: String, balance: String
            ) {
                when(token){
                    ContractAddressToken.ETH->{
                        ethBalanceString.set(balance)
                    }
                    ContractAddressToken.USDT->{
                        usdtBalanceString.set(balance)
                    }
                    ContractAddressToken.VDST->{//自来也
                        sumVdstAvailableString.set(balance)
                    }
                }
            }
        }
        Web3jManager.getInstance().addWeb3jObserver(web3Event)
        Web3jManager.getInstance().getTokenBalance(ethAddressString.get()!!,ContractAddressToken.ETH)
        Web3jManager.getInstance().getTokenBalance(ethAddressString.get()!!,ContractAddressToken.USDT)
        Web3jManager.getInstance().getTokenBalance(ethAddressString.get()!!,ContractAddressToken.VDST)//自来也
//        getEthBalance()
//        getUsdtBalance()
//        getVDSTBalance()
    }

    override fun onDestroy() {
        Web3jManager.getInstance().removeWeb3jObserver(web3Event)
    }



    fun loadLabel(){
        if(TextUtils.isEmpty(address!!.orginLabel)){
            titleString.set("")
        }else{
            titleString.set(String.format("(%s)",address!!.orginLabel))
        }
    }

    fun initMorePopueBtn() {
        val linearLayout = getPopupListView().findViewById(R.id.pop_list_layout) as LinearLayout
        val intent = Intent(this.context, ValidatePwdActivity::class.java)
        val noteView = getTextView(linearLayout, getStringRescourcesByResName("note"), false,R.drawable.icon_more_edit)
        val exportWordsView = getTextView(linearLayout, getStringRescourcesByResName("address_detail_pop_up_hd_qr_code"), false,R.drawable.icon_more_terms)
        val exportPrivateKeyView = getTextView(linearLayout, getStringRescourcesByResName("export_unencrypted_private_key"), false,R.drawable.icon_more_key)
        noteView.setOnClickListener {
            this@MultipleWalletViewModel.changeAddressNote()
            this@MultipleWalletViewModel.dismissPopupWindow()
        }
        exportWordsView.setOnClickListener(View.OnClickListener {
            startActivityForResult(Intent(this.context, ValidatePwdActivity::class.java), 1005)
            this@MultipleWalletViewModel.dismissPopupWindow()
        })
        exportPrivateKeyView.setOnClickListener(View.OnClickListener {
            this@MultipleWalletViewModel.dismissPopupWindow()
            startActivity(Intent(this.context, PrivateKeyTypeActivity::class.java).putExtra("address",this.addressString.get()).putExtra("direction",1))
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode==-1&&data!=null){
            val pwd = data.getStringExtra("pwd")
            when(requestCode){
                1005->{
                    checkAndShowQrCodeDialog(pwd)
                }
            }
        }
    }

    private fun checkAndShowQrCodeDialog(pwd: String) {
        var hdAccount:HDAccount?=null
        val hdAccountList = this.vCashCore.getHDAccountList()
        if (hdAccountList != null && hdAccountList.size > 0) {
            hdAccount= hdAccountList.get(0) as HDAccount
        }
        if (hdAccount != null) {
            try {
                val wordsString = hdAccount.getMnemonicWords(pwd as CharSequence)
                val info = getStringRescourcesByResName("hd_account_seed_qr")
                showQrCodeDialog(wordsString,"VDS", info, true,info,object :
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

    //m8771X
    fun changeAddressNote() {
        val label = this.address!!.getLabel()
        var z = true
        if (this.jumpType != 1) {
            z = false
        }
        showLabelChangeConfirmDialog(label, z)
    }

    override fun onAddressLabelConfirm(str: String) {
        if (TextUtils.isEmpty(this.address!!.getLabel())) {
            showToast(getStringRescourcesByResName("address_detail_remark_success"))
        } else {
            showToast(getStringRescourcesByResName("address_detail_reset_remark_success"))
        }
        this.address!!.setLabel(str, true)
        LocalBroadcastManager.getInstance(this.context.getApplicationContext())
            .sendBroadcast(Intent("ecology.reload_address"))
        loadLabel()
        dismissConfirmDialog()
    }

    fun getSumAvailableStringAndFormat() {
        val sumAvailable = this.address!!.getAvailableBalance(this.block_chain_type)
        this.sumVdsAvailableString.set(CAmount.toDecimalSatoshiString(java.lang.Long.valueOf(sumAvailable)))
        Log.LogDebug(ZLY,"1:"+this.block_chain_type+"2:"+sumAvailable+"3:"+ this.address.toString())
    }

    fun getVDSTBalance(){
        val params=HashMap<String,String>()
        params.put("module","account")
        params.put("action","tokenbalance")
        params.put("contractaddress","8011f78a9e2d4abc6568bd51ca1bb117618f280f")
        params.put("address",ethAddressString.get()!!)
        params.put("tag","latest")
        showLoadingFragmentReference(getStringRescourcesByResName("init_loading"))
        val request= CommonRequest.createEthExploerGetRequest(RequestParams(params))
        CommonOkHttpClient.sendCommonRequest(request,EthExploerJsonCallback(DisposeDataHandle(object : DisposeDataListener{
            override fun onSuccess(responseObj: Any?) {
                dismissLoadingFragmentReference()
                val jsonResult=JSONObject(responseObj.toString())
                sumVdstAvailableString.set(CAmount.toDecimalUsdtString(jsonResult.getLong("result")))
            }

            override fun onFailure(exception: OkHttpException?) {
                dismissLoadingFragmentReference()
            }
        })))
    }
//    fun getUsdtBalance(){
//        val params=HashMap<String,String>()
//        params.put("module","account")
//        params.put("action","tokenbalance")
//        params.put("contractaddress","0xdac17f958d2ee523a2206206994597c13d831ec7")
//        params.put("address",ethAddressString.get()!!)
//        params.put("tag","latest")
//        showLoadingFragmentReference(getStringRescourcesByResName("init_loading"))
//        val request= CommonRequest.createEthExploerGetRequest(RequestParams(params))
//        CommonOkHttpClient.sendCommonRequest(request,EthExploerJsonCallback(DisposeDataHandle(object : DisposeDataListener{
//            override fun onSuccess(responseObj: Any?) {
//                dismissLoadingFragmentReference()
//                val jsonResult=JSONObject(responseObj.toString())
//                usdtBalanceString.set(CAmount.toDecimalUsdtString(jsonResult.getLong("result")))
//            }
//
//            override fun onFailure(exception: OkHttpException?) {
//                dismissLoadingFragmentReference()
//            }
//        })))
//    }

    fun getEthBalance(){
        val params=HashMap<String,String>()
        params.put("module","account")
        params.put("action","balance")
        params.put("address",ethAddressString.get()!!)
        params.put("tag","latest")
        showLoadingFragmentReference(getStringRescourcesByResName("init_loading"))
        val request= CommonRequest.createEthExploerGetRequest(RequestParams(params))
        CommonOkHttpClient.sendCommonRequest(request,EthExploerJsonCallback(DisposeDataHandle(object : DisposeDataListener{
            override fun onSuccess(responseObj: Any?) {
                dismissLoadingFragmentReference()
                val jsonResult=JSONObject(responseObj.toString())
                ethBalanceString.set(CAmount.toDecimalEthString(jsonResult.getString("result")))
            }

            override fun onFailure(exception: OkHttpException?) {
                dismissLoadingFragmentReference()
            }
        })))
    }
    fun showPopueLayout() {
        showPopupAsDropDown((this.context as ChooseMultipleAddressDetailActivity).getMoreButton())
    }
    fun jumpVdsAddressDetail(){
        val intent = Intent(this.context, WalletAddressDetailActivity::class.java)
        intent.putExtra("address", address!!.getAddressString(BLOCK_CHAIN_TYPE.VCASH))
        intent.putExtra("coin_type", Constants.vcashName)
        intent.putExtra("jumpType", jumpType)
        Log.LogDebug("自来也1",address!!.getAddressString(BLOCK_CHAIN_TYPE.VCASH)+":"+
                Constants.vcashName+":"+jumpType)
        startActivity(intent)
    }
    fun jumpVdstAddressDetail(){//自来也
//        val intent = Intent(this.context, EthAddressDetailActivity::class.java)
//        intent.putExtra("address", address!!.getAddressString(BLOCK_CHAIN_TYPE.VCASH))
//        intent.putExtra("isToken",true)
//        intent.putExtra("contractAddress",ContractAddressToken.VDST)
//        intent.putExtra("ethBalance",ethBalanceString.get()!!)
//        intent.putExtra("balance",sumVdstAvailableString.get()!!)
//        startActivity(intent)
    }

    fun jumpEthAddressDetail(){
        startEthAddressDetail(false,ContractAddressToken.ETH,ethBalanceString.get()!!)
    }

    fun jumpUsdtAddressDetail(){
        startEthAddressDetail(true,ContractAddressToken.USDT,usdtBalanceString.get()!!)
    }

    private fun startEthAddressDetail(isToken:Boolean,token:ContractAddressToken,balance:String){
        val intent = Intent(this.context, EthAddressDetailActivity::class.java)
        intent.putExtra("address", address!!.getAddressString(BLOCK_CHAIN_TYPE.VCASH))
        intent.putExtra("isToken",isToken)
        intent.putExtra("contractAddress",token)
        intent.putExtra("ethBalance",ethBalanceString.get()!!)
        intent.putExtra("balance",balance)
        Log.LogDebug("自来也2",address!!.getAddressString(BLOCK_CHAIN_TYPE.VCASH)+":"+isToken+":"+token+":"+
                ethBalanceString.get()!!+":"+
                balance)
        startActivity(intent)
    }
}