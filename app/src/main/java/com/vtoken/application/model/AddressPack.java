package com.vtoken.application.model;

import androidx.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;

public class AddressPack extends BaseObservable implements Parcelable {
    public static final Creator<AddressPack> CREATOR = new Creator<AddressPack>() {
        public AddressPack createFromParcel(Parcel parcel) {
            return new AddressPack(parcel);
        }

        public AddressPack[] newArray(int i) {
            return new AddressPack[i];
        }
    };
    String btcAddress;
    public boolean checked = false;
    String vdsAddress;

    public int describeContents() {
        return 0;
    }

    public AddressPack() {
    }

    protected AddressPack(Parcel parcel) {
        boolean z = false;
        this.vdsAddress = parcel.readString();
        this.btcAddress = parcel.readString();
        if (parcel.readByte() != 0) {
            z = true;
        }
        this.checked = z;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.vdsAddress);
        parcel.writeString(this.btcAddress);
        parcel.writeByte(this.checked ? (byte) 1 : 0);
    }

    public String getVdsAddress() {
        return this.vdsAddress;
    }

    public void setVdsAddress(String str) {
        this.vdsAddress = str;
    }

    public String getBtcAddress() {
        return this.btcAddress;
    }

    public void setBtcAddress(String str) {
        this.btcAddress = str;
    }

    public void setChecked(boolean z) {
        this.checked = z;
        notifyPropertyChanged(16);
    }

    public boolean isChecked() {
        return this.checked;
    }
}