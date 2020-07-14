package com.vtoken.application.model;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;
import android.view.View;
import vdsMain.Constants;

import java.io.Serializable;

public class VIdentity extends Node implements Serializable {
    public static String INVITE_MAX_AMOUNT = "12";
    private String btcAddress;
    private boolean canCancel;
    private String clueTx;
    public ObservableInt firstFloor = new ObservableInt();
    private String inviteMaxAmount;
    public ObservableBoolean isApplyingVid = new ObservableBoolean();
    public ObservableBoolean isOnLine = new ObservableBoolean();
    private boolean isUnActivate;
    private String lockReceive;
    private String note;
    private View.OnClickListener onlineOnClickListener;
    private View.OnClickListener qrCodeOnClickListener;
    private String type;
    private String unLockBalance;
    private String vAddress;
    private String vBalance;

    public ObservableList<Node> getNodeList() {
        return null;
    }

    public int getNodeType() {
        return 2;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String str) {
        this.note = str;
    }

    public String getVAddress() {
        return this.vAddress;
    }

    public void setVAddress(String str) {
        this.vAddress = str;
    }

    public String getBtcAddress() {
        return this.btcAddress;
    }

    public void setBtcAddress(String str) {
        this.btcAddress = str;
    }


    public String getVBalance() {
        return this.vBalance;
    }

    public void setVBalance(String str) {
        this.vBalance = str;
    }

    public String getBalance(String str) {
        return getVBalance();
    }

    public String getAddress(String str) {
        if (str.equals(Constants.f276h)) {
            return getBtcAddress();
        }
        return getVAddress();
    }

    public String getInviteMaxAmount() {
        return this.inviteMaxAmount;
    }

    public void setInviteMaxAmount(String str) {
        this.inviteMaxAmount = str;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }

    public View.OnClickListener getQrCodeOnClickListener() {
        return this.qrCodeOnClickListener;
    }

    public void setQrCodeOnClickListener(View.OnClickListener onClickListener) {
        this.qrCodeOnClickListener = onClickListener;
    }

    public View.OnClickListener getOnlineOnClickListener() {
        return this.onlineOnClickListener;
    }

    public void setOnlineOnClickListener(View.OnClickListener onClickListener) {
        this.onlineOnClickListener = onClickListener;
    }

    private String getUnRead(String str) {
        return Integer.parseInt(str) > 99 ? "99+" : str;
    }

    public String getLockReceive() {
        return this.lockReceive;
    }

    public void setLockReceive(String str) {
        this.lockReceive = str;
    }

    public String getUnLockBalance() {
        return this.unLockBalance;
    }

    public void setUnLockBalance(String str) {
        this.unLockBalance = str;
    }

    public boolean isCanCancel() {
        return this.canCancel;
    }

    public void setCanCancel(boolean z) {
        this.canCancel = z;
    }

    public String getClueTx() {
        return this.clueTx;
    }

    public void setClueTx(String str) {
        this.clueTx = str;
    }

    public boolean isUnActivate() {
        return this.isUnActivate;
    }

    public void setUnActivate(boolean z) {
        this.isUnActivate = z;
    }

    public void setFirstFloor(int i) {
        this.firstFloor.set(i);
    }
}
