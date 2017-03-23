package com.esod.moviesdb.views.activities;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.esod.cjae.entities.Movie;
import com.esod.cjae.entities.MoviesWrapper;
import com.esod.moviesdb.MoviesDbApp;
import com.esod.moviesdb.R;
import com.esod.moviesdb.di.components.DaggerBasicMoviesUsecasesComponent;
import com.esod.moviesdb.di.modules.BasicMoviesUsecasesModule;
import com.esod.moviesdb.mvp.presenters.MoviesPresenter;
import com.esod.moviesdb.mvp.views.MoviesView;
import com.esod.moviesdb.utils.RecyclerInsetsDecoration;
import com.esod.moviesdb.utils.RecyclerViewClickListener;
import com.esod.moviesdb.views.renderer.MoviesAdapter;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MoviesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MoviesView, RecyclerViewClickListener {

    public static SparseArray<Bitmap> sPhotoCache = new SparseArray<Bitmap>(1);

    private final static String BUNDLE_MOVIES_WRAPPER = "movies_wrapper";
    private final static String BUNDLE_BACK_TRANSLATION = "background_translation";
    public final static String EXTRA_MOVIE_ID = "movie_id";
    public final static String EXTRA_MOVIE_LOCATION = "view_location";
    public final static String EXTRA_MOVIE_POSITION = "movie_position";
    public final static String SHARED_ELEMENT_COVER = "cover";

    private MoviesAdapter mMoviesAdapter;

    public float mBackgroundTranslation;

    @Nullable
    @Bind(R.id.activity_movies_background_view)
    View mTabletBackground;

    @Bind(R.id.activity_movies_progress)
    ProgressBar mProgressBar;

    @Bind(R.id.activity_movies_recycler)
    RecyclerView mRecycler;

    @Inject
    MoviesPresenter mMoviesPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ButterKnife.bind(this);
        initializeDependencyInjector();
        initializeRecycler();

        if (savedInstanceState == null)
            mMoviesPresenter.attachView(this);

        else
            initializeFromParams(savedInstanceState);
    }

    private void initializeFromParams(Bundle savedInstanceState) {
        MoviesWrapper moviesWrapper = (MoviesWrapper) savedInstanceState
                .getSerializable(BUNDLE_MOVIES_WRAPPER);

        mMoviesPresenter.onPopularMoviesReceived(moviesWrapper);
    }

    private void initializeRecycler() {
        mRecycler.addItemDecoration(new RecyclerInsetsDecoration(this));
        mRecycler.setOnScrollListener(recyclerScrollListener);
    }

    private void initializeDependencyInjector() {
        MoviesDbApp app = (MoviesDbApp) getApplication();

        DaggerBasicMoviesUsecasesComponent.builder()
                .appComponent(app.getAppComponent())
                .basicMoviesUsecasesModule(new BasicMoviesUsecasesModule())
                .build().inject(this);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_share) {

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMoviesPresenter.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMoviesAdapter != null) {
            outState.putSerializable(BUNDLE_MOVIES_WRAPPER, new MoviesWrapper(
                    mMoviesAdapter.getMovieList()));
            outState.putFloat(BUNDLE_BACK_TRANSLATION, mBackgroundTranslation);
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showMovies(List<Movie> movieList) {
        mMoviesAdapter = new MoviesAdapter(movieList);
        mMoviesAdapter.setRecyclerListListener(this);
        mRecycler.setAdapter(mMoviesAdapter);
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingLabel() {
        Snackbar loadingSnackBar = Snackbar.with(this)
                .text(getString(R.string.activity_movies_message_more_films))
                .actionLabel(getString(R.string.action_cancel))
                .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                .color(getResources().getColor(R.color.colorPrimary))
                .actionColor(getResources().getColor(R.color.colorAccent));

        SnackbarManager.show(loadingSnackBar);
    }

    @Override
    public void hideActionLabel() {
        SnackbarManager.dismiss();
    }

    @Override
    public boolean isTheListEmpty() {
        return (mMoviesAdapter == null) || mMoviesAdapter.getMovieList().isEmpty();
    }

    @Override
    public void appendMovies(List<Movie> movieList) {
        mMoviesAdapter.appendMovies(movieList);
    }

    @Override
    public void onClick(View v, int position, float x, float y) {
//        Intent movieDetailActivityIntent = new Intent(
//                MoviesActivity.this, MovieDetailActivity.class);
//
//        String movieID = mMoviesAdapter.getMovieList().get(moviePosition).getId();
//        movieDetailActivityIntent.putExtra(EXTRA_MOVIE_ID, movieID);
//        movieDetailActivityIntent.putExtra(EXTRA_MOVIE_POSITION, moviePosition);
//
//        ImageView mCoverImage = (ImageView) touchedView.findViewById(R.id.item_movie_cover);
//        BitmapDrawable bitmapDrawable = (BitmapDrawable) mCoverImage.getDrawable();
//
//        if (mMoviesAdapter.isMovieReady(moviePosition) || bitmapDrawable != null) {
//
//            sPhotoCache.put(0, bitmapDrawable.getBitmap());
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//                startDetailActivityBySharedElements(touchedView, moviePosition,
//                        movieDetailActivityIntent);
//            else
//                startDetailActivityByAnimation(touchedView, (int) touchedX,
//                        (int) touchedY, movieDetailActivityIntent);
//
//        } else {
//
//            Toast.makeText(this, getString(R.string.activity_movies_message_loading_film),
//                    Toast.LENGTH_SHORT).show();
//        }
    }

    private void startDetailActivityByAnimation(View touchedView,
                                                int touchedX, int touchedY, Intent movieDetailActivityIntent) {

        int[] touchedLocation = {touchedX, touchedY};
        int[] locationAtScreen = new int [2];
        touchedView.getLocationOnScreen(locationAtScreen);

        int finalLocationX = locationAtScreen[0] + touchedLocation[0];
        int finalLocationY = locationAtScreen[1] + touchedLocation[1];

        int [] finalLocation = {finalLocationX, finalLocationY};
        movieDetailActivityIntent.putExtra(EXTRA_MOVIE_LOCATION,
                finalLocation);

        startActivity(movieDetailActivityIntent);
    }

    @SuppressWarnings("unchecked")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startDetailActivityBySharedElements(View touchedView,
                                                     int moviePosition, Intent movieDetailActivityIntent) {

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this, new Pair<>(touchedView, SHARED_ELEMENT_COVER + moviePosition));

        startActivity(movieDetailActivityIntent, options.toBundle());
    }

    private RecyclerView.OnScrollListener recyclerScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount    = mRecycler.getLayoutManager().getChildCount();
            int totalItemCount      = mRecycler.getLayoutManager().getItemCount();
            int pastVisibleItems    = ((GridLayoutManager) mRecycler.getLayoutManager())
                    .findFirstVisibleItemPosition();

            if((visibleItemCount + pastVisibleItems) >= totalItemCount && !mMoviesPresenter.isLoading()) {
                mMoviesPresenter.onEndListReached();
            }

            if (mTabletBackground != null) {
                mBackgroundTranslation = mTabletBackground.getY() - (dy / 2);
                mTabletBackground.setTranslationY(mBackgroundTranslation);
            }
        }
    };


    @Override
    protected void onStop() {
        super.onStop();
        mMoviesPresenter.stop();
    }
}
