package vdsMain.table;

import android.database.Cursor;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import vdsMain.db.WalletDB;
import vdsMain.model.Address;

public class OriginNodeTable extends WalletTable {
    /* access modifiers changed from: protected */
    /* renamed from: a */
    public AbstractTableItem getTableItem(Cursor cursor, int i) {
        return null;
    }

    public OriginNodeTable(@NonNull WalletDB kiVar) {
        super(kiVar);
    }

    /* renamed from: a */
    public String getCreateFieldSql(int i) {
        return String.format(Locale.US, "(%s TEXT primary key)", new Object[]{"pub"});
    }

    /* renamed from: i */
    public List<String> mo44356i() {
        Cursor c = rawQuery(String.format(Locale.US, "SELECT * FROM %s ", new Object[]{getTableName()}), null);
        ArrayList arrayList = new ArrayList();
        while (c.moveToNext()) {
            arrayList.add(c.getString(0));
        }
        closeCursor(c);
        return arrayList;
    }

    //mo44355a
    public void deleteDataByAddressPubKey(Address address) {
        if (address != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("delete from ");
            sb.append(getTableName());
            sb.append(" where ");
            sb.append("pub");
            sb.append("='");
            sb.append(address.getSelfPubKey().getCKeyID().getHash());
            sb.append("';");
            getDbWritableDataHelper().execSQL(sb.toString());
        }
    }
}