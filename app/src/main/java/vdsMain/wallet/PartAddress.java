package vdsMain.wallet;

import vdsMain.AddressType;
import vdsMain.CTxDestination;

public class PartAddress {

    //915 f13495a
    CTxDestination destination;

    //915 f13496b
    String addressString;

    //915 f13497c
    String label;

    /* renamed from: d */
    int f13498d;

    /* renamed from: e */
    AddressType f13499e = AddressType.GENERAL;

    //915 mo44231a
    public void setDes(CTxDestination oVar) {
        this.destination = oVar;
    }

    //915 mo44229a
    public void setAddressString(String str) {
        this.addressString = str;
    }

    //915 mo44232b
    public void setLabel(String str) {
        this.label = str;
    }

    //915 mo44228a
    public void setAddressOrderIndex(int i) {
        this.f13498d = i;
    }

    //915 mo44230a
    public void setAddressType(AddressType jmVar) {
        this.f13499e = jmVar;
    }
}