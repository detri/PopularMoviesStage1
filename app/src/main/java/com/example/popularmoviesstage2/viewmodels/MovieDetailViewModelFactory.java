package com.example.popularmoviesstage2.viewmodels;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MovieDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Context context;
    private final int movieId;

    public MovieDetailViewModelFactory(Context context, int movieId) {
        this.context = context;
        this.movieId = movieId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieDetailViewModel(context, movieId);
    }
}
