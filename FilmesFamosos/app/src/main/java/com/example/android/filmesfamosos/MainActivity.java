package com.example.android.filmesfamosos;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.android.filmesfamosos.API.ApiRequest;
import com.example.android.filmesfamosos.Adapter.Adapter;
import com.example.android.filmesfamosos.Entity.Movie;
import com.example.android.filmesfamosos.Helper.CheckConnection;
import com.example.android.filmesfamosos.Helper.Singleton;
import com.example.android.filmesfamosos.MoviesDatabase.MoviesContract;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Adapter adapter;
    private GridView gvMovies;
    private ApiRequest request;
    private Button mBtnRefresh;
    private final Context context = MainActivity.this;
    private ActionBarDrawerToggle mToogle;
    private int index;
    private final String STATE_INDEX = "STATE_INDEX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        RequestQueue queue = Singleton.getInstance(this).getRequestQueue(context);
        request = new ApiRequest(queue, this);
        gvMovies = findViewById(R.id.gv_movies);
        mBtnRefresh = findViewById(R.id.btn_refresh);
        DrawerLayout mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        mToogle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToogle);
        mToogle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
        if (savedInstanceState == null) {
            if (CheckConnection.isNetworkConnected(context)) {
                request.checkMoviesNames(new ApiRequest.CheckMoviesNamesCallback() {
                    @Override
                    public void onSuccess(List<Movie> movieNames) {
                        adapter = new Adapter(getApplicationContext(), movieNames);
                        gvMovies.setAdapter(adapter);
                    }

                    @Override
                    public void dontExist(String message) {
                    }

                    @Override
                    public void onError(String message) {
                    }


                });
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.internetNotWorking), Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mToogle.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_topRated) {
            mBtnRefresh.setVisibility(View.GONE);
            if (CheckConnection.isNetworkConnected(context)) {
                request.checkMoviesRatedNames(new ApiRequest.CheckMoviesRatedNamesCallback() {
                    @Override
                    public void onSuccess(List<Movie> movieNames) {
                        adapter = new Adapter(getApplicationContext(), movieNames);
                        gvMovies.setAdapter(adapter);
                        Toast.makeText(getApplicationContext(), R.string.topRatedMovies, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void dontExist(String message) {
                    }

                    @Override
                    public void onError(String message) {
                    }
                });
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.internetNotWorking), Toast.LENGTH_SHORT).show();
            }
        }
        if (menuItem.getItemId() == R.id.nav_popular) {
            mBtnRefresh.setVisibility(View.GONE);
            if (CheckConnection.isNetworkConnected(context)) {
                request.checkMoviesNames(new ApiRequest.CheckMoviesNamesCallback() {
                    @Override
                    public void onSuccess(List<Movie> movieNames) {
                        adapter = new Adapter(getApplicationContext(), movieNames);
                        gvMovies.setAdapter(adapter);
                        Toast.makeText(getApplicationContext(), R.string.popularMovies, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void dontExist(String message) {
                    }

                    @Override
                    public void onError(String message) {
                    }


                });
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.internetNotWorking), Toast.LENGTH_SHORT).show();
            }
        }
        if (menuItem.getItemId() == R.id.nav_favorites) {
            mBtnRefresh.setVisibility(View.VISIBLE);
            Cursor cursor = getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    MoviesContract.MoviesEntry.COLUMN_NAME);
            List<Movie> allMovies = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    Movie movie = new Movie();
                    movie.setImageName(cursor.getString(cursor.getColumnIndex("image")));
                    movie.setName(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_NAME)));
                    movie.setId(cursor.getInt(cursor.getColumnIndex("movieid")));
                    movie.setOverview(cursor.getString(cursor.getColumnIndex("description")));
                    movie.setReleaseDate(cursor.getString(cursor.getColumnIndex("date")));
                    movie.setVoteAverage(cursor.getString(cursor.getColumnIndex("rating")));
                    allMovies.add(movie);
                }
                while (cursor.moveToNext());
                adapter = new Adapter(getApplicationContext(), allMovies);
                gvMovies.setAdapter(adapter);
                Toast.makeText(getApplicationContext(), R.string.favoriteMovies, Toast.LENGTH_SHORT).show();
                mBtnRefresh.setText(R.string.refresh);
                mBtnRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Cursor cursor = getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                MoviesContract.MoviesEntry.COLUMN_NAME);
                        List<Movie> allMovies = new ArrayList<>();
                        if (cursor.moveToFirst()) {
                            do {
                                Movie movie = new Movie();
                                movie.setImageName(cursor.getString(cursor.getColumnIndex("image")));
                                movie.setName(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_NAME)));
                                movie.setId(cursor.getInt(cursor.getColumnIndex("movieid")));
                                movie.setOverview(cursor.getString(cursor.getColumnIndex("description")));
                                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex("date")));
                                movie.setVoteAverage(cursor.getString(cursor.getColumnIndex("rating")));
                                allMovies.add(movie);
                            }
                            while (cursor.moveToNext());
                            adapter = new Adapter(getApplicationContext(), allMovies);
                            gvMovies.setAdapter(adapter);
                        }
                        cursor.close();
                    }
                });
            }
            cursor.close();
        }
        return true;
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(STATE_INDEX, index);
        savedInstanceState.putParcelable("movie", adapter);
        savedInstanceState.putInt("stats", mBtnRefresh.getVisibility());
        super.onSaveInstanceState(savedInstanceState);

    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        index = savedInstanceState.getInt(STATE_INDEX);
        adapter = savedInstanceState.getParcelable("movie");
        int mVisibility = savedInstanceState.getInt("stats");
        mBtnRefresh.setVisibility(mVisibility);
        mBtnRefresh.setText(R.string.refresh);
        gvMovies.setAdapter(adapter);
        gvMovies.smoothScrollToPosition(index);
    }
}
