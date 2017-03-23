package com.esod.cjae.rest;

import com.esod.cjae.entities.ConfigurationResponse;
import com.esod.cjae.entities.ImagesWrapper;
import com.esod.cjae.entities.MovieDetail;
import com.esod.cjae.entities.MoviesWrapper;
import com.esod.cjae.entities.ReviewsWrapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface representing the ApiInterface endpoints
 * used by retrofit
 */
interface ApiInterface {

    @GET("movie/popular")
    Call<MoviesWrapper> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<MovieDetail> getMovieDetail(@Query("api_key") String apiKey, @Path("id") String id);

    @GET("movie/popular")
    Call<MoviesWrapper> getPopularMoviesByPage(@Query("api_key") String apiKey,
                                @Query("page") String page);

    @GET("configuration")
    Call<ConfigurationResponse> getConfiguration(@Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ReviewsWrapper> getReviews(@Query("api_key") String apiKey,
                    @Path("id") String id);

    @GET("movie/{id}/images")
    Call<ImagesWrapper> getImages(@Query("api_key") String apiKey, @Path("id") String movieId);
}
