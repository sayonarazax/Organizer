package com.sayonarazax.organizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sayonarazax.organizer.database.OrgBaseHelper;
import com.sayonarazax.organizer.database.OrgCursorWrapper;
import com.sayonarazax.organizer.database.OrgDbSchema.OrgTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrgLab {
    private static OrgLab sOrgLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static OrgLab get(Context context) {
        if (sOrgLab == null) {
            sOrgLab = new OrgLab(context);
        }
        return sOrgLab;
    }
    private OrgLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new OrgBaseHelper(mContext)
                .getWritableDatabase();
    }

    public void addOrg(Org o) {
        ContentValues values = getContentValues(o);
        mDatabase.insert(OrgTable.NAME, null, values);
    }

    public List<Org> getOrgs() {
        List<Org> orgs = new ArrayList<>();
        OrgCursorWrapper cursor = queryOrgs(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                orgs.add(cursor.getOrg());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return orgs;
    }

    public Org getOrg(UUID id) {
        OrgCursorWrapper cursor = queryOrgs(
                OrgTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getOrg();
        } finally {
            cursor.close();
        }
    }

    public void updateOrg(Org org) {
        String uuidString = org.getId().toString();
        ContentValues values = getContentValues(org);
        mDatabase.update(OrgTable.NAME, values,
                OrgTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    private OrgCursorWrapper queryOrgs(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                OrgTable.NAME,
                null, // columns - с null выбираются все столбцы
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new OrgCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Org org) {
        ContentValues values = new ContentValues();
        values.put(OrgTable.Cols.UUID, org.getId().toString());
        values.put(OrgTable.Cols.TITLE, org.getTitle());
        values.put(OrgTable.Cols.DETAILS, org.getDetails());
        values.put(OrgTable.Cols.DATE, org.getDate().getTime());
        values.put(OrgTable.Cols.SOLVED, org.isSolved() ? 1 : 0);
        return values;
    }

    public void deleteOrg(UUID orgId)
    {
        String uuidString = orgId.toString();

        mDatabase.delete(OrgTable.NAME, OrgTable.Cols.UUID + " = ?", new String[] {uuidString});
    }
}
