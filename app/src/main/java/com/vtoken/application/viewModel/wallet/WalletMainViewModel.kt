package com.vtoken.application.viewModel.wallet
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import bitcoin.UInt256
import com.google.common.base.Function
import com.google.common.collect.Lists
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.vtoken.application.ApplicationLoader
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.adapter.AddressSimpleAdapter
import com.vtoken.application.adapter.RecyclerSimpleAdapter
import com.vtoken.application.databinding.DialogOpenMultipleAddressBinding
import com.vtoken.application.util.PreferenceUtil
import com.vtoken.application.util.okHttpRequest.*
import com.vtoken.application.view.activity.wallet.ChooseMultipleAddressDetailActivity
import com.vtoken.application.view.activity.wallet.WalletAddressCreateActivity
import vdsMain.model.Address
import com.vtoken.application.view.fragment.WalletFragment
import com.vtoken.application.viewHolder.ViewHolder
import com.vtoken.application.viewModel.BaseViewModel
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter
import vdsMain.*
import vdsMain.block.BlockHeader
import vdsMain.block.BlockInfo
import vdsMain.block.ChainSyncStatus
import vdsMain.observer.BlockChainObserver
import vdsMain.tool.SPUtils
import vdsMain.tool.SharedPreferencesUtil
import vdsMain.tool.Util
import vdsMain.transaction.CAmount
import vdsMain.transaction.Transaction
import java.math.BigDecimal
import java.util.*

class WalletMainViewModel :BaseViewModel{

    constructor(context: Context):super(context){

    }

    constructor(context: Context,fragment: WalletFragment):super(context,fragment){
        SPUtils.getSPUtils().startSPEdit("config", context)
        if (SPUtils.getSPUtils().getBoolean("is_first", true)) {
            //m9362V()
            SPUtils.getSPUtils().putBoolean("is_first", false)
        }

        checkFixPub()
        intiRecyclerView(context)
        this.f11514T = true
        initData()
        initAddressListByAddressFilter()
        this.f11514T = false
        initPopListLayout()
        //checkVXD()
    }

    init {
        blockChainObserver=object: BlockChainObserver{
            override fun onCurBlockNoChange(
                blockChainType: BLOCK_CHAIN_TYPE?,
                i: Int,
                cur: Int,
                max: Int
            ) {

            }

            override fun onTransactionsInvalid(
                igVar: BLOCK_CHAIN_TYPE?,
                list: MutableList<BlockHeader>?,
                list2: MutableList<Transaction>?,
                hashMap: HashMap<CTxDestination, Address>?
            ) {

            }

            override fun onUpdateBlock(
                blockChainType: BLOCK_CHAIN_TYPE?,
                blockInfo: BlockInfo?,
                list: MutableList<Transaction>?,
                hashMap: HashMap<CTxDestination, Address>?
            ) {

            }

            override fun onBlockNoUpdate(
                block_chain_type: BLOCK_CHAIN_TYPE?,
                chainSyncStatus: ChainSyncStatus?,
                blockHeader: BlockHeader?,
                maxBlockNo: Int
            ) {
                if(chainSyncStatus== ChainSyncStatus.SYNCHING&&maxBlockNo==0){
                    showErrorBar.set(false)
                }
            }

        };
        vCashCore.addObserver(blockChainObserver);
    }

    var  addressFilter = AddressFilter.GENERAL

    var showErrorBar = ObservableField<Boolean>(false)
    var errorBarMsg = ObservableField("")

    //f11517x
    lateinit var addressSimpleAdapter: AddressSimpleAdapter<com.vtoken.application.model.Address>

    //f11501G
    var blockChainTypeName = ObservableField<String>(Constants.vcashName)

    var f11519z = ObservableField<String>()

    //f11497C
    var lockAmountString = ObservableField<String>()

    //f11495A
    var sumAvailableString = ObservableField<String>()

    //f11496B
    var unConfirmedBalanceString = ObservableField<String>()

