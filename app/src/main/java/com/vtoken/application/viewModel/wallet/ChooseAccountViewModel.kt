package com.vtoken.application.viewModel.wallet

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import android.os.Process
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.vcwallet.core.part.PartWallet
import com.vtoken.vdsecology.vcash.InitInfo
import com.vtoken.application.ApplicationLoader
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.adapter.RecyclerSimpleAdapter
import com.vtoken.application.constant.Constant
import com.vtoken.application.service.SyncService
import com.vtoken.application.util.PartLoader
import com.vtoken.application.view.activity.LoadDataActivity
import com.vtoken.application.view.activity.ValidatePwdActivity
import com.vtoken.application.view.activity.create.WalletCreateFirstActivity
import com.vtoken.application.viewHolder.ViewHolder
import com.vtoken.application.viewModel.BaseViewModel
import com.vtoken.application.viewModel.PromptDialogViewModel
import generic.exceptions.PasswordException
import vcash.exception.SpaceNotEnoughException
import vdsMain.BLOCK_CHAIN_TYPE
import vdsMain.Constants
import vdsMain.FileUtils
import vdsMain.config.ConfigFile
import vdsMain.observer.CoreEventObserver
import vdsMain.tool.DeviceUtil
import vdsMain.tool.PeerUtils
import vdsMain.tool.SPUtils
import vdsMain.wallet.WalletType
import java.io.File
import java.io.IOException
import java.util.ArrayList

class ChooseAccountViewModel(context: Context) : BaseViewModel(context) {

    val walletBgNormalId=R.drawable.wallet_bg_normal

    val imageNormalId=R.drawable.icon_weixianzhong

    val imageSelectedlId=R.drawable.icon_choosed

    var isTransfer = ObservableBoolean()

    //915 f10121B
    var hasSelected = ObservableBoolean(false)

    var f10122C = ObservableBoolean(false)

    //f10123D
    var isEditStatus = ObservableBoolean(false)

    //915 f10132y
    var selectedIndex = ObservableInt(-1)

    //915 f10131x
    lateinit var simpleAdapter: RecyclerSimpleAdapter<PartWallet>

    //915 f10125F
    lateinit var partWalletList: ArrayList<PartWallet>

    private var f10128I: String? = null

    internal var firstInit = true

    //f10129J
    private var isDeleteCurWallet: Boolean = false

    //f10130K
    private var hasStopCurWallet: Boolean = false

    //915 f10127H
    var selectedImageView: ImageView? = null

    //915 f10126G
    var selectedWalletView: View? = null

    var lastSelectIndex=0

    internal var f10133z: CoreEventObserver? = null

    init {
        initData()
    }

