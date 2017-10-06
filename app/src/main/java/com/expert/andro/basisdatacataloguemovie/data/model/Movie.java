package com.expert.andro.basisdatacataloguemovie.data.model;

import android.database.Cursor;

import com.expert.andro.basisdatacataloguemovie.data.provider.MovieDbContract;

import org.parceler.Parcel;

/**
 * Created by adul on 27/09/17.
 */

@Parcel(Parcel.Serialization.BEAN)
public class Movie {
    private int id;
    private String title;
    private String poster_path;
    private String backdrop_path;
    private String overview;
    private String release_date;
    private boolean isFavMovie = false;

    public Movie() {
    }

    private Movie(Builder builder) {
        setId(builder.id);
        setTitle(builder.title);
        setPoster_path(builder.poster_path);
        setBackdrop_path(builder.backdrop_path);
        setOverview(builder.overview);
        setRelease_date(builder.release_date);
        setFavMovie(builder.isFavMovie);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public boolean isFavMovie() {
        return isFavMovie;
    }

    public void setFavMovie(boolean favMovie) {
        isFavMovie = favMovie;
    }

    public static final class Builder {
        private int id;
        private String title;
        private float popularity;
        private String poster_path;
        private String backdrop_path;
        private String overview;
        private String release_date;
        private int vote_count;
        private float vote_average;
        private boolean isFavMovie;

        public Builder() {
        }

        public Builder id(int val) {
            id = val;
            return this;
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder popularity(float val) {
            popularity = val;
            return this;
        }

        public Builder poster_path(String val) {
            poster_path = val;
            return this;
        }

        public Builder backdrop_path(String val) {
            backdrop_path = val;
            return this;
        }

        public Builder overview(String val) {
            overview = val;
            return this;
        }

        public Builder release_date(String val) {
            release_date = val;
            return this;
        }

        public Builder vote_count(int val) {
            vote_count = val;
            return this;
        }

        public Builder vote_average(float val) {
            vote_average = val;
            return this;
        }

        public Builder isFavMovie(boolean val) {
            isFavMovie = val;
            return this;
        }

        public Movie build() {
            return new Movie(this);
        }
    }

    public static Movie fromCursor(Cursor query) {
        return new Movie.Builder()
                .id(query.getInt(query.getColumnIndex(MovieDbContract.Movie.COLUMN_MOVIE_ID)))
                .title(query.getString(query.getColumnIndex(MovieDbContract.Movie.COLUMN_MOVIE_TITLE)))
                .overview(query.getString(query.getColumnIndex(MovieDbContract.Movie.COLUMN_MOVIE_OVERVIEW)))
                .poster_path(query.getString(query.getColumnIndex(MovieDbContract.Movie.COLUMN_MOVIE_POSTER_PATH)))
                .release_date(query.getString(query.getColumnIndex(MovieDbContract.Movie.COLUMN_MOVIE_RELEASE_DATE)))
                .backdrop_path(query.getString(query.getColumnIndex(MovieDbContract.Movie.COLUMN_MOVIE_BACKDROP_PATH)))
                .isFavMovie(query.getInt(query.getColumnIndex(MovieDbContract.Movie.COLUMN_MOVIE_FAVORED)) > 0)
                .build();
    }
}
