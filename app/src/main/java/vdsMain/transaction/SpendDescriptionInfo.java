package vdsMain.transaction;

import bitcoin.BaseBlob;
import bitcoin.UInt256;
import vdsMain.SaplingPaymentAddress;
import zcash.RustZCash;

public class SpendDescriptionInfo {

    /* renamed from: a */
    public SaplingPaymentAddress f12225a;

    /* renamed from: b */
    public SaplingNote f12226b;

    /* renamed from: c */
    public UInt256 f12227c;

    /* renamed from: d */
    public UInt256 f12228d;

    /* renamed from: e */
    public SaplingWitness f12229e;

    /* renamed from: f */
    public SaplingOutpoint f12230f;

    /* renamed from: g */
    public long f12231g;

    /* renamed from: h */
    public UInt256 f12232h = new UInt256();

    public SpendDescriptionInfo(SaplingPaymentAddress bsf, SaplingOutpoint brk, long j, SaplingNote bsc, UInt256 uInt256, SaplingWitness bsh) {
        this.f12225a = bsf;
        this.f12226b = new SaplingNote(bsc);
        this.f12228d = new UInt256((BaseBlob) uInt256);
        this.f12229e = new SaplingWitness(bsh);
        this.f12227c = new UInt256();
        this.f12231g = j;
        this.f12230f = brk;
        RustZCash.sapling_generate_r(this.f12227c.data());
        this.f12227c.updateHash();
    }
}