    //f11503I
    var hdAddress = ObservableField<Address>()

    var hdAddressLabel = ObservableField<String>()

    //f11499E
    var hdAddressString = ObservableField<String>()

    var isHdAccountVxd=ObservableField<Boolean>(false)

    //f11498D
    var hdAccountBalanceString = ObservableField<String>()

    var f11505K = ObservableField(java.lang.Boolean.valueOf(true))

    var f11506L = ObservableField(java.lang.Boolean.valueOf(true))

    var f11518y = ObservableField("")

    //f11512R
    lateinit var addressEmptyViewModel: AddressEmptyViewModel

    //f11502H
    var addressObservableList: ObservableList<com.vtoken.application.model.Address> = ObservableArrayList()

    //f11515U
    private var sumAvailable: Long = 0

    var f11513S = false

    private var f11514T = true

    var blockChainObserver :BlockChainObserver

    fun showLeftMenu(){
        (this.fragment as WalletFragment).showLeftMenu()
    }

    override fun onDestroy() {
        vCashCore.removeObserver(blockChainObserver);
    }

    fun checkFixPub(){
        //SharedPreferencesUtil.getSharedPreferencesUtil().putBooleanValue("needFixFullPub" + vCashCore.getAppWalletPath(),true,ApplicationLoader.applicationContext)
        val needFix=SharedPreferencesUtil.getSharedPreferencesUtil().getBooleanValue("needFixFullPub" + vCashCore.getAppWalletPath(),false,ApplicationLoader.applicationContext);
        if(needFix){
            val dataBinding: DialogOpenMultipleAddressBinding = DataBindingUtil.inflate(
                LayoutInflater.from(
                    ApplicationLoader.getSingleApplicationContext()), R.layout.dialog_open_multiple_address,null,false)
            val viewModel= OpenMultipleAddressDialogViewModel(context,this)
            dataBinding.setVariable(BR.openMultipleAddressDialogViewModel,viewModel)
            setConfirmDialogView(dataBinding.root)
            showConfirmDialog("fixPub",false)
        }
    }

    //mo42249a
    fun jumpToAddressDetail(address: com.vtoken.application.model.Address, vararg iArr: Int) {
        val i: Int
        //val intent = Intent(this.context, WalletAddressDetailActivity::class.java)
        val intent = Intent(this.context, ChooseMultipleAddressDetailActivity::class.java)
        var filterType = 3
        intent.putExtra("address", address.vaddress)
        intent.putExtra("coin_type", this.blockChainTypeName.get() as String)
        if (iArr.size > 0) {
            i = iArr[0]
        } else {
            when (this.addressFilter) {
                AddressFilter.GENERAL -> filterType = 3
                else -> filterType = 3
            }
            i = if (!address.isHdAddress) filterType else 4
        }
        intent.putExtra("jumpType", i)
        startActivity(intent)
    }

