package com.example.popularmoviesstage2.db;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.popularmoviesstage2.dao.MovieDao;
import com.example.popularmoviesstage2.dao.ReviewDao;
import com.example.popularmoviesstage2.dao.VideoDao;
import com.example.popularmoviesstage2.models.Movie;
import com.example.popularmoviesstage2.models.Review;
import com.example.popularmoviesstage2.models.Video;

import java.util.List;

public class TmdbRepository {
    private TmdbDatabase tmdbDatabase;
    private static TmdbRepository instance;
    private MovieDao movieDao;
    private VideoDao videoDao;
    private ReviewDao reviewDao;

    private TmdbRepository(Context context) {
        tmdbDatabase = Room.databaseBuilder(context, TmdbDatabase.class, "movie-database")
                .build();
        movieDao = tmdbDatabase.movieDao();
        videoDao = tmdbDatabase.videoDao();
        reviewDao = tmdbDatabase.reviewDao();
    }

    public static TmdbRepository getInstance(Context context) {
        if (instance == null) {
            instance = new TmdbRepository(context);
        }
        return instance;
    }

    public LiveData<List<Movie>> getMovieList() {
        Log.d("Database Query", "Grabbing all movies");
        return movieDao.getAll();
    }

    public LiveData<Movie> getMovieById(int id) {
        Log.d("Database Query", "Grabbing movie ID " + id);
        return movieDao.getById(id);
    }

    public void insertMovies(Movie... movies) {
        Log.d("Database Query", "Inserting movies");
        movieDao.insertAll(movies);
    }

    public void updateMovie(Movie movie) {
        Log.d("Database Query", "Updating movie " + movie.getId());
        movieDao.update(movie);
    }

    public void insertVideos(Video... videos) {
        for (int i = 0; i < videos.length; i++) {
            Log.d("Database Query", "Inserting video with ID " + videos[i].getId());
        }
        videoDao.insertVideos(videos);
    }

    public LiveData<List<Video>> getTrailersByMovie(int movieId) {
        Log.d("Database Query", "Grabbing videos for movie " + movieId);
        return videoDao.getTrailersByMovie(movieId);
    }

    public void insertReviews(Review... reviews) {
        Log.d("Database Query", "Inserting reviews");
        reviewDao.insertReviews(reviews);
    }

    public LiveData<List<Review>> getReviewsByMovie(int movieId) {
        Log.d("Database Query", "Grabbing reviews for movie " + movieId);
        return reviewDao.getByMovie(movieId);
    }

    public MovieDao getMovieDao() {
        return movieDao;
    }
}
