package com.vtoken.application.util.okHttpRequest;

public class DisposeDataHandle {

    public DisposeDataListener mListener;
    public Class<?> mClass = null;//字节码

    /**
     * 数据原封不动
     *
     * @param listener
     */
    public DisposeDataHandle(DisposeDataListener listener) {
        this.mListener = listener;
    }

    /*** json对象到实体对象的转化
     * @param listener
     * @param clazz
     */
    public DisposeDataHandle(DisposeDataListener listener, Class<?> clazz) {
        this.mListener = listener;
        this.mClass = clazz;
    }
}
