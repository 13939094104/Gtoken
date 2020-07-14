package vdsMain.transaction;


import java.util.List;

//bsl
public interface ZTxInterface extends TransactionInteface {
    /* renamed from: A_ */
    boolean isSpendDescriptionListNotEmpty();

    /* renamed from: B_ */
    boolean isOutputDescriptionListNotEmpty();

    /* renamed from: a */
    void mo43033a(OutputDescription brj);

    /* renamed from: a */
    void mo43034a(SpendDescription brl);

    /* renamed from: a */
    void setSpendDescriptionList(List<SpendDescription> list, boolean z);

    /* renamed from: a */
    void setSigt(byte[] bArr, boolean z);

    /* renamed from: a_ */
    void setVBalance(long j);

    /* renamed from: b */
    void setOutputDescriptionList(List<OutputDescription> list, boolean z);

    /* renamed from: d */
    void mo43039d(long j);

    /* renamed from: e */
    void setExp(long j);

    /* renamed from: i */
    List<OutputDescription> getOutputDescriptionList();

    /* renamed from: j */
    byte[] getBsign();

    /* renamed from: l */
    long getVBalance();

    /* renamed from: m */
    List<SpendDescription> getSpendDescriptionList();

    /* renamed from: x_ */
    long getExp();
}
