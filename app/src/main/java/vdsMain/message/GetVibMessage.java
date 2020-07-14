package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.VParams;
import vdsMain.wallet.Wallet;

import java.io.IOException;

//bls
//910 bmh
public class GetVibMessage extends VMessage {

    /* renamed from: a */
    private long f11891a = (m9862a() - 2);

    /* renamed from: b */
    private long f11892b = (m9862a() + 2);

    public GetVibMessage(@NonNull Wallet izVar) {
        super(izVar, "getvib");
        if (this.f11891a < 1) {
            this.f11891a = 1;
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeUInt32T(this.f11891a);
        streamWriter.writeUInt32T(this.f11892b);
    }

    /* renamed from: a */
    private long m9862a() {
        VParams bjh = (VParams) this.wallet.getChainParams().mo43961n();
        int i = bjh.f11752v;
        long R = this.wallet.getCurrentBlockNo();
        if (R <= ((long) bjh.nBlockCountOf1stSeason)) {
            return 1;
        }
        return ((R - ((long) bjh.nBlockCountOf1stSeason)) / ((long) bjh.nBlockCountOfSeason)) + 2;
    }
}
