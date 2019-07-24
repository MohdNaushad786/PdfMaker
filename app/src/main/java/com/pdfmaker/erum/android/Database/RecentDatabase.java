package com.pdfmaker.erum.android.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class RecentDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recent.db";
    private static final String TABLE_NAME = "recent";
    private static final int VERSION_NAME = 1;


    public RecentDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NAME);
        SQLiteDatabase db = this.getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT ,NAME TEXT,PATH TEXT,SIZE TEXT,DATE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertRecent(String pdfName, String path, String size, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", pdfName);
        contentValues.put("PATH", path);
        contentValues.put("SIZE",size);
        contentValues.put("DATE", date);

        db.insert(TABLE_NAME, null, contentValues);
    }

    public Cursor getRecent() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + TABLE_NAME, null);
    }

    public void updateRecent(String pdfName, String path, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("DATE", date);

        db.update(TABLE_NAME, contentValues, "NAME=? AND PATH=?", new String[]{pdfName, path});

    }

    public void deleteRecent(String pdfName, String path){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME,"NAME=? AND PATH=?", new String[]{pdfName, path});
    }

    public boolean isRecentDataInserted(String pdfName, String path){
        SQLiteDatabase db = this.getWritableDatabase();

        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where NAME=? AND PATH=? ", new String[]{pdfName,path});

        return cursor.getCount() > 0;
    }

}
