package com.vtoken.application.view.activity

import android.content.Intent
import androidx.databinding.DataBindingUtil
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import android.os.Bundle
import androidx.annotation.RequiresApi
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.zxing.client.android.Intents
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.databinding.ActivityScanBinding
import com.vtoken.application.util.PhotoUtil
import com.vtoken.application.viewModel.ScanViewModel
import vdsMain.CaptureManager
import vdsMain.Constants

class ScanActivity : BaseActivity(), DecoratedBarcodeView.TorchListener {

    //f3596a
    var qrResult: String?=null

    //f3599d
    var isTorchOn = false

    //f3597b
    lateinit var dataBinding:ActivityScanBinding

    //f3598c
    lateinit var captureManager: CaptureManager


    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        captureManager.mo43428a(outState)
    }

    @RequiresApi(api = 21)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.hasExtra("qrResult")) {
            this.qrResult = intent.getStringExtra("qrResult")
        }
        window.statusBarColor = 0
        this.dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_scan)
        dataBinding.setVariable(BR.scanViewModel,ScanViewModel(this))
        this.captureManager = CaptureManager(this, this.dataBinding.scanDecoratedBarcodeView)
        if (!TextUtils.isEmpty(this.qrResult)) {
            this.captureManager.setQrResult(this.qrResult)
        }
        this.captureManager.mo43427a(intent, savedInstanceState)
        this.captureManager.mo43431b()
        this.dataBinding.scanDecoratedBarcodeView.setOnClickListener(View.OnClickListener {
            if (this@ScanActivity.isTorchOn) {
                this@ScanActivity.dataBinding.scanDecoratedBarcodeView.setTorchOff()
                this@ScanActivity.onTorchOff()
                return@OnClickListener
            }
            this@ScanActivity.dataBinding.scanDecoratedBarcodeView.setTorchOn()
            this@ScanActivity.onTorchOn()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == -1 && requestCode == Constants.f274f) {
            try {
                val query = contentResolver.query(intent?.data, arrayOf("_data"), null, null, null)
                var str: String? = ""
                if (query.moveToFirst()) {
                    str = query.getString(query.getColumnIndexOrThrow("_data"))
                    if (str == null) {
                        str = PhotoUtil.getFilePathByUri(applicationContext, intent?.data)
                    }
                }
                query.close()
                val sb = StringBuilder()
                sb.append("onActivityResult: ")
                sb.append(str)
                Log.i("ScanActivity", sb.toString())
                val a = PhotoUtil.m6979a(str)
                if (a == null) {
                    val makeText = Toast.makeText(this, getString("scan_failed"), Toast.LENGTH_SHORT)
                    makeText.setGravity(17, 0, 0)
                    makeText.show()
                } else {
                    val intent2 = Intent()
                    intent2.putExtra(Intents.Scan.RESULT, a)
                    setResult(-1, intent2)
                    finish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    override fun onPause() {
        super.onPause()
        captureManager.mo43433d()
    }

    override fun onResume() {
        super.onResume()
        this.captureManager.mo43432c()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.captureManager.mo43434e()
    }

    //mo18524a
    override fun onTorchOn() {
        this.isTorchOn = true
    }

    //mo18525b
    override fun onTorchOff() {
        this.isTorchOn = false
    }

}
