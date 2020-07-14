package com.vtoken.application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.orhanobut.logger.Logger
import vdsMain.AddressFilter
import vdsMain.BLOCK_CHAIN_TYPE
import vdsMain.CTxDestination
import vdsMain.block.BlockHeader
import vdsMain.block.BlockInfo
import vdsMain.block.ChainSyncStatus
import vdsMain.model.Address
import vdsMain.observer.BlockChainObserver
import vdsMain.tool.Util
import vdsMain.transaction.CAmount
import vdsMain.transaction.Transaction
import vdsMain.wallet.WalletType
import java.util.HashMap

class MainActivity : AppCompatActivity() {

    lateinit var curBlock:TextView

    lateinit var maxBlock:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        curBlock=findViewById(R.id.curBlock)
        maxBlock=findViewById(R.id.maxBlock)
        //initNewWallet()
        //initNewWalletFromWords()

        getHDAddress()
        var blockChainObserver= object : BlockChainObserver{
            override fun onCurBlockNoChange(blockChainType: BLOCK_CHAIN_TYPE?, i: Int, cur: Int, max: Int) {

            }

            override fun onBlockNoUpdate(block_chain_type: BLOCK_CHAIN_TYPE?, chainSyncStatus: ChainSyncStatus?, blockHeader: BlockHeader?, maxBlockNo: Int) {
                runOnUiThread {
                    curBlock.text= blockHeader?.blockNo.toString()
                    maxBlock.text=maxBlockNo.toString()
                }

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
                runOnUiThread {
                    curBlock.text = blockInfo?.blockNo.toString()
                }
            }

        }
        ApplicationLoader.getVcashCore().addObserver(blockChainObserver);
    }
    fun getHDAddress(){
        var list= ApplicationLoader.getVcashCore().getHDAccountList();
        if(list!=null)
        {
            var hd=list[0]
            var address=hd.getAddrAddress()
            if(address!=null){
                var addressString=address.getAddressString(BLOCK_CHAIN_TYPE.VCASH)
                Logger.d(addressString)
                if(!address.isAccount()||address.isFlagIndentity){
                    val d = address.getSumBalance(BLOCK_CHAIN_TYPE.VCASH)
                    var balance=Util.m7017a(CAmount.toDecimalSatoshiDouble(java.lang.Long.valueOf(d)),Util.m7014a(BLOCK_CHAIN_TYPE.VCASH))
                    Logger.d(balance)
                    val e = d - address.getAvailableBalance(BLOCK_CHAIN_TYPE.VCASH)
                    Logger.d(e)
                } else {
                    val account = ApplicationLoader.getVcashCore().getAccount(address.getAccount())
                    val balance = account!!.getBalance(BLOCK_CHAIN_TYPE.VCASH)
                    val a3 = account.getSumBalance(false, BLOCK_CHAIN_TYPE.VCASH)
                    var balanceString= Util.m7019a(CAmount.toDecimalSatoshiDouble(java.lang.Long.valueOf(balance)), BLOCK_CHAIN_TYPE.VCASH.name)
                    Logger.d(balanceString)
                    val j = balance - a3
                    if (j > 0) {
                        Util.m7019a(CAmount.toDecimalSatoshiDouble(java.lang.Long.valueOf(j)),BLOCK_CHAIN_TYPE.VCASH.name)
                    }
                    //val transactionList = ApplicationLoader.getVcashCore().mo43750a(address.getAccount(), TransactionConfirmType.ALL, BLOCK_CHAIN_TYPE.VCASH)
                }
            }
        }
        ApplicationLoader.getVcashCore().checkAndStartPeerManagerNetwork();
        var generalAddressList= ApplicationLoader.getVcashCore().getAddressListByFilter(AddressFilter.GENERAL);
        if (generalAddressList != null) {
            Logger.d(generalAddressList)
            val arrayList = ArrayList<Address>()
            for(generalAddress in generalAddressList){
                Logger.d(generalAddress.getAddressString(BLOCK_CHAIN_TYPE.VCASH))
                if(!generalAddress.hide&&!generalAddress.isWatchedFlag()&&!generalAddress.isRecycleFlag){
                    arrayList.add(generalAddress)
                }
            }
        }
    }
}
