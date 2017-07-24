package com.example.user.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.user.content.Contract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 05-Jul-17.
 */

public class DataController {

    private static DataController sInstance = null;

    private List<DataChangeObserver> mObservers = new ArrayList<>();
    private Context mContext;
    private ContentObserver mContentObserver = new ContentProviderObserver();

    public interface DataChangeObserver {
        void onChanged(String newList);
    }

    public static DataController getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DataController(context);
        }
        return sInstance;
    }

    private DataController(Context context) {
        mContext = context;
    }

    public void add(final String key, final String value) {
        new AsyncTask<Void, Void, Uri>() {
            @Override
            protected Uri doInBackground(Void... voids) {
                ContentValues val = new ContentValues();
                val.put(Contract.COLUMN_KEY, key);
                val.put(Contract.COLUMN_VALUE, value);
                return mContext.getContentResolver().insert(Contract.URI_VALUES, val);
            }
            @Override
            protected void onPostExecute(Uri result) {
                showResult("Added ", result);
            }
        }.execute();
    }

    public void delete(final String key) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                return mContext.getContentResolver().delete(Contract.URI_VALUES,
                        Contract.COLUMN_KEY + "=?", new String[] {key});
            }
            @Override
            protected void onPostExecute(Integer result) {
                showResult("Deleted ", result);
            }
        }.execute();
    }

    public void update(final String key, final String value) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                ContentValues val = new ContentValues();
                val.put(Contract.COLUMN_KEY, key);
                val.put(Contract.COLUMN_VALUE, value);
                return mContext.getContentResolver().update(Contract.URI_VALUES, val,
                        Contract.COLUMN_KEY + "=?", new String[] {key});
            }
            @Override
            protected void onPostExecute(Integer result) {
                showResult("Updated ", result);
            }
        }.execute();
    }

    public void fetchList() {
        new AsyncTask<Void, Void, Cursor>() {
            @Override
            protected Cursor doInBackground(Void... voids) {
                return mContext.getContentResolver().query(
                        Contract.URI_VALUES,
                        new String[] { Contract.COLUMN_KEY, Contract.COLUMN_VALUE }, null, null,
                        Contract.COLUMN_KEY);
            }
            @Override
            protected void onPostExecute(Cursor result) {
                showResult("Fetched ", result == null ? null : result.getCount());
                notifyObservers(textFromCursor(result));
            }

            private String textFromCursor(Cursor c) {
                String text = "";
                if (c == null || c.getCount() == 0 || !c.moveToFirst()) {
                    return text;
                }
                do {
                    text += getString(c, 0);
                    text += " = ";
                    text += getString(c, 1);
                    text += "\n";
                } while (c.moveToNext());
                c.close();
                return text;
            }

            private String getString(Cursor c, int col) {
                return c.isNull(col) ? "null" : c.getString(col);
            }
        }.execute();
    }

    public void addObserver(DataChangeObserver observer) {
        if (mObservers.isEmpty()) {
            startObserveContentProvider();
        }
        mObservers.add(observer);
    }

    public void removeObserver(DataChangeObserver observer) {
        mObservers.remove(observer);
        if (mObservers.isEmpty()) {
            stopObserveContentProvider();
        }
    }

    private void notifyObservers(String text) {
        for (DataChangeObserver o : mObservers) {
            o.onChanged(text);
        }
    }

    private void startObserveContentProvider() {
        mContext.getContentResolver().registerContentObserver(Contract.URI_VALUES,
                true,
                mContentObserver);
    }

    private void stopObserveContentProvider() {
        mContext.getContentResolver().unregisterContentObserver(mContentObserver);
    }

    private void showResult(String op, Object result) {
        Toast.makeText(mContext, op + " " + String.valueOf(result), Toast.LENGTH_SHORT).show();
    }

    private class ContentProviderObserver extends ContentObserver {

        public ContentProviderObserver() {
            super(new Handler(Looper.getMainLooper()));
        }

        @Override
        public boolean deliverSelfNotifications() {
            return super.deliverSelfNotifications();
        }

        @Override
        public void onChange(boolean selfChange) {
            fetchList();
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            fetchList();
        }
    }
}
