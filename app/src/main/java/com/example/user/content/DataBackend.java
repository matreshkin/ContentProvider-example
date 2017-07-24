package com.example.user.content;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.AnyThread;
import android.support.annotation.WorkerThread;

@WorkerThread
public class DataBackend {

    private DbHelper mDbHelper;

    @AnyThread
    public DataBackend(DbHelper dbHelper) {
        mDbHelper = dbHelper;
    }

    public int deleteValues(String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.delete(DbHelper.TABLE_VALUES, selection, selectionArgs);
    }

    public long insertValue(ContentValues values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.insert(DbHelper.TABLE_VALUES, null, values);
    }

    public Cursor queryValues(String[] projection, String selection,
                              String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.query(DbHelper.TABLE_VALUES, projection, selection, selectionArgs, null, null,
                sortOrder);
    }

    public int updateValues(ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.update(DbHelper.TABLE_VALUES, values, selection, selectionArgs);
    }
}
