package zcash;

import android.content.Context;
import android.content.res.AssetManager;
import androidx.annotation.NonNull;

public class ZCJoinSplitParamManager {
    private native void initNative(AssetManager assetManager, String str);

    //mo40491a
    public void initNative(@NonNull Context context, String str) {
        initNative(context.getAssets(), str);
    }
}
