package com.example.root.popularmoviesapp2;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import static com.example.root.popularmoviesapp2.MainActivity.ID_FAVORITES_LOADER;

public class LoaderActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private Context context;
    private AdapterMain favoriteAdapter;

    public LoaderActivity(Context context, AdapterMain favoriteAdapter) {
        this.context = context;
        this.favoriteAdapter = favoriteAdapter;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case ID_FAVORITES_LOADER:
                String[] projection = {
                        DbContract.FavouriMovCon.COLUMN_MOVIEID,
                        DbContract.FavouriMovCon.COLUMN_TITLE,
                        DbContract.FavouriMovCon.COLUMN_PLOT_REVIEW,
                        DbContract.FavouriMovCon.COLUMN_POSTER_PATH,
                        DbContract.FavouriMovCon.COLUMN_USERRATING
                };
                return new CursorLoader(context, DbContract.FavouriMovCon.CONTENT_URI, projection
                        , null, null, null);
            default:
                throw new RuntimeException("Loader failed" + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        favoriteAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        favoriteAdapter.swapCursor(null);
    }
}