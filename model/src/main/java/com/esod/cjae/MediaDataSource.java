package com.esod.cjae;

/**
 * Created by Jedidiah on 21/03/2017.
 */

public interface MediaDataSource {

    void getMovies();

    void getDetailMovie (String id);

    /**
     * Get the reviews for a particular movie id.
     *
     * @param movieId movie id
     */
    void getReviews (String movieId);

    void getConfiguration ();

    /**
     * Get a list of images represented by a MoviesWrapper
     * class
     *
     * @param movieId the movie id
     */
    void getImages (String movieId);
}
