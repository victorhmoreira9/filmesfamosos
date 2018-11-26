package com.example.android.filmesfamosos.Entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private String imageName;
    private String name;
    private String overview;
    private String voteAverage;
    private String releaseDate;
    private int Id;
    private String trailer;

    public Movie(String imageName, String name, String overview, String voteAverage, String releaseDate, int Id) {
        this.imageName = imageName;
        this.name = name;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.Id = Id;
    }
    private Movie(Parcel p){
        imageName = p.readString();
        name = p.readString();
        overview = p.readString();
        voteAverage = p.readString();
        releaseDate = p.readString();
        Id = p.readInt();
        trailer = p.readString();
    }
    public Movie() {
        // Normal actions performed by class, since this is still a normal object!
    }

    public static final Parcelable.Creator<Movie>
            CREATOR = new Parcelable.Creator<Movie>() {

        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverwiew() {
        return overview;
    }

    public void setOverview(String overwview) {
        this.overview = overwview;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getId() { return Id; }

    public void setId(int Id) {this.Id = Id;}




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageName);
        parcel.writeString(name);
        parcel.writeString(overview);
        parcel.writeString(voteAverage);
        parcel.writeString(releaseDate);
        parcel.writeInt(Id);
        parcel.writeString(trailer);
    }
}

