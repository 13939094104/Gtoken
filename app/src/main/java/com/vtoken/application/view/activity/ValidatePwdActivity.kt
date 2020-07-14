package com.vtoken.application.view.activity

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.databinding.ActivityValidatePwdBinding
import com.vtoken.application.databinding.DialogExportImportKeyBinding
import com.vtoken.application.viewModel.ValidatePwdViewModel

class ValidatePwdActivity : BaseActivity() {

    lateinit var dataBinding:ActivityValidatePwdBinding

    lateinit var validateModel:ValidatePwdViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setStatusBarColor(0);
        //getWindow().addFlags(134217728);
        //0 普通验证框 1 eth导出私钥
        val type=intent.getIntExtra("type",0)
        validateModel= ValidatePwdViewModel(this)
        if(type==0){
            dataBinding=DataBindingUtil.setContentView(this, R.layout.activity_validate_pwd)
            dataBinding.setVariable(BR.validatePwdModel,validateModel)
        }else{
            val ethDataBinding=DataBindingUtil.setContentView<DialogExportImportKeyBinding>(this, R.layout.dialog_export_import_key)
            ethDataBinding.setVariable(BR.validatePwdModel,validateModel)
            //0 vds 1 eth
            val chainType=intent.getIntExtra("chainType",0)
            //0 import 1 export
            val actionType=intent.getIntExtra("actionType",0)
            val iconId=if(chainType==0) R.drawable.icon_vds else R.drawable.icon_eth
            val chainName=if(chainType==0) "VDS" else "ETH"
            val action=if(actionType==0) "导入" else "导出"
            ethDataBinding.chainIcon.setImageResource(iconId)
            ethDataBinding.chainTip.setText(String.format("%s%s公链地址私钥",action,chainName))
        }
    }
}
