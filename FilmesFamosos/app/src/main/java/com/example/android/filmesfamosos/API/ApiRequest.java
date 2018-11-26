package com.example.android.culinariaapp.API;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.android.filmesfamosos.MainActivity;
import com.example.android.filmesfamosos.Entity.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.filmesfamosos.BuildConfig.api_key;

public class ApiRequest extends MainActivity{
    private RequestQueue queue;
    private static final String LOG_TAG = ApiRequest.class.getName();

    public ApiRequest() {
    }

    public ApiRequest(RequestQueue queue, Context context) {
        this.queue = queue;
    }

    public void checkMoviesNames( final CheckMoviesNamesCallback callback) {
        String url = "http://api.themoviedb.org/3/movie/popular?api_key=" + api_key;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(LOG_TAG, response.toString());
                try {
                    List<Movie> allMovies = new ArrayList<>();
                    JSONArray aux = response.getJSONArray("results");
                    int j = aux.length();
                    for(int i = 0;j> i; i++){
                    JSONObject aux2 = aux.getJSONObject(i);
                    String movieName = aux2.getString("title");
                    String movieImage = aux2.getString("backdrop_path");
                    String movieImage_r = movieImage.replace("\\","");
                    String movieOverview = aux2.getString("overview");
                    String movieVoteAverage = aux2.getString("vote_average");
                    int movieId = aux2.getInt("id");
                    String movieReleaseDate = aux2.getString("release_date");
                    Movie movie = new Movie();
                    movie.setImageName(movieImage_r);
                    movie.setName(movieName);
                    movie.setOverview(movieOverview);
                    movie.setVoteAverage(movieVoteAverage);
                    movie.setReleaseDate(movieReleaseDate);
                    movie.setId(movieId);
                    allMovies.add(movie);}
                    callback.onSuccess(allMovies);
                } catch (JSONException e) {
                    e.printStackTrace();
                }}
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NetworkError) {
                    callback.onError("Nao foi possivel se conectar");
                } else if (error instanceof ServerError) {
                    callback.dontExist("Nao existe nenhum filme com esse nome");
                }
                Log.d(LOG_TAG, error.toString());

            }
        });
        queue.add(request);
    }

    public interface CheckMoviesNamesCallback {
        void onSuccess(List<Movie> movieNames);
        void dontExist(String message);
        void onError(String message);
    }

    public void checkMoviesTrailer (int id, final CheckMoviesTrailerCallback callback){
        String url = "http://api.themoviedb.org/3/movie/"+ id + "/videos?api_key=" + api_key;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(LOG_TAG, response.toString());
                try {
                    List<String> allTrailers = new ArrayList<>();
                    JSONArray aux = response.getJSONArray("results");
                    int j = aux.length();
                    for(int i = 0;j> i; i++) {
                        JSONObject aux2 = aux.getJSONObject(i);
                        String trailer = aux2.getString("key");
                        allTrailers.add(trailer);
                    }
                    callback.onSuccess(allTrailers);
                } catch (JSONException e) {
                    e.printStackTrace();
                }}
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NetworkError) {
                    callback.onError("Nao foi possivel se conectar");
                } else if (error instanceof ServerError) {
                    callback.dontExist("Nao existe nenhum filme com esse nome");
                }
                Log.d(LOG_TAG, error.toString());

            }
        });
        queue.add(request);
    }

    public interface CheckMoviesTrailerCallback {
        void onSuccess(List<String> allTrailers);
        void dontExist(String message);
        void onError(String message);
    }

    public void checkMoviesReview (int id,  final CheckMoviesReviewCallback callback ){
        String url = "http://api.themoviedb.org/3/movie/"+ id + "/reviews?api_key=" + api_key;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(LOG_TAG, response.toString());
                try {
                    List<String> allReviews = new ArrayList<>();
                    JSONArray aux = response.getJSONArray("results");
                    int j = aux.length();
                    for(int i = 0;j> i; i++) {
                        JSONObject aux2 = aux.getJSONObject(i);
                        String author = aux2.getString("author");
                        String reviews = aux2.getString("content");
                        allReviews.add("- > Author: " + author + "\n");
                        allReviews.add(reviews + "\n");
                    }
                    callback.onSuccess(allReviews);
                } catch (JSONException e) {
                    e.printStackTrace();
                }}
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NetworkError) {
                    callback.onError("Nao foi possivel se conectar");
                } else if (error instanceof ServerError) {
                    callback.dontExist("Nao existe nenhum filme com esse nome");
                }
                Log.d(LOG_TAG, error.toString());

            }
        });
        queue.add(request);
    }

    public interface CheckMoviesReviewCallback {
        void onSuccess(List<String> allReviews);
        void dontExist(String message);
        void onError(String message);
    }

    public void checkMoviesRatedNames( final CheckMoviesRatedNamesCallback callback) {
        String url = "http://api.themoviedb.org/3/movie/top_rated?api_key=" + api_key;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(LOG_TAG, response.toString());
                try {
                    List<Movie> allMovies = new ArrayList<>();
                    JSONArray aux = response.getJSONArray("results");
                    int j = aux.length();
                    for(int i = 0;j> i; i++){
                        JSONObject aux2 = aux.getJSONObject(i);
                        String movieName = aux2.getString("title");
                        String movieImage = aux2.getString("backdrop_path");
                        String movieImage_r = movieImage.replace("\\","");
                        String movieOverview = aux2.getString("overview");
                        String movieVoteAverage = aux2.getString("vote_average");
                        String movieReleaseDate = aux2.getString("release_date");
                        int movieId = aux2.getInt("id");
                        Movie movie = new Movie();
                        movie.setImageName(movieImage_r);
                        movie.setName(movieName);
                        movie.setOverview(movieOverview);
                        movie.setVoteAverage(movieVoteAverage);
                        movie.setReleaseDate(movieReleaseDate);
                        movie.setId(movieId);
                        allMovies.add(movie);}
                    callback.onSuccess(allMovies);
                } catch (JSONException e) {
                    e.printStackTrace();
                }}
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NetworkError) {
                    callback.onError("Nao foi possivel se conectar");
                } else if (error instanceof ServerError) {
                    callback.dontExist("Nao existe nenhum filme com esse nome");
                }
                Log.d(LOG_TAG, error.toString());

            }
        });
        queue.add(request);
    }

    public interface CheckMoviesRatedNamesCallback {
        void onSuccess(List<Movie> movieNames);
        void dontExist(String message);
        void onError(String message);
    }
}