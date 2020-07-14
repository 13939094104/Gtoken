package vdsMain.transaction;

public class TxInfo {

    //f13350a
    String sendAddress;

    //f13351b
    String receiveAddress;

    /* renamed from: c */
    String f13352c;

    //f13353d
    String sumVinValue;

    //f13354e
    String sendSumValue;

    //f13355f
    String selfAddressValue;

    //f13356g
    String feeValue;

    //f13357h
    String selfPaySumValue;

    //mo44674a
    public String getSendAddress() {
        return this.sendAddress;
    }

    /* renamed from: a */
    public void mo44675a(String str) {
        this.sendAddress = str;
    }

    /* renamed from: b */
    public String getReceiveAddress() {
        return this.receiveAddress;
    }

    //mo44677b
    public void setReceiveAddress(String str) {
        this.receiveAddress = str;
    }

    /* renamed from: c */
    public String mo44678c() {
        return this.f13352c;
    }

    /* renamed from: c */
    public void mo44679c(String str) {
        this.f13352c = str;
    }

    //mo44680d
    public String getSumVinValue() {
        return this.sumVinValue;
    }

    //mo44681d
    public void setSumVinValue(String str) {
        this.sumVinValue = str;
    }

    //mo44682e
    public String getSendSumValue() {
        return this.sendSumValue;
    }

    //mo44683e
    public void setSendSumValue(String str) {
        this.sendSumValue = str;
    }

    /* renamed from: f */
    public String mo44684f() {
        return this.selfAddressValue;
    }

    //mo44685f
    public void setSelfAddressValue(String str) {
        this.selfAddressValue = str;
    }

    //mo44686g
    public String getFeeValue() {
        return this.feeValue;
    }

    //mo44687g
    public void setFeeValue(String str) {
        this.feeValue = str;
    }

    /* renamed from: h */
    public String getSelfPaySumValue() {
        return this.selfPaySumValue;
    }

    //mo44689h
    public void setSelfPaySumValue(String str) {
        this.selfPaySumValue = str;
    }
}
