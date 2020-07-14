package vdsMain.tool;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Environment;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.vtoken.application.ApplicationLoader;
import com.vtoken.application.R;
import com.vtoken.application.model.Address;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import vdsMain.StringToolkit;
import vdsMain.BLOCK_CHAIN_TYPE;
import vdsMain.Constants;
import vdsMain.Log;
import vdsMain.transaction.Utxo;

/* renamed from: bbw */
public class Util {

    //f9422a
    static final SimpleDateFormat dataFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm");

    /* renamed from: b */
    private static final Integer f9423b = Integer.valueOf(70);

    //f9424c
    private static NumberFormat numberFormat;

    /* renamed from: a */
    public static boolean m7035a(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.getState() == State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    /* renamed from: a */
    public static boolean m7034a() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter != null) {
            return defaultAdapter.isEnabled();
        }
        return false;
    }

    /* renamed from: a */
    public static String m7025a(String str, String str2) {
        return m7023a(str, getPointNumber(str2));
    }

    /* renamed from: a */
    public static String m7019a(double d, String str) {
        return m7018a(d, getPointNumber(str), true);
    }

    /* renamed from: a */
    public static String m7023a(String doubleStr, int pointNumber) {
        return fortmatPointNumber(doubleStr, pointNumber, true);
    }

    //m7024a
    public static String fortmatPointNumber(String str, int pointNumber, boolean z) {
        if (TextUtils.isEmpty(str) || TextUtils.equals(".", str)) {
            if (pointNumber == 0) {
                return "0";
            }
            String str2 = "0.";
            for (int i = 0; i < pointNumber; i++) {
                StringBuilder sb = new StringBuilder();
                sb.append(str2);
                sb.append("0");
                str2 = sb.toString();
            }
            return str2;
        } else if (str.contains(".")) {
            if (str.indexOf(".") != str.length() - 1) {
                try {
                    if (numberFormat == null) {
                        numberFormat = DecimalFormat.getInstance(Locale.US);
                    }
                    BigDecimal bigDecimal = new BigDecimal(str.replace(",", ""));
                    numberFormat.setMinimumFractionDigits(0);
                    numberFormat.setMaximumFractionDigits(pointNumber);
                    numberFormat.setRoundingMode(RoundingMode.DOWN);
                    numberFormat.setGroupingUsed(z);
                    return numberFormat.format(bigDecimal);
                } catch (Exception e) {
                    e.printStackTrace();
                    return str;
                }
            } else if (z) {
                return m7059i(str);
            } else {
                return str.replace(",", "");
            }
        } else if (z) {
            return m7059i(str);
        } else {
            return str.replace(",", "");
        }
    }

    /* renamed from: a */
    public static String m7018a(double d, int i, boolean z) {
        if (d != 0.0d) {
            if (numberFormat == null) {
                numberFormat = DecimalFormat.getInstance(Locale.US);
            }
            BigDecimal bigDecimal = new BigDecimal(Double.toString(d));
            numberFormat.setMinimumFractionDigits(0);
            numberFormat.setMaximumFractionDigits(i);
            numberFormat.setRoundingMode(RoundingMode.DOWN);
            numberFormat.setGroupingUsed(z);
            return numberFormat.format(bigDecimal);
        }
        String str = "0.";
        if (i == 0) {
            return "0";
        }
        for (int i2 = 0; i2 < i; i2++) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append("0");
            str = sb.toString();
        }
        return str;
    }

    /* renamed from: a */
    public static String m7017a(double value, int pointNumber) {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(value);
        return m7023a(sb.toString(), pointNumber);
    }

    //m7015a
    public static int getPointNumber(String str) {
        if (str.equals(Constants.vcashName)) {
            return Constants.digit4;
        }
        if (str.equals(Constants.f276h)) {
            return Constants.digit8;
        }
        return str.equals("CNY") ? 2 : 5;
    }

    /* renamed from: a */
    public static int m7014a(BLOCK_CHAIN_TYPE igVar) {
        if (igVar.equals(BLOCK_CHAIN_TYPE.VCASH)) {
            return Constants.digit4;
        }
        if (igVar.equals(BLOCK_CHAIN_TYPE.BITCOIN)) {
            return Constants.digit8;
        }
        return igVar.equals(BLOCK_CHAIN_TYPE.UNKNOWN) ? 2 : 5;
    }

    /* renamed from: b */
    public static String m7039b(String str) {
        if (str.equals(Constants.vcashName)) {
            return "";
        }
        if (str.equals(Constants.f276h)) {
            return "";
        }
        return str.equals("CNY") ? "¥" : "";
    }

    /* renamed from: b */
    public static String m7036b() {
        return m7039b(Constants.vcashName);
    }

    /* renamed from: c */
    public static boolean m7046c() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    /* renamed from: a */
    public static Bitmap m7016a(Bitmap bitmap, Bitmap bitmap2) {
        if (bitmap == null || bitmap2 == null) {
            return bitmap;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int width2 = bitmap2.getWidth();
        int height2 = bitmap2.getHeight();
        Bitmap bitmap3 = null;
        if (width == 0 || height == 0) {
            return null;
        }
        if (width2 == 0 || height2 == 0) {
            return bitmap;
        }
        float f = ((((float) width) * 1.0f) / 5.0f) / ((float) width2);
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(createBitmap);
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
            canvas.scale(f, f, (float) (width / 2), (float) (height / 2));
            canvas.drawBitmap(bitmap2, (float) ((width - width2) / 2), (float) ((height - height2) / 2), null);
            canvas.save();
            canvas.restore();
            bitmap.recycle();
            bitmap3 = createBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap3;
    }

    /* renamed from: a */
    public static String m7022a(long j, String str) {
        return new SimpleDateFormat(str).format(new Date(j));
    }

    /* renamed from: b */
    public static String m7038b(long j, String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str);
        if (j <= 0) {
            return simpleDateFormat.format(new Date(System.currentTimeMillis()));
        }
        return simpleDateFormat.format(new Date(j * 1000));
    }

    /* renamed from: a */
    public static String m7021a(long j) {
        if (j <= 0) {
            return dataFormat.format(new Date(System.currentTimeMillis()));
        }
        return dataFormat.format(new Date(j * 1000));
    }

    /* renamed from: b */
    public static String m7037b(long j) {
        return new SimpleDateFormat("KK:mm aa", Locale.US).format(new Date(j));
    }

    /* renamed from: b */
    public static String m7040b(String str, String str2) {
        return m7026a(str, str2, true);
    }

    /* renamed from: a */
    public static String m7026a(String str, String str2, boolean z) {
        String str3;
        if (str == null || "".equals(str)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(m7025a(str, str2));
        if (z) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(" ");
            sb2.append(m7039b(str2));
            str3 = sb2.toString();
        } else {
            str3 = "";
        }
        sb.append(str3);
        return sb.toString();
    }

    /* renamed from: a */
    public static void m7031a(String str, Context context) {
        ((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText(null, str));
    }

    /* renamed from: c */
    public static String m7045c(String str) {
        return (str == null || str.toCharArray().length <= 0) ? "" : String.valueOf(str.toCharArray()[0]);
    }



    /* renamed from: d */
    public static String m7050d(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, 4));
        sb.append("....");
        sb.append(str.substring(str.length() - 4, str.length()));
        return sb.toString();
    }

    /* renamed from: a */
    public static void m7030a(EditText editText) {
        editText.requestFocus();
        ((InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(editText, 0);
    }

    /* renamed from: b */
    public static void m7042b(EditText editText) {
        editText.clearFocus();
        ((InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /* renamed from: b */
    public static void m7041b(Context context) {
        ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate((long) f9423b.intValue());
    }

    /* renamed from: c */
    public static String m7044c(long j) {
        return dataFormat.format(new Date(j));
    }

    //m7053e
    public static void copyAndShowToast(String str) {
        if (!TextUtils.isEmpty(str)) {
            m7031a(str, (Context) ApplicationLoader.getSingleApplicationContext());
            Log.LogErrorNoThrow("copy_success", ApplicationLoader.getSingleApplicationContext().getStringFromLocal("copy_success"));
            Toast makeText = Toast.makeText(ApplicationLoader.getSingleApplicationContext(), ApplicationLoader.getSingleApplicationContext().getStringFromLocal("copy_success"), Toast.LENGTH_SHORT);
            makeText.setGravity(17, 0, 0);
            makeText.show();
        }
    }

    /* renamed from: d */
    public static String m7048d() {
        File file = new File(Constants.audioDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(file.getPath());
        sb.append(File.separator);
        sb.append(System.currentTimeMillis());
        sb.append(".amr");
        return sb.toString();
    }

    /* renamed from: e */
    public static String m7051e() {
        File file = new File(Constants.imageDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(file.getPath());
        sb.append(File.separator);
        sb.append(System.currentTimeMillis());
        sb.append(".jpg");
        return sb.toString();
    }

    /* renamed from: d */
    public static String m7049d(long j) {
        BigDecimal divide = new BigDecimal(j).divide(new BigDecimal("1000"), 2, 3);
        if (divide.doubleValue() > 0.8d && divide.doubleValue() < 1.0d) {
            return "0:01";
        }
        if (divide.doubleValue() > 59.8d) {
            return "1:00";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(divide.intValue());
        sb.append("");
        if (sb.toString().length() == 1) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("0:0");
            sb2.append(divide.intValue());
            return sb2.toString();
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("0:");
        sb3.append(divide.intValue());
        return sb3.toString();
    }

    /* renamed from: f */
    public static int m7054f() {
        return new Random().nextInt(HeadColorUtil.f9405a.size() - 1);
    }

    /* renamed from: e */
    public static String m7052e(long j) {
        long currentTimeMillis = (((System.currentTimeMillis() - j) / 1000) / 60) / 60;
        if (currentTimeMillis < 24) {
            return new SimpleDateFormat("KK:mm aa", Locale.US).format(new Date(j));
        }
        long j2 = currentTimeMillis / 24;
        if (j2 >= 7) {
            return j2 > 365 ? new SimpleDateFormat("yyyy", Locale.US).format(new Date(j)) : new SimpleDateFormat("MM/dd KK:mm aa", Locale.US).format(new Date(j));
        } else if (j2 >= 1) {
            return new SimpleDateFormat("E KK:mm aa", Locale.US).format(new Date(j));
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(ApplicationLoader.getSingleApplicationContext().getStringFromLocal("yesterday"));
            sb.append(new SimpleDateFormat("KK:mm aa", Locale.US).format(new Date(j)));
            return sb.toString();
        }
    }

    /* renamed from: a */
    public static String m7020a(int i) {
        if (i == 0) {
            return "";
        }
        if (i > 99) {
            return "99+";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append("");
        return sb.toString();
    }

    /* renamed from: c */
    public static boolean m7047c(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean z = false;
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.getState() == State.CONNECTED) {
                if (((ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo().getType() == 0) {
                    z = true;
                }
                return z;
            }
        }
        return false;
    }

    /* renamed from: g */
    public static byte[] m7057g() {
        return StringToolkit.m11526a("13D269CA6CDC8D4BE2A427CD3EB1A537C380DD17");
    }



    /* renamed from: a */
    public static void m7033a(List<Utxo> list) {
        Collections.sort(list, new Comparator<Utxo>() {
            /* renamed from: a */
            public int compare(Utxo utxo, Utxo lzVar2) {
                int i = (utxo.getValue() > lzVar2.getValue() ? 1 : (utxo.getValue() == lzVar2.getValue() ? 0 : -1));
                if (i > 0) {
                    return -1;
                }
                return i < 0 ? 1 : 0;
            }
        });
    }

    //m7043b
    public static void sortAddressModelByUnlockBalance(List<Address> list) {
        Collections.sort(list, new Comparator<Address>() {
            /* renamed from: a */
            public int compare(Address address, Address address2) {
                int i = (address.getUnLockBalance() > address2.getUnLockBalance() ? 1 : (address.getUnLockBalance() == address2.getUnLockBalance() ? 0 : -1));
                if (i > 0) {
                    return -1;
                }
                return i < 0 ? 1 : 0;
            }
        });
    }


    //m7032a
    public static void getQrCodeBitmapWithCenterDrawable(final String str, Observer<Bitmap> observer, final int i) {
        if (str != null) {
            Activity activity = ApplicationLoader.sActivity;
            Observable create = Observable.create(new ObservableOnSubscribe<Bitmap>() {
                public void subscribe(ObservableEmitter<Bitmap> observableEmitter) throws WriterException {
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    Hashtable hashtable = new Hashtable();
                    hashtable.put(EncodeHintType.MARGIN, Integer.valueOf(1));
                    hashtable.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
                    Bitmap bitmap = new BarcodeEncoder().createBitmap(multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, Constants.f263ad.intValue(), Constants.f263ad.intValue(), hashtable));
                    if (bitmap != null) {
                        if (i != -1) {
                            if (i != 0) {
                                bitmap = Util.m7016a(bitmap, BitmapFactory.decodeResource(ApplicationLoader.getSingleApplicationContext().getResources(), i));
                            } else {
                                bitmap = Util.m7016a(bitmap, BitmapFactory.decodeResource(ApplicationLoader.getSingleApplicationContext().getResources(), R.drawable.qr_center_logo_v));
                            }
                        }
                        observableEmitter.onNext(bitmap);
                        return;
                    }
                    observableEmitter.onError(new NullPointerException(String.format(Locale.getDefault(), "try generate a qrBitmap by %s; but get a 'null' qrBitmap", new Object[]{str})));
                }
            });
            if (activity != null && (activity instanceof RxAppCompatActivity)) {
                create = create.compose(((RxAppCompatActivity) activity).bindToLifecycle());
            }
            create.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
        }
    }


    /* renamed from: h */
    public static String m7058h(String str) {
        return ApplicationLoader.getSingleApplicationContext().getStringFromLocal(str);
    }

    /* renamed from: i */
    public static String m7059i(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        String replace = str.replace(",", "");
        try {
            BigDecimal bigDecimal = new BigDecimal(replace);
            if (bigDecimal.compareTo(new BigDecimal("1000")) < 0) {
                return replace;
            }
            NumberFormat numberInstance = NumberFormat.getNumberInstance(Locale.US);
            numberInstance.setGroupingUsed(true);
            String format = numberInstance.format(bigDecimal);
            if (replace.contains(".") && !format.contains(".")) {
                StringBuilder sb = new StringBuilder();
                sb.append(format);
                sb.append(".");
                format = sb.toString();
            }
            return format;
        } catch (Exception e) {
            e.printStackTrace();
            return replace;
        }
    }



    /* renamed from: k */
    public static String m7061k(String str) {
        return str != null ? str.toLowerCase(Locale.ENGLISH) : str;
    }
}
