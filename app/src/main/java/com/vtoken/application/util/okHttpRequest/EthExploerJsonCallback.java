package com.vtoken.application.util.okHttpRequest;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class EthExploerJsonCallback implements Callback {

    protected final String RESULT_CODE = "status";//有返回则对于http请求来说是成功的
    protected final String DATA_CODE = "data";
    protected final int RESULT_CODE_VALUE = 1;
    protected final String ERROR_MSG = "message";
    protected final String EMPTY_MSG = "";


    private Class<?> mClass;
    private Handler mDeliveryHandler;//进行消息的转发，将子线程的数据转发到UI线程
    private DisposeDataListener mListener;

    public EthExploerJsonCallback(DisposeDataHandle handle) {
        this.mClass = handle.mClass;
        this.mListener = handle.mListener;
        this.mDeliveryHandler = new Handler(Looper.getMainLooper());
    }


    //请求失败处理
    @Override
    public void onFailure(final Call call, final IOException e) {

        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(new OkHttpException(OkHttpExceptionEnum.NETWORK_ERROR, e));
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String result = response.body().string();
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(result);
            }
        });
    }


    /**
     *       * 处理服务器返回的数据
     *       * @param responseObj
     *
     */
    private void handleResponse(Object responseObj) {

        //为了保证代码的健壮性
        if (responseObj == null && responseObj.toString().trim().equals("")) {
            mListener.onFailure(new OkHttpException(OkHttpExceptionEnum.NETWORK_ERROR,EMPTY_MSG));
            return;
        }

        try {
            String responseStr=responseObj.toString();
            Logger.d(responseStr);
            JSONObject result = new JSONObject(responseStr);
            //开始尝试解析json
            if (result.has(RESULT_CODE)) {
                //从json对象中取出我们的响应码,若为0（与服务器一致），则是正常的响应
                if (result.getInt(RESULT_CODE) == RESULT_CODE_VALUE) {
                    if (mClass == null) {
                        mListener.onSuccess(responseObj);
                    } else {
                        Gson gson = new Gson();
                        Object obj = gson.fromJson(responseObj.toString(), mClass);
                        if (obj != null) {
                            mListener.onSuccess(obj);
                        } else {
                            //返回的不是合法的json
                            mListener.onFailure(new OkHttpException(OkHttpExceptionEnum.JSON_ERROR, EMPTY_MSG));
                        }
                    }
                } else {
                    //将服务器返回给我们的异常回调到应用层去处理
                    Logger.d(responseStr);
                    mListener.onFailure(new OkHttpException(OkHttpExceptionEnum.SERVER_ERROR, result.getInt(RESULT_CODE),result.getString(ERROR_MSG)));
                }
            }else if(result.has("jsonrpc")){
                mListener.onSuccess(responseStr);
            }

        } catch (Exception e) {
            e.printStackTrace();
            mListener.onFailure(new OkHttpException(OkHttpExceptionEnum.OTHER_ERROR, e.getMessage()));
        }

    }
}
