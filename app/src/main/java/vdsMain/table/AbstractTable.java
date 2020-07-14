package vdsMain.table;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.vc.libcommon.exception.AddressFormatException;
import generic.exceptions.UnsupportAddressException;
import vdsMain.db.DataBaseHelper;

/* renamed from: fx */
public abstract class AbstractTable {

    //f12718a
    protected Context context;

    //f12719b
    private DataBaseHelper dataBaseHelper;

    //f12720c
    private String tableName;

    //f12721d
    private Object lock = new Object();

    //mo41100a
    public abstract AbstractTableItem getTableItem(Cursor cursor, int i) throws UnsupportAddressException, AddressFormatException;

    //mo41101a
    public abstract String getCreateFieldSql(int i);

    /* access modifiers changed from: protected */
    /* renamed from: c */
    public String[] mo43576c(int i) {
        return null;
    }

    public AbstractTable(Context context, DataBaseHelper dataBaseHelper) {
        this.context = context;
        this.dataBaseHelper = dataBaseHelper;
    }

    /* renamed from: a */
    public Context getContext() {
        return this.context;
    }

    //mo43566b
    public SQLiteDatabase getDbWritableDataHelper() {
        DataBaseHelper baseHelper = this.dataBaseHelper;
        if (baseHelper != null) {
            return baseHelper.getWritableDatabase();
        }
        return null;
    }

    //mo43574c
    //910 mo43637c
    public void beginTransaction() {
        synchronized (this.lock) {
            SQLiteDatabase b = getDbWritableDataHelper();
            if (!b.inTransaction()) {
                b.beginTransaction();
            }
        }
    }

    //mo43565a
    //910 mo43628a
    public void endTransaction(boolean isSuccess) {
        synchronized (this.lock) {
            SQLiteDatabase b = getDbWritableDataHelper();
            if (b != null) {
                if (b.inTransaction()) {
                    if (isSuccess) {
                        b.setTransactionSuccessful();
                    }
                    b.endTransaction();
                }
            }
        }
    }

    //mo43579d
    //910 mo43642d
    public boolean isInTransaction() {
        synchronized (this.lock) {
            SQLiteDatabase sqLiteDatabase = getDbWritableDataHelper();
            if (sqLiteDatabase == null) {
                return false;
            }
            boolean inTransaction = sqLiteDatabase.inTransaction();
            return inTransaction;
        }
    }

    //mo43563a
    public synchronized void execSqlSynchronized(String str, Object[] objArr) {
        execSQL(str, objArr);
    }

    //mo43570b
    public synchronized void execSQL(String str, Object[] objArr) {
        getDbWritableDataHelper().execSQL(str, objArr);
    }

    //mo43562a
    public void execSql(String sql) {
        execSqlSynchronized(sql);
    }

    //mo43569b
    public synchronized void execSqlSynchronized(String str) {
        getDbWritableDataHelper().execSQL(str);
    }

    //mo43572c
    public synchronized Cursor rawQueryNoArgs(String str) {
        return getDbWritableDataHelper().rawQuery(str, null);
    }

    //mo43564a
    public synchronized void deleteDataSynchronized(String str, String[] strArr) {
        deleteData(str, strArr);
    }

    /* access modifiers changed from: protected */
    //mo43571b
    public void deleteData(String where, String[] strArr) {
        getDbWritableDataHelper().delete(getTableName(), where, strArr);
    }

    //mo43573c
    //910 mo43636c
    public Cursor rawQuery(String str, String[] strArr) {
        return getDbWritableDataHelper().rawQuery(str, strArr);
    }

    //mo43560a
    public void closeCursor(Cursor cursor) {
        if (!cursor.isClosed()) {
            cursor.close();
        }
    }

    //mo43580e
    public Cursor selectAll() {
        if (getDbWritableDataHelper() == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ");
        sb.append(getTableName());
        return rawQuery(sb.toString(), null);
    }

    //mo43567b
    public AbstractTableItem getCompleteTableItem(Cursor cursor) {
        if (!cursor.moveToNext()) {
            return null;
        }
        AbstractTableItem abstractTableItem = null;
        try {
            abstractTableItem = getTableItem(cursor, getDbWritableDataHelper().getVersion());
        } catch (UnsupportAddressException e) {
            e.printStackTrace();
        } catch (AddressFormatException e) {
            e.printStackTrace();
        }
        abstractTableItem.initAllTableItemVariable(cursor, this.dataBaseHelper.getWritableDatabase().getVersion(), 0);
        return abstractTableItem;
    }

    //mo43578d
    public final void setTableName(String str) {
        this.tableName = str;
    }

    //mo43581f
    //910 mo43644f
    //915 mo43885f
    public final String getTableName() {
        return this.tableName;
    }

    //mo43561a
    public void replace(AbstractTableItem abstractTableItem) {
        replaceSynchronized(abstractTableItem);
    }

    //mo43568b
    public synchronized void replaceSynchronized(AbstractTableItem abstractTableItem) {
        getDbWritableDataHelper().replace(getTableName(), null, abstractTableItem.getContentValues());
    }

    //mo43575c
    public synchronized void replaceDataSynchronized(AbstractTableItem abstractTableItem) {
        abstractTableItem.replaceData(this);
    }

    //mo43577d
    public synchronized void deleteDataByOtherTableItemSynchronized(AbstractTableItem abstractTableItem) {
        abstractTableItem.deleteData(this);
    }

    //mo43582g
    public void clearTable() {
        SQLiteDatabase b = getDbWritableDataHelper();
        StringBuilder sb = new StringBuilder();
        sb.append("delete from ");
        sb.append(getTableName());
        sb.append(";");
        b.execSQL(sb.toString());
    }

    //mo42535b
    public void createTable(int i) {
        String str;
        String sb2 = "create table if not exists " +
                getTableName();
        String a = getCreateFieldSql(i);
        int i2 = 0;
        if (a.charAt(0) != ' ') {
            str = sb2 + " " + a;
        } else {
            str = sb2 + a;
        }
        if (str.charAt(str.length() - 1) != ';') {
            str = str + ";";
        }
        getDbWritableDataHelper().execSQL(str);
        String[] c = mo43576c(i);
        if (c != null && c.length != 0) {
            StringBuffer stringBuffer = new StringBuffer("create index if not exists ");
            stringBuffer.append(getTableName());
            stringBuffer.append("_index on ");
            stringBuffer.append(getTableName());
            stringBuffer.append("(");
            while (i2 < c.length) {
                stringBuffer.append(c[i2]);
                i2++;
                if (i2 != c.length) {
                    stringBuffer.append(",");
                }
            }
            stringBuffer.append(")");
            getDbWritableDataHelper().execSQL(stringBuffer.toString());
        }
    }

    //mo43583h
    public int getVersion() {
        return getDbWritableDataHelper().getVersion();
    }
}
