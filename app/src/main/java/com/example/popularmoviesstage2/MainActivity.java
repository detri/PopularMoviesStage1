package com.example.popularmoviesstage2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.popularmoviesstage2.adapters.MovieListAdapter;
import com.example.popularmoviesstage2.models.Movie;
import com.example.popularmoviesstage2.utils.MovieRequestQueue;
import com.example.popularmoviesstage2.utils.NetworkUtils;
import com.example.popularmoviesstage2.viewmodels.MovieListViewModel;
import com.example.popularmoviesstage2.viewmodels.MovieListViewModelFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String sortMode = "popular";
    private boolean hasFetchedPopularMovies = false;
    private boolean hasFetchedTopRatedMovies = false;

    private static final String FETCHED_POPULAR_MOVIES_KEY = "fetched_popular_movies";
    private static final String FETCHED_TOP_RATED_MOVIES_KEY = "fetched_top_rated_movies";
    private static final String POPULAR_MOVIE_IDS_KEY = "popular_movie_ids";
    private static final String TOP_RATED_MOVIE_IDS_KEY = "top_rated_movie_ids";
    private static final String JSON_RESULTS_KEY = "results";

    private TextView mErrorText;
    private TextView mLoadingText;

    private RecyclerView mMoviesList;

    private int[] topRatedMovieIds = new int[0];
    private int[] popularMovieIds = new int[0];
    private int[] favoriteMovieIds = new int[0];

    private MovieListAdapter adapter;
    private MovieListViewModel viewModel;

    private List<Movie> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadSavedState(savedInstanceState);

        // grab our components
        mErrorText = findViewById(R.id.tv_error_text);
        mLoadingText = findViewById(R.id.tv_loading_text);
        mMoviesList = findViewById(R.id.rv_movie_list);

        setupRecyclerView();

        observeMovies();

        fetchMovies();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(getApplicationContext());
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_sort_by_favorite) {
            setSortMode("favorites");
            return true;
        }
        if (item.getItemId() == R.id.action_sort_by_popular) {
            setSortMode("popular");
            return true;
        }
        if (item.getItemId() == R.id.action_sort_by_top_rated) {
            setSortMode("top_rated");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(FETCHED_POPULAR_MOVIES_KEY, hasFetchedPopularMovies);
        outState.putBoolean(FETCHED_TOP_RATED_MOVIES_KEY, hasFetchedTopRatedMovies);
        outState.putIntArray(POPULAR_MOVIE_IDS_KEY, popularMovieIds);
        outState.putIntArray(TOP_RATED_MOVIE_IDS_KEY, topRatedMovieIds);
        super.onSaveInstanceState(outState);
    }

    public void setSortMode(String sortMode) {
        this.sortMode = sortMode;
        sortMovies();
    }

    private void fetchMovies() {
        // set up NetworkUtils and make initial request
        NetworkUtils.setApiKey(getString(R.string.api_key));

        fetchPopularMovieData();
        fetchTopRatedMovieData();
    }

    private void sortMovies() {
        adapter.setMovies(filterMoviesById());
    }

    private void loadSavedState(Bundle savedState) {
        if (savedState != null) {
            hasFetchedPopularMovies = savedState.getBoolean(FETCHED_POPULAR_MOVIES_KEY, false);
            hasFetchedTopRatedMovies = savedState.getBoolean(FETCHED_TOP_RATED_MOVIES_KEY, false);
            if (savedState.containsKey(TOP_RATED_MOVIE_IDS_KEY)) {
                topRatedMovieIds = savedState.getIntArray(TOP_RATED_MOVIE_IDS_KEY);
            }
            if (savedState.containsKey(POPULAR_MOVIE_IDS_KEY)) {
                popularMovieIds = savedState.getIntArray(POPULAR_MOVIE_IDS_KEY);
            }
        }
    }

    private void setupRecyclerView() {
        // set up layout manager
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mMoviesList.setHasFixedSize(true);
        mMoviesList.setLayoutManager(layoutManager);

        // set up adapter
        adapter = new MovieListAdapter(new LinkedList<Movie>(), getApplicationContext());
        mMoviesList.setAdapter(adapter);
    }

    private void observeMovies() {
        MovieListViewModelFactory factory = new MovieListViewModelFactory(this);
        viewModel = ViewModelProviders.of(this, factory).get(MovieListViewModel.class);

        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                movieList = movies;
                favoriteMovieIds = getFavoriteMovieIds();
                sortMovies();
                if (mMoviesList.getVisibility() == View.INVISIBLE) {
                    showMoviesList();
                }
            }
        });
    }

    private void fetchPopularMovieData() {
        MovieRequestQueue.getInstance(this)
                .getRequestQueue()
                .add(new JsonObjectRequest(NetworkUtils.getPopularUrl(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray results = response.optJSONArray(JSON_RESULTS_KEY);
                        List<Movie> popularMovies = NetworkUtils.jsonArrayToMovieList(results);
                        popularMovieIds = getMovieIds(popularMovies);
                        viewModel.populateMovieTable(popularMovies);
                        hasFetchedPopularMovies = true;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        showErrorText();
                    }
                }));
    }

    private void fetchTopRatedMovieData() {
        MovieRequestQueue.getInstance(this)
                .getRequestQueue()
                .add(new JsonObjectRequest(NetworkUtils.getTopRatedUrl(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray results = response.optJSONArray(JSON_RESULTS_KEY);
                        List<Movie> topRatedMovies = NetworkUtils.jsonArrayToMovieList(results);
                        topRatedMovieIds = getMovieIds(topRatedMovies);
                        viewModel.populateMovieTable(topRatedMovies);
                        hasFetchedTopRatedMovies = true;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        showErrorText();
                    }
                }));
    }

    private void showErrorText() {
        mMoviesList.setVisibility(View.INVISIBLE);
        mLoadingText.setVisibility(View.INVISIBLE);
        mErrorText.setVisibility(View.VISIBLE);
    }

    private void showMoviesList() {
        mErrorText.setVisibility(View.INVISIBLE);
        mLoadingText.setVisibility(View.INVISIBLE);
        mMoviesList.setVisibility(View.VISIBLE);
    }

    private int[] getFavoriteMovieIds() {
        List<Movie> favoriteMovies = new LinkedList<>();
        for (Movie movie : movieList) {
            if (movie.isFavorited()) {
                favoriteMovies.add(movie);
            }
        }
        int[] movieIds = new int[favoriteMovies.size()];
        for (int i = 0; i < favoriteMovies.size(); i++) {
            movieIds[i] = favoriteMovies.get(i).getId();
        }
        return movieIds;
    }

    private int[] getMovieIds(List<Movie> movies) {
        int[] movieIds = new int[movies.size()];
        for (int i = 0; i < movies.size(); i++) {
            movieIds[i] = movies.get(i).getId();
        }
        return movieIds;
    }

    private List<Movie> filterMoviesById() {
        List<Movie> filteredMovies = new LinkedList<>();
        int[] filterIds;

        if (sortMode.equals("popular")) {
            filterIds = popularMovieIds;
        } else if (sortMode.equals("top_rated")) {
            filterIds = topRatedMovieIds;
        } else {
            filterIds = favoriteMovieIds;
        }

        for (Movie movie : movieList) {
            for (int id : filterIds) {
                if (movie.getId() == id) {
                    filteredMovies.add(movie);
                }
            }
        }
        return filteredMovies;
    }
}
