package com.expert.andro.basisdatacataloguemovie.data.repository;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.expert.andro.basisdatacataloguemovie.data.model.Movie;
import com.expert.andro.basisdatacataloguemovie.data.provider.MovieDbContract;
import com.expert.andro.basisdatacataloguemovie.data.service.MovieDbApi;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

import static com.expert.andro.basisdatacataloguemovie.data.provider.MovieDbContract.Movie.COLUMN_MOVIE_BACKDROP_PATH;
import static com.expert.andro.basisdatacataloguemovie.data.provider.MovieDbContract.Movie.COLUMN_MOVIE_FAVORED;
import static com.expert.andro.basisdatacataloguemovie.data.provider.MovieDbContract.Movie.COLUMN_MOVIE_ID;
import static com.expert.andro.basisdatacataloguemovie.data.provider.MovieDbContract.Movie.COLUMN_MOVIE_OVERVIEW;
import static com.expert.andro.basisdatacataloguemovie.data.provider.MovieDbContract.Movie.COLUMN_MOVIE_POSTER_PATH;
import static com.expert.andro.basisdatacataloguemovie.data.provider.MovieDbContract.Movie.COLUMN_MOVIE_RELEASE_DATE;
import static com.expert.andro.basisdatacataloguemovie.data.provider.MovieDbContract.Movie.COLUMN_MOVIE_TITLE;
import static com.expert.andro.basisdatacataloguemovie.data.provider.MovieDbContract.Movie.CONTENT_URI;

/**
 * Created by adul on 27/09/17.
 */

public class MovieRepositoryImpl implements MovieRepository{

    private static final String TAG = MovieRepositoryImpl.class.getSimpleName();
    private MovieDbApi services;
    private ContentResolver contentResolver;

    private final String[] movieProjection = new String[]{
            COLUMN_MOVIE_ID,
            COLUMN_MOVIE_TITLE,
            COLUMN_MOVIE_OVERVIEW,
            COLUMN_MOVIE_RELEASE_DATE,
            COLUMN_MOVIE_FAVORED,
            COLUMN_MOVIE_POSTER_PATH,
            COLUMN_MOVIE_BACKDROP_PATH,
    };

    public MovieRepositoryImpl(MovieDbApi services, ContentResolver contentResolver) {
        this.services = services;
        this.contentResolver = contentResolver;
    }

    @Override
    public Observable<List<Movie>> getUpcomingMovie(int page) {
        return services.getUpcomingMovie(page).flatMap(movieResponse -> Observable.just(movieResponse.results));
    }

    @Override
    public Observable<List<Movie>> getNowPlayingMovie(int page) {
        return services.getNowPlayingMovie(page).flatMap(movieResponse -> Observable.just(movieResponse.results));
    }

    @Override
    public Observable<List<Movie>> getSearchMovie(String search, int page) {
        return services.getSearchMovie(search,page).flatMap(movieResponse -> Observable.just(movieResponse.results));
    }

    @Override
    public Completable saveMovie(Movie movie) {
        Log.d(TAG, "saveMovie: " +movie.getTitle());
        return Completable.create(e -> {
            final ContentValues movieValues = getMovieValues(movie);
            contentResolver.insert(CONTENT_URI, movieValues);
            e.onComplete();
        });
    }

    @Override
    public Completable deleteMovie(Movie movie) {
        Log.d(TAG, "deleteMovie: "+movie.getTitle());
        return Completable.create(e -> {
            final String where = String.format("%s=?", COLUMN_MOVIE_ID);
            final String[] args = new String[]{String.valueOf(movie.getId())};
            contentResolver.delete(CONTENT_URI, where, args);
            e.onComplete();
        });
    }

    @Override
    public Single<Movie> getMovie(Movie movie) {
        return Single.create(e -> {
            final String where = String.format("%s=?", COLUMN_MOVIE_ID);
            final String[] args = new String[]{String.valueOf(movie.getId())};
            final Cursor cursor = contentResolver.query(CONTENT_URI, movieProjection, where, args, null);
            Log.d(TAG, "getMovie: "+movie.getTitle());
            Log.d(TAG, "getMovie: cursor"+cursor.getCount());
            if (cursor.getCount() >= 1){
                cursor.moveToFirst();
                final Movie resultMovie = Movie.fromCursor(cursor);
                e.onSuccess(resultMovie);
            } else {
                e.onError(new Throwable("no movies"));
            }
        });
    }

    @Override
    public Observable<List<Movie>> getFavoriteMovies() {
        return Observable.create(e -> {
            List<Movie> movies = new ArrayList<Movie>();
            final Cursor query = contentResolver.query(CONTENT_URI, movieProjection, null, null, null);
            if (query.moveToFirst()){
                do {
                    movies.add(Movie.fromCursor(query));
                }while (query.moveToNext());
            }
            Log.d(TAG, "getFavoriteMovies: "+movies.size());
            e.onNext(movies);
        });
    }

    public static ContentValues getMovieValues(Movie movie) {
        final ContentValues values = new ContentValues();
        values.put(COLUMN_MOVIE_ID, movie.getId());
        values.put(COLUMN_MOVIE_TITLE, movie.getTitle());
        values.put(COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        values.put(COLUMN_MOVIE_RELEASE_DATE, movie.getRelease_date());
        values.put(COLUMN_MOVIE_FAVORED, movie.isFavMovie());
        values.put(COLUMN_MOVIE_POSTER_PATH, movie.getPoster_path());
        values.put(COLUMN_MOVIE_BACKDROP_PATH, movie.getBackdrop_path());
        return values;
    }
}
