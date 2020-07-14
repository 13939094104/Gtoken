package vdsMain.table;

import android.content.Context;
import android.database.Cursor;
import vdsMain.db.DataBaseHelper;

import java.util.HashMap;

public class SettingsTable extends AbstractTable {

    /* renamed from: b */
    HashMap<String, String> hashMap = new HashMap<>();

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public AbstractTableItem getTableItem(Cursor cursor, int i) {
        return null;
    }

    /* renamed from: a */
    public String getCreateFieldSql(int i) {
        return "(name text not null primary key,  value text);";
    }

    /* access modifiers changed from: protected */
    /* renamed from: c */
    public void mo43615c(String str, String str2) {

    }

    public SettingsTable(Context context, DataBaseHelper dataBaseHelper) {
        super(context, dataBaseHelper);
    }

    //mo43612a
    //915 mo43916a
    public synchronized String getMapValue(String key, String defaultValue) {
        String tempStr;
        tempStr = (String) this.hashMap.get(key);
        if (tempStr == null) {
            tempStr = defaultValue;
        }
        return tempStr;
    }


    //mo43614b
    //915 mo43918b
    public synchronized void insertOrUpdateData(String name, String value) {
        boolean isDelete;
        boolean containsKey = this.hashMap.containsKey(name);
        if (containsKey) {
            this.hashMap.remove(name);
        }
        if (value != null) {
            this.hashMap.put(name, value);
            isDelete = false;
        } else {
            isDelete = true;
        }
        if (isDelete) {
            deleteDataSynchronized("name=?", new String[]{name});
        } else if (containsKey) {
            StringBuilder sb = new StringBuilder();
            sb.append("update ");
            sb.append(getTableName());
            sb.append(" set value=? where name=?");
            execSqlSynchronized(sb.toString(), new Object[]{value, name});
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("insert into ");
            sb2.append(getTableName());
            sb2.append(" ( name, value ) values ( ? , ? )");
            execSqlSynchronized(sb2.toString(), new Object[]{name, value});
        }
    }

    //915 mo43917a
    public boolean getBoolValue(String str, boolean defaultValue) {
        String value = (String) this.hashMap.get(str);
        return (value == null || value.isEmpty()) ? defaultValue : java.lang.Boolean.parseBoolean(value);
    }

    //915 mo43919b
    public void setBoolValue(String str, boolean z) {
        insertOrUpdateData(str, Boolean.toString(z));
    }

    //mo43616i
    public void selectAllDataAndInit() {
        this.hashMap.clear();
        beginTransaction();
        Cursor cursor = selectAll();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String key = cursor.getString(0);
                String value = cursor.getString(1);
                if (value != null) {
                    this.hashMap.put(key, value);
                }
                mo43615c(key, value);
            }
        }
        closeCursor(cursor);
        endTransaction(false);
    }

    /* renamed from: g */
    public void clearTable() {
        this.hashMap.clear();
        super.clearTable();
    }
}
