package vdsMain.peer;

import generic.network.AddressInfo;
import vdsMain.StringToolkit;

public class PeerInfo {

    //f13290a
    protected AddressInfo mainAddressInfo;

    /* renamed from: b */
    protected AddressInfo f13291b;

    //f13292c
    //910 f13431c
    protected int protocalVersion = 0;

    /* renamed from: d */
    protected long f13293d = 0;

    //f13294e
    protected long serviceInt;

    //f13295f
    protected long time;

    //f13296g
    //910 f13435g
    protected long height;

    //f13297h
    protected long nonce;

    /* renamed from: i */
    protected long f13298i;

    /* renamed from: j */
    protected byte[] f13299j;

    //f13300k
    protected String subVersion;

    public PeerInfo(long j) {
        this.f13298i = j;
    }

    //mo44601a
    //910 mo44683a
    public int getProtocalVersion() {
        return this.protocalVersion;
    }

    //mo44602a
    public void setProtocalVersion(int i) {
        this.protocalVersion = i;
    }

    //mo44607b
    //910 mo44689b
    public AddressInfo getMainAddressInfo() {
        return this.mainAddressInfo;
    }

    //mo44604a
    public void setMainAddressInfo(AddressInfo addressInfo) {
        this.mainAddressInfo = addressInfo;
    }

    /* renamed from: c */
    public AddressInfo mo44610c() {
        return this.f13291b;
    }

    /* renamed from: b */
    public void mo44609b(AddressInfo addressInfo) {
        this.f13291b = addressInfo;
    }

    /* renamed from: d */
    public long mo44612d() {
        return this.serviceInt;
    }

    //mo44603a
    public void setServiceInt(long j) {
        this.serviceInt = j;
    }

    /* renamed from: e */
    public long mo44614e() {
        return this.time;
    }

    //mo44608b
    public void setTime(long j) {
        this.time = j;
    }

    //mo44615f
    //910 mo44697f
    public long getHeight() {
        return this.height;
    }

    //mo44611c
    //910 mo44693c
    public void setHeight(long j) {
        this.height = j;
    }

    //mo44613d
    public void setNonce(long j) {
        this.nonce = j;
    }

    /* renamed from: g */
    public long mo44616g() {
        return this.f13298i;
    }

    //mo44617h
    public String getSubVersion() {
        return this.subVersion;
    }

    //mo44605a
    public void setVersion(String str) {
        this.subVersion = str;
    }

    /* renamed from: i */
    public long mo44618i() {
        return this.f13293d;
    }

    /* renamed from: j */
    public String mo44619j() {
        return StringToolkit.bytesToString(this.f13299j);
    }

    /* renamed from: a */
    public void mo44606a(byte[] bArr) {
        this.f13299j = bArr;
    }
}
