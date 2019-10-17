package com.example.popularmoviesstage1.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviesstage1.MovieDetailActivity;
import com.example.popularmoviesstage1.R;
import com.example.popularmoviesstage1.models.Movie;
import com.example.popularmoviesstage1.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieItemViewHolder> {
    private List<Movie> mDataset;

    public MovieListAdapter(List<Movie> movieList) {
        mDataset = movieList;
    }

    public static class MovieItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView movieThumbnail;

        public MovieItemViewHolder(ImageView view) {
            super(view);
            movieThumbnail = view;
        }
    }

    @NonNull
    @Override
    public MovieItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageView view = (ImageView) LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        return new MovieItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieItemViewHolder holder, int position) {
        final Movie movie = mDataset.get(position);
        String posterPath = movie.getPosterPath();
        String fullPosterPath = NetworkUtils.getFullPosterPath(posterPath);
        Log.d("Poster Path", fullPosterPath);
        Picasso.get().load(fullPosterPath).error(R.drawable.ic_launcher_background).into(holder.movieThumbnail);

        holder.movieThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context mainContext = holder.movieThumbnail.getContext();
                Intent movieDetailIntent = new Intent(mainContext, MovieDetailActivity.class);
                movieDetailIntent.putExtra("movie", movie);
                mainContext.startActivity(movieDetailIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDataset == null) {
            return 0;
        }
        return mDataset.size();
    }
}
