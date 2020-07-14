package com.vtoken.application.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import vdsMain.transaction.Utxo;

public class ChangeAddress extends BaseObservable implements Parcelable {
    public static final Creator<ChangeAddress> CREATOR = new Creator<ChangeAddress>() {
        public ChangeAddress createFromParcel(Parcel parcel) {
            return new ChangeAddress(parcel);
        }

        public ChangeAddress[] newArray(int i) {
            return new ChangeAddress[i];
        }
    };
    private String balance;
    @Bindable
    private boolean isCheck;
    private boolean isConfirm;
    ChangeChooseListener listener;
    private Utxo txOut;
    @Bindable
    private String type;

    /* renamed from: v.dimensional.model.ChangeAddress$ChangeChooseListener */
    public interface ChangeChooseListener {
        void onChangeChooseStatusChanged(ChangeAddress changeAddress, boolean z);
    }

    public int describeContents() {
        return 0;
    }

    public ChangeAddress() {
    }

    protected ChangeAddress(Parcel parcel) {
        boolean z = true;
        this.isCheck = parcel.readByte() != 0;
        if (parcel.readByte() == 0) {
            z = false;
        }
        this.isConfirm = z;
        this.balance = parcel.readString();
        this.type = parcel.readString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte(this.isCheck ? (byte) 1 : 0);
        parcel.writeByte(this.isConfirm ? (byte) 1 : 0);
        parcel.writeString(this.balance);
        parcel.writeString(this.type);
    }

    public boolean isCheck() {
        return this.isCheck;
    }

    public void setCheck(boolean z) {
        this.isCheck = z;
        notifyPropertyChanged(70);
        ChangeChooseListener changeChooseListener = this.listener;
        if (changeChooseListener != null) {
            changeChooseListener.onChangeChooseStatusChanged(this, z);
        }
    }

    public Utxo getTxOut() {
        return this.txOut;
    }

    public void setTxOut(Utxo utxo) {
        this.txOut = utxo;
    }

    public String getTxid() {
        return this.txOut.getTxOutTxid().hashString();
    }

    public void setTxid(String str) {
        this.txOut.mo44692a(str);
    }

    public boolean isConfirm() {
        return this.isConfirm;
    }

    public void setConfirm(boolean z) {
        this.isConfirm = z;
    }

    public ChangeChooseListener getListener() {
        return this.listener;
    }

    public void setListener(ChangeChooseListener changeChooseListener) {
        this.listener = changeChooseListener;
    }

    public String getBalance() {
        return this.balance;
    }

    public void setBalance(String str) {
        this.balance = str;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }
}
