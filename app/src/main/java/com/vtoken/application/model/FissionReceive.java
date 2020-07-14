package com.vtoken.application.model;

import android.view.View;

import java.io.Serializable;

public class FissionReceive implements Serializable {
    private String address;
    private View.OnClickListener detailOnClick;
    private boolean isAbandoned;
    private boolean isCancel;
    private boolean isConfirm;
    private long lock;
    private String receiveAmount;
    private long timeStamp;
    private String txId;

    public String getTxId() {
        return this.txId;
    }

    public void setTxId(String str) {
        this.txId = str;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String str) {
        this.address = str;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }


    public void setTimeStamp(long j) {
        this.timeStamp = j;
    }

    public String getReceiveAmount() {
        return this.receiveAmount;
    }

    public void setReceiveAmount(String str) {
        this.receiveAmount = str;
    }

    public long getLock() {
        return this.lock;
    }

    public String getLockString() {
        return "("+ String.valueOf(this.lock)+")";
    }

    public void setLock(long j) {
        this.lock = j;
    }

    public View.OnClickListener getDetailOnClick() {
        return this.detailOnClick;
    }

    public void setDetailOnClick(View.OnClickListener onClickListener) {
        this.detailOnClick = onClickListener;
    }

    public boolean isConfirm() {
        return this.isConfirm;
    }

    public void setConfirm(boolean z) {
        this.isConfirm = z;
    }

    public boolean isLocked() {
        return this.lock > 0;
    }

    public boolean isCancel() {
        return this.isCancel;
    }

    public void setCancel(boolean z) {
        this.isCancel = z;
    }

    public boolean isAbandoned() {
        return this.isAbandoned;
    }

    public void setAbandoned(boolean z) {
        this.isAbandoned = z;
    }
}