    private fun initData() {
        val isTransfer = getIntent().getBooleanExtra("is_transfer", false)
        this.isTransfer.set(isTransfer)
        this.simpleAdapter = RecyclerSimpleAdapter(R.layout.item_wallet, BR.partWallet)
        val partWalletList = getIntent().getParcelableArrayListExtra<PartWallet>("part_wallet")
        this.partWalletList = ArrayList<PartWallet>()
        if (isTransfer) {
            //this.f9916g.setTitle(getStringRescourcesByResName("transfer_to"))
            this.f10128I = getIntent().getStringExtra("pwd")
            val it = partWalletList.iterator()
            while (it.hasNext()) {
                val partWallet = it.next() as PartWallet
                if (!TextUtils.equals(this.vCashCore.getAppWalletPath(), partWallet.getPath())) {
                    this.partWalletList.add(partWallet)
                }
            }
        } else {
            val spUtils = SPUtils.getSPUtils()
            spUtils.startSPEdit("config_vds", ApplicationLoader.getSingleApplicationContext() as Context)
            val lastPath = spUtils.getStringNoDefault("last_path")
            val sb = StringBuilder()
            sb.append("last path ")
            sb.append(lastPath)
            Log.i("ChooseWalletViewModel", sb.toString())
            val walletIterator = partWalletList.iterator()
            while (walletIterator.hasNext()) {
                val tempWallet = walletIterator.next() as PartWallet
                if (this.vCashCore != null && this.vCashCore.isInitSuccess() && TextUtils.equals(
                        this.vCashCore.getAppWalletPath(),
                        tempWallet.getPath()
                    )
                ) {
                    tempWallet.setIsHideAllAmount(!this.vCashCore.getWalletByChainType().getIsHideTotalAmount())
                    tempWallet.setLabel(this.vCashCore.getWalletByChainType(BLOCK_CHAIN_TYPE.VCASH).getLabel())
                }

                this.partWalletList.add(tempWallet)
                this.simpleAdapter.setDataList(this.partWalletList)
                if (TextUtils.equals(tempWallet.getPath(), lastPath)) {
                    this.hasSelected.set(true)
                    lastSelectIndex=partWalletList.indexOf(tempWallet)
                    this.selectedIndex.set(lastSelectIndex)
                    simpleAdapter.dataList.get(lastSelectIndex).isSelect=true
                    simpleAdapter.notifyItemChanged(lastSelectIndex)
                }
            }
            PartLoader.getSingleInstance(this.context).setPartWalletList(partWalletList)
            this.simpleAdapter.setRecyclerBindEvent(object : RecyclerSimpleAdapter.RecyclerSimpleBindEvent {
                override fun onBind(viewHolder: ViewHolder, position: Int) {
                    val walletList = this@ChooseAccountViewModel.simpleAdapter.getDataList()
                    if (walletList != null && !walletList.isEmpty()) {
                        val top=viewHolder.itemView.findViewById<LinearLayout>(R.id.top).layoutParams as LinearLayout.LayoutParams
                        val margin=DeviceUtil.dp2px(context,16f)
                        top.width=DeviceUtil.getActivityWidthPixels(activity)-2*margin
                        top.setMargins(margin,0,margin,0)
                        val partWallet = walletList.get(position) as PartWallet
                        if (firstInit&&!TextUtils.isEmpty(lastPath) && TextUtils.equals(partWallet.getPath(), lastPath)
                        ) {
                            firstInit=false
                            val bem = this@ChooseAccountViewModel
                            bem.hasSelected.set(true)
                            this@ChooseAccountViewModel.selectedIndex.set(position)
                        }
                    }
                }
            })
        }
        val partWalletIterator = this.partWalletList.iterator()
        while (partWalletIterator.hasNext()) {
            val tempWallet = partWalletIterator.next() as PartWallet
            tempWallet.mo18998c(false)
            tempWallet.setIsEdit(false)
        }
        this.simpleAdapter.setDataList(this.partWalletList)
        this.simpleAdapter.setSelcetEvent(object :
            RecyclerSimpleAdapter.RecyclerSimpleAdapterSelectEvent {
            /* renamed from: b */
            override fun onLongClick(i: Int, view: View) {}

            /* renamed from: a */
            override fun onClick(i: Int, view: View) {
                if(selectedIndex.get()!=-1){
                    simpleAdapter.dataList.get(selectedIndex.get()).isSelect=false
                }

                simpleAdapter.dataList.get(i).isSelect=true
                simpleAdapter.notifyDataSetChanged()
                selectedIndex.set(i)
//                if (this@ChooseAccountViewModel.isEditStatus.get()) {
//                    this@ChooseAccountViewModel.f10122C.set(true)
//                    if (this@ChooseAccountViewModel.selectedImageView != null) {
//                        this@ChooseAccountViewModel.selectedImageView!!.setImageResource(imageNormalId)
//                    }
//                    val imageView = view.findViewById<View>(R.id.check) as ImageView
//                    imageView.setImageResource(imageSelectedlId)
//                    this@ChooseAccountViewModel.selectedImageView = imageView
//                    return
//                }
//                if (this@ChooseAccountViewModel.selectedWalletView != null) {
//                    this@ChooseAccountViewModel.selectedWalletView!!.setBackgroundResource(walletBgNormalId)
//                }
//                val findViewById = view.findViewById<View>(R.id.top)
//                findViewById.setBackgroundResource(R.drawable.wallet_bg_selected)
//                this@ChooseAccountViewModel.selectedWalletView = findViewById
                this@ChooseAccountViewModel.hasSelected.set(true)
            }
        })
    }

