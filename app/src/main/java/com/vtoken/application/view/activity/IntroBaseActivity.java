package com.vtoken.application.view.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;
import com.vtoken.application.R;

public class IntroBaseActivity extends BaseActivity{
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        InitWindow();
        //getWindow().addFlags(134217728);
    }

    /* renamed from: a */
    private void InitWindow() {
        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //window.addFlags(Integer.MIN_VALUE);
        //window.clearFlags(67108864);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.app_dark_bg));
        //window.getDecorView().setSystemUiVisibility(1280);
    }

    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        super.onResume();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onBackPressed() {
        finish();
    }
}
