package com.example.popularmoviesstage2.utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MovieRequestQueue {
    private static MovieRequestQueue ourInstance;
    private final RequestQueue requestQueue;

    public static MovieRequestQueue getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new MovieRequestQueue(context);
        }
        return ourInstance;
    }

    private MovieRequestQueue(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
