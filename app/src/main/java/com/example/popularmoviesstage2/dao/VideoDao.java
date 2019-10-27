package com.example.popularmoviesstage2.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.popularmoviesstage2.models.Video;

import java.util.List;

@Dao
public interface VideoDao {
    @Query("SELECT * FROM video WHERE movie_id = :movieId AND type = 'Trailer' AND site = 'YouTube'")
    LiveData<List<Video>> getTrailersByMovie(int movieId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertVideos(Video... videos);
}
