package com.vtoken.application.util.okHttpRequest;

import android.os.Handler;
import android.os.Looper;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import okhttp3.*;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class CommonJsonCallback implements Callback {

    private Request relatedRequest;
    private int curTryBackIndex=0;
    private int sumBackHostsSize;
    protected final String RESULT_CODE = "code";//有返回则对于http请求来说是成功的
    protected final String DATA_CODE = "data";
    protected final int RESULT_CODE_VALUE = 0;
    protected final String ERROR_MSG = "msg";
    protected final String EMPTY_MSG = "";


    private Class<?> mClass;
    private Handler mDeliveryHandler;//进行消息的转发，将子线程的数据转发到UI线程
    private DisposeDataListener mListener;

    public CommonJsonCallback(DisposeDataHandle handle) {
        this.mClass = handle.mClass;
        this.mListener = handle.mListener;
        this.mDeliveryHandler = new Handler(Looper.getMainLooper());
        sumBackHostsSize=CommonRequest.backBaseHosts.length;
    }

    public void setRelatedRequest(Request relatedRequest) {
        this.relatedRequest = relatedRequest;
    }

    //请求失败处理
    @Override
    public void onFailure(final Call call, final IOException e) {

        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                if(retryServer()){
                    return;
                }
                mListener.onFailure(new OkHttpException(OkHttpExceptionEnum.NETWORK_ERROR, e));
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if(response.code()!=200){
            if(retryServer()){
                return;
            }
        }
        final String result = response.body().string();
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(result);
            }
        });
    }

    private boolean retryServer(){
        curTryBackIndex++;
        if(curTryBackIndex<sumBackHostsSize){
            List<String> paths=relatedRequest.url().pathSegments();
            StringBuilder newUrl= new StringBuilder(CommonRequest.backBaseHosts[curTryBackIndex]);
            for(String path:paths){
                newUrl.append("/").append(path);
            }
            Request newRequest=relatedRequest.newBuilder().url(newUrl.toString()).build();
            CommonOkHttpClient.sendRequest(newRequest,this);
            return true;
        }
        return false;
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
            Logger.d(relatedRequest.url());
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
            }

        } catch (Exception e) {
            e.printStackTrace();
            FormBody body=(FormBody)relatedRequest.body();
            for(int i=0;i<body.size();i++){
                Logger.d(body.encodedName(i));
                Logger.d(body.encodedValue(i));
            }
            mListener.onFailure(new OkHttpException(OkHttpExceptionEnum.OTHER_ERROR, e.getMessage()));
        }

    }
}
