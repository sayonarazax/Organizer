package com.sayonarazax.organizer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sayonarazax.organizer.database.OrgDbSchema.OrgTable;

public class OrgBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "OrgBase.db";
    public OrgBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + OrgTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                OrgTable.Cols.UUID + ", " +
                OrgTable.Cols.TITLE + ", " +
                OrgTable.Cols.DETAILS + ", " +
                OrgTable.Cols.DATE + ", " +
                OrgTable.Cols.SOLVED +
                ")"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
