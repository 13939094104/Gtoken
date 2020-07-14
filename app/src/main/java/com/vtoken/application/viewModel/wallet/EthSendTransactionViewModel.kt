package com.vtoken.application.viewModel.wallet

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.google.gson.JsonParser
import com.google.zxing.integration.android.IntentIntegrator
import com.orhanobut.logger.Logger
import com.vtoken.application.ApplicationLoader
import com.vtoken.application.R
import com.vtoken.application.util.okHttpRequest.*
import com.vtoken.application.view.activity.ValidatePwdActivity
import com.vtoken.application.viewModel.BaseViewModel
import etherum.web3j.Web3jManager
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.crypto.*
import org.web3j.utils.Numeric
import vdsMain.BLOCK_CHAIN_TYPE
import vdsMain.model.Address
import vdsMain.transaction.CAmount
import java.math.BigDecimal
import java.math.BigInteger

class EthSendTransactionViewModel(context: Context): BaseViewModel(context) {


    var address: Address? = null
    var ethAddressString = ObservableField<String>("")
    var ethBalance:String="0"
    var balanceString = ObservableField<String>("0")
    var contractToken:ContractAddressToken
    var contractTokenName= ObservableField<String>("")
    var isToken:Boolean
    var transferValueString= ObservableField("")
    var titleString= ObservableField("")
    var tipString= ObservableField("")
    var transferValue:Double?=null
    var sumValue:Double?=null
    var nonce:String=""
    var gasPrice: BigInteger = BigInteger.ZERO
    var gasLimit:BigInteger= BigInteger.ZERO
    var minerFee= ObservableField("0")
    var toAddressString= ObservableField("")

    var web3Event:Web3jManager.Web3jManagerEvent

    init {
        this.address = this.vCashCore.getAddressFromAddressString(intent.getStringExtra("address"), BLOCK_CHAIN_TYPE.VCASH)
        ethAddressString.set(this.address!!.getAddressString(BLOCK_CHAIN_TYPE.ETH))
        balanceString.set(intent.getStringExtra("balance"))
        ethBalance=intent.getStringExtra("ethBalance")
        contractToken=intent.getSerializableExtra("contractAddress") as ContractAddressToken
        contractTokenName.set(contractToken.name)
        titleString.set(contractToken.name+"转账")
        tipString.set(String.format("请输入%s地址",contractToken.name))
        isToken=intent.getBooleanExtra("isToken",true)
        gasLimit=contractToken.getContractToken().gasLimit
        getTransactionCount()
        getGasPrice()
        web3Event= object:Web3jManager.Web3jManagerEvent(){
            override fun onBalanceChange(token: ContractAddressToken, ethAddress: String, balance: String
            ) {
                if(contractToken==token&&ethAddressString.get()==ethAddress){
                    balanceString.set(balance)
                }else if(ContractAddressToken.ETH==token){
                    ethBalance=balance
                }
            }
        }
        Web3jManager.getInstance().addWeb3jObserver(web3Event)
    }



    override fun onDestroy() {
        Web3jManager.getInstance().removeWeb3jObserver(web3Event)
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(data!=null){
            val parseActivityResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (parseActivityResult != null&&parseActivityResult.contents != null) {
                toAddressString.set(parseActivityResult.contents)
                return
            }
            if(requestCode==10011&&resultCode==Activity.RESULT_OK){
                showSendTxConfirmDialog(data.getStringExtra("pwd"))
            }
        }
    }

    fun transferAll(){
        val balance=BigDecimal(balanceString.get())
        val fee=BigDecimal(minerFee.get()!!)
        if(isToken){
            transferValueString.set(balanceString.get())
        }else{
            if(balance>fee){
                transferValueString.set(balance.minus(fee).toString())
            }
        }
    }

    fun checkTransferEth(){
        if(isStringEmpty(nonce)||gasPrice.equals(BigInteger.ZERO)){
            showToast("转账初始化未完成")
            return
        }
        if(!checkSimpleEthAddressFormat(toAddressString.get())){
            showToast("请输入正确以太坊地址")
            return
        }
        transferValue=transferValueString.get()!!.toDoubleOrNull()
        if(transferValue==null||(transferValue==0.toDouble())){
            showToast("请输入正确金额")
            return
        }
        if(isToken){
            if(transferValue!!>balanceString.get()!!.toDouble())
            {
                showToast("转账金额大于余额")
                return
            }
            if(ethBalance.toDouble()< minerFee.get()!!.toDouble()){
                showToast("手续费不足")
                return
            }
        }else{
            sumValue=transferValue!!+minerFee.get()!!.toDouble()
            if(sumValue!!>balanceString.get()!!.toDouble())
            {
                showToast("转账金额加手续费大于余额")
                return
            }
        }

        startActivityForResult(Intent(this.context,ValidatePwdActivity::class.java),10011)
    }

