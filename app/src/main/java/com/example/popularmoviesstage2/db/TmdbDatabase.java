package com.example.popularmoviesstage2.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.popularmoviesstage2.dao.MovieDao;
import com.example.popularmoviesstage2.dao.ReviewDao;
import com.example.popularmoviesstage2.dao.VideoDao;
import com.example.popularmoviesstage2.models.Movie;
import com.example.popularmoviesstage2.models.Review;
import com.example.popularmoviesstage2.models.Video;

@Database(entities = {Movie.class, Video.class, Review.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class TmdbDatabase extends RoomDatabase {
    public abstract MovieDao movieDao();
    public abstract VideoDao videoDao();
    public abstract ReviewDao reviewDao();
}
