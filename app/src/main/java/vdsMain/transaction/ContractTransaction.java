package vdsMain.transaction;

import androidx.annotation.NonNull;
import bitcoin.CSHA256;
import net.bither.bitherj.utils.Utils;
import vdsMain.BytesArrayBuffer;
import vdsMain.CTxDestination;
import vdsMain.DataTypeToolkit;
import vdsMain.wallet.Wallet;
import zcash.crypto.CRIPEMD160;

import java.util.Iterator;
import java.util.List;

//bjp
public class ContractTransaction {
    public byte[] f11795a = new byte[20];

    /* renamed from: b */
    public Transaction f11796b;

    public ContractTransaction(@NonNull Wallet izVar) {
    }

    /* renamed from: a */
    public CTxDestination mo42519a() {
        byte[] bArr = this.f11795a;
        if (bArr == null) {
            return null;
        }
        return new CTxDestinationContract(bArr);
    }

    /* renamed from: b */
    public Transaction mo42521b() {
        return this.f11796b;
    }

    /* renamed from: a */
    public void mo42520a(Transaction dhVar, boolean... zArr) {
        if (dhVar == null) {
            this.f11796b = null;
        } else {
            this.f11796b = dhVar.clone();
        }
        if (zArr.length == 0 || zArr[0]) {
            m9744c();
        }
    }

    /* renamed from: c */
    private void m9744c() {
        DataTypeToolkit.setBytesZero(this.f11795a);
        Transaction dhVar = this.f11796b;
        if (dhVar == null) {
            DataTypeToolkit.setBytesZero(this.f11795a);
            return;
        }
        Transaction D = dhVar.getCloneTransaction();
        byte[] bArr = new byte[32];
        BytesArrayBuffer gdVar = new BytesArrayBuffer(32);
        gdVar.writeAllBytes(D.getTxId().data());
        long j = 0;
        List d = D.getSelfTxOutList();
        byte[] bArr2 = new byte[4];
        if (d != null && !d.isEmpty()) {
            Iterator it = d.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                } else if (((TxOut) it.next()).getScript().mo43156a()) {
                    Utils.uint32ToByteArrayLE(j, bArr2, 0);
                    gdVar.writeAllBytes(bArr2);
                    break;
                } else {
                    j++;
                }
            }
        }
        new CSHA256().writeBytes(gdVar.getBytes(), gdVar.getWritePos()).mo9463b(bArr);
        new CRIPEMD160().mo40492a(bArr).mo40494b(this.f11795a);
    }
}
