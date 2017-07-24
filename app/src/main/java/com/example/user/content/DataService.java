package com.example.user.content;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.AnyThread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@AnyThread
public class DataService {

    private static DataService sInstance;

    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private final DataBackend mBackend;

    public static DataService getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DataService(context.getApplicationContext());
        }
        return sInstance;
    }

    private DataService(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        mBackend = new DataBackend(dbHelper);
    }

    public int deleteValues(final String selection, final String[] selectionArgs) {
        Future<Integer> future = mExecutor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return mBackend.deleteValues(selection, selectionArgs);
            }
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public long insertValue(final ContentValues values) {
        Future<Long> future = mExecutor.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return mBackend.insertValue(values);
            }
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public Cursor queryValues(final String[] projection, final String selection,
                              final String[] selectionArgs, final String sortOrder) {
        Future<Cursor> future = mExecutor.submit(new Callable<Cursor>() {
            @Override
            public Cursor call() throws Exception {
                return mBackend.queryValues(projection, selection, selectionArgs, sortOrder);
            }
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public int updateValues(final ContentValues values, final String selection,
                            final String[] selectionArgs) {
        Future<Integer> future = mExecutor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return mBackend.updateValues(values, selection, selectionArgs);
            }
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