    //915 mo41722Q
    fun checkSwitchAndBack() {
        if (!intent.getBooleanExtra("is_switch", false)) {
            (this.context as Activity).finish()
        } else if (!this.isDeleteCurWallet || !this.hasStopCurWallet) {
            checkWalletTypeAndEnterMain()
        } else {
            ApplicationLoader.getSingleApplicationContext().stopApp()
        }
    }

    //915 mo40044a
    fun showEnterWalletConfirm() {
        val sb = StringBuilder()
        sb.append("selected: ")
        sb.append(this.selectedIndex.get())
        Log.i("ChooseWalletViewModel", sb.toString())
        if (this.selectedIndex.get() >= 0 && this.selectedIndex.get() < this.partWalletList.size) {
            if (this.isTransfer.get()) {
//                startActivityForResult(
//                    Intent(
//                        this.context,
//                        InputPwdNoCheckActivity::class.java
//                    ).putExtra(NotificationCompat.CATEGORY_MESSAGE, mo41568c(R.string.input_select_wallet_pwd as Int)),
//                    PointerIconCompat.TYPE_NO_DROP as Int
//                )
            } else {
                val partWallet = this.partWalletList.get(this.selectedIndex.get()) as PartWallet
                val dialogViewModel = PromptDialogViewModel(this.context)
                dialogViewModel.setSpanned(
                    SpannableString(
                        String.format(
                            getStringRescourcesByResName("prompt_enter_account"), (if (TextUtils.isEmpty(partWallet.getLabel())) " " else partWallet.getLabel())
                        )
                    ) as Spanned
                )
                createConfirmView(dialogViewModel, {
                    this@ChooseAccountViewModel.dismissConfirmDialog()
                    this@ChooseAccountViewModel.checkWalletStatusAndLoadData(partWallet)
                }, { this@ChooseAccountViewModel.dismissConfirmDialog() })
                showConfirmDialog("loadPrompt", false)
            }
        }
    }

    //915 m7627a
    fun checkWalletStatusAndLoadData(partWallet: PartWallet) {
        Log.d("自来也partWallet:",partWallet.path);
        val path = partWallet.getPath()
        val spUtils = SPUtils.getSPUtils()
        spUtils.startSPEdit("config_vds", ApplicationLoader.getSingleApplicationContext() as Context)
        spUtils.putEditorString("last_path", path)
        if (this.vCashCore == null || !this.vCashCore.isInitSuccess() || !TextUtils.equals(this.vCashCore.getAppWalletPath(), path)) {
            if (this.vCashCore != null && this.vCashCore.isInitSuccess()) {
                this.vCashCore.destoryWalletAndDB()
            }
            this.context.stopService(Intent(this.context, SyncService::class.java))
            //this.context.stopService(Intent(this.context, ImService::class.java))
            PartLoader.getSingleInstance(this.context).setCurWallet(partWallet)
            startActivity(Intent(this.context, LoadDataActivity::class.java).putExtra("path", partWallet.getPath()))
            finish()
            return
        }
        checkWalletTypeAndEnterMain()
    }

