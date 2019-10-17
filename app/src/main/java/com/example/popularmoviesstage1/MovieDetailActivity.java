package com.example.popularmoviesstage1;

import android.os.Bundle;

import com.example.popularmoviesstage1.models.Movie;
import com.example.popularmoviesstage1.utils.NetworkUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieDetailActivity extends AppCompatActivity {
    private Movie currentMovie;

    private TextView mTitleDisplay;
    private TextView mPlotSynposisDisplay;
    private ImageView mPosterDisplay;
    private TextView mReleaseDateDisplay;
    private TextView mUserRatingDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        currentMovie = getIntent().getParcelableExtra("movie");

        mTitleDisplay = findViewById(R.id.tv_original_title);
        mPlotSynposisDisplay = findViewById(R.id.synopsis_content);
        mPosterDisplay = findViewById(R.id.iv_movie_poster_large);
        mReleaseDateDisplay = findViewById(R.id.release_date_content);
        mUserRatingDisplay = findViewById(R.id.vote_average_content);

        mTitleDisplay.setText(currentMovie.getTitle());
        mPlotSynposisDisplay.setText(currentMovie.getOverview());
        mReleaseDateDisplay.setText(currentMovie.getReleaseDate());
        mUserRatingDisplay.setText(String.valueOf(currentMovie.getVoteAverage()));

        Picasso.get().load(NetworkUtils.getLargePosterPath(currentMovie.getPosterPath())).into(mPosterDisplay);
    }

}
