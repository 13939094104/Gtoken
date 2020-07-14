package com.vcwallet.core.part;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

public class PartWallet implements Parcelable {


    public static final Creator<PartWallet> CREATOR = new Creator<PartWallet>() {
        /* renamed from: a */
        public PartWallet createFromParcel(Parcel parcel) {
            return new PartWallet(parcel);
        }

        /* renamed from: a */
        public PartWallet[] newArray(int i) {
            return new PartWallet[i];
        }
    };

    //915 f684a
    String path;

    //915 f685b
    String label;

    //915 f686c
    String balance;

    //915 f687d
    String btcBalance;

    //915 f688e
    int generalNumber;

    //915 f689f
    int vidNumber;

    //915 f690g
    int anonymousNumber;

    //915 f691h
    int mutiSignNumber;

    int vxdNumber;

    /* renamed from: i */
    int f692i;

    /* renamed from: j */
    int addr;

    /* renamed from: k */
    boolean f694k = true;

    //f695l
    boolean isEdit = false;

    /* renamed from: m */
    boolean f696m = false;

    boolean isSelect = false;

    public int describeContents() {
        return 0;
    }

    public PartWallet(String str) {
        this.path = str;
    }

    public PartWallet(String str, String str2) {
        this.path = str;
        this.label = str2;
        this.balance = "0";
    }

    protected PartWallet(Parcel parcel) {
        boolean z = true;
        this.path = parcel.readString();
        this.label = parcel.readString();
        this.balance = parcel.readString();
        this.btcBalance = parcel.readString();
        this.generalNumber = parcel.readInt();
        this.vidNumber = parcel.readInt();
        this.anonymousNumber = parcel.readInt();
        this.mutiSignNumber = parcel.readInt();
        this.f692i = parcel.readInt();
        this.addr = parcel.readInt();
        this.vxdNumber=parcel.readInt();
        if (parcel.readByte() == 0) {
            z = false;
        }
        this.f694k = z;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.path);
        parcel.writeString(this.label);
        parcel.writeString(this.balance);
        parcel.writeString(this.btcBalance);
        parcel.writeInt(this.generalNumber);
        parcel.writeInt(this.vidNumber);
        parcel.writeInt(this.anonymousNumber);
        parcel.writeInt(this.mutiSignNumber);
        parcel.writeInt(this.f692i);
        parcel.writeInt(this.addr);
        parcel.writeInt(this.vxdNumber);
        parcel.writeByte(this.f694k ? (byte) 1 : 0);
    }

    //mo18987a
    public String getLabel() {
        return this.label;
    }

    //915 mo18989a
    public void setLabel(String str) {
        this.label = str;
    }

    //915 mo18991b
    public String getPath() {
        return this.path;
    }

    //915 mo18995c
    public String getBalance() {
        return this.balance;
    }

    //915 mo18993b
    public void setBalance(String str) {
        this.balance = str;
    }

    //915 mo18999d
    public int getGeneralNumber() {
        return this.generalNumber;
    }

    //915 mo18988a
    public void setGeneralNumber(int i) {
        this.generalNumber = i;
    }

    //915 mo19002e
    public int getVidNumber() {
        return this.vidNumber;
    }

    //
    public void setVidNumber(int i) {
        this.vidNumber = i;
    }

    /* renamed from: f */
    public int mo19004f() {
        return this.anonymousNumber;
    }

    //915 mo18996c
    public void setAnonymousNumber(int i) {
        this.anonymousNumber = i;
    }

    /* renamed from: g */
    public int mo19006g() {
        return this.mutiSignNumber;
    }

    //915 mo19000d
    public void setMutliSignNumber(int i) {
        this.mutiSignNumber = i;
    }

    //915 mo19007h
    public String getVdsPath() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.path);
        sb.append(File.separator);
        sb.append("vds");
        return sb.toString();
    }

    /* renamed from: i */
    public int mo19008i() {
        return this.generalNumber + this.anonymousNumber + this.vidNumber + this.mutiSignNumber;
    }

    /* renamed from: e */
    public void mo19003e(int i) {
        this.f692i = i;
    }

    /* renamed from: j */
    public int mo19009j() {
        return this.addr;
    }

    //915 mo19005f
    public void setAddr(int i) {
        this.addr = i;
    }

    /* renamed from: k */
    public boolean mo19010k() {
        return this.f694k;
    }

    //915 mo18990a
    public void setIsHideAllAmount(boolean z) {
        this.f694k = z;
    }

    public void setVxdNumber(int i) {
        this.vxdNumber = i;
    }

    public int getVxdNumber() {
        return this.vxdNumber;
    }

    /* renamed from: l */
    public String mo19011l() {
        return this.btcBalance;
    }

    //915 mo18997c
    public void setBtcBalance(String str) {
        this.btcBalance = str;
    }

    //915 mo19012m
    public boolean getIsEdit() {
        return this.isEdit;
    }

    //915 mo18994b
    public void setIsEdit(boolean z) {
        this.isEdit = z;
    }

    /* renamed from: c */
    public void mo18998c(boolean z) {
        this.f696m = z;
    }

    public boolean getIsSelect() {
        return this.isSelect;
    }

    public void setIsSelect(boolean z) {
        this.isSelect = z;
    }
}