    //m9367a
    private fun intiRecyclerView(context: Context) {
        this.addressSimpleAdapter = AddressSimpleAdapter<com.vtoken.application.model.Address>(R.layout.item_wallet_general_address, BR.generalAddress, R.layout.item_address_empty_with_add)
        this.addressEmptyViewModel = AddressEmptyViewModel(context)
        this.addressEmptyViewModel.mo42238a(this.blockChainTypeName.get() as String)
        this.addressSimpleAdapter.setEmptyBR(BR.emptyAddressModel)
        this.addressSimpleAdapter.setEmptyModel(this.addressEmptyViewModel as Any)
        //TODO 拖拽换位子
        //mo41355a(this.addressSimpleAdapter)
        this.addressSimpleAdapter.setOnItemRightIconClickListener(object : AddressSimpleAdapter.C2555b {
            /* renamed from: a */
            override fun mo38470a(i: Int, viewHolder: ViewHolder) {
                this@WalletMainViewModel.f11513S = true
                Util.m7041b(context)
                this@WalletMainViewModel.f9475h.startDrag(viewHolder)
            }
        })
        this.addressSimpleAdapter.setOnItemClickListener(object :
            AddressSimpleAdapter.AddressItemClickEvent {
            /* renamed from: a */
            override fun onItemClick(i: Int) {
                val walletMainViewModel = this@WalletMainViewModel
                val address=walletMainViewModel.addressSimpleAdapter.getDataList().get(i) as com.vtoken.application.model.Address;
                if(address.isHdAddress)
                {
                    jumpToHDAddressDetail()
                }else{
                    walletMainViewModel.jumpToAddressDetail(address, *IntArray(0))
                }

            }

            /* renamed from: a */
            override fun onLongClick(viewHolder: ViewHolder, i: Int) {
//                this@WalletMainViewModel.f11513S = true
//                Util.m7041b(context)
//                this@WalletMainViewModel.f9475h.startDrag(viewHolder)
                val cm=context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val address=this@WalletMainViewModel.addressSimpleAdapter.getDataList().get(i) as com.vtoken.application.model.Address
                val addressString=address.vaddress
                val clipData=ClipData.newPlainText("address",addressString)
                cm.primaryClip=clipData
            }
        })
        this.addressSimpleAdapter.setBindEvent(object :
            RecyclerSimpleAdapter.RecyclerSimpleBindEvent {
            override fun onBind(viewHolder: ViewHolder, i: Int) {
                viewHolder.getViewDataBinding().setVariable(BR.walletMainModel, this@WalletMainViewModel)
                val imgAddView = viewHolder.getViewDataBinding().getRoot().findViewById<ImageView>(R.id.img_add)
                if (imgAddView != null) {
                    imgAddView.setOnClickListener(View.OnClickListener {
                        val intent = Intent(context, WalletAddressCreateActivity::class.java)
                        intent.putExtra("only_general", true)
                        this@WalletMainViewModel.startActivity(intent)
                    })
                }
            }
        } as RecyclerSimpleAdapter.RecyclerSimpleBindEvent)
    }

    fun jumpCreateAddressActivity(){
        val intent = Intent(context, WalletAddressCreateActivity::class.java)
        intent.putExtra("only_general", true)
        startActivity(intent)
    }

    //mo39929b
    fun initData() {
        this.blockChainTypeName.set(this.block_chain_type.name)
        val hdAccountList = this.vCashCore.getHDAccountList()
        if (hdAccountList == null || hdAccountList.size == 0) {
            Log.e("HotWalletViewModel", "initData: but HDAccounts is empty")
            return
        }
        var includeAnonymousAddress = false
        val hdAccount = hdAccountList.get(0)
        this.hdAddress.set(hdAccount.getAddrAddress())
        if (this.hdAddress.get() == null) {
            Log.e("HotWalletViewModel", "initData: but current hd address is null")
            return
        }
        this.isHdAccountVxd.set(hdAddress.get()!!.isAppingVxd)
        hdAddressLabel.set(hdAddress.get()!!.label)
        this.hdAddressString.set((this.hdAddress.get() as Address).getAddressString(this.block_chain_type))
        this.hdAccountBalanceString.set(
            Util.m7025a(
                java.lang.Double.toString(
                    CAmount.toDecimalSatoshiDouble(
                        java.lang.Long.valueOf(
                            hdAccount.getSumBalance(
                                false,
                                this.block_chain_type
                            )
                        )
                    )
                ), this.block_chain_type.name
            )
        )
        if (this.fragment is WalletFragment) {
            (this.fragment as WalletFragment).mo39872b(this.hdAddress.get() as Address)
        }
        this.f11518y.set(Util.m7039b(this.block_chain_type.name))
        val unConfirmedBalance = this.vCashCore.getAllAddressUnConfirmedBalance(this.block_chain_type === BLOCK_CHAIN_TYPE.VCASH, this.block_chain_type).getValue()
        if (unConfirmedBalance < 0) {
            this.unConfirmedBalanceString.set("0")
        } else {
            this.unConfirmedBalanceString.set(
                Util.m7023a(
                    CAmount.toDecimalSatoshiDouble(java.lang.Long.valueOf(unConfirmedBalance)).toString(),
                    Util.getPointNumber(this.blockChainTypeName.get() as String)
                )
            )
        }
        getSumAvailableStringAndFormat()
        val vCashCore1 = this.vCashCore
        if (this.block_chain_type === BLOCK_CHAIN_TYPE.VCASH) {
            includeAnonymousAddress = true
        }
        this.lockAmountString.set(
            Util.m7017a(CAmount.toDecimalSatoshiDouble(vCashCore1.getSumLockBalance(includeAnonymousAddress,this.block_chain_type) - unConfirmedBalance),
                Util.getPointNumber(this.blockChainTypeName.get())
            )
        )
    }


