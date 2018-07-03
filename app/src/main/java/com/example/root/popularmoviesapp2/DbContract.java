package com.example.root.popularmoviesapp2;

import android.net.Uri;
import android.provider.BaseColumns;

public class DbContract {
    static final String CONTENT_AUTHORITY = "com.example.root.popularmoviesapp2";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "favorite";

    public static class FavouriMovCon implements BaseColumns {
        static public final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_MOVIEID = "movieid";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_USERRATING = "userrating";
        public static final String COLUMN_POSTER_PATH = "posterpath";
        public static final String COLUMN_PLOT_REVIEW = "overview";
    }
}