package vdsMain.table;

import android.content.ContentValues;
import android.database.Cursor;
import androidx.annotation.NonNull;
import vdsMain.db.WalletDB;

import java.util.Locale;

public class SubAddressTable extends WalletTable {
    /* access modifiers changed from: protected */
    /* renamed from: a */
    public AbstractTableItem getTableItem(Cursor cursor, int i) {
        return null;
    }

    public SubAddressTable(@NonNull WalletDB walletDB) {
        super(walletDB);
    }

    /* renamed from: a */
    public String getCreateFieldSql(int i) {
        return String.format(Locale.getDefault(), "(%s TEXT primary key, %s TEXT)", new Object[]{"addr", "lable"});
    }

    //915 mo44748e
    //mo44369e
    public String getLabelByAddressString(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * from ");
        sb.append(getTableName());
        sb.append(" WHERE ");
        sb.append("addr");
        sb.append("=? ");
        Cursor c = rawQuery(sb.toString(), new String[]{str});
        String string = c.moveToNext() ? c.getString(1) : null;
        closeCursor(c);
        return string;
    }

    /* renamed from: a */
    public void mo44368a(String str, String str2) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("addr", str);
        contentValues.put("lable", str2);
        getDbWritableDataHelper().replace(getTableName(), null, contentValues);
    }
}
