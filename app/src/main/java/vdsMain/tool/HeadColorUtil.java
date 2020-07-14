package vdsMain.tool;

import android.util.ArrayMap;
import com.vtoken.application.R;
import vdsMain.Constants;

import java.util.List;

//bbk
public class HeadColorUtil {

    public static ArrayMap<Integer, Integer> f9405a = new ArrayMap<>();

    /* renamed from: b */
    public static ArrayMap<Integer, List<Integer>> f9406b = new ArrayMap<>();

    static {
        f9405a.put(Constants.f241I, Integer.valueOf(R.drawable.icon_red_cross));

    }

    /* renamed from: a */
    public static int m6958a(int i) {
        return ((Integer) f9405a.get(Integer.valueOf(i))).intValue();
    }

    /* renamed from: b */
    public static List<Integer> m6959b(int i) {
        return (List) f9406b.get(Integer.valueOf(i));
    }
}