    fun jumpToHDAddressDetail(){
        val address=com.vtoken.application.model.Address()
        address.initByHdRealAddress(hdAddress.get(),block_chain_type)
        jumpToAddressDetail(address,4)
    }


    //mo39924a
    fun showPopup() {
        showPopupAsDropDown((this.fragment as WalletFragment).getMoreImageButton() as View)
    }

    //mo42244T
    fun getSumAvailableStringAndFormat() {
        when (this.block_chain_type) {
            BLOCK_CHAIN_TYPE.VCASH -> {
                this.f11505K.set(java.lang.Boolean.valueOf(true))
                if ((this.f11506L.get() as Boolean)) {
                    this.sumAvailable = this.vCashCore.getAllAddressAvailBalance(true, BLOCK_CHAIN_TYPE.VCASH).getValue()
                    this.sumAvailableString.set(mo42245a(CAmount.toDecimalSatoshiDouble(java.lang.Long.valueOf(this.sumAvailable))))
                    Log.d("自来也余额",mo42245a(CAmount.toDecimalSatoshiDouble(java.lang.Long.valueOf(this.sumAvailable))))
                    PreferenceUtil.commitString("money",mo42245a(CAmount.toDecimalSatoshiDouble(java.lang.Long.valueOf(this.sumAvailable))))
                    this.f11519z.set("")
                    return
                }
                    //设置单位小数
//                else if (SharedPreferencesUtil.getSharedPreferencesUtil().getBooleanValue("isVCustom", false, this.f9472e)) {
//                    this.f11515U = this.vCashCore.getAllAddressAvailBalance(true, BLOCK_CHAIN_TYPE.VCASH).mo18938b()
//                    this.f11507M = SharedPreferencesUtil.getSharedPreferencesUtil().mo41244b(this.f9472e, "vollarSetUnitModel") as VollarSetUnitModel
//                    val a = SharedPreferencesUtil.getSharedPreferencesUtil().mo41236a("vollar", "0", this.f9472e)
//                    if (java.lang.Double.parseDouble(a) > 0.0) {
//                        this.f11495A.set(
//                            mo42245a(
//                                java.lang.Double.valueOf(
//                                    java.lang.Double.parseDouble(a) * CAmount.m10851a(
//                                        java.lang.Long.valueOf(this.f11515U)
//                                    ).doubleValue()
//                                )
//                            )
//                        )
//                        this.f11519z.set("")
//                        m9369a(this.f11507M.coinType.get() as String)
//                        return
//                    }
//                    return
//                }
                else {
                    return
                }
            }
            else -> return
        }
    }

