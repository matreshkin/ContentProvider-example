package com.example.user.content;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final String NAME = "values";
    private static final int VERSION = 1;

    static final String TABLE_VALUES = "table_values";
    static final String COLUMN_KEY = "col_key";
    static final String COLUMN_VALUE = "col_value";

    public DbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_VALUES + "(" +
                COLUMN_KEY + "," +
                COLUMN_VALUE +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int from, int to) {
        db.execSQL("DROP TABLE " + TABLE_VALUES);
        onCreate(db);
    }
}
