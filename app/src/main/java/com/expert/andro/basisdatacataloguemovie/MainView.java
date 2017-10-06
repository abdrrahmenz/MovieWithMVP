package com.expert.andro.basisdatacataloguemovie;

import com.expert.andro.basisdatacataloguemovie.data.model.Movie;

import java.util.List;

/**
 * Created by adul on 27/09/17.
 */

public interface MainView {

    void showLoading(boolean show);

    void displayMovies(List<Movie> results);

    void showError(String message);
}
