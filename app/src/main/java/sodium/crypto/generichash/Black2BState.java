package sodium.crypto.generichash;

public class Black2BState {

    /* renamed from: a */
    private byte[] f3255a;

    private native byte[] createCInstanceData();

    private native byte[] update(byte[] bArr, byte[] bArr2);

    public Black2BState() {
        this.f3255a = null;
        this.f3255a = createCInstanceData();
    }

    /* renamed from: a */
    public byte[] mo38396a() {
        return this.f3255a;
    }

    /* renamed from: a */
    public void mo38395a(byte[] bArr) {
        this.f3255a = update(this.f3255a, bArr);
    }
}