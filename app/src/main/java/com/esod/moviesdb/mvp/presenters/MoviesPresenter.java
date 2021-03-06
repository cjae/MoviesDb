/*
 * Copyright (C) 2015 Saúl Molinero.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.esod.moviesdb.mvp.presenters;

import com.esod.cjae.domain.usecase.ConfigurationUseCase;
import com.esod.cjae.domain.usecase.GetMoviesUseCase;
import com.esod.cjae.entities.MoviesWrapper;
import com.esod.cjae.util.Constants;
import com.esod.moviesdb.mvp.views.MoviesView;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;


public class MoviesPresenter extends Presenter {

    private final Bus mBus;
    private ConfigurationUseCase mConfigureUsecase;
    private GetMoviesUseCase mGetPopularShows;
    private MoviesView mMoviesView;

    private boolean isLoading = false;
    private boolean mRegistered;

    @Inject
    public MoviesPresenter(ConfigurationUseCase configurationUsecase, GetMoviesUseCase getMoviesUsecase, Bus bus) {

        mConfigureUsecase   = configurationUsecase;
        mGetPopularShows    = getMoviesUsecase;
        mBus = bus;
    }

    public void attachView (MoviesView moviesView) {

        mMoviesView = moviesView;
    }

    @Subscribe
    public void onPopularMoviesReceived(MoviesWrapper moviesWrapper) {

        if (mMoviesView.isTheListEmpty()) {

            mMoviesView.hideLoading();
            mMoviesView.showMovies(moviesWrapper.getResults());

        } else {

            mMoviesView.hideActionLabel();
            mMoviesView.appendMovies(moviesWrapper.getResults());
        }

        isLoading = false;
    }

    @Subscribe
    public void onConfigurationFinished (String baseImageUrl) {

        Constants.BASIC_STATIC_URL = baseImageUrl;
        mGetPopularShows.execute();
    }

    public void onEndListReached () {

        mGetPopularShows.execute();
        mMoviesView.showLoadingLabel ();
        isLoading = true;
    }

    @Override
    public void start() {

        if (mMoviesView.isTheListEmpty()) {

            mBus.register(this);
            mRegistered = true;

            mMoviesView.showLoading();
            mConfigureUsecase.execute();
        }
    }

    @Override
    public void stop() {
    }

    public boolean isLoading() {

        return isLoading;
    }

    public void setLoading(boolean isLoading) {

        this.isLoading = isLoading;
    }
}