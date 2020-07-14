package vdsMain.table;

import android.content.ContentValues;
import android.database.Cursor;
import androidx.annotation.NonNull;
import vdsMain.db.WalletDB;
import vdsMain.model.Account;
import vdsMain.model.HDAccount;

import java.util.Locale;

public class HDAccountTable extends WalletTable {

    public HDAccountTable(@NonNull WalletDB walletDB) {
        super(walletDB);
    }

    /* renamed from: a */
    public String getCreateFieldSql(int i) {
        return String.format(Locale.US, "(%s text primary key not null, %s integer, %s blob, %s blob, %s integer);", new Object[]{"id", "addr", "seed", "i_pub", "monitor"});
    }

    //mo41200a
    public void replaceHDAccount(Account account) {
        replace((AbstractTableItem) account);
    }

    //mo41199a
    public void updateSeedBytes(HDAccount hdAccount) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("seed", hdAccount.getEncyptedHDSeed().getBytes());
        getDbWritableDataHelper().update(getTableName(), contentValues, "id=?", new String[]{Integer.toString(hdAccount.getId())});
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public AbstractTableItem getTableItem(Cursor cursor, int i) {
        return new HDAccount(this);
    }
}