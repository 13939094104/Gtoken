package vdsMain.block;

public class CValidationState {

    /* renamed from: a */
    public C3109a f8247a = C3109a.MODE_VALID;

    /* renamed from: b */
    public int f8248b = 0;

    /* renamed from: c */
    public String f8249c = "";

    /* renamed from: d */
    public int f8250d = 0;

    /* renamed from: e */
    public boolean f8251e = false;

    /* renamed from: at$a */
    /* compiled from: CValidationState */
    public enum C3109a {
        MODE_VALID,
        MODE_INVALID,
        MODE_ERROR
    }

    /* renamed from: a */
    public boolean mo41041a(int i, boolean z, int i2, String str) {
        return mo41042a(i, z, i2, str, false);
    }

    /* renamed from: a */
    public boolean mo41042a(int i, boolean z, int i2, String str, boolean z2) {
        this.f8250d = i2;
        this.f8249c = str;
        this.f8251e = z2;
        if (this.f8247a == C3109a.MODE_ERROR) {
            return z;
        }
        this.f8248b += i;
        this.f8247a = C3109a.MODE_INVALID;
        return z;
    }

    /* renamed from: a */
    public boolean mo41043a(boolean z, int i, String str) {
        return mo41042a(0, z, i, str, false);
    }

    /* renamed from: a */
    public int mo41040a() {
        return this.f8250d;
    }

    /* renamed from: b */
    public String mo41044b() {
        return this.f8249c;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CValidationState{mode=");
        sb.append(this.f8247a);
        sb.append(", nDoS=");
        sb.append(this.f8248b);
        sb.append(", strRejectReason='");
        sb.append(this.f8249c);
        sb.append('\'');
        sb.append(", chRejectCode=");
        sb.append(this.f8250d);
        sb.append(", corruptionPossible=");
        sb.append(this.f8251e);
        sb.append('}');
        return sb.toString();
    }
}
