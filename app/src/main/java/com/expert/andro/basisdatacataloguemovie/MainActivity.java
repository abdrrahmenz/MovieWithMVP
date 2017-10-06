package com.expert.andro.basisdatacataloguemovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.expert.andro.basisdatacataloguemovie.data.model.Movie;
import com.expert.andro.basisdatacataloguemovie.data.repository.MovieRepository;
import com.expert.andro.basisdatacataloguemovie.data.repository.MovieRepositoryImpl;
import com.expert.andro.basisdatacataloguemovie.data.service.MovieDbApi;
import com.expert.andro.basisdatacataloguemovie.databinding.ItemMovieBinding;
import com.expert.andro.basisdatacataloguemovie.ui.DetailActivity;
import com.expert.andro.basisdatacataloguemovie.ui.utils.EndlessScrollListener;
import com.github.nitrico.lastadapter.Holder;
import com.github.nitrico.lastadapter.ItemType;
import com.github.nitrico.lastadapter.LastAdapter;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainView, SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int UPCOMING = 0;
    public static final int NOW_PLAYING = 1;
    private static final String RECYCLER_POSITION = "rv_position";
    private static final String STATE_MOVIE_VIDEOS = "state_movies";
    private static final String STATE_PAGE = "state_page";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.content)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.rv_list_movies)
    RecyclerView recyclerView;
    @BindView(R.id.tv_error)
    TextView errorText;
    @BindView(R.id.loading)
    ProgressBar loading;

    private EndlessScrollListener endlessScrollListener;

    private MovieRepository movieRepository;
    private MovieDbApi movieDbApi;

    private MainPresenter presenter;
    private int selectedSort = 0;
    private int page = 1;
    private LastAdapter lastAdapter;
    private List<Movie> movies = new ArrayList<>();
    private int recyclerPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // init retrofit
        movieDbApi = MovieDbApi.Creator.instance();
        // init repository
        movieRepository = new MovieRepositoryImpl(movieDbApi, MainActivity.this.getContentResolver());

        // init presenter
        presenter = new MainPresenter(movieRepository, this);

        initRecyclerView();

        if (savedInstanceState == null) {
            // get movie data
            presenter.getMovie(UPCOMING, page);
        } else {
            // set page
            page = savedInstanceState.getInt(STATE_PAGE);
            recyclerPosition = savedInstanceState.getInt(RECYCLER_POSITION, 0);

            final List<Movie> movies = Parcels.unwrap(savedInstanceState.getParcelable(STATE_MOVIE_VIDEOS));
            this.movies.clear();
            this.movies.addAll(movies);

            lastAdapter.notifyDataSetChanged();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (recyclerView != null)
            outState.putInt(RECYCLER_POSITION, recyclerView.getScrollY());
        if (movies != null) {
            outState.putParcelable(STATE_MOVIE_VIDEOS, Parcels.wrap(movies));
        }

        outState.putInt(STATE_PAGE, page);
    }

    private void initRecyclerView() {
        final int columnCount = getResources().getInteger(R.integer.grid_count);
        final LinearLayoutManager layoutManager = new GridLayoutManager(this, columnCount);
        recyclerView.setLayoutManager(layoutManager);
        refreshLayout.setOnRefreshListener(this);

        endlessScrollListener = new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                Log.d(TAG, "onLoadMore: " + currentPage);
                presenter.getMovie(selectedSort, currentPage);
            }
        };

        recyclerView.addOnScrollListener(endlessScrollListener);
        if (recyclerPosition > 0)
            recyclerView.setScrollY(recyclerPosition);

        lastAdapter = new LastAdapter(movies, BR.movie)
                .map(Movie.class, new ItemType<ItemMovieBinding>(R.layout.item_movie) {
                    @Override
                    public void onCreate(final Holder<ItemMovieBinding> holder) {
                        super.onCreate(holder);
                        holder.itemView.setOnClickListener(v -> {
                            Log.d(TAG, "onClick: " + holder.getBinding().getMovie().getTitle());
                            Bundle data = new Bundle();
                            data.putParcelable("movie", Parcels.wrap(holder.getBinding().getMovie()));
                            startActivity(new Intent(MainActivity.this, DetailActivity.class).putExtras(data));
                        });
                    }
                })
                .into(recyclerView);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        page = 1;
        movies.clear();
        switch (item.getItemId()) {
            case R.id.action_now_playing:
                selectedSort = NOW_PLAYING;
                presenter.getMovie(NOW_PLAYING, page);
                break;
            case R.id.action_favorite:
                presenter.getFavoriteMovie();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showLoading(boolean show) {
        errorText.setVisibility(View.GONE);
        if (show){
            recyclerView.setVisibility(View.VISIBLE);
            loading.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.GONE);
        } else {
            loading.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void displayMovies(List<Movie> results) {
        movies.addAll(results);
        lastAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        errorText.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.attachView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.detachView();
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(false);
        page = 1;
        presenter.getMovie(selectedSort,1);
    }
}
