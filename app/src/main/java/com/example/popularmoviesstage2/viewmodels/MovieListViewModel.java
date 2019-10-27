package com.example.popularmoviesstage2.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.popularmoviesstage2.db.TmdbRepository;
import com.example.popularmoviesstage2.executors.DiskIOExecutor;
import com.example.popularmoviesstage2.models.Movie;

import java.util.List;

public class MovieListViewModel extends ViewModel {
    private LiveData<List<Movie>> movies;
    private Context context;

    public MovieListViewModel(Context context) {
        this.context = context;
        movies = TmdbRepository.getInstance(context).getMovieList();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public void populateMovieTable(final List<Movie> movies) {
        DiskIOExecutor.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                TmdbRepository.getInstance(context).insertMovies(movies.toArray(new Movie[0]));
            }
        });
    }
}
