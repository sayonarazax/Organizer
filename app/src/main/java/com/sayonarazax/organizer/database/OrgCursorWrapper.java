package com.sayonarazax.organizer.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.sayonarazax.organizer.Org;
import com.sayonarazax.organizer.database.OrgDbSchema.OrgTable;

import java.util.Date;
import java.util.UUID;

public class OrgCursorWrapper extends CursorWrapper {
    public OrgCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public Org getOrg() {
        String uuidString = getString(getColumnIndex(OrgTable.Cols.UUID));
        String title = getString(getColumnIndex(OrgTable.Cols.TITLE));
        String details = getString(getColumnIndex(OrgTable.Cols.DETAILS));
        long date = getLong(getColumnIndex(OrgTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(OrgTable.Cols.SOLVED));

        Org org = new Org(UUID.fromString(uuidString));
        org.setTitle(title);
        org.setDetails(details);
        org.setDate(new Date(date));
        org.setSolved(isSolved != 0);
        return org;
    }

}
