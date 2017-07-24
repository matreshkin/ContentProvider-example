package com.example.user.content;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {

    private static final int TABLE_VALUES_ID = 1;

    private static final UriMatcher URIS = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        URIS.addURI(Contract.AUTH, Contract.TABLE_VALUES, TABLE_VALUES_ID);
    }

    private DataService mService;

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = URIS.match(uri);
        switch (match) {
            case TABLE_VALUES_ID:
                int count = mService.deleteValues(selection, selectionArgs);
                notifyChange();
                return count;
            default:
                throw new RuntimeException("Unsupported URI " + uri.toString());
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match = URIS.match(uri);
        switch (match) {
            case TABLE_VALUES_ID:
                if (values.size() == 0 || !values.containsKey(Contract.COLUMN_KEY)) {
                    return null;
                }
                long id = mService.insertValue(values);
                if (id != -1) {
                    notifyChange();
                    return ContentUris.withAppendedId(Contract.URI_VALUES, id);

                }
                return null;
            default:
                throw new RuntimeException("Unsupported URI " + uri.toString());
        }
    }

    @Override
    public boolean onCreate() {
        mService = DataService.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        int match = URIS.match(uri);
        switch (match) {
            case TABLE_VALUES_ID:
                return mService.queryValues(projection, selection, selectionArgs, sortOrder);
            default:
                throw new RuntimeException("Unsupported URI " + uri.toString());
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int match = URIS.match(uri);
        switch (match) {
            case TABLE_VALUES_ID:
                int count = mService.updateValues(values, selection, selectionArgs);
                notifyChange();
                return count;
            default:
                throw new RuntimeException("Unsupported URI " + uri.toString());
        }
    }

    private void notifyChange() {
        getContext().getContentResolver().notifyChange(Contract.URI_VALUES, null);
    }
}
