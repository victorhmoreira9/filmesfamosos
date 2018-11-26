package com.example.android.filmesfamosos;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.android.filmesfamosos.API.ApiRequest;
import com.example.android.filmesfamosos.Entity.Movie;
import com.example.android.filmesfamosos.Helper.CheckConnection;
import com.example.android.filmesfamosos.Helper.Singleton;
import com.example.android.filmesfamosos.MoviesDatabase.MoviesContract;
import com.example.android.filmesfamosos.databinding.ActivityDescriptionMovieBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class DescriptionMovieActivity extends AppCompatActivity {
    private Context mContext;
    private String mImageName;
    private String mName;
    private String mOverview;
    private String mVoteAverage;
    private String mReleaseDate;
    private int mId;
    private TextView mTvReview;
    private ImageButton mIbAddFavorite;
    private ImageButton mIbRemoveFavorite;
    private Button mBtnTrailer;
    private Button mBtnTrailer2;
    private Button mBtnTrailer3;
    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w780/";
    private static final String VIDEO_BASE_URL = "http://www.youtube.com/watch?v=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDescriptionMovieBinding movieBinding = DataBindingUtil.setContentView(this, R.layout.activity_description_movie);
        ImageView ivImageName = movieBinding.ivMoviePoster;
        TextView mTvName = movieBinding.includeLayoutMovieInfo.tvMovieName;
        TextView mTvOverview = movieBinding.includeLayoutMovieInfo.tvMovieOverview;
        TextView mTvVoteAverage = movieBinding.includeLayoutMovieInfo.tvMovieVoteaverage;
        TextView mTvReleaseDate = movieBinding.includeLayoutMovieInfo.tvMovieReleasedate;
        mBtnTrailer = movieBinding.includeLayoutMovieInfo.videoViewTrailers.btnTrailer1;
        mBtnTrailer2 = movieBinding.includeLayoutMovieInfo.videoViewTrailers.btnTrailer2;
        mBtnTrailer3 = movieBinding.includeLayoutMovieInfo.videoViewTrailers.btnTrailer3;
        mTvReview = movieBinding.includeLayoutMovieInfo.tvReview;
        mIbAddFavorite = movieBinding.includeLayoutMovieInfo.ivFavoriteNotSelected;
        mIbRemoveFavorite = movieBinding.includeLayoutMovieInfo.ivFavoriteSelected;
        RequestQueue queue = Singleton.getInstance(this).getRequestQueue(mContext);
        final ApiRequest request = new ApiRequest(queue, this);


        mIbAddFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIbAddFavorite.getVisibility() == View.VISIBLE) {
                    mIbAddFavorite.setVisibility(View.INVISIBLE);
                    mIbRemoveFavorite.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), mName + getString(R.string.wasAddFavorites), Toast.LENGTH_LONG).show();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MoviesContract.MoviesEntry.COLUMN_IMAGE, mImageName);
                    contentValues.put(MoviesContract.MoviesEntry.COLUMN_NAME, mName);
                    contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIEID, mId);
                    contentValues.put(MoviesContract.MoviesEntry.COLUMN_DESCRIPTION, mOverview);
                    contentValues.put(MoviesContract.MoviesEntry.COLUMN_DATE, mReleaseDate);
                    contentValues.put(MoviesContract.MoviesEntry.COLUMN_RATING, mVoteAverage);
                    Uri uri = getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, contentValues);

                }
            }
        });
        mIbRemoveFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIbRemoveFavorite.getVisibility() == View.VISIBLE) {
                    mIbAddFavorite.setVisibility(View.VISIBLE);
                    mIbRemoveFavorite.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), mName + getString(R.string.wasRemovedFavorites), Toast.LENGTH_LONG).show();
                    String stringId = Integer.toString(mId);
                    Uri uri = MoviesContract.MoviesEntry.CONTENT_URI;
                    uri = uri.buildUpon().appendPath(stringId).build();
                    getContentResolver().delete(uri, null, null);
                }
            }
        });
        Movie movie = getIntent().getParcelableExtra("movie");
        if (movie != null) {
            mImageName = movie.getImageName();
            mName = movie.getName();
            mOverview = movie.getOverwiew();
            mVoteAverage = movie.getVoteAverage();
            mReleaseDate = movie.getReleaseDate();
            mId = movie.getId();

            Picasso.with(mContext).load(POSTER_BASE_URL + mImageName).into(ivImageName);
            mTvName.setText(mName);
            mTvOverview.setText(mOverview);
            mTvVoteAverage.setText(mVoteAverage);
            mTvReleaseDate.setText(mReleaseDate);

            Cursor cursor = getContentResolver()
                    .query(MoviesContract.MoviesEntry.CONTENT_URI, null, null, null, null);
            final List<Integer> allMovies = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    allMovies.add(cursor.getInt(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIEID)));
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            if (allMovies.contains(mId)) {
                mIbAddFavorite.setVisibility(View.INVISIBLE);
                mIbRemoveFavorite.setVisibility(View.VISIBLE);
            } else {
                mIbAddFavorite.setVisibility(View.VISIBLE);
                mIbRemoveFavorite.setVisibility(View.INVISIBLE);
            }

            if (CheckConnection.isNetworkConnected(getApplicationContext())) {
                request.checkMoviesReview(mId, new ApiRequest.CheckMoviesReviewCallback() {
                    @Override
                    public void onSuccess(List<String> allReviews) {
                        StringBuilder builder = new StringBuilder();
                        for (String details : allReviews) {
                            builder.append(details).append("\n");
                        }
                        mTvReview.setText(builder.toString());
                    }

                    @Override
                    public void dontExist(String message) {
                        mTvReview.setText(R.string.norReviews);
                    }

                    @Override
                    public void onError(String message) {
                        mTvReview.setText(R.string.norReviews);
                    }
                });
            }
            request.checkMoviesTrailer(mId, new ApiRequest.CheckMoviesTrailerCallback() {
                @Override
                public void onSuccess(final List<String> allTrailers) {
                    int d = allTrailers.size();
                    if (allTrailers.size() == 2)
                        mBtnTrailer2.setVisibility(View.VISIBLE);
                    if (allTrailers.size() >= 3) {
                        mBtnTrailer2.setVisibility(View.VISIBLE);
                        mBtnTrailer3.setVisibility(View.VISIBLE);
                    }
                    mBtnTrailer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            btn_click_video(allTrailers.get(0));
                        }
                    });
                    mBtnTrailer2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            btn_click_video(allTrailers.get(1));
                        }
                    });
                    mBtnTrailer3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            btn_click_video(allTrailers.get(2));
                        }
                    });
                }

                @Override
                public void dontExist(String message) {

                }

                @Override
                public void onError(String message) {

                }
            });
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.internetNotWorking), Toast.LENGTH_SHORT).show();
        }
    }

    private void btn_click_video(String id_video) {
        try {
            Intent trailerIntent =  new Intent(Intent.ACTION_VIEW, Uri.parse(VIDEO_BASE_URL + id_video));
            if(trailerIntent.resolveActivity(getPackageManager()) != null)
            startActivity(trailerIntent);
            else {
                Toast.makeText(mContext, getString(R.string.errorYoutubeIntent), Toast.LENGTH_LONG).show();
            }
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(mContext, getString(R.string.errorYoutubeIntent), Toast.LENGTH_LONG).show();
        }

    }

}
