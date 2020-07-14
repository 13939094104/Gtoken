package vdsMain.model;

import android.content.ContentValues;
import android.database.Cursor;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import generic.exceptions.EncryptException;
import generic.exceptions.NotMatchException;
import vdsMain.db.PersonalDB;
import vdsMain.table.AddressTable;
import vdsMain.table.WalletTable;

import java.util.ArrayList;
import java.util.List;

public abstract class GeneralAccount extends Account {
    /* access modifiers changed from: protected */
    //mo40678a
    public abstract Address getAddressEntity(WalletTable walletTable, CharSequence charSequence);

    /* renamed from: a */
    public void initTableItemVariable(Cursor cursor, int i, int i2, int columnIndex) {
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
        return -1;
    }

    public GeneralAccount(@NonNull WalletTable kjVar) {
        super(kjVar);
    }

    /* renamed from: a */
    public List<Address> createAddress(CharSequence charSequence, int addressNumber, CharSequence... charSequenceArr) throws EncryptException, NotMatchException {
        if (addressNumber >= 1) {
            PersonalDB personalDB = getWallet().getPersonalDB();
            this.wallet.checkWalletPassword(charSequence);
            ArrayList arrayList = new ArrayList();
            this.wallet.lock();
            try {
                AddressTable addressTable = personalDB.getSelfAddressTable();
                for (int i = 0; i < addressNumber; i++) {
                    Address address = getAddressEntity(addressTable, charSequence);
                    if (charSequenceArr != null && charSequenceArr.length > 0 && !TextUtils.isEmpty(charSequenceArr[0])) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(charSequenceArr[0].toString());
                        sb.append(i);
                        address.setLabel(sb.toString(), true);
                    }
                    arrayList.add(address);
                }
                checkAndAddToBitCoinCtxDestAddressMap((List<Address>) arrayList, true);
                this.wallet.unLock();
                return arrayList;
            }  catch (Throwable th) {
                th.printStackTrace();
                this.wallet.unLock();
                throw th;
            }
        } else {
            throw new IllegalArgumentException("address number must bigger than 0");
        }
    }
}
