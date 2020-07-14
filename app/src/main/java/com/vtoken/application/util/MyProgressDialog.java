package com.vtoken.application.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.cunoraz.gifview.library.GifView;
import com.vtoken.application.R;



public class MyProgressDialog extends ProgressDialog {
    public MyProgressDialog(Context context) {
        super(context);
    }

    public MyProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(getContext());
    }
    private void init(Context context) {
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.myprogressdialog);//loading的xml文件
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
//        ProgressDialog mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_DARK);
//        mProgressDialog.setMessage("正在登录...");
//        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        mProgressDialog.setCancelable(true);
//        mProgressDialog.show();
//        Point size = new Point();
//        mProgressDialog.getWindow().getWindowManager().getDefaultDisplay().getSize(size);

        GifView gifView1 = findViewById(R.id.gif1);
        gifView1.play();
//        gifView1.pause();
//        gifView1.setGifResource(R.drawable.myprogressdialog);
//        gifView1.getGifResource();
//        gifView1.setMovieTime(time);
//        gifView1.getMovie();
    }
}
