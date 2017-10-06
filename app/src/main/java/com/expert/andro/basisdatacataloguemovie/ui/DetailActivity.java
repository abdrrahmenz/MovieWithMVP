package com.expert.andro.basisdatacataloguemovie.ui;

import android.databinding.DataBindingUtil;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.expert.andro.basisdatacataloguemovie.R;
import com.expert.andro.basisdatacataloguemovie.data.model.Movie;
import com.expert.andro.basisdatacataloguemovie.data.repository.MovieRepository;
import com.expert.andro.basisdatacataloguemovie.data.repository.MovieRepositoryImpl;
import com.expert.andro.basisdatacataloguemovie.data.service.MovieDbApi;
import com.expert.andro.basisdatacataloguemovie.databinding.ActivityDetailBinding;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity implements DetailView {

    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final String STATE_MOVIE = "state_movie";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.btn_favorite)
    FloatingActionButton fabFavorite;
    @BindView(R.id.poster)
    ImageView imgPoster;

    private Movie movie;
    private DetailPresenter presenter;
    private MovieRepository movieRepository;
    private MovieDbApi movieDbApi;

    private boolean isFavorited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        movieDbApi = MovieDbApi.Creator.instance();

        movieRepository = new MovieRepositoryImpl(movieDbApi, DetailActivity.this.getContentResolver());
        presenter = new DetailPresenter(movieRepository, this);

        movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));

        if (savedInstanceState != null){
            Log.d(TAG, "onCreate: instance state");
            movie = Parcels.unwrap(savedInstanceState.getParcelable(STATE_MOVIE));
        }

        if (movie != null){

            ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
            ButterKnife.bind(this, binding.getRoot());

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            binding.setMovie(movie);
            collapsingToolbarLayout.setTitle("");

            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

                boolean isShow = false;
                int scrollRange = -1;

                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (scrollRange == -1){
                        scrollRange = appBarLayout.getTotalScrollRange();
                    }

                    if (scrollRange + verticalOffset == 0){
                        collapsingToolbarLayout.setTitle(movie.getTitle());
                        isShow = true;
                    }else {
                        collapsingToolbarLayout.setTitle("");
                        isShow = false;
                    }
                }
            });

            presenter.isMovieFavorited(movie);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (movie != null){
            outState.putParcelable(STATE_MOVIE, Parcels.wrap(movie));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void displayError(String message) {

    }

    @Override
    public void favoriteSuccess() {
        Log.d(TAG, "favoriteSuccess: ");
        isFavorited = true;
        fabFavorite.setImageResource(R.drawable.ic_favorite_white_36dp);
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void movieIsFavorited() {
        isFavorited = true;
        fabFavorite.setImageResource(R.drawable.ic_favorite_white_36dp);
    }

    @OnClick(R.id.btn_favorite)
    void onFavoriteClick(){
        if (isFavorited){
            presenter.deleteFavorite(movie);
            fabFavorite.setImageResource(R.drawable.ic_favorite_border_white_36dp);
        }else {
            presenter.favoriteMovie(movie);
        }
    }
}
