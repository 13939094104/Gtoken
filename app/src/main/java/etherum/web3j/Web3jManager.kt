package etherum.web3j

import android.annotation.SuppressLint
import android.os.Handler
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.orhanobut.logger.Logger
import com.vtoken.application.util.GsonUtil
import com.vtoken.application.util.okHttpRequest.*
import com.vtoken.application.viewModel.wallet.ContractAddressToken
import org.json.JSONObject
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.protocol.websocket.WebSocketClient
import vdsMain.peer.UserThread
import org.web3j.protocol.websocket.WebSocketService
import vdsMain.transaction.CAmount
import java.net.URI
import android.os.Environment
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.vtoken.application.ApplicationLoader
import java.io.*
import java.math.BigInteger


class Web3jManager {

    var web3j:Web3j?=null

    var webSocketService:WebSocketService?=null

    var web3jTread:Web3jThread?=null

    var handler:Handler?=null

    var transactionMap:HashMap<String, TransactionReceipt> = HashMap()

    var ethTokenBalanceMap: HashMap<Int,HashMap<String,String>> = HashMap<Int,HashMap<String,String>>()

    init {
        handler= Handler()
        loadEthTokenMapFromFile()
    }

    fun loadEthTokenMapFromFile(){

        var reader:BufferedReader?=null
        try {
            val file= FileReader(ApplicationLoader.applicationContext.getExternalFilesDir("").path+File.separator+"ethdata" +File.separator+"ethBalance.txt");
            var outStr=""
            reader = BufferedReader(file);
            outStr=reader.readText()

            val typeToken=object : TypeToken<HashMap<Int,HashMap<String,String>>>(){}.type
            val map:HashMap<Int,HashMap<String,String>>?=GsonBuilder().enableComplexMapKeySerialization().create().fromJson(outStr,typeToken)
            if(map!=null){
                ethTokenBalanceMap=map
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace();
        }finally {
            if (reader != null) {    //如果打印流不为空，则关闭打印流
                reader.close();
            }
        }
    }

    fun saveEthTokenMapToFile(){
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//判断是否存在SD卡
            return ;
        }
        val file= File(ApplicationLoader.applicationContext.getExternalFilesDir("").path+File.separator+"ethdata" +File.separator+"ethBalance.txt");
        if(!file.getParentFile().exists()){//判断父文件是否存在，如果不存在则创建
            file.getParentFile().mkdirs();
        }
        var out: PrintStream? =null;   //打印流
        try {
            out=PrintStream(FileOutputStream(file));  //实例化打印流对象
            val str=Gson().toJson(ethTokenBalanceMap)
            out.print(str);     //输出数据
        } catch (e: FileNotFoundException) {
            e.printStackTrace();
        }finally {
            if (out != null) {    //如果打印流不为空，则关闭打印流
                out.close();
            }
        }
    }

    var eventList=ArrayList<Web3jManagerEvent>()

    fun addWeb3jObserver(event:Web3jManagerEvent){
        if(!eventList.contains(event))
            eventList.add(event)
    }

    fun removeWeb3jObserver(event:Web3jManagerEvent){
        if(eventList.contains(event))
            eventList.remove(event)
    }

    interface Web3jManagerInterface {

        fun onBalanceChange(token: ContractAddressToken,ethAddress: String,balance:String)

        fun onNonceChange(ethAddress: String,nonce:String)

        fun onGasPriceChange(gasPrice:String)
    }

    open class Web3jManagerEvent:Web3jManagerInterface{

        override fun onGasPriceChange(gasPrice: String) {

        }

        override fun onNonceChange(ethAddress: String, nonce: String) {

        }

        override fun onBalanceChange(token: ContractAddressToken, ethAddress: String, balance: String
        ) {

        }

    }

    private fun notifyBalanceChange(token: ContractAddressToken,ethAddress: String,balance:String){
        for(event in eventList){
            event.onBalanceChange(token,ethAddress,balance)
        }
    }

    private fun notifyNonceChange(ethAddress: String,nonce:String){
        for(event in eventList){
            event.onNonceChange(ethAddress,nonce)
        }
    }

    private fun notifyGasPriceChange(gasPrice:String){
        for(event in eventList){
            event.onGasPriceChange(gasPrice)
        }
    }

    private constructor()


    inner class Web3jThread() : UserThread() {
        override fun threadStartEvent() {
            if(webSocketService==null){
                webSocketService=WebSocketService(WebSocketClient(URI("")),false)
                webSocketService!!.connect()
            }
            if(web3j==null){
                web3j= Web3j.build(webSocketService)
            }
//            val request=Request<Any,EthSubscribe>("eth_subscribe",
//                mutableListOf("newHeads") as List<Any>?,webSocketService,EthSubscribe::class.java)
//            val events=webSocketService!!.subscribe(request,"eth_unsubscribe",NewHeadsNotification::class.java)
//            val disposable=events.subscribe {
//                Logger.d(it)
//            }
//            val pendingrequest=Request<Any,EthSubscribe>("eth_subscribe",
//                mutableListOf("newPendingTransactions") as List<Any>?,webSocketService,EthSubscribe::class.java)
//            val pendingevents=webSocketService!!.subscribe(pendingrequest,"eth_unsubscribe",PendingTransactionNotification::class.java)
//            val pendingdisposable=pendingevents.subscribe {
//                Logger.d(it)
//            }
        }

        override fun threadEndEvent() {
            synchronized(this@Web3jManager) {
                if (this@Web3jManager.web3jTread === this) {
                    this@Web3jManager.web3jTread = null
                }
            }
        }

    }

