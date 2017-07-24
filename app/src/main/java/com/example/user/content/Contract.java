package com.example.user.content;

import android.net.Uri;

public class Contract {
    public static final String AUTH = "com.example.user.content";

    public static final String TABLE_VALUES = "table_values";
    public static final String COLUMN_KEY = "col_key";
    public static final String COLUMN_VALUE = "col_value";
    public static final Uri URI_VALUES = Uri.parse("content://" + AUTH + "/" + TABLE_VALUES);
}
