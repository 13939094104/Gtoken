package com.vtoken.application.viewModel.wallet

import android.content.Context
import android.content.Intent
import androidx.databinding.ObservableField
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.adapter.PullLoadingAdapter
import com.vtoken.application.model.EthTransaction
import com.vtoken.application.util.GsonUtil
import com.vtoken.application.util.okHttpRequest.*
import com.vtoken.application.view.activity.wallet.EthSendTransactionActivity
import com.vtoken.application.view.activity.wallet.EthTransactionDetailActivity
import com.vtoken.application.viewHolder.ViewHolder
import com.vtoken.application.viewModel.BaseViewModel
import etherum.web3j.Web3jManager
import vdsMain.BLOCK_CHAIN_TYPE
import vdsMain.model.Address
import vdsMain.tool.Util
import java.math.BigInteger

class ContractToken(val contractAddress:String,val gasLimit:BigInteger,val tokenDecimal:Int){

}

enum class ContractAddressToken(val address:String){
    ETH(""),
    USDT("0xdac17f958d2ee523a2206206994597c13d831ec7"),
    VDST("8011f78a9e2d4abc6568bd51ca1bb117618f280f");

    fun getContractToken():ContractToken{
        when(this){
            USDT-> return ContractToken(this.address, BigInteger("60000"),6)
            ETH-> return ContractToken(this.address,BigInteger("21000"),18)
            VDST-> return ContractToken(this.address,BigInteger("60000"),6)
        }
    }

    fun getTokenIcon():Int{
        when(this){
            USDT-> return R.drawable.icon_usdt
            ETH-> return R.drawable.icon_eth
            VDST-> return R.drawable.icon_vdst
        }
    }
}

class EthAddressDetailViewModel(context: Context): BaseViewModel(context) {


    var address: Address? = null
    var ethAddressString = ObservableField<String>("")
    var ethBalance:String="0"
    var balanceString = ObservableField<String>("0.0000")
    var contractToken:ContractAddressToken
    var isToken:Boolean
    var recyclerSimpleAdapter: PullLoadingAdapter<EthTransaction>
    val sumList= ArrayList<EthTransaction>()
    val receiveList= ArrayList<EthTransaction>()
    val sendList= ArrayList<EthTransaction>()
    var transferPullLoadingEvent:PullLoadingAdapter.PullLoadingEvent
    var searchType=0
    var offset=0
    var web3Event:Web3jManager.Web3jManagerEvent

    init {
        this.address = this.vCashCore.getAddressFromAddressString(intent.getStringExtra("address"), BLOCK_CHAIN_TYPE.VCASH)
        ethAddressString.set(this.address!!.getAddressString(BLOCK_CHAIN_TYPE.ETH))
        balanceString.set(intent.getStringExtra("balance"))
        ethBalance=intent.getStringExtra("ethBalance")
        contractToken=intent.getSerializableExtra("contractAddress") as ContractAddressToken
        isToken=intent.getBooleanExtra("isToken",true)
        transferPullLoadingEvent=object: PullLoadingAdapter.PullLoadingEvent{
            override fun onRequestLoadingData() {
                getEthTransactionList()
            }
        }
        this.recyclerSimpleAdapter = PullLoadingAdapter(R.layout.item_eth_transaction, BR.ethTransaction,R.layout.item_loading_more)
        recyclerSimpleAdapter.pageSize=5
        recyclerSimpleAdapter.pullLoadingEvent=transferPullLoadingEvent
        recyclerSimpleAdapter.itemClickEvent=object: PullLoadingAdapter.ItemClickEvent{
            override fun onClick(position: Int) {
                var transaction:EthTransaction?=null
                when(searchType){
                    0-> transaction=sumList.get(position)
                    1-> transaction=receiveList.get(position)
                    2-> transaction=sendList.get(position)
                }
                if(transaction!=null){
                    startActivity(Intent(this@EthAddressDetailViewModel.context,EthTransactionDetailActivity::class.java).putExtra("transaction",transaction))
                }
            }

            override fun onLongClick(viewHolder: ViewHolder, position: Int) {

            }

        }
        contractToken.getTokenIcon()
        web3Event= object:Web3jManager.Web3jManagerEvent(){
            override fun onBalanceChange(token: ContractAddressToken, ethAddress: String, balance: String
            ) {
                if(contractToken==token&&ethAddressString.get()==ethAddress){
                    balanceString.set(balance)
                }
            }
        }
        Web3jManager.getInstance().addWeb3jObserver(web3Event)
    }



