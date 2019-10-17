package com.example.popularmoviesstage1.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String posterPath;
    private String overview;
    private String releaseDate;
    private int id;
    private String title;
    private double voteAverage;

    public Movie(String posterPath, String overview, String releaseDate, int id, String title, double voteAverage) {
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.id = id;
        this.title = title;
        this.voteAverage = voteAverage;
    }

    private Movie(Parcel parcel) {
        posterPath = parcel.readString();
        overview = parcel.readString();
        releaseDate = parcel.readString();
        id = parcel.readInt();
        title = parcel.readString();
        voteAverage = parcel.readDouble();
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(posterPath);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeDouble(voteAverage);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
}
