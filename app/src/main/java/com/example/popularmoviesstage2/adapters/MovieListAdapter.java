package com.example.popularmoviesstage2.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviesstage2.MovieDetailActivity;
import com.example.popularmoviesstage2.R;
import com.example.popularmoviesstage2.models.Movie;
import com.example.popularmoviesstage2.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieItemViewHolder> {
    private List<Movie> mDataset;
    private final int deviceWidth;

    public MovieListAdapter(List<Movie> movieList, Context context) {
        mDataset = movieList;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        deviceWidth = displayMetrics.widthPixels;
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

        Picasso.get()
                .load(fullPosterPath)
                .error(R.drawable.ic_launcher_background)
                .resize(deviceWidth / 2, deviceWidth / 2)
                .centerCrop()
                .into(holder.movieThumbnail);

        holder.movieThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context mainContext = holder.movieThumbnail.getContext();
                Intent movieDetailIntent = new Intent(mainContext, MovieDetailActivity.class);
                movieDetailIntent.putExtra("movie_id", movie.getId());
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

    public void setMovies(List<Movie> movies) {
        mDataset.clear();
        mDataset.addAll(movies);
        notifyDataSetChanged();
        Log.d("Dataset", "Data set changed");
    }
}