    fun startWeb3jThread() {

        if (this.web3jTread == null || !this.web3jTread!!.getAtomicBoolean()) {
            this.web3jTread = Web3jThread()
            this.web3jTread!!.start()
        }
    }

    fun getTokenBalance(ethAddress: String,token: ContractAddressToken){
        val params=HashMap<String,String>()
        params.put("module","account")
        if(token==ContractAddressToken.ETH){
            params.put("action","balance")
        }else{
            params.put("action","tokenbalance")
            params.put("contractaddress",token.address)
        }
        params.put("address",ethAddress)
        params.put("tag","latest")
        val request= CommonRequest.createEthExploerGetRequest(RequestParams(params))
        CommonOkHttpClient.sendCommonRequest(request,EthExploerJsonCallback(DisposeDataHandle(object : DisposeDataListener{
            override fun onSuccess(responseObj: Any?) {
                val jsonResult= JSONObject(responseObj.toString())
                val balance=CAmount.toDecimalTokenString(jsonResult.getString("result"),token.getContractToken().tokenDecimal)
                if(ethTokenBalanceMap.containsKey(token.ordinal)){
                    val address2BalanceMap = ethTokenBalanceMap.get(token.ordinal)
                    if(address2BalanceMap!!.containsKey(ethAddress)){
                        address2BalanceMap[ethAddress]=balance
                    }else{
                        address2BalanceMap.put(ethAddress,balance)
                    }
                }else{
                    val address2BalanceMap = HashMap<String,String>()
                    address2BalanceMap.put(ethAddress,balance)
                    ethTokenBalanceMap.put(token.ordinal,address2BalanceMap)
                }
                notifyBalanceChange(token,ethAddress,balance)
                saveEthTokenMapToFile()
            }

            override fun onFailure(exception: OkHttpException?) {

            }
        })))
    }

    fun getTransactionCount(ethAddress: String){
        val params=HashMap<String,String>()
        params.put("module","proxy")
        params.put("action","eth_getTransactionCount")
        params.put("address",ethAddress)
        params.put("tag","latest")
        val request= CommonRequest.createEthExploerGetRequest(RequestParams(params))
        CommonOkHttpClient.sendCommonRequest(request,
            EthExploerJsonCallback(DisposeDataHandle(object : DisposeDataListener {
                override fun onSuccess(responseObj: Any?) {
                    val jsonObject= JsonParser().parse(responseObj.toString()).asJsonObject
                    val nonce=jsonObject.get("result").asString.substring(2)
                    notifyNonceChange(ethAddress,nonce)
                }

                override fun onFailure(exception: OkHttpException?) {

                }
            }))
        )
    }

    fun getGasPrice(){
        val params=HashMap<String,String>()
        params.put("module","proxy")
        params.put("action","eth_gasPrice")
        val request= CommonRequest.createEthExploerGetRequest(RequestParams(params))
        CommonOkHttpClient.sendCommonRequest(request,
            EthExploerJsonCallback(DisposeDataHandle(object : DisposeDataListener {
                override fun onSuccess(responseObj: Any?) {
                    val jsonObject= JsonParser().parse(responseObj.toString()).asJsonObject
                    val bit16price=jsonObject.get("result").asString.substring(2)
                    val gasPrice= BigInteger(bit16price,16)
                    notifyGasPriceChange(gasPrice.toString())
                }

                override fun onFailure(exception: OkHttpException?) {

                }
            }))
        )
    }

    @SuppressLint("NewApi")
    fun sendRawTransaction(ethAddress:String, hexData:String){
        Logger.d(web3j!!.ethGetBalance(ethAddress, DefaultBlockParameterName.LATEST).send().balance)
        Logger.d(web3j!!.ethBlockNumber().send().blockNumber)
       val ethSendTransaction=web3j!!.ethSendRawTransaction(hexData).send()
        if(!ethSendTransaction.hasError()){
            val transactionReceipt=web3j!!.ethGetTransactionReceipt(ethSendTransaction.transactionHash).send()
            if(!transactionReceipt.hasError()&&transactionReceipt.transactionReceipt.isPresent){
                transactionMap.put(ethAddress,transactionReceipt.transactionReceipt.get())
            }
        }
    }

    fun getTransactionReceipt(txhash:String){
        val params=HashMap<String,String>()
        params.put("module","proxy")
        params.put("action","eth_getTransactionReceipt")
        params.put("txhash",txhash)
        val request= CommonRequest.createEthExploerPostRequest(RequestParams(params))
        CommonOkHttpClient.sendCommonRequest(request,
            EthExploerJsonCallback(DisposeDataHandle(object : DisposeDataListener {
                override fun onSuccess(responseObj: Any?) {
                    val jsonObject= JsonParser().parse(responseObj.toString()).asJsonObject
                    if(jsonObject.has("error")){

                    }else{
                       val receipt=GsonUtil.getInstance().getGson().fromJson(jsonObject.get("result"),TransactionReceipt::class.java)
                        Logger.d(receipt)
                    }
                }

                override fun onFailure(exception: OkHttpException?) {

                }
            }))
        )
    }

    companion object{

        private var web3jManager:Web3jManager?=null

        fun getInstance():Web3jManager{
            if(web3jManager==null){
                web3jManager= Web3jManager()
            }
            return web3jManager!!
        }
    }
}