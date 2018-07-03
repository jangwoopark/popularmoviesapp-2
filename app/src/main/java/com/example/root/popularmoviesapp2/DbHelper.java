package com.example.root.popularmoviesapp2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favorite.db";
    private static final int DATABASE_VERSION = 1;
    public static final String LOGTAG = "FAVORITE";
    SQLiteOpenHelper dbhandler;
    SQLiteDatabase db;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + DbContract.FavouriMovCon.TABLE_NAME + " (" +
                DbContract.FavouriMovCon._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DbContract.FavouriMovCon.COLUMN_MOVIEID + " INTEGER," +
                DbContract.FavouriMovCon.COLUMN_TITLE + " TEXT NOT NULL," +
                DbContract.FavouriMovCon.COLUMN_USERRATING + " REAL NOT NULL," +
                DbContract.FavouriMovCon.COLUMN_POSTER_PATH + " TEXT NOT NULL," +
                DbContract.FavouriMovCon.COLUMN_PLOT_REVIEW + " TEXT NOT NULL" +
                "); ";
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbContract.FavouriMovCon.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}