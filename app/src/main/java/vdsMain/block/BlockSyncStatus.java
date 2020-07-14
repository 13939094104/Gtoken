package vdsMain.block;

/* renamed from: ik */
public enum BlockSyncStatus {
    UNSYNC(0),
    SYNCING(1),
    SYNCHED(2);


    //f12942d
    private int value;

    private BlockSyncStatus(int i) {
        this.value = i;
    }

    //910 mo44032a
    //mo43970a
    public int getValue() {
        return this.value;
    }

    //910 m12284a
    public static BlockSyncStatus m12158a(int i) {
        switch (i) {
            case 0:
                return UNSYNC;
            case 1:
                return SYNCING;
            case 2:
                return SYNCHED;
            default:
                return UNSYNC;
        }
    }
}