    //915 mo40032P
    fun prepareInitAndCreateNewWallet() {
        showLoadingFragment(getStringRescourcesByResName("preparing_for_initialization") as CharSequence)
        if (this.vCashCore != null && this.vCashCore.isInitSuccess()) {
            this.vCashCore.destoryWalletAndDB()
            this.context.stopService(Intent(this.context, SyncService::class.java))
           // this.context.stopService(Intent(this.context, ImService::class.java))
        }
        try {
            val path = PartLoader.getSingleInstance(this.context).getNewWalletDirectory()
            this.f10133z = object : CoreEventObserver {
                /* renamed from: a */
                override fun mo39596a() {}

                /* renamed from: a */
                override fun mo39598a(jfVar: WalletType) {}

                /* renamed from: a */
                override fun onResynched(z: Boolean) {}

                /* renamed from: b */
                override fun mo39600b() {}

                /* renamed from: a */
                override fun handleInitInfoEnum(initInfo: InitInfo) {
                    when (initInfo.initInfo.ordinal) {
                        2 -> {
                            this@ChooseAccountViewModel.vCashCore.removeObserver(this as Any)
                            this@ChooseAccountViewModel.dismissLoadingDialog()
                            val bem = this@ChooseAccountViewModel
                            bem.initAndJumpWalletCreate(bem.vCashCore.getAppWalletPath())
                            return
                        }
                        3 -> {
                            Process.killProcess(Process.myPid())
                            return
                        }
                        else -> return
                    }
                }
            }
            this.vCashCore.addObserver(this.f10133z as Any)
            this.vCashCore.initAppWithPath(this.context as Activity, path)
        } catch (e2: SpaceNotEnoughException) {
            e2.printStackTrace()
            dismissLoadingAndShowToast(getStringRescourcesByResName("space_not_enough"))
        }
    }

    //915 m7628a
    fun initAndJumpWalletCreate(path: String) {
        Constants.InitFileDir(this.vCashCore)
        startService(Intent(this.context, SyncService::class.java))
        //mo41564b(Intent(this.context, ImService::class.java))
        PeerUtils.checkPeersStatus(null, null)
        startActivity(Intent(this.context, WalletCreateFirstActivity::class.java).putExtra("path", path))
        finish()
    }

