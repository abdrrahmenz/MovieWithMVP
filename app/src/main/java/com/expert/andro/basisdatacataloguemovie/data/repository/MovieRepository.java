package com.expert.andro.basisdatacataloguemovie.data.repository;

import com.expert.andro.basisdatacataloguemovie.data.model.Movie;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by adul on 27/09/17.
 */

public interface MovieRepository {

    Observable<List<Movie>> getUpcomingMovie(int page);

    Observable<List<Movie>> getNowPlayingMovie(int page);

    Observable<List<Movie>> getSearchMovie(String search, int page);

    Completable saveMovie(Movie movie);

    Completable deleteMovie(Movie movie);

    Single<Movie> getMovie(Movie movie);

    Observable<List<Movie>> getFavoriteMovies();
}
