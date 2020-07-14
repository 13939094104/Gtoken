package vdsMain.model;

import android.content.ContentValues;
import android.database.Cursor;
import androidx.annotation.NonNull;
import vdsMain.table.WalletTable;
import vdsMain.table.WalletTableItem;

import java.util.Locale;

public class VidGroup extends WalletTableItem {

    //f13129a
    private int id;

    //f13130b
    private String groupLabel;

    //f13131c
    private int groupIndex;

    /* renamed from: a */
    public String getKey() {
        return "_id";
    }

    public VidGroup(@NonNull WalletTable kjVar) {
        super(kjVar);
    }

    /* renamed from: a */
    public void initTableItemVariable(Cursor cursor, int i, int i2, int i3) {
        switch (i3) {
            case 0:
                this.id = cursor.getInt(i3);
                return;
            case 1:
                this.groupLabel = cursor.getString(i3);
                return;
            case 2:
                this.groupIndex = cursor.getInt(i3);
                return;
            default:
                return;
        }
    }

    /* renamed from: c */
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", Integer.valueOf(this.id));
        contentValues.put("group_label", this.groupLabel);
        contentValues.put("group_index", Integer.valueOf(this.groupIndex));
        return contentValues;
    }

    /* renamed from: e_ */
    public String getValue() {
        return Long.toString((long) this.id);
    }

    //mo44281d
    public int getId() {
        return this.id;
    }

    //mo44278a
    public void setId(int i) {
        this.id = i;
    }

    /* renamed from: e */
    public String mo44282e() {
        return this.groupLabel;
    }

    //mo44279a
    public void setGroupLabel(String str) {
        this.groupLabel = str;
        ContentValues contentValues = new ContentValues();
        contentValues.put("group_label", str);
        updateData(contentValues, String.format(Locale.US, "%s=?", new Object[]{"_id"}), new String[]{Integer.toString(this.id)});
    }

    /* renamed from: f */
    public int mo44283f() {
        return this.groupIndex;
    }

    //mo44280b
    public void setGroupIndex(int i) {
        this.groupIndex = i;
    }

    public String toString() {
        return String.format("id=%d; label=%s; index=%d", new Object[]{Integer.valueOf(this.id), this.groupLabel, Integer.valueOf(this.groupIndex)});
    }
}
