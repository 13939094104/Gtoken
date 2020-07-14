package com.vtoken.application.viewModel

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.google.zxing.client.android.Intents
import com.google.zxing.integration.android.IntentIntegrator
import com.vtoken.application.model.QrPage
import com.vtoken.application.view.activity.ScanActivity
import java.util.HashMap

//ban
interface QrPageInstallListener {
    /* renamed from: a */
    fun mo41209a(list: List<QrPage>, str: String)

    //mo41210a
    fun finishWithResultOK(list: List<QrPage>, str: String, str2: String)

    /* renamed from: b */
    fun mo41211b(list: List<QrPage>, str: String)

    /* renamed from: c */
    fun mo41212c(list: List<QrPage>, str: String)
}

//bcn
class PartsScanViewModel(context: Context) :BaseViewModel(context), QrPageInstallListener{

    var f9622x: MutableList<QrPage>? = null


    //mo41411q
    fun initScan() {
        IntentIntegrator(this.context as Activity).setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
            .setOrientationLocked(false).setBeepEnabled(true)
            .setCaptureActivity(ScanActivity::class.java).initiateScan()
    }


    override fun finishWithResultOK(list: List<QrPage>, str: String, str2: String) {
        val intent = Intent()
        intent.putExtra(Intents.Scan.RESULT, str2)
        activity.setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun mo41211b(list: List<QrPage>, str: String) {

    }

    override fun mo41212c(list: List<QrPage>, str: String) {
        val str2: String
        val qrPage = Gson().fromJson<Any>(str, QrPage::class.java) as QrPage
        val hashMap = HashMap<String,String>()
        if (qrPage.postion < qrPage.size - 1) {
            val sb = StringBuilder()
            sb.append(qrPage.postion + 2)
            sb.append("")
            str2 = sb.toString()
        } else {
            val sb2 = StringBuilder()
            sb2.append(qrPage.size)
            sb2.append("")
            str2 = sb2.toString()
        }
        hashMap.put("postion", str2)
        val sb3 = StringBuilder()
        sb3.append(qrPage.size)
        sb3.append("")
        hashMap.put("size", sb3.toString())
        hashMap.put("qrResult", str)
        mo41353a(hashMap as Map<String, String>)
    }

    override fun mo41209a(list: List<QrPage>, str: String) {
        val str2: String
        val qrPage = Gson().fromJson<Any>(str, QrPage::class.java) as QrPage
        val hashMap = HashMap<String,String>()
        if (qrPage.postion < qrPage.size - 1) {
            val sb = StringBuilder()
            sb.append(qrPage.postion + 2)
            sb.append("")
            str2 = sb.toString()
        } else {
            val sb2 = StringBuilder()
            sb2.append(qrPage.size)
            sb2.append("")
            str2 = sb2.toString()
        }
        hashMap.put("postion", str2)
        val sb3 = StringBuilder()
        sb3.append(qrPage.size)
        sb3.append("")
        hashMap.put("size", sb3.toString())
        hashMap.put("qrResult", str)
        mo41353a(hashMap as Map<String, String>)
    }
}