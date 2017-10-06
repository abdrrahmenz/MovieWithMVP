package com.expert.andro.basisdatacataloguemovie.ui;

/**
 * Created by adul on 28/09/17.
 */

public interface DetailView {

    void displayError(String message);

    void favoriteSuccess();

    void showError(String message);

    void movieIsFavorited();
}
