package com.vtoken.application.util.okHttpRequest;

import okhttp3.FormBody;
import okhttp3.Request;

import java.util.Map;

public class CommonRequest {

    public static String baseHost="";

    public static String[] backBaseHosts=new String[]{};

    public static String ethExploerHost="http://api-cn.etherscan.com/api";


    public static Request createPostRequest(String url, RequestParams params) {

        FormBody.Builder mFormBodyBuild = new FormBody.Builder();

        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                mFormBodyBuild.add(entry.getKey(), entry.getValue());
            }
        }
        //通过请求构件类的build方法获取到真正的请求体对象
        FormBody mFormBody = mFormBodyBuild.build();
        return new Request.Builder().url(url).post(mFormBody).build();
    }

    public static Request createPostRequestByRoute(String route, RequestParams params) {

        FormBody.Builder mFormBodyBuild = new FormBody.Builder();

        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                mFormBodyBuild.add(entry.getKey(), entry.getValue());
            }
        }
        //通过请求构件类的build方法获取到真正的请求体对象
        FormBody mFormBody = mFormBodyBuild.build();
        return new Request.Builder().url(baseHost+route).post(mFormBody).build();
    }

    public static Request createEthExploerGetRequest(RequestParams params) {

        params.put("apikey","");
        StringBuilder urlBuilder = new StringBuilder(ethExploerHost).append("?");
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                //将请求参数遍历添加到我们的请求构件类中
                urlBuilder.append(entry.getKey()).append("=").
                        append(entry.getValue()).append("&");
            }
        }

        return new Request.Builder().url(urlBuilder.substring(0, urlBuilder.length() - 1))
                .get().build();
    }

    public static Request createEthExploerPostRequest(RequestParams params) {
        params.put("apikey","");
        FormBody.Builder mFormBodyBuild = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
            mFormBodyBuild.add(entry.getKey(), entry.getValue());
        }
        //通过请求构件类的build方法获取到真正的请求体对象
        FormBody mFormBody = mFormBodyBuild.build();
        return new Request.Builder().url(ethExploerHost).post(mFormBody).build();
    }

    public static Request createGetRequest(String url, RequestParams params) {

        StringBuilder urlBuilder = new StringBuilder(url).append("?");
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                //将请求参数遍历添加到我们的请求构件类中
                urlBuilder.append(entry.getKey()).append("=").
                        append(entry.getValue()).append("&");
            }
        }

        return new Request.Builder().url(urlBuilder.substring(0, urlBuilder.length() - 1))
                .get().build();
    }
}
