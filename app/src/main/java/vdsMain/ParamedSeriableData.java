package vdsMain;

import androidx.annotation.NonNull;
import generic.serialized.SeriableData;
import vdsMain.wallet.ChainParams;

public abstract class ParamedSeriableData extends SeriableData {

    //f12972k
    protected ChainParams chainParams;

    public ParamedSeriableData(@NonNull ChainParams uVar) {
        this.chainParams = uVar;
    }

    public ParamedSeriableData(ParamedSeriableData itVar) {
        super((SeriableData) itVar);
        this.chainParams = itVar.chainParams;
    }

    /* access modifiers changed from: protected */
    /* renamed from: w */
    public ChainParams mo44013w() {
        return this.chainParams;
    }
}