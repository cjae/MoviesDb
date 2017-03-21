/**
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
package com.esod.cjae.domain.controller;

import com.esod.cjae.MediaDataSource;
import com.esod.cjae.domain.usecase.GetMovieDetailUseCase;
import com.esod.cjae.entities.ImagesWrapper;
import com.esod.cjae.entities.MovieDetail;
import com.esod.cjae.entities.ReviewsWrapper;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

/**
 * This class is an implementation of {@link GetMovieDetailUseCase}
 */
public class GetMovieDetailUseCaseController implements GetMovieDetailUseCase {

    private final MediaDataSource mMovieDataSource;
    private final String mMovieId;
    private final Bus mUiBus;
    private MovieDetail mMovieDetail;


    public GetMovieDetailUseCaseController(String movieId, Bus uiBus,
                                           MediaDataSource dataSource) {
        mMovieId        = movieId;
        mUiBus          = uiBus;
        mMovieDataSource= dataSource;

        mUiBus.register(this);
    }

    @Override
    public void requestMovieDetail(String movieId) {
        mMovieDataSource.getDetailMovie(movieId);
    }

    @Subscribe
    @Override
    public void onMovieDetailResponse(MovieDetail movieDetail) {
        mMovieDetail = movieDetail;
        requestMovieImages(mMovieId);
    }

    @Subscribe
    @Override
    public void onMovieReviewsResponse (ReviewsWrapper reviewsWrapper) {
        sendDetailMovieToPresenter(mMovieDetail);

        mUiBus.post(reviewsWrapper);
        mUiBus.unregister(this);
    }

    @Subscribe
    @Override
    public void onMovieImagesResponse(ImagesWrapper imageWrapper) {
        mMovieDetail.setMovieImagesList(imageWrapper.getBackdrops());
        requestMovieReviews(mMovieId);
    }

    @Override
    public void sendDetailMovieToPresenter(MovieDetail response) {
        mUiBus.post(response);
    }

    @Override
    public void requestMovieReviews(String movieId) {
        mMovieDataSource.getReviews(movieId);
    }

    @Override
    public void requestMovieImages(String movieId) {
        mMovieDataSource.getImages(movieId);
    }

    @Override
    public void execute() {
        requestMovieDetail(mMovieId);
    }
}