    fun showSendTxConfirmDialog(pwd:String) {
//        val binding = DataBindingUtil.inflate<DialogEthTransactionConfirmBinding>(
//            LayoutInflater.from(ApplicationLoader.getSingleApplicationContext()), R.layout.dialog_eth_transaction_confirm, null, false
//        )
//        binding.fromAddress.setText(ethAddressString.get())
//        binding.toAddress.setText(toAddressString.get())
//        binding.sendAmount.setText(transferValueString.get())
//        binding.fee.setText(minerFee.get())
//        setConfirmDialogView(binding.root)
//        showConfirmDialog("sendTxAsk", true)
//        binding.back.setOnClickListener {
//            dismissConfirmDialog()
//        }
//        binding.confirmBtn.setOnClickListener {
//            dismissConfirmDialog()
//            confirmTransferEth(pwd)
//        }
    }

    fun confirmTransferEth(pwd:String) {
        var rawTransaction:RawTransaction
        if(isToken){
            val function=Function("transfer",
                arrayListOf(org.web3j.abi.datatypes.Address(toAddressString.get()!!.substring(2)),
                    Uint256(BigInteger(CAmount.toDecimalTokenLongString(transferValue,this.contractToken.getContractToken().tokenDecimal)))
            ), arrayListOf())
            val functionData= FunctionEncoder.encode(function)
            rawTransaction=RawTransaction.createTransaction(BigInteger(nonce,16),gasPrice,gasLimit,contractToken.address,functionData)
        }else{
            rawTransaction= RawTransaction.createEtherTransaction(BigInteger(nonce,16),gasPrice,gasLimit,toAddressString.get(),
                BigInteger(CAmount.toDecimalTokenLongString(transferValue,this.contractToken.getContractToken().tokenDecimal))
            );
        }
        val privateKey=vCashCore.getAddressOrginPrivateKey(this.address!!.getAddressString(BLOCK_CHAIN_TYPE.VCASH),pwd)
        val signedMessage=TransactionEncoder.signMessage(rawTransaction,1L, Credentials.create(privateKey))
        val hexData=Numeric.toHexString(signedMessage)
        sendRawTransaction(hexData)
    }


    fun sendRawTransaction(hexdata:String){
        val params=HashMap<String,String>()
        params.put("module","proxy")
        params.put("action","eth_sendRawTransaction")
        params.put("hex",hexdata)
        val request= CommonRequest.createEthExploerPostRequest(RequestParams(params))
        CommonOkHttpClient.sendCommonRequest(request,
            EthExploerJsonCallback(DisposeDataHandle(object : DisposeDataListener {
                override fun onSuccess(responseObj: Any?) {
                    val jsonObject= JsonParser().parse(responseObj.toString()).asJsonObject
                    if(jsonObject.has("error")){
                        showToast("提交交易失败")
                    }else{
                        val txHash=jsonObject.get("result").asString
                        Logger.d(txHash)
                        showToast("提交交易成功")
                        //Web3jManager.getInstance().getTransactionReceipt(txHash)
                        finish()
                    }
                }

                override fun onFailure(exception: OkHttpException?) {
                    showToast("提交交易失败")
                }
            }))
        )
    }

    fun getTransactionCount(){
            val params=HashMap<String,String>()
            params.put("module","proxy")
            params.put("action","eth_getTransactionCount")
            params.put("address",ethAddressString.get()!!)
            params.put("tag","latest")
        showLoadingFragmentReference(getStringRescourcesByResName("init_loading"))
            val request= CommonRequest.createEthExploerGetRequest(RequestParams(params))
            CommonOkHttpClient.sendCommonRequest(request,
                EthExploerJsonCallback(DisposeDataHandle(object : DisposeDataListener {
                    override fun onSuccess(responseObj: Any?) {
                        dismissLoadingFragmentReference()
                        val jsonObject= JsonParser().parse(responseObj.toString()).asJsonObject
                        nonce=jsonObject.get("result").asString.substring(2)
                    }

                    override fun onFailure(exception: OkHttpException?) {
                        dismissLoadingFragmentReference()
                    }
                }))
            )
    }

    fun getGasPrice(){
        val params=HashMap<String,String>()
        params.put("module","proxy")
        params.put("action","eth_gasPrice")
        val request= CommonRequest.createEthExploerGetRequest(RequestParams(params))
        showLoadingFragmentReference(getStringRescourcesByResName("init_loading"))
        CommonOkHttpClient.sendCommonRequest(request,
            EthExploerJsonCallback(DisposeDataHandle(object : DisposeDataListener {
                override fun onSuccess(responseObj: Any?) {
                    dismissLoadingFragmentReference()
                    val jsonObject= JsonParser().parse(responseObj.toString()).asJsonObject
                    val bit16price=jsonObject.get("result").asString.substring(2)
                    gasPrice=BigInteger(bit16price,16)
                    minerFee.set(CAmount.toDecimalEthString(gasPrice.multiply(gasLimit).toString()))
                }

                override fun onFailure(exception: OkHttpException?) {
                    dismissLoadingFragmentReference()
                    Logger.d(exception)
                }
            }))
        )
    }
}