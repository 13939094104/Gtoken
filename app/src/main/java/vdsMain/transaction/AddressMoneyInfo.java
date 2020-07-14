package vdsMain.transaction;

import java.util.Locale;

public class AddressMoneyInfo {

    //f13077a
    public String receiveAddress = "";

    //f13078b
    public long sendValue = 0;

    //f13079c
    private int flag = 0;

    public AddressMoneyInfo() {
    }

    public AddressMoneyInfo(String str, long j) {
        this.receiveAddress = str;
        this.sendValue = j;
    }

    public AddressMoneyInfo(String str, long sendValue, int flag) {
        this.receiveAddress = str;
        this.sendValue = sendValue;
        this.flag = flag;
    }

    //mo44254a
    public int getFlag() {
        return this.flag;
    }

    /* renamed from: a */
    public void mo44255a(int i) {
        this.flag = i;
    }

    public String toString() {
        return String.format(Locale.getDefault(), "%s { %s, %d, %d}", new Object[]{getClass().getName(), this.receiveAddress, Integer.valueOf(this.flag), Long.valueOf(this.sendValue)});
    }
}