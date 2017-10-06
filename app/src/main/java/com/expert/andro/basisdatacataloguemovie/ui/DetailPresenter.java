package com.expert.andro.basisdatacataloguemovie.ui;

import android.util.Log;

import com.expert.andro.basisdatacataloguemovie.data.model.Movie;
import com.expert.andro.basisdatacataloguemovie.data.repository.MovieRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by adul on 28/09/17.
 */

public class DetailPresenter {

    private static final String TAG = DetailPresenter.class.getSimpleName();
    private MovieRepository movieRepository;
    private DetailView view;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public DetailPresenter(MovieRepository movieRepository, DetailView view) {
        this.movieRepository = movieRepository;
        this.view = view;
    }

    public void favoriteMovie(Movie movie){
        compositeDisposable.add(movieRepository.saveMovie(movie)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(() -> view.favoriteSuccess(),
                throwable -> view.showError(throwable.getLocalizedMessage())));
    }

    public void isMovieFavorited(Movie movie){
        compositeDisposable.add(movieRepository.getMovie(movie)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> view.movieIsFavorited(), error -> {
            Log.e(TAG, "isMovieFavorited: " +error.getLocalizedMessage());
        }));
    }

    public void deleteFavorite(Movie movie){
        compositeDisposable.add(movieRepository.deleteMovie(movie)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(() -> {
            Log.d(TAG, "deleteFavorite: success");
        }, throwable -> {
            Log.d(TAG, "deleteFavorite: failed");
        }));
    }
}
