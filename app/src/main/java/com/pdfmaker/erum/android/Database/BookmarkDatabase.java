package com.pdfmaker.erum.android.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BookmarkDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bookmark.db";
    private static final String TABLE_NAME = "bookmark";
    private static final int VERSION_NAME = 1;

    public BookmarkDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NAME);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT ,NAME TEXT,PAGE INTEGER,PATH TEXT,DATE TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertBookmark(String pdfName, int pageNo, String path, String date){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME",pdfName);
        contentValues.put("PAGE",pageNo);
        contentValues.put("PATH",path);
        contentValues.put("DATE",date);

        db.insert(TABLE_NAME,null,contentValues);
    }

    public Cursor getBookmark(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_NAME,null);
    }

    public void deleteBookmark(String pdfName, String path){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME,"NAME=? AND PATH=?", new String[]{pdfName, path});

    }
}
