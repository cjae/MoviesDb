package com.esod.cjae.rest;


import com.esod.cjae.MediaDataSource;

/**
 * Created by saulmm on 25/02/15.
 */
public interface RestDataSource extends MediaDataSource {

    public void getMoviesByPage(int page);
}
