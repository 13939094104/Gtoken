package com.vtoken.application.util.okHttpRequest;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CommonOkHttpClient {
    private static final int TIME_OUT = 60;//超时参数
    private static OkHttpClient mOkHttpClient;

    static {
        //创建我们client对象的构建者
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient().newBuilder();
        //为构建者填充超时时间
        okHttpBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        //确保支持重定向
        okHttpBuilder.followRedirects(true);

        //生成我们client对象
        mOkHttpClient = okHttpBuilder.build();
    }

    public static Call sendRequest(Request request, CommonJsonCallback commonCallback) {
        commonCallback.setRelatedRequest(request);
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(commonCallback);
        return call;
    }

    public static Call sendCommonRequest(Request request, Callback commonCallback) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(commonCallback);
        return call;
    }

    public static String sendRequestSynchronize(Request request) throws IOException {
        Call call = mOkHttpClient.newCall(request);
        return call.execute().body().string();
    }
}
