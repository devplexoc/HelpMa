package com.plexoc.helpma.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "EmergencyDetail";

    public static final String TABLE_NAME_POLICE = "EmergencyContactPolice";
    public static final String TABLE_NAME_AMBULANCE = "EmergencyContactAmbulance";

    public static final String KEY_ID = "id";
    public static final String KEY_POLICE_NUMBER = "police_number";
    public static final String KEY_AMBULANCE_NUMBER = "ambulance_number";

    private static final String SQL_CREATE_POLICETABLE =
            "CREATE TABLE " + TABLE_NAME_POLICE + " (" + KEY_POLICE_NUMBER + "  TEXT );";

    private static final String SQL_CREATE_AMBULANCETABEL =
            "CREATE TABLE " + TABLE_NAME_AMBULANCE + " (" + KEY_AMBULANCE_NUMBER + "  TEXT );";

    private static final String SQL_DELETE_EMERGENCYINFO =
            "DROP TABLE IF EXISTS " + TABLE_NAME_POLICE;

    private static final String SQL_DELETE_EMERGENCYINFO1 =
            "DROP TABLE IF EXISTS " + TABLE_NAME_AMBULANCE;

    public SQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_POLICETABLE);
        db.execSQL(SQL_CREATE_AMBULANCETABEL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_EMERGENCYINFO);
        db.execSQL(SQL_DELETE_EMERGENCYINFO1);
        this.onCreate(db);
    }

    public void insertPoliceNumber(String PoliceNumber) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_POLICE_NUMBER, PoliceNumber);
        db.insert(TABLE_NAME_POLICE, null, contentValues);
        db.close();

    }

    public void insertAmbulanceNumber(String AmbulanceNumber) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_AMBULANCE_NUMBER, AmbulanceNumber);
        db.insert(TABLE_NAME_AMBULANCE, null, contentValues);
        db.close();

    }

    public void updatePoliceNumber(String PoliceNumber) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_POLICE_NUMBER, PoliceNumber);
        db.update(TABLE_NAME_POLICE, contentValues, null, null);
        db.close();
    }

    public void updateAmbulanceNumber(String AmbulanceNumber) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_AMBULANCE_NUMBER, AmbulanceNumber);
        db.update(TABLE_NAME_AMBULANCE, contentValues, null, null);
        db.close();
    }

    public String getPoliceNumber() {
        String IDS = null, SelectQuery;
        SelectQuery = " SELECT   ( " + KEY_POLICE_NUMBER + " ) FROM  " + TABLE_NAME_POLICE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SelectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                IDS = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        return IDS;
    }

    public String getAmbulanceNumber() {
        String IDS = null, SelectQuery;
        SelectQuery = " SELECT  ( " + KEY_AMBULANCE_NUMBER + " ) FROM  " + TABLE_NAME_AMBULANCE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SelectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                IDS = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        return IDS;
    }

    public void DeleteNumbersPolice() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete  from " + TABLE_NAME_POLICE);
    }

    public void DeleteNumbersAmbulance() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete  from " + TABLE_NAME_AMBULANCE);
    }


}
