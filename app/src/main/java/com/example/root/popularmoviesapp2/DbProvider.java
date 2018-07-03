package com.example.root.popularmoviesapp2;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DbProvider extends ContentProvider {
    private DbHelper favMovDBHelper;
    private static final int MOVIE = 1001;
    private static final int MOVIE_WITH_ID = 1002;
    private static final UriMatcher uriMatcher = buildUriMatcher();

    public DbProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = favMovDBHelper.getWritableDatabase();
        int mstch = uriMatcher.match(uri);
        int rowDeleted = 0;
        switch (mstch) {
            case MOVIE:
                rowDeleted = sqLiteDatabase.delete(DbContract.FavouriMovCon.TABLE_NAME, selection,
                        selectionArgs);
                break;
            default:
                sqLiteDatabase.close();
                throw new UnsupportedOperationException("Couldn't delete!");
        }
        if (rowDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        sqLiteDatabase.close();
        return rowDeleted;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase sqLiteDatabase = favMovDBHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case MOVIE:
                long id = sqLiteDatabase.insert(DbContract.FavouriMovCon.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(DbContract.BASE_CONTENT_URI, id);
                } else {
                    sqLiteDatabase.close();
                    throw new UnsupportedOperationException("Failed to insert row" + uri);
                }
                break;
            default:
                sqLiteDatabase.close();
                throw new UnsupportedOperationException("Unknown uri" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        sqLiteDatabase.close();
        return returnUri;
    }

    @Override
    public boolean onCreate() {
        favMovDBHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase sqLiteDatabase = favMovDBHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Cursor cursor;
        switch (match) {
            case MOVIE:
                cursor = sqLiteDatabase.query(DbContract.FavouriMovCon.TABLE_NAME, projection, selection, selectionArgs, null
                        , null, sortOrder);
                break;
            case MOVIE_WITH_ID:
                cursor = sqLiteDatabase.query(DbContract.FavouriMovCon.TABLE_NAME, projection, DbContract.FavouriMovCon._ID + "= ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))}, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final SQLiteDatabase sqLiteDatabase = favMovDBHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int Updated = 0;
        if (values == null) {
            throw new IllegalArgumentException("Cannot have null content values!");
        }
        switch (match) {
            case MOVIE:
                Updated = sqLiteDatabase.update(DbContract.FavouriMovCon.TABLE_NAME,
                        values, selection, selectionArgs);
                break;
            case MOVIE_WITH_ID:
                Updated = sqLiteDatabase.update(DbContract.FavouriMovCon.TABLE_NAME,
                        values, DbContract.FavouriMovCon._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (Updated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return Updated;
    }

    public static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(DbContract.CONTENT_AUTHORITY, DbContract.PATH_MOVIES, MOVIE);
        uriMatcher.addURI(DbContract.CONTENT_AUTHORITY, DbContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);
        return uriMatcher;
    }
}