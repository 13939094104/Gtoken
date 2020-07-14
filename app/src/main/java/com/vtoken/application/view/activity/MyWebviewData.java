package com.vtoken.application.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TableRow;

import androidx.annotation.Nullable;

import com.vtoken.application.R;

public class MyWebviewData extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        String uril=getIntent().getStringExtra("url");
        WebView webView=(findViewById(R.id.webView));
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webView.loadDataWithBaseURL(null,uril, "text/html" , "utf-8", null);
        TableRow tableRow=findViewById(R.id.finishpage);
        tableRow.setOnClickListener(v -> finish());
        ImageView iv_reflash=findViewById(R.id.iv_reflash);
        iv_reflash.setOnClickListener(v->webView.reload());
    }
}