    fun mo42245a(value: Double): String {
        if (value - 100000.0 < 0.0) {
            return Util.m7019a(value.toDouble(), this.blockChainTypeName.get() as String)
        }
        val divide = BigDecimal(value.toDouble()).divide(BigDecimal("1000"))
        val sb = StringBuilder()
        sb.append(java.lang.Long.toString(divide.toLong()))
        sb.append("k")
        val sb2 = sb.toString()
        if (sb2.length <= 8) {
            return sb2
        }
        val divide2 = divide.divide(BigDecimal("1000"))
        val sb3 = StringBuilder()
        sb3.append(java.lang.Long.toString(divide2.toLong()))
        sb3.append("M")
        return sb3.toString()
    }
    //mo42239O
    fun initAddressListByAddressFilter() {
        if (!this.f11513S) {
            this.addressObservableList.clear()
            //添加hd地址
            val hdAddressModel=com.vtoken.application.model.Address();
            hdAddressModel.initByHdRealAddress(hdAddress.get(),BLOCK_CHAIN_TYPE.VCASH);
            this.addressObservableList.add(hdAddressModel);
            Log.d("自来也hd地址",hdAddressModel.address);
            PreferenceUtil.commitString("username",hdAddressModel.address)//自来也
            //
            val identityList= Lists.transform(this.vCashCore.getAddressListByFilter(AddressFilter.INDENTITY)!!,object:Function<Address,com.vtoken.application.model.Address>{
                override fun apply(input: Address?): com.vtoken.application.model.Address? {
                    val tempAddress=com.vtoken.application.model.Address();
                    tempAddress.initByRealAddress(input!!,BLOCK_CHAIN_TYPE.VCASH);
                    return tempAddress;
                }
            })
            val generalList= Lists.transform(this.vCashCore.getAddressListByFilter(AddressFilter.GENERAL)!!,object:Function<Address,com.vtoken.application.model.Address>{
                override fun apply(input: Address?): com.vtoken.application.model.Address? {
                    val tempAddress=com.vtoken.application.model.Address();
                    tempAddress.initByRealAddress(input!!,BLOCK_CHAIN_TYPE.VCASH);
                    return tempAddress;
                }
            })
            this.addressObservableList.addAll(identityList)
            this.addressObservableList.addAll(generalList)
            //this.addressObservableList.addAll(this.vCashCore.getAddressListByFilter(this.addressFilter)!!)
            if (this.addressObservableList.size > 0) {
                Collections.sort(this.addressObservableList, Comparator<com.vtoken.application.model.Address> { address1, address2 ->
                    if(address1.isHdAddress)
                    {
                        return@Comparator -1;
                    }else if(address2.isHdAddress){
                        return@Comparator 1;
                    }
                    address1.getAddressOrderIndex() - address2.getAddressOrderIndex()
                })
            }
            setAdapterDataList()
        }
    }

    //m9363W
    private fun setAdapterDataList() {
        this.addressSimpleAdapter.setDataList(this.addressObservableList as List<com.vtoken.application.model.Address>)
        this.addressSimpleAdapter.notifyDataSetChanged()
        if (this.f11514T) {
            //startRecyclerAnimation()
            checkAddressBalanceAndRefreshAddress()
        }
    }

    private fun checkVXD(){
        val jsonArr=JsonArray()
        jsonArr.add(hdAddressString.get())
        for(address in addressObservableList){
            jsonArr.add(address.vaddress)
        }
        val params=HashMap<String,String>()
        params.put("addresses",jsonArr.toString())
        val request= CommonRequest.createPostRequestByRoute("vxd/checkVXD", RequestParams(params))
        CommonOkHttpClient.sendRequest(request, CommonJsonCallback(DisposeDataHandle(object : DisposeDataListener {
            override fun onFailure(exception: OkHttpException?) {

            }

            override fun onSuccess(responseObj: Any?) {
                val resultArray= JsonParser().parse(responseObj as String).asJsonObject.get("data").asJsonArray
                for(result in resultArray){
                    val status=result.asJsonObject.get("status").asInt
                    if(status==1){
                       val realAddress = vCashCore.getAddressFromAddressString(result.asJsonObject.get("address").asString, BLOCK_CHAIN_TYPE.VCASH)
                        if(realAddress!=null){
                            vCashCore.addUnConfirmVxdTransaction(UInt256(),realAddress)
                        }
                    }else if(status==-1){
                        val realAddress = vCashCore.getAddressFromAddressString(result.asJsonObject.get("address").asString, BLOCK_CHAIN_TYPE.VCASH)
                        if(realAddress!=null){
                            vCashCore.clearVxd(realAddress)
                        }
                    }
                }
                LocalBroadcastManager.getInstance(ApplicationLoader.getSingleApplicationContext()).sendBroadcast(Intent("refresh_address"))
            }
        })))
    }

