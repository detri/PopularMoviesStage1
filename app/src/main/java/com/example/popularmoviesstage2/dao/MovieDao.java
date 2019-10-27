package com.example.popularmoviesstage2.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.popularmoviesstage2.models.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movie ORDER BY created_at")
    LiveData<List<Movie>> getAll();

    @Query("SELECT * FROM movie WHERE id = :id")
    LiveData<Movie> getById(int id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(Movie... movies);

    @Update
    void update(Movie movie);
}
