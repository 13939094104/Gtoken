package vdsMain;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.client.android.InactivityTimer;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CameraPreview;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.vtoken.application.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CaptureManager {
    /* access modifiers changed from: private */

    /* renamed from: c */
    public static final String f12567c = "ew";

    /* renamed from: d */
    private static int f12568d = 250;

    //f12569a
    Intent intent;

    //f12570b
    Bundle bundle;
    /* access modifiers changed from: private */

    //f12571e
    public String qrResult;

    //f12572f
    private Activity activity;
    /* access modifiers changed from: private */

    /* renamed from: g */
    public DecoratedBarcodeView decoratedBarcodeView;

    /* renamed from: h */
    private int f12574h = -1;

    /* renamed from: i */
    private boolean f12575i = false;

    /* renamed from: j */
    private boolean f12576j = false;

    /* renamed from: k */
    private InactivityTimer f12577k;
    /* access modifiers changed from: private */

    /* renamed from: l */
    public BeepManager f12578l;
    /* access modifiers changed from: private */

    /* renamed from: m */
    public Handler f12579m;
    /* access modifiers changed from: private */

    /* renamed from: n */
    public boolean f12580n = false;

    /* renamed from: o */
    private BarcodeCallback f12581o = new BarcodeCallback() {
        /* renamed from: a */
        public void possibleResultPoints(List<ResultPoint> list) {
        }

        /* renamed from: a */
        public void barcodeResult(final BarcodeResult barcodeResult) {
            CaptureManager.this.f12579m.post(new Runnable() {
                public void run() {
                    if (barcodeResult.getText().equals(CaptureManager.this.qrResult)) {
                        Log.i("qr", "repeat");
                        CaptureManager.this.decoratedBarcodeView.resume();
                        CaptureManager.this.mo43427a(CaptureManager.this.intent, CaptureManager.this.bundle);
                        CaptureManager.this.mo43431b();
                        return;
                    }
                    Log.i("qr", "result");
                    CaptureManager.this.f12578l.playBeepSoundAndVibrate();
                    CaptureManager.this.decoratedBarcodeView.pause();
                    CaptureManager.this.mo43429a(barcodeResult);
                }
            });
        }
    };

    /* renamed from: p */
    private final CameraPreview.StateListener f12582p = new CameraPreview.StateListener() {
        /* renamed from: a */
        public void previewSized() {
        }

        /* renamed from: b */
        public void previewStarted() {
        }

        /* renamed from: c */
        public void previewStopped() {
        }

        /* renamed from: a */
        public void cameraError(Exception exc) {
            CaptureManager.this.mo43437h();
        }

        /* renamed from: d */
        public void cameraClosed() {
            if (CaptureManager.this.f12580n) {
                Log.d(CaptureManager.f12567c, "Camera closed; finishing activity");
                CaptureManager.this.m11190k();
            }
        }
    };

    /* renamed from: q */
    private boolean f12583q = false;

    public CaptureManager(Activity activity, DecoratedBarcodeView decoratedBarcodeView) {
        this.activity = activity;
        this.decoratedBarcodeView = decoratedBarcodeView;
        decoratedBarcodeView.getBarcodeView().addStateListener(this.f12582p);
        this.f12579m = new Handler();
        this.f12577k = new InactivityTimer(activity, new Runnable() {
            public void run() {
                Log.d(CaptureManager.f12567c, "Finishing due to inactivity");
                CaptureManager.this.m11190k();
            }
        });
        this.f12578l = new BeepManager(activity);
    }

    //mo43430a
    public void setQrResult(String str) {
        this.qrResult = str;
    }

    /* renamed from: a */
    public void mo43427a(Intent intent, Bundle bundle) {
        this.intent = intent;
        this.bundle = bundle;
        this.activity.getWindow().addFlags(128);
        if (bundle != null) {
            this.f12574h = bundle.getInt("SAVED_ORIENTATION_LOCK", -1);
        }
        if (intent != null) {
            if (intent.getBooleanExtra(Intents.Scan.ORIENTATION_LOCKED, true)) {
                mo43425a();
            }
            if (Intents.Scan.ACTION.equals(intent.getAction())) {
                this.decoratedBarcodeView.initializeFromIntent(intent);
            }
            if (!intent.getBooleanExtra(Intents.Scan.BEEP_ENABLED, true)) {
                this.f12578l.setBeepEnabled(false);
            }
            if (intent.hasExtra(Intents.Scan.TIMEOUT)) {
                this.f12579m.postDelayed(new Runnable() {
                    public void run() {
                        CaptureManager.this.mo43436g();
                    }
                }, intent.getLongExtra(Intents.Scan.TIMEOUT, 0));
            }
            if (intent.getBooleanExtra(Intents.Scan.BARCODE_IMAGE_ENABLED, false)) {
                this.f12575i = true;
            }
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    @SuppressLint("WrongConstant")
    public void mo43425a() {
        if (this.f12574h == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            int rotation = this.activity.getWindowManager().getDefaultDisplay().getRotation();
            int i = this.activity.getResources().getConfiguration().orientation;
            int i2 = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            if (i == 2) {
                if (!(rotation == 0 || rotation == 1)) {
                    i2 = 8;
                }
            } else if (i == 1) {
                i2 = (rotation == 0 || rotation == 3) ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
            }
            this.f12574h = i2;
        }
        this.activity.setRequestedOrientation(this.f12574h);
    }

    /* renamed from: b */
    public void mo43431b() {
        this.decoratedBarcodeView.decodeContinuous(this.f12581o);
    }

    /* renamed from: c */
    public void mo43432c() {
        if (Build.VERSION.SDK_INT >= 23) {
            m11189j();
        } else {
            this.decoratedBarcodeView.resume();
        }
        this.f12577k.start();
    }

    @TargetApi(23)
    /* renamed from: j */
    private void m11189j() {
        if (ContextCompat.checkSelfPermission(this.activity, "android.permission.CAMERA") == 0) {
            this.decoratedBarcodeView.resume();
        } else if (!this.f12583q) {
            ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.CAMERA"}, f12568d);
            this.f12583q = true;
        }
    }

    /* renamed from: a */
    public void mo43426a(int i, String[] strArr, int[] iArr) {
        if (i != f12568d) {
            return;
        }
        if (iArr.length <= 0 || iArr[0] != 0) {
            mo43437h();
        } else {
            this.decoratedBarcodeView.resume();
        }
    }

    /* renamed from: d */
    public void mo43433d() {
        this.f12577k.cancel();
        this.decoratedBarcodeView.pauseAndWait();
    }

    /* renamed from: e */
    public void mo43434e() {
        this.f12576j = true;
        this.f12577k.cancel();
        this.f12579m.removeCallbacksAndMessages(null);
    }

    /* renamed from: a */
    public void mo43428a(Bundle bundle) {
        bundle.putInt("SAVED_ORIENTATION_LOCK", this.f12574h);
    }

    /* renamed from: a */
    @SuppressLint("WrongConstant")
    public static Intent m11180a(BarcodeResult barcodeResult, String str) {
        Intent intent = new Intent(Intents.Scan.ACTION);
        intent.addFlags(524288);
        intent.putExtra(Intents.Scan.RESULT, barcodeResult.toString());
        intent.putExtra(Intents.Scan.RESULT_FORMAT, barcodeResult.getBarcodeFormat().toString());
        byte[] c = barcodeResult.getRawBytes();
        if (c != null && c.length > 0) {
            intent.putExtra(Intents.Scan.RESULT_BYTES, c);
        }
        Map e = barcodeResult.getResultMetadata();
        if (e != null) {
            if (e.containsKey(ResultMetadataType.UPC_EAN_EXTENSION)) {
                intent.putExtra(Intents.Scan.RESULT_UPC_EAN_EXTENSION, e.get(ResultMetadataType.UPC_EAN_EXTENSION).toString());
            }
            Number number = (Number) e.get(ResultMetadataType.ORIENTATION);
            if (number != null) {
                intent.putExtra(Intents.Scan.RESULT_ORIENTATION, number.intValue());
            }
            String str2 = (String) e.get(ResultMetadataType.ERROR_CORRECTION_LEVEL);
            if (str2 != null) {
                intent.putExtra(Intents.Scan.RESULT_ERROR_CORRECTION_LEVEL, str2);
            }
            Iterable<byte[]> iterable = (Iterable) e.get(ResultMetadataType.BYTE_SEGMENTS);
            if (iterable != null) {
                int i = 0;
                for (byte[] bArr : iterable) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(Intents.Scan.RESULT_BYTE_SEGMENTS_PREFIX);
                    sb.append(i);
                    intent.putExtra(sb.toString(), bArr);
                    i++;
                }
            }
        }
        if (str != null) {
            intent.putExtra(Intents.Scan.RESULT_BARCODE_IMAGE_PATH, str);
        }
        return intent;
    }

    /* renamed from: b */
    private String m11183b(BarcodeResult barcodeResult) {
        if (this.f12575i) {
            Bitmap a = barcodeResult.getBitmap();
            try {
                File createTempFile = File.createTempFile("barcodeimage", ".jpg", this.activity.getCacheDir());
                FileOutputStream fileOutputStream = new FileOutputStream(createTempFile);
                a.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.close();
                return createTempFile.getAbsolutePath();
            } catch (IOException e) {
                String str = f12567c;
                StringBuilder sb = new StringBuilder();
                sb.append("Unable to create temporary file and store bitmap! ");
                sb.append(e);
                Log.w(str, sb.toString());
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    /* renamed from: k */
    public void m11190k() {
        this.activity.finish();
    }

    /* access modifiers changed from: protected */
    /* renamed from: f */
    public void mo43435f() {
        if (this.decoratedBarcodeView.getBarcodeView().isCameraClosed()) {
            m11190k();
        } else {
            this.f12580n = true;
        }
        this.decoratedBarcodeView.pause();
        this.f12577k.cancel();
    }

    /* access modifiers changed from: protected */
    /* renamed from: g */
    public void mo43436g() {
        Intent intent = new Intent(Intents.Scan.ACTION);
        intent.putExtra(Intents.Scan.TIMEOUT, true);
        this.activity.setResult(0, intent);
        mo43435f();
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo43429a(BarcodeResult evVar) {
        this.activity.setResult(-1, m11180a(evVar, m11183b(evVar)));
        mo43435f();
    }

    /* access modifiers changed from: protected */
    /* renamed from: h */
    public void mo43437h() {
        if (!this.activity.isFinishing() && !this.f12576j && !this.f12580n) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
            builder.setTitle(this.activity.getString(R.string.zxing_app_name));
            builder.setMessage(this.activity.getString(R.string.zxing_msg_camera_framework_bug));
            builder.setPositiveButton(R.string.zxing_button_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    CaptureManager.this.m11190k();
                }
            });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialogInterface) {
                    CaptureManager.this.m11190k();
                }
            });
            builder.show();
        }
    }
}
