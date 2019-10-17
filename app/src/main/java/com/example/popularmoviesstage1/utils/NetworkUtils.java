package com.example.popularmoviesstage1.utils;

import android.net.Uri;

public class NetworkUtils {
    private static String apiKey;
    private static Uri.Builder BASE_URI;
    private static String POPULAR_URL;
    private static String TOP_RATED_URL;

    public static void setApiKey(String newKey) {
        apiKey = newKey;
    }

    private static Uri.Builder getBaseUri() {
        if (BASE_URI == null) {
            BASE_URI = new Uri.Builder()
                    .scheme("https")
                    .path("api.themoviedb.org/3")
                    .appendQueryParameter("api_key", apiKey);
        }
        return BASE_URI;
    }

    public static String getPopularUrl() {
        if (POPULAR_URL == null) {
            POPULAR_URL = getBaseUri()
                    .appendPath("/movie/popular")
                    .build()
                    .toString();
        }
        return POPULAR_URL;
    }

    public static String getTopRatedUrl() {
        if (TOP_RATED_URL == null) {
            TOP_RATED_URL = getBaseUri()
                    .appendPath("/movie/top_rated")
                    .build()
                    .toString();
        }
        return TOP_RATED_URL;
    }
}
