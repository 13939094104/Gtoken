package vdsMain.table;

import android.content.ContentValues;
import android.database.Cursor;
import androidx.annotation.NonNull;
import bitcoin.UInt256;
import vdsMain.db.WalletDB;

import java.util.Locale;

public class TxMarkTable extends WalletTable {
    /* access modifiers changed from: protected */
    /* renamed from: a */
    public AbstractTableItem getTableItem(Cursor cursor, int i) {
        return null;
    }

    public TxMarkTable(@NonNull WalletDB kiVar) {
        super(kiVar);
    }

    /* renamed from: a */
    public String getCreateFieldSql(int i) {
        return String.format(Locale.US, "(%s TEXT NOT NULL PRIMARY KEY, %s TEXT);", new Object[]{"txid", "tx_mark"});
    }

    //mo44378a
    public void inersetOrUpdateTxMark(UInt256 txid, String remark) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("txid", txid.hashString());
        contentValues.put("tx_mark", remark);
        getDbWritableDataHelper().replace(getTableName(), null, contentValues);
    }
}