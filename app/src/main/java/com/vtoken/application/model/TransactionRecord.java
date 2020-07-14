package com.vtoken.application.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

public class TransactionRecord implements Parcelable {
    public static final Creator<TransactionRecord> CREATOR = new Creator<TransactionRecord>() {
        public TransactionRecord createFromParcel(Parcel parcel) {
            return new TransactionRecord(parcel);
        }

        public TransactionRecord[] newArray(int i) {
            return new TransactionRecord[i];
        }
    };
    String address;
    boolean confirmed;
    String des;
    String type;
    String value;
    boolean wrong;

    public int describeContents() {
        return 0;
    }

    public TransactionRecord(String str, String str2, String str3) {
        this.address = str;
        this.value = str2;
        this.type = str3;
        this.des = str;
    }

    protected TransactionRecord(Parcel parcel) {
        this.value = parcel.readString();
        this.address = parcel.readString();
        this.type = parcel.readString();
        this.confirmed = parcel.readByte() != 0;
        this.des = parcel.readString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.value);
        parcel.writeString(this.address);
        parcel.writeString(this.type);
        parcel.writeByte(this.confirmed ? (byte) 1 : 0);
        parcel.writeString(this.des);
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String str) {
        this.value = str;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String str) {
        this.address = str;
    }

    public String toString() {
        return String.format(Locale.getDefault(), "address=%s; value=%s", new Object[]{this.address, this.value});
    }

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }

    public boolean isConfirmed() {
        return this.confirmed;
    }

    public void setConfirmed(boolean z) {
        this.confirmed = z;
    }

    public String getDes() {
        return this.des;
    }

    public void setDes(String str) {
        this.des = str;
    }

    public boolean isWrong() {
        return this.wrong;
    }

    public void setWrong(boolean z) {
        this.wrong = z;
    }
}
