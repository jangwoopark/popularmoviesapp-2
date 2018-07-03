package com.example.root.popularmoviesapp2;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private RecyclerView recyclerView;
    private AdapterMovie movieView;
    private List<DataMovie> resultsItems;
    GridLayoutManager layoutManager;
    private int selectedOption = R.id.action_popular;
    public static final int ID_FAVORITES_LOADER = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.movieImages);
        layoutManager = new GridLayoutManager(this, getSpan());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();
        if (savedInstanceState == null) {
            isInternetOn();
        } else {
            loadAdapterPerOptionSelected(
                    savedInstanceState.getInt("optionSelected", R.id.action_popular));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("optionSelected", selectedOption);
    }

    public void viewData() {
        resultsItems = new ArrayList<>();
        movieView = new AdapterMovie(this, resultsItems);
        recyclerView.setAdapter(movieView);
        movieView.notifyDataSetChanged();
        //checkSortOrder();
    }

    public void viewData2() {
        AdapterMain adapter = new AdapterMain();
        recyclerView.setAdapter(adapter);
        getSupportLoaderManager().initLoader(ID_FAVORITES_LOADER, null, new LoaderActivity(this, adapter));
    }

    public void loadData() {
        Client Client = new Client();
        Servant apiKey = Client.getClient().create(Servant.class);
        Call<DataMovieResp> call = apiKey.getPopularMovies(BuildConfig.API_KEY);
        call.enqueue(new Callback<DataMovieResp>() {
            @Override
            public void onResponse(@NonNull Call<DataMovieResp> call, @NonNull Response<DataMovieResp> response) {
                List<DataMovie> itemList = response.body().getResults();
                recyclerView.setAdapter(new AdapterMovie(getApplicationContext(), itemList));
                Log.i("sffsf", itemList.toString());
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Successfully loaded!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DataMovieResp> call, Throwable t) {
                Log.i("fdsfdfs", t.toString());
            }
        });
    }

    public void loadData2() {
        Client Client = new Client();
        Servant apiKey = Client.getClient().create(Servant.class);
        Call<DataMovieResp> call = apiKey.getTopRatedMovies(BuildConfig.API_KEY);
        call.enqueue(new Callback<DataMovieResp>() {
            @Override
            public void onResponse(@NonNull Call<DataMovieResp> call, @NonNull Response<DataMovieResp> response) {
                List<DataMovie> itemList = response.body().getResults();
                recyclerView.setAdapter(new AdapterMovie(getApplicationContext(), itemList));
                Log.i("sffsf", itemList.toString());
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Successfully loaded!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DataMovieResp> call, Throwable t) {
                Log.i("fdsfdfs", t.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        loadAdapterPerOptionSelected(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    private void loadAdapterPerOptionSelected(int selectedOption) {
        this.selectedOption = selectedOption;
        if (selectedOption == R.id.action_popular) {
            loadData();
        }
        if (selectedOption == R.id.action_top_rated) {
            loadData2();
        }
        if (selectedOption == R.id.action_favorites) {
            viewData2();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        checkSortOrder();
    }

    public void checkSortOrder() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortOrder = sharedPreferences.getString(this.getString(R.string.pref_sort_order_key),
                this.getString(R.string.pref_most_popular));
        if (sortOrder.equals(this.getString(R.string.pref_most_popular))) {
            loadData();
        } else if (sortOrder.equals(this.getString(R.string.favorite))) {
            viewData2();
        } else {
            loadData2();
        }
    }

    private int getSpan() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return 4;
        } else {
            return 2;
        }
    }

    public final boolean isInternetOn() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        // Check for network connections
        if (connec != null) {
            if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                    connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
                // if connected with internet
                viewData();
                return true;
            } else if (
                    connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                            connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
                Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return false;
    }
}