    //m9364X
    private fun checkAddressBalanceAndRefreshAddress() {
        Observable.create(object : ObservableOnSubscribe<HashMap<Int, List<Address>>> {
            override fun subscribe(observableEmitter: ObservableEmitter<HashMap<Int, List<Address>>>) {
                val arrayList = ArrayList<ComplexBitcoinAddress>()
                this@WalletMainViewModel.vCashCore.mo43789a(arrayList as List<ComplexBitcoinAddress>)
                val hashMap = HashMap<Int,List<Address>>()
                if (!arrayList.isEmpty()) {
                    for (complexBitcoinAddress in arrayList) {
                        val hashSet = complexBitcoinAddress.mo42494d()
                        val it = hashSet.iterator()
                        var showCount = 0
                        var hasBalanceAddressCount = 0
                        while (it.hasNext()) {
                            val address = it.next() as Address
                            if (!address.getHide() && !address.isFlagIndentity() && !address.isRecycleFlag()) {
                                showCount++
                            }
                            if (address.getSumBalance(BLOCK_CHAIN_TYPE.VCASH) != 0L) {
                                hasBalanceAddressCount++
                            }
                        }
                        if (showCount > 1) {
                            if (hasBalanceAddressCount == 0) {
                                Log.i("HotWalletViewModel", "correctError: showCount>1 && hasBalanceAddressCount==0")
                                val it2 = hashSet.iterator()
                                while (it2.hasNext()) {
                                    val address = it2.next() as Address
                                    if (address.getAddressType() === AddressType.GENERAL) {
                                        this@WalletMainViewModel.vCashCore.updateAddressIsHideAndOperateSpareArr(address, false)
                                    } else {
                                        this@WalletMainViewModel.vCashCore.updateAddressIsHideAndOperateSpareArr(address, true)
                                    }
                                }
                            } else if (hasBalanceAddressCount == 1) {
                                Log.i("HotWalletViewModel", "correctError: showCount>1 && hasBalanceAddressCount==1")
                                val it3 = hashSet.iterator()
                                while (it3.hasNext()) {
                                    val address = it3.next() as Address
                                    if (address.getSumBalance(BLOCK_CHAIN_TYPE.VCASH) == 0L) {
                                        this@WalletMainViewModel.vCashCore.updateAddressIsHideAndOperateSpareArr(address, true)
                                    } else {
                                        this@WalletMainViewModel.vCashCore.updateAddressIsHideAndOperateSpareArr(address, false)
                                    }
                                }
                            } else {
                                Log.i("HotWalletViewModel", "correctError: showCount>1 && hasBalanceAddressCount>1")
                                val hasBalanceAddressList = ArrayList<Address>()
                                val it4 = hashSet.iterator()
                                while (it4.hasNext()) {
                                    val address = it4.next() as Address
                                    if (address.getSumBalance(BLOCK_CHAIN_TYPE.VCASH) != 0L) {
                                        hasBalanceAddressList.add(address)
                                    }
                                }
                                hashMap.put(hashMap.size, hasBalanceAddressList)
                            }
                        }
                    }
                }
                observableEmitter.onNext(hashMap)
            }
        }).compose(bindDestoryEvent()).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : io.reactivex.Observer<HashMap<Int, List<Address>>> {
                override fun onComplete() {}

                override fun onError(th: Throwable) {}

                override fun onSubscribe(disposable: Disposable) {}

                /* renamed from: a */
                override fun onNext(hashMap: HashMap<Int, List<Address>>) {
                    var str: String
                    LocalBroadcastManager.getInstance(ApplicationLoader.getSingleApplicationContext())
                        .sendBroadcast(Intent("refresh_address"))
                    }
                })
    }
}