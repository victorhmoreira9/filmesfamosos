package com.example.android.filmesfamosos.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.filmesfamosos.DescriptionMovieActivity;
import com.example.android.filmesfamosos.Entity.Movie;
import com.example.android.filmesfamosos.R;
import com.squareup.picasso.Picasso;

import java.util.List;

@SuppressLint("ParcelCreator")
public class Adapter extends BaseAdapter implements Parcelable {

    private final Context context;
    private final List<Movie> movieList;
    private final LayoutInflater inflater;

    public Adapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        if(movieList == null)
            return 0;
        return movieList.size();
    }

    @Override
    public Object getItem(int position) {
        return movieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieViewHolder movieViewHolder;

        if(convertView == null){

            convertView = inflater.inflate(R.layout.galeria_movies, parent, false);
            movieViewHolder = new MovieViewHolder();
            movieViewHolder.image = convertView.findViewById(R.id.iv_movie);
            movieViewHolder.name = convertView.findViewById(R.id.tv_movie);
            convertView.setTag(movieViewHolder);

        }else {
            movieViewHolder = (MovieViewHolder) convertView.getTag();
        }

        final Movie movie =  movieList.get(position);



        final String nome = movie.getName();
        final String image_name = movie.getImageName();
            Picasso.with(context).load("http://image.tmdb.org/t/p/w342/" + image_name).into(movieViewHolder.image);

        movieViewHolder.name.setText(nome);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, DescriptionMovieActivity.class);
                intent.putExtra("movie", movie);
                context.startActivity(intent);
            }
        });



        return convertView;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
parcel.writeList(movieList);
    }

    private class MovieViewHolder{
        ImageView image;
        TextView name;
    }

}
