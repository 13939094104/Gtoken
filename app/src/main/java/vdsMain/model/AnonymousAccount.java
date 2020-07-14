package vdsMain.model;

import android.content.ContentValues;
import android.database.Cursor;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import bitcoin.account.hd.HDSeed;
import com.vc.libcommon.exception.AddressFormatException;
import generic.exceptions.EncryptException;
import generic.exceptions.NotMatchException;
import vdsMain.DataTypeToolkit;
import vdsMain.table.AddressTable;
import vdsMain.table.WalletTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

//bre
public class AnonymousAccount extends Account {
    /* renamed from: a */
    public void initTableItemVariable(Cursor cursor, int i, int i2, int i3) {
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void mo40792b(Address jjVar) {
    }

    /* renamed from: c */
    public ContentValues getContentValues() {
        return null;
    }

    /* access modifiers changed from: protected */
    /* renamed from: g */
    public void mo40799g() {
    }

    public int hashCode() {
        return -2;
    }

    public AnonymousAccount(@NonNull WalletTable kjVar) {
        super(kjVar);
    }

    /* renamed from: a */
    public boolean isBitCoinAddress(Address jjVar) {
        return jjVar instanceof VAnonymousAddress;
    }

    /* renamed from: a */
    public List<Address> createAddress(CharSequence charSequence, int i, CharSequence... charSequenceArr) throws NotMatchException, AddressFormatException, EncryptException {
        String str;
        if (i >= 1) {
            this.wallet.checkWalletPassword(charSequence);
            AddressTable j = getWallet().getPersonalDB().getSelfAddressTable();
            ArrayList arrayList = new ArrayList();
            Vector d = this.wallet.getSelfAccountModel().getHDAccountVector();
            if (!d.isEmpty()) {
                HDSeed a = this.wallet.decryptToHDSeed(((HDAccount) d.get(0)).getEncyptedHDSeed(), charSequence);
                byte[] bArr = new byte[32];
                System.arraycopy(a.getExtKey().cKey.getCopyBytes(), 0, bArr, 0, 32);
                for (int i2 = 0; i2 < i; i2++) {
                    VAnonymousAddress bjb = new VAnonymousAddress(j, bArr, 0, charSequence.toString());
                    if (charSequenceArr != null && charSequenceArr.length > 0 && !TextUtils.isEmpty(charSequenceArr[0])) {
                        if (i2 > 1) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(charSequenceArr[0].toString());
                            sb.append(i2);
                            str = sb.toString();
                        } else {
                            str = charSequenceArr[0].toString();
                        }
                        bjb.setLabel(str, true);
                    }
                    arrayList.add(bjb);
                }
                DataTypeToolkit.setBytesZero(bArr);
                a.reset();
                this.wallet.lock();
                try {
                    checkAndAddToBitCoinCtxDestAddressMap((List<Address>) arrayList, new boolean[0]);
                    this.wallet.unLock();
                    return arrayList;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }  catch (Throwable th) {
                    this.wallet.unLock();
                    throw th;
                }
            } else {
                throw new EncryptException("Wallet not have hd seed yet");
            }
        } else {
            throw new IllegalArgumentException("address number must bigger than 0");
        }
    }
}
