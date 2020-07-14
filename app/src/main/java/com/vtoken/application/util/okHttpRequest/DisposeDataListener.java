package com.vtoken.application.util.okHttpRequest;

public interface DisposeDataListener {

    public void onSuccess(Object responseObj);


    public void onFailure(OkHttpException exception);

}