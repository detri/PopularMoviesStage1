package com.example.popularmoviesstage2.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.popularmoviesstage2.models.Review;

import java.util.List;

@Dao
public interface ReviewDao {
    @Query("SELECT * FROM review WHERE movie_id = :movieId")
    LiveData<List<Review>> getByMovie(int movieId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertReviews(Review... reviews);
}
