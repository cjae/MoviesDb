package com.esod.cjae.rest;


import com.esod.cjae.entities.ConfigurationResponse;
import com.esod.cjae.entities.ImagesWrapper;
import com.esod.cjae.entities.MovieDetail;
import com.esod.cjae.entities.MoviesWrapper;
import com.esod.cjae.entities.ReviewsWrapper;
import com.esod.cjae.util.Constants;
import com.squareup.otto.Bus;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by saulmm on 31/01/15.
 */
public class APIClient implements RestDataSource {

    private static Retrofit retrofit = null;
    private final ApiInterface apiService;
    private final Bus bus;

    public APIClient(Bus bus) {
        apiService = getClient().create(ApiInterface.class);
        this.bus = bus;
    }

    private static Retrofit getClient() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(40, TimeUnit.SECONDS)
                .connectTimeout(40, TimeUnit.SECONDS)
                .build();

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.MOVIE_DB_HOST)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    @Override
    public void getMovies() {
        apiService.getPopularMovies(Constants.API_KEY, retrofitCallback);
    }

    @Override
    public void getDetailMovie(String id) {
        apiService.getMovieDetail(Constants.API_KEY, id, retrofitCallback);
    }

    @Override
    public void getReviews(String id) {
        apiService.getReviews(Constants.API_KEY, id, retrofitCallback);
    }

    @Override
    public void getConfiguration() {
        apiService.getConfiguration(Constants.API_KEY, retrofitCallback);
    }

    @Override
    public void getImages(String movieId) {
        apiService.getImages(Constants.API_KEY, movieId, retrofitCallback);
    }

    @Override
    public void getMoviesByPage(int page) {
        apiService.getPopularMoviesByPage(Constants.API_KEY, page + "", retrofitCallback);
    }

    private Callback retrofitCallback = new Callback() {
        @Override
        public void onResponse(Call call, Response response) {
            if (response.body() instanceof MovieDetail) {
                MovieDetail detailResponse = (MovieDetail) response.body();
                bus.post(detailResponse);

            } else if (response.body() instanceof MoviesWrapper) {
                MoviesWrapper moviesApiResponse = (MoviesWrapper) response.body();
                bus.post(moviesApiResponse);

            } else if (response.body() instanceof ConfigurationResponse) {
                ConfigurationResponse configurationResponse = (ConfigurationResponse) response.body();
                bus.post(configurationResponse);

            } else if (response.body() instanceof ReviewsWrapper) {
                ReviewsWrapper reviewsWrapper = (ReviewsWrapper) response.body();
                bus.post(reviewsWrapper);
            } else if (response.body() instanceof ImagesWrapper) {
                ImagesWrapper imagesWrapper = (ImagesWrapper) response.body();
                bus.post(imagesWrapper);
            }
        }

        @Override
        public void onFailure(Call call, Throwable t) {
            System.out.printf("[DEBUG] APIClient failure - " + t.getMessage());
        }
    };
}
