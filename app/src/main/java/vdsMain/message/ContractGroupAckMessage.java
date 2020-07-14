package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.contract.CommuSpvStruct;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

//bne
public class ContractGroupAckMessage extends ContractAckMessage {

    /* renamed from: b */
    public CommuSpvStruct f11946b;

    public ContractGroupAckMessage(@NonNull Wallet izVar, String str, int i) {
        super(izVar, str, i);
    }

    /* access modifiers changed from: protected */
    /* renamed from: c */
    public void mo42611c(StreamWriter streamWriter) throws IOException {
        CommuSpvStruct bjn = this.f11946b;
        if (bjn == null) {
            new CommuSpvStruct().serialToStream(streamWriter);
        } else {
            bjn.serialToStream(streamWriter);
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: d */
    public void mo42612d() {
        this.f11946b = new CommuSpvStruct();
        this.f11946b.decodeSerialStream(getTempInput());
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void mo42609b(StreamWriter streamWriter) throws IOException {
        super.mo42609b(streamWriter);
        mo42611c(streamWriter);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo42608a() throws UnsupportedEncodingException {
        super.mo42608a();
        this.f11946b = null;
        if (!mo42610b()) {
            mo42612d();
        }
    }
}
