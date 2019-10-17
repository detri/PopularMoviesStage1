package com.example.popularmoviesstage1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.popularmoviesstage1.adapters.MovieListAdapter;
import com.example.popularmoviesstage1.models.Movie;
import com.example.popularmoviesstage1.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;

    private boolean sortingByPopular = true;

    private TextView mErrorText;
    private TextView mLoadingText;

    private MenuItem mToggleSort;

    private RecyclerView mMoviesList;

    private List<Movie> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // grab our components
        mErrorText = findViewById(R.id.tv_error_text);
        mLoadingText = findViewById(R.id.tv_loading_text);
        mMoviesList = findViewById(R.id.rv_movie_list);

        // set up NetworkUtils and make initial request
        requestQueue = Volley.newRequestQueue(this);
        NetworkUtils.setApiKey(getString(R.string.api_key));
        requestQueue.add(fetchPopularMovieData());
    }

    private JsonObjectRequest fetchPopularMovieData() {
        return new JsonObjectRequest(NetworkUtils.getPopularUrl(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray results = response.optJSONArray("results");
                        movieList = NetworkUtils.jsonArrayToMovieList(results);
                        showMoviesList();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        showErrorText();
                    }
                });
    }

    private JsonObjectRequest fetchTopRatedMovieData() {
        return new JsonObjectRequest(NetworkUtils.getTopRatedUrl(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray results = response.optJSONArray("results");
                        movieList = NetworkUtils.jsonArrayToMovieList(results);
                        showMoviesList();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        showErrorText();
                    }
                });
    }

    private void showErrorText() {
        mLoadingText.setVisibility(View.INVISIBLE);
        mErrorText.setVisibility(View.VISIBLE);
    }

    private void showMoviesList() {
        // set up layout manager
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mMoviesList.setHasFixedSize(true);
        mMoviesList.setLayoutManager(layoutManager);

        // set up adapter
        MovieListAdapter adapter = new MovieListAdapter(movieList);
        mMoviesList.setAdapter(adapter);

        mLoadingText.setVisibility(View.INVISIBLE);
        mMoviesList.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(getApplicationContext());
        inflater.inflate(R.menu.main, menu);
        mToggleSort = menu.findItem(R.id.action_toggle_sort);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_toggle_sort) {
            toggleSort();
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleSort() {
        sortingByPopular = !sortingByPopular;
        if (sortingByPopular) {
            requestQueue.add(fetchPopularMovieData());
            mToggleSort.setTitle(R.string.sort_by_popular);
        } else {
            requestQueue.add(fetchTopRatedMovieData());
            mToggleSort.setTitle(R.string.sort_by_top_rated);
        }
    }
}
