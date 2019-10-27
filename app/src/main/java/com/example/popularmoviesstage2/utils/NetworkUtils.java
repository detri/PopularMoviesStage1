package com.example.popularmoviesstage2.utils;

import android.net.Uri;

import com.example.popularmoviesstage2.models.Movie;
import com.example.popularmoviesstage2.models.Review;
import com.example.popularmoviesstage2.models.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class NetworkUtils {
    private static String apiKey;

    private static final String MOVIE_PATH = "movie";
    private static final String VIDEO_PATH = "videos";
    private static final String REVIEW_PATH = "reviews";
    private static final String TOP_RATED_PATH = "top_rated";
    private static final String POPULAR_PATH = "popular";
    private static final String LARGE_POSTER_URI = "image.tmdb.org/t/p/w342";
    private static final String POSTER_URI = "image.tmdb.org/t/p/w185";
    private static final String BASE_URI = "api.themoviedb.org/3";
    private static final String URI_SCHEME = "https";
    private static final String POSTER_PATH_KEY = "poster_path";
    private static final String OVERVIEW_KEY = "overview";
    private static final String RELEASE_DATE_KEY = "release_date";
    private static final String ID_KEY = "id";
    private static final String ORIGINAL_TITLE_KEY = "original_title";
    private static final String VOTE_AVERAGE_KEY = "vote_average";
    private static final String NAME_KEY = "name";
    private static final String VIDEO_KEY = "key";
    private static final String SITE_KEY = "site";
    private static final String TYPE_KEY = "type";
    private static final String AUTHOR_KEY = "author";
    private static final String CONTENT_KEY = "content";
    private static final String VERY_SPECIAL_VIDEO_KEY = "oHg5SJYRHA0";

    public static void setApiKey(String newKey) {
        apiKey = newKey;
    }

    private static Uri.Builder getBaseUri() {
        return new Uri.Builder()
                .scheme(URI_SCHEME)
                .path(BASE_URI)
                .appendQueryParameter("api_key", apiKey);
    }

    private static Uri.Builder getPosterBaseUri() {
        return new Uri.Builder()
                .scheme(URI_SCHEME)
                .path(POSTER_URI);
    }

    private static Uri.Builder getLargePosterBaseUri() {
        return new Uri.Builder()
                .scheme(URI_SCHEME)
                .path(LARGE_POSTER_URI);
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
                .appendPath(MOVIE_PATH)
                .appendPath(POPULAR_PATH)
                .build()
                .toString();
    }

    public static String getTopRatedUrl() {
        return getBaseUri()
                .appendPath(MOVIE_PATH)
                .appendPath(TOP_RATED_PATH)
                .build()
                .toString();
    }

    public static String getVideoUrl(int movieId) {
        return getBaseUri()
                .appendPath(MOVIE_PATH)
                .appendPath(String.valueOf(movieId))
                .appendPath(VIDEO_PATH)
                .build()
                .toString();
    }

    public static String getReviewUrl(int movieId) {
        return getBaseUri()
                .appendPath(MOVIE_PATH)
                .appendPath(String.valueOf(movieId))
                .appendPath(REVIEW_PATH)
                .build()
                .toString();
    }

    public static List<Review> jsonArrayToReviewList(JSONArray jsonArray, int movieId) {
        List<Review> reviewList = new LinkedList<>();
        if (jsonArray == null) {
            return reviewList;
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                Review review = jsonObjectToReview(jsonArray.getJSONObject(i), movieId);
                reviewList.add(review);
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
        return reviewList;
    }

    private static Review jsonObjectToReview(JSONObject jsonObject, int movieId) {
        String id = jsonObject.optString(ID_KEY, "-1");
        String author = jsonObject.optString(AUTHOR_KEY, "Anonymous");
        String content = jsonObject.optString(CONTENT_KEY, "No content.");

        return new Review(
                id,
                author,
                content,
                movieId
        );
    }

    public static List<Video> jsonArrayToVideoList(JSONArray jsonArray, int movieId) {
        List<Video> videoList = new LinkedList<>();
        if (jsonArray == null) {
            return videoList;
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                Video video = jsonObjectToVideo(jsonArray.getJSONObject(i), movieId);
                videoList.add(video);
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
        return videoList;
    }

    public static Video jsonObjectToVideo(JSONObject jsonObject, int movieId) {
        String id = jsonObject.optString(ID_KEY, "-1");
        String name = jsonObject.optString(NAME_KEY, "Untitled Trailer");
        String key = jsonObject.optString(VIDEO_KEY, VERY_SPECIAL_VIDEO_KEY);
        String site = jsonObject.optString(SITE_KEY, "YouTube");
        String type = jsonObject.optString(TYPE_KEY, "Trailer");

        return new Video(
                id,
                name,
                key,
                site,
                type,
                movieId
        );
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
