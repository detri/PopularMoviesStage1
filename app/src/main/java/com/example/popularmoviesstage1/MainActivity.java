package com.example.popularmoviesstage1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.popularmoviesstage1.utils.NetworkUtils;

import org.json.JSONArray;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue = Volley.newRequestQueue(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up NetworkUtils and make initial request
        NetworkUtils.setApiKey(getString(R.string.api_key));
        requestQueue.add(fetchPopularMovieData());
    }

    private JsonArrayRequest fetchPopularMovieData() {
        return new JsonArrayRequest(NetworkUtils.getPopularUrl(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
    }
}
