package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.VWallet;
import vdsMain.VibInfo;
import vdsMain.wallet.Wallet;

//bms
public class VibMessage extends VMessage {

    //f11940a
    private VibInfo vibInfo;

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) {
    }

    public VibMessage(@NonNull Wallet izVar) {
        super(izVar, "vibinfo");
    }

    public void onDecodeSerialData() {
        this.vibInfo = new VibInfo((VWallet) this.wallet);
        try {
            this.vibInfo.decodeSerialStream(getTempInput());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //mo42605a
    public VibInfo getVibInfo() {
        return this.vibInfo;
    }
}