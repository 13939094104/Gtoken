package vdsMain.table;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.orhanobut.logger.Logger;
import vdsMain.StringToolkit;


/* renamed from: fy */
public abstract class AbstractTableItem {

    /* renamed from: V */
    protected AbstractTable abstractTable = null;

    /* renamed from: W */
    protected Context context = null;

    /* renamed from: V */
    public String getKey2() {
        return null;
    }

    /* renamed from: W */
    public String getValue2() {
        return null;
    }

    //mo42423a
    public abstract String getKey();

    //mo40785a
    public abstract void initTableItemVariable(Cursor cursor, int i, int i2, int columnIndex);

    //mo40794c
    //915 mo40929c
    public abstract ContentValues getContentValues();

    //mo42432e_
    public abstract String getValue();

    public AbstractTableItem() {
    }

    public AbstractTableItem(AbstractTableItem abstractTableItem) {
        if (abstractTableItem != null) {
            this.context = abstractTableItem.context;
            this.abstractTable = abstractTableItem.abstractTable;
        }
    }

    public AbstractTableItem(Context context, AbstractTable abstractTable) {
        this.context = context;
        this.abstractTable = abstractTable;
    }

    //mo43590a
    public void setTable(AbstractTable abstractTable) {
        this.abstractTable = abstractTable;
    }

    /* renamed from: T */
    public AbstractTable getTable() {
        return this.abstractTable;
    }

    //mo43589a
    public void initAllTableItemVariable(Cursor cursor, int version, int i2) {
        int columnCount = cursor.getColumnCount();
        for (int index = 0; index < columnCount; index++) {
            try {
                initTableItemVariable(cursor, i2, version,index);
            } catch (Exception e) {
                String name = getClass().getName();
                StringBuilder sb = new StringBuilder();
                sb.append("loadFromDBColumn failed: ");
                sb.append(e.toString());
                Logger.e(name,sb.toString());
                e.printStackTrace();
            }
        }
    }

    //mo43591b
    public void replaceData(AbstractTable abstractTable) {
        ContentValues contentValues = getContentValues();
        if (this.abstractTable == null) {
            setTable(abstractTable);
        }
        abstractTable.getDbWritableDataHelper().replace(this.abstractTable.getTableName(), null, contentValues);
    }

    //  mo43585U
    public void mo43585U() {
        replaceData(this.abstractTable);
    }

    //mo43588a
    //910 mo43651a
    //915 mo43892a
    public void updateData(ContentValues contentValues, String str, String[] strArr) {
        updateData(this.abstractTable, contentValues, str, strArr);
    }

    //m11398a
    //910 m11523a
    private void updateData(AbstractTable abstractTable, ContentValues contentValues, String whereClause, String[] strArr) {
        if (this.abstractTable == null) {
            setTable(abstractTable);
        }
        abstractTable.getDbWritableDataHelper().update(this.abstractTable.getTableName(), contentValues, whereClause, strArr);
    }

    //mo43592c
    public void deleteData(AbstractTable abstractTable) {
        String key = getKey();
        String value = getValue();
        String key2 = getKey2();
        String value2 = getValue2();
        String sb2 = key + "=?";
        if (StringToolkit.isNull((CharSequence) key2)) {
            abstractTable.deleteDataSynchronized(sb2, new String[]{value});
            return;
        }
        String sb3 = sb2 + " and " + key2 + "=?";
        abstractTable.deleteDataSynchronized(sb3, new String[]{value, value2});
    }
}
