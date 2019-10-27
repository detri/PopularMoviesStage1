package com.example.popularmoviesstage2.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.popularmoviesstage2.db.TmdbRepository;
import com.example.popularmoviesstage2.executors.DiskIOExecutor;
import com.example.popularmoviesstage2.models.Movie;
import com.example.popularmoviesstage2.models.Review;
import com.example.popularmoviesstage2.models.Video;

import java.util.List;

public class MovieDetailViewModel extends ViewModel {
    private LiveData<Movie> movie;
    private LiveData<List<Review>> reviews;
    private LiveData<List<Video>> trailers;
    private Context context;

    public MovieDetailViewModel(Context context, int movieId) {
        this.context = context;
        TmdbRepository repo = TmdbRepository.getInstance(context);
        movie = repo.getMovieById(movieId);
        reviews = repo.getReviewsByMovie(movieId);
        trailers = repo.getTrailersByMovie(movieId);
    }

    public LiveData<Movie> getMovie() {
        return movie;
    }

    public LiveData<List<Review>> getReviews() {
        return reviews;
    }

    public LiveData<List<Video>> getTrailers() {
        return trailers;
    }

    public void populateTrailers(final List<Video> videos) {
        DiskIOExecutor.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                TmdbRepository.getInstance(context).insertVideos(videos.toArray(new Video[0]));
            }
        });
    }

    public void populateReviews(final List<Review> reviews) {
        DiskIOExecutor.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                TmdbRepository.getInstance(context).insertReviews(reviews.toArray(new Review[0]));
            }
        });
    }
}
