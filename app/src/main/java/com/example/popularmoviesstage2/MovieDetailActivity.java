package com.example.popularmoviesstage2;

import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.popularmoviesstage2.adapters.ReviewListAdapter;
import com.example.popularmoviesstage2.adapters.TrailerListAdapter;
import com.example.popularmoviesstage2.db.TmdbRepository;
import com.example.popularmoviesstage2.executors.DiskIOExecutor;
import com.example.popularmoviesstage2.models.Movie;
import com.example.popularmoviesstage2.models.Review;
import com.example.popularmoviesstage2.models.Video;
import com.example.popularmoviesstage2.utils.MovieRequestQueue;
import com.example.popularmoviesstage2.utils.NetworkUtils;
import com.example.popularmoviesstage2.viewmodels.MovieDetailViewModel;
import com.example.popularmoviesstage2.viewmodels.MovieDetailViewModelFactory;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {
    private String JSON_RESULTS_KEY = "results";

    private TextView mTitleDisplay;
    private TextView mPlotSynposisDisplay;
    private ImageView mPosterDisplay;
    private TextView mReleaseDateDisplay;
    private TextView mUserRatingDisplay;
    private MenuItem mFavoriteButton;
    private RecyclerView mTrailerList;
    private RecyclerView mReviewList;

    private Movie currentMovie;
    private int currentMovieId;

    private List<Video> videoList = new LinkedList<>();
    private List<Review> reviewList = new LinkedList<>();
    private TrailerListAdapter trailerAdapter;
    private ReviewListAdapter reviewAdapter;

    private MovieDetailViewModel movieDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mTitleDisplay = findViewById(R.id.tv_original_title);
        mPlotSynposisDisplay = findViewById(R.id.synopsis_content);
        mPosterDisplay = findViewById(R.id.iv_movie_poster_large);
        mReleaseDateDisplay = findViewById(R.id.release_date_content);
        mUserRatingDisplay = findViewById(R.id.vote_average_content);
        mTrailerList = findViewById(R.id.rv_trailer_list);
        mReviewList = findViewById(R.id.rv_review_list);

        LinearLayoutManager trailerManager = new LinearLayoutManager(this);
        trailerManager.setOrientation(LinearLayoutManager.VERTICAL);
        mTrailerList.setLayoutManager(trailerManager);
        mTrailerList.setHasFixedSize(true);
        trailerAdapter = new TrailerListAdapter(videoList);
        mTrailerList.setAdapter(trailerAdapter);

        LinearLayoutManager reviewManager = new LinearLayoutManager(this);
        reviewManager.setOrientation(LinearLayoutManager.VERTICAL);
        mReviewList.setLayoutManager(reviewManager);
        reviewAdapter = new ReviewListAdapter(reviewList);
        mReviewList.setAdapter(reviewAdapter);

        currentMovieId = getIntent().getIntExtra("movie_id", -1);

        MovieDetailViewModelFactory factory = new MovieDetailViewModelFactory(getApplicationContext(), currentMovieId);
        movieDetailViewModel = ViewModelProviders.of(this, factory).get(MovieDetailViewModel.class);

        observeMovie();

        fetchTrailers();
        fetchReviews();
    }

    private void observeMovie() {
        movieDetailViewModel.getMovie().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                if (movie != null) {
                    currentMovie = movie;
                    populateUI();
                    if (mFavoriteButton != null) {
                        updateMenu();
                    }
                }
            }
        });
        movieDetailViewModel.getReviews().observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviews) {
                reviewAdapter.setReviews(reviews);
            }
        });
        movieDetailViewModel.getTrailers().observe(this, new Observer<List<Video>>() {
            @Override
            public void onChanged(List<Video> videos) {
                Log.d("Videos", "Setting videos");
                trailerAdapter.setVideos(videos);
            }
        });
    }

    private void fetchTrailers() {
        MovieRequestQueue.getInstance(this)
                .getRequestQueue()
                .add(new JsonObjectRequest(NetworkUtils.getVideoUrl(currentMovieId), null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                JSONArray videoArray = response.optJSONArray(JSON_RESULTS_KEY);
                                List<Video> videos = NetworkUtils.jsonArrayToVideoList(videoArray, currentMovieId);
                                List<Video> trailers = new LinkedList<>();
                                for (Video video : videos) {
                                    if (video.getType().equals("Trailer")) {
                                        trailers.add(video);
                                    }
                                }
                                movieDetailViewModel.populateTrailers(trailers);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        }));
    }

    private void fetchReviews() {
        MovieRequestQueue.getInstance(this)
                .getRequestQueue()
                .add(new JsonObjectRequest(NetworkUtils.getReviewUrl(currentMovieId), null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                JSONArray reviewArray = response.optJSONArray(JSON_RESULTS_KEY);
                                List<Review> reviews = NetworkUtils.jsonArrayToReviewList(reviewArray, currentMovieId);
                                movieDetailViewModel.populateReviews(reviews);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        }));
    }

    private void updateMenu() {
        if (currentMovie.isFavorited()) {
            mFavoriteButton.setTitle(R.string.unfavorite_movie);
            mFavoriteButton.setIcon(R.drawable.ic_favorited);
        } else {
            mFavoriteButton.setTitle(R.string.favorite_movie);
            mFavoriteButton.setIcon(R.drawable.ic_unfavorited);
        }
    }

    private void populateUI() {
        mTitleDisplay.setText(currentMovie.getTitle());
        mPlotSynposisDisplay.setText(currentMovie.getOverview());
        mReleaseDateDisplay.setText(currentMovie.getReleaseDate());
        mUserRatingDisplay.setText(String.valueOf(currentMovie.getVoteAverage()));

        Picasso.get().load(NetworkUtils.getLargePosterPath(currentMovie.getPosterPath())).into(mPosterDisplay);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        mFavoriteButton = menu.findItem(R.id.toggle_favorite);
        if (currentMovie != null) {
            updateMenu();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.toggle_favorite) {
            DiskIOExecutor.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    if (currentMovie != null) {
                        TmdbRepository repo = TmdbRepository.getInstance(getApplicationContext());
                        currentMovie.setFavorited(!currentMovie.isFavorited());
                        repo.updateMovie(currentMovie);
                    }
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
