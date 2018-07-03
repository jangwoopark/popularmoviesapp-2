package com.example.root.popularmoviesapp2;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    String movieImages, title1, releaseDate1, ratings1, review1;
    @BindView(R.id.movieImage)
    ImageView movieImage;
    @BindView(R.id.titleView)
    TextView title;
    @BindView(R.id.releaseDate)
    TextView releaseDate;
    @BindView(R.id.ratings)
    TextView ratings;
    @BindView(R.id.review)
    TextView review;
    @BindView(R.id.trailerView)
    RecyclerView trailerRecycler;
    @BindView(R.id.reviewsView)
    RecyclerView reviewRecycler;
    List<DataTrailer> trailerData;
    List<DataReview> reviewData;
    AdapterTrailer trailerView;
    AdapterReview reviewView;
    ImageButton imageFavorite;
    List<DataMovie> items;
    Gson gson;
    String array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        imageFavorite = findViewById(R.id.imageButton);
        gson = new Gson();
        array = getIntent().getStringExtra("movieDetails");
        Log.i("irkjafh", String.valueOf(items));
        items = Collections.singletonList(gson.fromJson(array, DataMovie.class));
        setDetails(items);
        DbHelper favMovDBHelper = new DbHelper(this);
        if (FavoriteMovieDB(items)) {
            changeToFilledFavIcon();
        } else {
            changeToEmptyFavIcon();
        }
        imageFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!FavoriteMovieDB(items)) {
                    changeToFilledFavIcon();
                    saveFavorite(items);
                    Snackbar.make(v, "Added to Favorite", Snackbar.LENGTH_LONG).show();
                } else {
                    changeToEmptyFavIcon();
                    deleteMovies(items);
                    Snackbar.make(v, "Deleted from Favorite", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        trailer();
        review();
    }

    private void changeToEmptyFavIcon() {
        imageFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
    }

    private void changeToFilledFavIcon() {
        imageFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
    }

    public void trailer() {
        trailerData = new ArrayList<>();
        trailerView = new AdapterTrailer(this, trailerData);
        trailerRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        trailerRecycler.setAdapter(trailerView);
        trailerView.notifyDataSetChanged();
        loadTrailer();
    }

    public void loadTrailer() {
        int id = items.get(0).getId();
        Client Client = new Client();
        Servant apiKey = Client.getClient().create(Servant.class);
        Call<DataTrailerResp> call = apiKey.getMovieTrailer(id, BuildConfig.API_KEY);
        call.enqueue(new Callback<DataTrailerResp>() {
            @Override
            public void onResponse(@NonNull Call<DataTrailerResp> call, @NonNull Response<DataTrailerResp> response) {
                List<DataTrailer> itemList = response.body().getResults();
                trailerRecycler.setAdapter(new AdapterTrailer(getApplicationContext(), itemList));
                Log.i("sffsf", itemList.toString());
                if (response.isSuccessful()) {
                    Toast.makeText(DetailActivity.this, "Successfully loaded!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DataTrailerResp> call, Throwable t) {
                Log.i("fdsfdfs", t.toString());
            }
        });
    }

    public void review() {
        reviewData = new ArrayList<>();
        reviewView = new AdapterReview(this, reviewData);
        reviewRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        reviewRecycler.setAdapter(reviewView);
        reviewView.notifyDataSetChanged();
        loadReview();
    }

    public void loadReview() {
        int id = items.get(0).getId();
        Client Client = new Client();
        Servant apiKey = Client.getClient().create(Servant.class);
        Call<DataReviewResp> call = apiKey.getMovieReview(id, BuildConfig.API_KEY);
        call.enqueue(new Callback<DataReviewResp>() {
            @Override
            public void onResponse(@NonNull Call<DataReviewResp> call, @NonNull Response<DataReviewResp> response) {
                List<DataReview> itemList = response.body().getResults();
                reviewRecycler.setAdapter(new AdapterReview(getApplicationContext(), itemList));
                Log.i("sffsf", itemList.toString());
                if (response.isSuccessful()) {
                    Toast.makeText(DetailActivity.this, "Successfully loaded!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DataReviewResp> call, Throwable t) {
                Log.i("fdsfdfs", t.toString());
            }
        });
    }

    public void saveFavorite(List<DataMovie> movieItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.FavouriMovCon.COLUMN_MOVIEID, movieItem.get(0).getId());
        contentValues.put(DbContract.FavouriMovCon.COLUMN_TITLE, movieItem.get(0).getTitle());
        contentValues.put(DbContract.FavouriMovCon.COLUMN_USERRATING, movieItem.get(0).getVoteAverage());
        contentValues.put(DbContract.FavouriMovCon.COLUMN_POSTER_PATH, movieItem.get(0).getPosterPath());
        contentValues.put(DbContract.FavouriMovCon.COLUMN_PLOT_REVIEW, movieItem.get(0).getOverview());
        getContentResolver().insert(DbContract.FavouriMovCon.CONTENT_URI, contentValues);
    }

    public void deleteMovies(List<DataMovie> movieItem) {
        String selection = DbContract.FavouriMovCon.COLUMN_MOVIEID + "=?";
        String[] selectionArgs = {String.valueOf(movieItem.get(0).getId())};
        getContentResolver().delete(DbContract.FavouriMovCon.CONTENT_URI, selection, selectionArgs);
    }

    public boolean FavoriteMovieDB(List<DataMovie> item) {
        Cursor cursor = getContentResolver().query(DbContract.FavouriMovCon.CONTENT_URI,
                null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int movieId = cursor.getInt(cursor.getColumnIndex(DbContract.FavouriMovCon.COLUMN_MOVIEID));
                if (movieId == item.get(0).getId()) {
                    return true;
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return false;
    }

    private void setDetails(List<DataMovie> item) {
        Picasso.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w185/" + item.get(0).getPosterPath()).into(movieImage);
        title.setText(item.get(0).getTitle());
        releaseDate.setText(item.get(0).getReleaseDate());
        ratings.setText(String.valueOf(item.get(0).getVoteAverage()));
        review.setText(item.get(0).getOverview());
    }
}