    //915 mo41723R
    fun editSwitch() {
        val observableBoolean = this.isEditStatus
        observableBoolean.set(!observableBoolean.get())
        //this.selectedIndex.set(-1)
        val it = this.partWalletList.iterator()
        while (it.hasNext()) {
            (it.next()).setIsEdit(this.isEditStatus.get())
        }
        this.simpleAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != -1) {
            return
        }
        if (requestCode ==0 && data!=null) {
            val imageView = this.selectedImageView
            if (imageView != null) {
                imageView.setImageResource(imageNormalId)
            }
            checkPwdAndDeleteWallet(data.getStringExtra("pwd"))
            this.selectedIndex.set(-1)
        }
    }

    //915 m7638r
    private fun checkPwdAndDeleteWallet(str: String) {
        val personalDbPath: String
        val partLoader = PartLoader.getSingleInstance(getContext())
        val vdsPath = (this.partWalletList.get(this.selectedIndex.get()) as PartWallet).getVdsPath()
        if (TextUtils.isEmpty(vdsPath) || !File(vdsPath).exists()) {
            dismissLoadingAndShowToast("selected wallet is not exist ")
        }
        val confFilePath = StringBuilder()
        confFilePath.append(vdsPath)
        confFilePath.append(File.separator)
        confFilePath.append("wallet.conf")
        val file = File(confFilePath.toString())
        if (!file.exists()) {
            val sb2 = StringBuilder()
            sb2.append(vdsPath)
            sb2.append(File.separator)
            sb2.append("db")
            sb2.append(File.separator)
            sb2.append("personal")
            personalDbPath = sb2.toString()
        } else {
            val configFile = ConfigFile()
            try {
                configFile.excuteConfigFile(file)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (configFile.getBooleanValue("regtest", false)) {
                val sb3 = StringBuilder()
                sb3.append(vdsPath)
                sb3.append(File.separator)
                sb3.append("regtest")
                sb3.append(File.separator)
                sb3.append("db")
                sb3.append(File.separator)
                sb3.append("personal")
                personalDbPath = sb3.toString()
            } else if (configFile.getBooleanValue("testnet", false)) {
                val sb4 = StringBuilder()
                sb4.append(vdsPath)
                sb4.append(File.separator)
                sb4.append("test")
                sb4.append(File.separator)
                sb4.append("db")
                sb4.append(File.separator)
                sb4.append("personal")
                personalDbPath = sb4.toString()
            } else {
                val sb5 = StringBuilder()
                sb5.append(vdsPath)
                sb5.append(File.separator)
                sb5.append("db")
                sb5.append(File.separator)
                sb5.append("personal")
                personalDbPath = sb5.toString()
            }
        }
        try {
            if (partLoader.checkWalletPwd(str, personalDbPath)) {
                this.isDeleteCurWallet = false
                if (this.vCashCore != null && this.vCashCore.isInitSuccess() && this.vCashCore.getAppWalletPath().equals(
                        (this.partWalletList.get(
                            this.selectedIndex.get()
                        ) as PartWallet).getPath()
                    )
                ) {
                    this.isDeleteCurWallet = true
                }
                if (this.isDeleteCurWallet) {
                    this.vCashCore.destoryWalletAndDB()
                    //this.context.stopService(Intent(ApplicationLoader.m3654a(), ImService::class.java))
                    this.context.stopService(Intent(ApplicationLoader.getSingleApplicationContext(), SyncService::class.java))
                    this.hasStopCurWallet = true
                }
                if (("storage/emulated/0/"+ Constant.walletConstPath) == (this.partWalletList.get(this.selectedIndex.get()) as PartWallet).getPath()) {
                    val sb6 = StringBuilder()
                    sb6.append((this.partWalletList.get(this.selectedIndex.get()) as PartWallet).getPath())
                    sb6.append(File.separator)
                    sb6.append("bitcoin")
                    FileUtils.checkAndDeleteDirectory(sb6.toString())
                    val sb7 = StringBuilder()
                    sb7.append((this.partWalletList.get(this.selectedIndex.get()) as PartWallet).getPath())
                    sb7.append(File.separator)
                    sb7.append("vds")
                    FileUtils.checkAndDeleteDirectory(sb7.toString())
                    val sb8 = StringBuilder()
                    sb8.append((this.partWalletList.get(this.selectedIndex.get()) as PartWallet).getPath())
                    sb8.append(File.separator)
                    sb8.append("vps")
                    FileUtils.checkAndDeleteDirectory(sb8.toString())
                    val sb9 = StringBuilder()
                    sb9.append((this.partWalletList.get(this.selectedIndex.get()) as PartWallet).getPath())
                    sb9.append(File.separator)
                    sb9.append("logs")
                    FileUtils.checkAndDeleteDirectory(sb9.toString())
                } else {
                    FileUtils.checkAndDeleteDirectory((this.partWalletList.get(this.selectedIndex.get()) as PartWallet).getPath())
                }
                this.partWalletList.removeAt(this.selectedIndex.get())
                PartLoader.getSingleInstance(getContext()).setPartWalletList(this.partWalletList)
                this.simpleAdapter.setDataList(this.partWalletList)
                this.simpleAdapter.notifyDataSetChanged()
                return
            }
            dismissLoadingAndShowToast(getStringRescourcesByResName("wallet_pwd_error"))
        } catch (e2: PasswordException) {
            e2.printStackTrace()
        }

    }

    //915 mo41724S
    fun deleteWallet() {
        val sb = StringBuilder()
        sb.append("delete: ")
        sb.append(this.selectedIndex.get())
        Log.i("ChooseWalletViewModel", sb.toString())
        if (this.selectedIndex.get() >= 0 && this.selectedIndex.get() < this.partWalletList.size) {
            confirmDeleteWallet()
        }
    }



    private fun confirmDeleteWallet() {
        val promptString = getStringRescourcesByResName("prompt")
        val sb = StringBuilder()
        sb.append(getStringRescourcesByResName("delete_account"))
        sb.append(" < ")
        sb.append((this.partWalletList.get(this.selectedIndex.get()) as PartWallet).getLabel())
        sb.append(" > ")
        showNorConfirmDialog(
            promptString,
            sb.toString(),
            getStringRescourcesByResName("confirm"),
            getStringRescourcesByResName("cancel"),
            {
                val bem = this@ChooseAccountViewModel
                bem.startActivityForResult(Intent(bem.getContext(), ValidatePwdActivity::class.java).putExtra("skipCheck",true)
                   , 0)
                this@ChooseAccountViewModel.dismissConfirmDialog()
            },
            { this@ChooseAccountViewModel.dismissConfirmDialog() })
    }

}