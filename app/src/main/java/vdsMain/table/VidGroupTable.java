package vdsMain.table;

import android.content.ContentValues;
import android.database.Cursor;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import vdsMain.db.WalletDB;
import vdsMain.model.VidGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VidGroupTable extends WalletTable {
    /* access modifiers changed from: protected */
    /* renamed from: a */
    public AbstractTableItem getTableItem(Cursor cursor, int i) {
        return null;
    }

    public VidGroupTable(@NonNull WalletDB kiVar) {
        super(kiVar);
    }

    /* renamed from: a */
    public String getCreateFieldSql(int i) {
        return String.format(Locale.getDefault(), "(%s integer primary key autoincrement, %s text,%s integer)", new Object[]{"_id", "group_label", "group_index"});
    }

    /* renamed from: a */
    public void mo44379a(VidGroup vidGroup) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("group_label", vidGroup.mo44282e());
        contentValues.put("group_index", Integer.valueOf(vidGroup.mo44283f()));
        getDbWritableDataHelper().replace(getTableName(), null, contentValues);
    }

    //mo44384i
    public List<VidGroup> getVidGroupList() {
        Cursor c = rawQuery(String.format(Locale.US, "SELECT * FROM %s order by %s", new Object[]{getTableName(), "group_index"}), null);
        ArrayList<VidGroup> arrayList = new ArrayList<>();
        boolean z = false;
        while (c.moveToNext()) {
            int i = c.getInt(0);
            String string = c.getString(1);
            int i2 = c.getInt(2);
            VidGroup vidGroup = new VidGroup(this);
            vidGroup.setId(i);
            vidGroup.setGroupLabel(string);
            vidGroup.setGroupIndex(i2);
            arrayList.add(vidGroup);
            if (i == -1) {
                z = true;
            }
        }
        closeCursor(c);
        if (!z) {
            VidGroup vidGroup = new VidGroup(this);
            vidGroup.setId(-1);
            vidGroup.setGroupLabel("");
            vidGroup.setGroupIndex(0);
            vidGroup.mo43585U();
            arrayList.add(0, vidGroup);
        }
        return arrayList;
    }

    /* renamed from: b */
    public void mo44380b(VidGroup jsVar) {
        if (jsVar != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("delete from ");
            sb.append(getTableName());
            sb.append(" where ");
            sb.append("_id");
            sb.append("='");
            sb.append(Long.toString((long) jsVar.getId()));
            sb.append("';");
            getDbWritableDataHelper().execSQL(sb.toString());
        }
    }

    /* renamed from: d */
    public void mo44381d(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete from ");
        sb.append(getTableName());
        sb.append(" where ");
        sb.append("_id");
        sb.append("='");
        sb.append(Long.toString((long) i));
        sb.append("';");
        getDbWritableDataHelper().execSQL(sb.toString());
    }

    @Deprecated
    /* renamed from: e */
    public VidGroup mo44383e(String str) {
        VidGroup jsVar = null;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * from ");
        sb.append(getTableName());
        sb.append(" WHERE ");
        sb.append("group_label");
        sb.append("=? ");
        Cursor c = rawQuery(sb.toString(), new String[]{str});
        if (c.moveToNext()) {
            int i = c.getInt(0);
            int i2 = c.getInt(2);
            VidGroup jsVar2 = new VidGroup(this);
            jsVar2.setId(i);
            jsVar2.setGroupLabel(str);
            jsVar2.setGroupIndex(i2);
            jsVar = jsVar2;
        }
        closeCursor(c);
        return jsVar;
    }

    /* renamed from: e */
    public VidGroup mo44382e(int i) {
        VidGroup jsVar;
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * from ");
        sb.append(getTableName());
        sb.append(" WHERE ");
        sb.append("_id");
        sb.append("=? ");
        Cursor c = rawQuery(sb.toString(), new String[]{Integer.toString(i)});
        if (c.moveToNext()) {
            String string = c.getString(1);
            int i2 = c.getInt(2);
            jsVar = new VidGroup(this);
            jsVar.setId(i);
            jsVar.setGroupLabel(string);
            jsVar.setGroupIndex(i2);
        } else {
            jsVar = null;
        }
        closeCursor(c);
        return jsVar;
    }

    /* renamed from: j */
    public int mo44385j() {
        int i = 0;
        Cursor c = rawQuery(String.format(Locale.US, "SELECT %s FROM %s order by %s desc", new Object[]{"group_index", getTableName(), "group_index"}), null);
        if (c.moveToNext()) {
            i = c.getInt(0);
        }
        closeCursor(c);
        return i;
    }
}
