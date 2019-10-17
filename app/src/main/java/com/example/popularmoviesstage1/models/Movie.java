package com.example.popularmoviesstage1.models;

public class Movie {
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
}