    override fun onDestroy() {
        Web3jManager.getInstance().removeWeb3jObserver(web3Event)
    }

    fun getTransactionRecord(searchType:Int){
        this.searchType=searchType
        if(this.searchType==3){
            this@EthAddressDetailViewModel.recyclerSimpleAdapter.setDataList(emptyList())
            this@EthAddressDetailViewModel.recyclerSimpleAdapter.notifyDataSetChanged()
            return
        }
        getEthTransactionList()
    }

    fun getEthTransactionList(isInit:Boolean=false){
        if(recyclerSimpleAdapter.isLoading)
            return
        if(isInit){
            recyclerSimpleAdapter.curPage=1
            recyclerSimpleAdapter.hasMore=true
        }
        if(!recyclerSimpleAdapter.hasMore){
            reloadTransaction()
        }
        val params=HashMap<String,String>()
        params.put("module","account")
        if(isToken){
            params.put("action","tokentx")
            params.put("contractaddress",contractToken.address)
        }else{
            params.put("action","txlist")
        }
        params.put("address",ethAddressString.get()!!)
        params.put("startblock","0")
        params.put("endblock","latest")
        params.put("sort","desc")
        params.put("page",recyclerSimpleAdapter.curPage.toString())
        params.put("offset",recyclerSimpleAdapter.pageSize.toString())
        val request= CommonRequest.createEthExploerGetRequest(RequestParams(params))
        CommonOkHttpClient.sendCommonRequest(request,
            EthExploerJsonCallback(DisposeDataHandle(object : DisposeDataListener {
                override fun onSuccess(responseObj: Any?) {
                    val recordArray= JsonParser().parse(responseObj.toString()).asJsonObject.get("result").asJsonArray
                    val type=object:TypeToken<List<EthTransaction>>(){}.type
                    val transactionList=GsonUtil.getInstance().getGson().fromJson<List<EthTransaction>>(recordArray,type)
                    val tempAddress=ethAddressString.get()
                    if(isInit){
                        sendList.clear()
                        receiveList.clear()
                    }
                    offset=transactionList.size
                    for (transaction in transactionList){
                        if(transaction.from==tempAddress){
                            transaction.type=1
                            sendList.add(transaction)
                        }else{
                            receiveList.add(transaction)
                        }
                        sumList.add(transaction)
                    }
                    this@EthAddressDetailViewModel.recyclerSimpleAdapter.curPage++
                    reloadTransaction()
                    //this@EthAddressDetailViewModel.recyclerSimpleAdapter.notifyDataSetChanged()
                }
                override fun onFailure(exception: OkHttpException?) {
                    reloadTransaction()
                }
            }))
        )
    }

    fun reloadTransaction(){
        when(searchType){
            0->{
                this@EthAddressDetailViewModel.recyclerSimpleAdapter.setFullDataList(sumList,offset)
            }
            1->{
                this@EthAddressDetailViewModel.recyclerSimpleAdapter.setFullDataList(receiveList,offset)
            }
            2->{
                this@EthAddressDetailViewModel.recyclerSimpleAdapter.setFullDataList(sendList,offset) }
            }
    }


    fun showQrCodeDialog(){
        showQrCodeDialog(ethAddressString.get(),contractToken.name,"ETH公链地址",true,ethAddressString.get(),object :
            QrOptionDialogEvent {
            override fun onCopyClick() {}

            override fun onSaveClick() {}

            override fun mo41443c() {}
        },contractToken.getTokenIcon())
    }

    fun jumpEthTransfer(){
        val intent = Intent(this.context, EthSendTransactionActivity::class.java)
        intent.putExtra("address", address!!.getAddressString(BLOCK_CHAIN_TYPE.VCASH))
        intent.putExtra("isToken",isToken)
        intent.putExtra("contractAddress",contractToken)
        intent.putExtra("balance",balanceString.get())
        intent.putExtra("ethBalance",ethBalance)
        startActivity(intent)
    }
    fun copyaddressString(){
        Util.copyAndShowToast(ethAddressString.get())
    }
}