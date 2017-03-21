package com.esod.cjae.rest;

import com.esod.cjae.entities.ConfigurationResponse;
import com.esod.cjae.entities.ImagesWrapper;
import com.esod.cjae.entities.MovieDetail;
import com.esod.cjae.entities.MoviesWrapper;
import com.esod.cjae.entities.ReviewsWrapper;

import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface representing the ApiInterface endpoints
 * used by retrofit
 */
interface ApiInterface {

    @GET("/movie/popular")
    void getPopularMovies(@Query("api_key") String apiKey,
                          Callback<MoviesWrapper> callback);

    @GET("/movie/{id}")
    void getMovieDetail(@Query("api_key") String apiKey, @Path("id") String id,
                        Callback<MovieDetail> callback);

    @GET("/movie/popular")
    void getPopularMoviesByPage(@Query("api_key") String apiKey,
                                @Query("page") String page,
                                Callback<MoviesWrapper> callback);

    @GET("/configuration")
    void getConfiguration(@Query("api_key") String apiKey, Callback<ConfigurationResponse> callback);

    @GET("/movie/{id}/reviews")
    void getReviews(@Query("api_key") String apiKey,
                    @Path("id") String id, Callback<ReviewsWrapper> callback);

    @GET("/movie/{id}/images")
    void getImages(@Query("api_key") String apiKey, @Path("id") String movieId,
                   Callback<ImagesWrapper> callback);
}
