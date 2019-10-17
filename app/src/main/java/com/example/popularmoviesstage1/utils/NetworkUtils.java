package com.example.popularmoviesstage1.utils;

import android.net.Uri;

import com.example.popularmoviesstage1.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class NetworkUtils {
    private static String apiKey;

    private static final String POSTER_PATH_KEY = "poster_path";
    private static final String OVERVIEW_KEY = "overview";
    private static final String RELEASE_DATE_KEY = "release_date";
    private static final String ID_KEY = "id";
    private static final String ORIGINAL_TITLE_KEY = "original_title";
    private static final String VOTE_AVERAGE_KEY = "vote_average";

    public static void setApiKey(String newKey) {
        apiKey = newKey;
    }

    private static Uri.Builder getBaseUri() {
        return new Uri.Builder()
                .scheme("https")
                .path("api.themoviedb.org/3")
                .appendQueryParameter("api_key", apiKey);
    }

    private static Uri.Builder getPosterBaseUri() {
        return new Uri.Builder()
                .scheme("https")
                .path("image.tmdb.org/t/p/w185");
    }

    private static Uri.Builder getLargePosterBaseUri() {
        return new Uri.Builder()
                .scheme("https")
                .path("image.tmdb.org/t/p/w500");
    }

    public static String getLargePosterPath(String posterPath) {
        return getLargePosterBaseUri()
                .appendPath(posterPath.replace("/", ""))
                .build()
                .toString();
    }

    public static String getFullPosterPath(String posterPath) {
        return getPosterBaseUri()
                .appendPath(posterPath.replace("/", ""))
                .build()
                .toString();
    }

    public static String getPopularUrl() {
        return getBaseUri()
                .appendPath("movie")
                .appendPath("popular")
                .build()
                .toString();
    }

    public static String getTopRatedUrl() {
        return getBaseUri()
                .appendPath("movie")
                .appendPath("top_rated")
                .build()
                .toString();
    }

    public static List<Movie> jsonArrayToMovieList(JSONArray jsonArray) {
        List<Movie> movieList = new LinkedList<>();
        if (jsonArray == null) {
            return movieList;
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                Movie movie = jsonObjectToMovie(jsonArray.getJSONObject(i));
                movieList.add(movie);
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
        return movieList;
    }

    private static Movie jsonObjectToMovie(JSONObject jsonObject) {
        String posterPath = jsonObject.optString(POSTER_PATH_KEY, "");
        String overview = jsonObject.optString(OVERVIEW_KEY, "No plot synopsis found.");
        String releaseDate = jsonObject.optString(RELEASE_DATE_KEY, "No release date found.");
        int id = jsonObject.optInt(ID_KEY, -1);
        String originalTitle = jsonObject.optString(ORIGINAL_TITLE_KEY, "No title found.");
        double voteAverage = jsonObject.optDouble(VOTE_AVERAGE_KEY, 0);

        return new Movie(
                posterPath,
                overview,
                releaseDate,
                id,
                originalTitle,
                voteAverage
        );
    }
}
