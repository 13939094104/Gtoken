package vdsMain.transaction;

public enum SigVersion {
    BASE(0),
    WITNESS_V0(1);


    /* renamed from: c */
    private int f12367c;

    private SigVersion(int i) {
        this.f12367c = 0;
        this.f12367c = i;
    }

    /* renamed from: a */
    public static SigVersion m10834a(int i) {
        switch (i) {
            case 0:
                return BASE;
            case 1:
                return WITNESS_V0;
            default:
                return null;
        }
    }

    /* renamed from: a */
    public int mo43200a() {
        return this.f12367c;
    }
}
