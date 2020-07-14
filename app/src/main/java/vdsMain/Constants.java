package vdsMain;

import android.content.Context;
import com.vtoken.vdsecology.vcash.VCashCore;
import com.vtoken.application.ApplicationLoader;

import vdsMain.tool.DeviceUtil;
import vdsMain.tool.SharedPreferencesUtil;

//acj
public class Constants {

    public static final Integer f233A = Integer.valueOf(2);

    /* renamed from: B */
    public static final Integer f234B = Integer.valueOf(0);

    /* renamed from: C */
    public static final Integer f235C = Integer.valueOf(1);

    /* renamed from: D */
    public static final Integer f236D = Integer.valueOf(2);

    /* renamed from: E */
    public static final Integer f237E = Integer.valueOf(3);

    /* renamed from: F */
    public static final Integer f238F = Integer.valueOf(4);

    /* renamed from: G */
    public static final Integer f239G = Integer.valueOf(0);

    /* renamed from: H */
    public static final Integer f240H = Integer.valueOf(1);

    /* renamed from: I */
    public static final Integer f241I = Integer.valueOf(0);

    /* renamed from: J */
    public static final Integer f242J = Integer.valueOf(1);

    /* renamed from: K */
    public static final Integer f243K = Integer.valueOf(2);

    /* renamed from: L */
    public static final Integer f244L = Integer.valueOf(3);

    /* renamed from: M */
    public static final Integer f245M = Integer.valueOf(4);

    /* renamed from: N */
    public static final Integer f246N = Integer.valueOf(5);

    /* renamed from: O */
    public static final Integer f247O = Integer.valueOf(6);


    public static final Integer f248P = Integer.valueOf(7);

    //f250R
    public static final Integer scrapeWidth = Integer.valueOf(35);

    //f249Q
    public static long delay = 500;

    //f254V
    public static String logDir = null;

    public static String f251S = null;

    //f252T
    public static String imageDir = null;

    //f253U
    public static String paramDir = null;

    //f255W
    public static String audioDir = null;

    public static final Integer f256X = Integer.valueOf(4);

    //f257Y
    public static final Integer PWD_MIN_LENGTH = Integer.valueOf(6);

    /* renamed from: Z */
    public static final Integer f258Z = Integer.valueOf(43);

    public static int f259a = 3;

    public static final Integer f260aa = Integer.valueOf(0);

    /* renamed from: ab */
    public static final Integer f261ab = Integer.valueOf(1);

    /* renamed from: ac */
    public static final Integer f262ac = Integer.valueOf(60000);

    /* renamed from: ad */
    public static final Integer f263ad = Integer.valueOf(DeviceUtil.dp2px((Context) ApplicationLoader.getSingleApplicationContext(), 300.0f));

    /* renamed from: ae */
    public static final Integer f264ae = Integer.valueOf(12);

    //f265af
    public static boolean balanceVisible = true;

    /* renamed from: ag */
    public static final Object f266ag = "backups";

    public static int f267ah = 8;

    /* renamed from: ai */
    public static int f268ai = 4;

    /* renamed from: aj */
    public static int f269aj = 2;

    /* renamed from: b */
    public static final Integer f270b = Integer.valueOf(31);

    /* renamed from: c */
    public static final Integer f271c = Integer.valueOf(20000);

    /* renamed from: d */
    public static final Integer f272d = Integer.valueOf(20000);

    /* renamed from: e */
    public static String f273e = "never_prompt_load_by_mobile_data_again";

    /* renamed from: f */
    public static final Integer f274f = Integer.valueOf(0);

    //f275g
    public static final String vcashName = BLOCK_CHAIN_TYPE.VCASH.name();

    /* renamed from: h */
    public static final String f276h = BLOCK_CHAIN_TYPE.BITCOIN.name();

    /* renamed from: k */
    public static final Integer f279k = Integer.valueOf(0);

    /* renamed from: l */
    public static final Integer f280l = Integer.valueOf(1);

    /* renamed from: m */
    public static final Integer f281m = Integer.valueOf(2);

    /* renamed from: n */
    public static final Integer f282n = Integer.valueOf(3);

    /* renamed from: o */
    public static final Integer f283o = Integer.valueOf(0);

    /* renamed from: p */
    public static final Integer f284p = Integer.valueOf(0);

    /* renamed from: q */
    public static final Integer f285q = Integer.valueOf(1);

    /* renamed from: r */
    public static final Integer f286r = Integer.valueOf(2);

    /* renamed from: s */
    public static final Integer f287s = Integer.valueOf(3);

    /* renamed from: t */
    public static final Integer f288t = Integer.valueOf(4);

    /* renamed from: u */
    public static final Integer f289u = Integer.valueOf(5);

    /* renamed from: v */
    public static final Integer f290v = Integer.valueOf(0);

    /* renamed from: w */
    public static final Integer f291w = Integer.valueOf(1);

    /* renamed from: x */
    public static final Integer f292x = Integer.valueOf(2);

    /* renamed from: y */
    public static final Integer f293y = Integer.valueOf(0);

    /* renamed from: z */
    public static final Integer f294z = Integer.valueOf(1);



    //f278j
    public static int digit8 = 8;

    //f277i
    public static int digit4 = 4;

    //m207a
    //915 m211a
    public static void InitFileDir(VCashCore vCashCore) {
        f251S = vCashCore.mo43880l();
        StringBuilder sb = new StringBuilder();
        sb.append(vCashCore.getFileRootDir());
        sb.append("/image/");
        imageDir = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(vCashCore.getFileRootDir());
        sb2.append("/param/");
        paramDir = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(vCashCore.getFileRootDir());
        sb3.append("/audio/");
        audioDir = sb3.toString();
        StringBuilder sb4 = new StringBuilder();
        sb4.append(vCashCore.getFileRootDir());
        sb4.append("/log/");
        logDir = sb4.toString();
        balanceVisible = SharedPreferencesUtil.getSharedPreferencesUtil().getBooleanValue("balance_visible", true, (Context) ApplicationLoader.getSingleApplicationContext());
        digit8 = SharedPreferencesUtil.getSharedPreferencesUtil().getAndroidWalletInt("digit", 8, ApplicationLoader.getSingleApplicationContext().getApplicationContext());
        digit4 = SharedPreferencesUtil.getSharedPreferencesUtil().getAndroidWalletInt("digit", 5, ApplicationLoader.getSingleApplicationContext().getApplicationContext());
    }
}
