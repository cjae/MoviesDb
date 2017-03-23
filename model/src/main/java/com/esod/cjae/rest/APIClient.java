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
        Call<MoviesWrapper> call = apiService.getPopularMovies(Constants.API_KEY);
        call.enqueue(new Callback<MoviesWrapper>() {
            @Override
            public void onResponse(Call<MoviesWrapper> call, Response<MoviesWrapper> response) {
                bus.post(response.body());
            }

            @Override
            public void onFailure(Call<MoviesWrapper> call, Throwable t) {
                System.out.printf("[DEBUG] APIClient failure - " + t.getMessage());
            }
        });
    }

    @Override
    public void getDetailMovie(String id) {
        Call<MovieDetail> call = apiService.getMovieDetail(Constants.API_KEY, id);
        call.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                bus.post( response.body());
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
                System.out.printf("[DEBUG] APIClient failure - " + t.getMessage());
            }
        });
    }

    @Override
    public void getReviews(String id) {
        Call<ReviewsWrapper> call = apiService.getReviews(Constants.API_KEY, id);
        call.enqueue(new Callback<ReviewsWrapper>() {
            @Override
            public void onResponse(Call<ReviewsWrapper> call, Response<ReviewsWrapper> response) {
                bus.post(response.body());
            }

            @Override
            public void onFailure(Call<ReviewsWrapper> call, Throwable t) {
                System.out.printf("[DEBUG] APIClient failure - " + t.getMessage());
            }
        });
    }

    @Override
    public void getConfiguration() {
        Call<ConfigurationResponse> call = apiService.getConfiguration(Constants.API_KEY);
        call.enqueue(new Callback<ConfigurationResponse>() {
            @Override
            public void onResponse(Call<ConfigurationResponse> call, Response<ConfigurationResponse> response) {
                bus.post(response.body());
            }

            @Override
            public void onFailure(Call<ConfigurationResponse> call, Throwable t) {
                System.out.printf("[DEBUG] APIClient failure - " + t.getMessage());
            }
        });
    }

    @Override
    public void getImages(String movieId) {
        Call<ImagesWrapper> call = apiService.getImages(Constants.API_KEY, movieId);
        call.enqueue(new Callback<ImagesWrapper>() {
            @Override
            public void onResponse(Call<ImagesWrapper> call, Response<ImagesWrapper> response) {
                bus.post(response.body());
            }

            @Override
            public void onFailure(Call<ImagesWrapper> call, Throwable t) {
                System.out.printf("[DEBUG] APIClient failure - " + t.getMessage());
            }
        });
    }

    @Override
    public void getMoviesByPage(int page) {
        Call<MoviesWrapper> call = apiService.getPopularMoviesByPage(Constants.API_KEY, page + "");
        call.enqueue(new Callback<MoviesWrapper>() {
            @Override
            public void onResponse(Call<MoviesWrapper> call, Response<MoviesWrapper> response) {
                bus.post(response.body());
            }

            @Override
            public void onFailure(Call<MoviesWrapper> call, Throwable t) {
                System.out.printf("[DEBUG] APIClient failure - " + t.getMessage());
            }
        });
    }
}
