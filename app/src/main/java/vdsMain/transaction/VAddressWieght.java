package vdsMain.transaction;

import bitcoin.UInt256;

import java.io.Serializable;

public class VAddressWieght implements Serializable {

    //f13126a
    protected double weight;

    //f13127b
    protected UInt256 txid = new UInt256();

    //f13128c
    protected long blockNo = -1;

    /* renamed from: a */
    public double mo44273a() {
        return this.weight;
    }

    //mo44274a
    public void setWeight(double d) {
        this.weight = d;
    }

    //mo44276a
    public void setTxid(UInt256 uInt256) {
        this.txid = uInt256;
    }

    /* renamed from: b */
    public long mo44277b() {
        return this.blockNo;
    }

    //mo44275a
    public void setBlockNo(long j) {
        this.blockNo = j;
    }
}