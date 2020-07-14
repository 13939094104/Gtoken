package vdsMain.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import vdsMain.table.AbstractTable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* renamed from: fz */
public abstract class DataBaseHelper extends SQLiteOpenHelper {

    //f12724a
    private Lock lock = new ReentrantLock();

    //f12725b
    private Context context;

    //f12726c
    private SQLiteDatabase sqLiteDatabase;

    //f12727d
    private int oldVersion = -1;

    //f12728e
    private Vector<AbstractTable> tableVector = new Vector<>();

    //f12729f
    private HashMap<String, AbstractTable> tableMap = new HashMap<>();


    public DataBaseHelper(Context context, String prefix, String name, int version) {
        super(context, "/"+prefix+"/"+name,null,version);
        this.context = context;
    }

    //mo43596c
    public String getWriteDatabasePath() {
        return super.getWritableDatabase().getPath();
    }

    //mo43598d
    public Context getContext() {
        return this.context;
    }

    //mo43599e
    public void getWriteableSqlDatabase() {
        super.getWritableDatabase();
    }

    public void close() {
        SQLiteDatabase sQLiteDatabase = this.sqLiteDatabase;
        if (sQLiteDatabase != null && sQLiteDatabase.isOpen()) {
            if (this.sqLiteDatabase.inTransaction()) {
                this.sqLiteDatabase.setTransactionSuccessful();
                this.sqLiteDatabase.endTransaction();
            }
            this.sqLiteDatabase.close();
            this.sqLiteDatabase = null;
        }
        super.close();
    }

    //mo43594a
    public synchronized boolean addAbstractTable(AbstractTable abstractTable) {
        if (abstractTable == null) {
            return false;
        }
        String tableName = abstractTable.getTableName();
        if (this.tableMap.containsKey(tableName)) {
            return false;
        }
        this.tableMap.put(tableName, abstractTable);
        this.tableVector.add(abstractTable);
        return true;
    }

    //mo43595a
    public synchronized boolean addAbstractTableByName(AbstractTable abstractTable, String str) {
        if (abstractTable == null) {
            return false;
        }
        if (this.tableMap.containsKey(str)) {
            return false;
        }
        abstractTable.setTableName(str);
        this.tableMap.put(str, abstractTable);
        this.tableVector.add(abstractTable);
        return true;
    }

    public void onOpen(SQLiteDatabase sQLiteDatabase) {
        this.sqLiteDatabase = sQLiteDatabase;
        int version = this.sqLiteDatabase.getVersion();
        Iterator it = this.tableVector.iterator();
        while (it.hasNext()) {
            ((AbstractTable) it.next()).createTable(version);
        }
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int oldVersion, int newVersion) {
        this.oldVersion = oldVersion;
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        this.sqLiteDatabase = sQLiteDatabase;
    }

    public SQLiteDatabase getWritableDatabase() {
        return this.sqLiteDatabase;
    }

    public SQLiteDatabase getReadableDatabase() {
        return this.sqLiteDatabase;
    }

    //mo43600f
    //910 mo43663f
    public void beginTransaction() {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        if (!writableDatabase.inTransaction()) {
            writableDatabase.beginTransaction();
        }
    }

    //mo43593a
    public void endTransaction(boolean isSuccess) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        if (writableDatabase.inTransaction()) {
            if (isSuccess) {
                writableDatabase.setTransactionSuccessful();
            }
            writableDatabase.endTransaction();
        }
    }

    //mo43601g
    public void clearTables() {
        beginTransaction();
        Iterator it = this.tableVector.iterator();
        while (it.hasNext()) {
            ((AbstractTable) it.next()).clearTable();
        }
        endTransaction(true);
    }
}
