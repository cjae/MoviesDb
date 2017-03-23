package com.esod.moviesdb.views.renderer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esod.cjae.entities.Movie;
import com.esod.moviesdb.R;
import com.esod.moviesdb.utils.RecyclerViewClickListener;
import com.pedrogomez.renderers.Renderer;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Jedidiah on 22/03/2017.
 */

public class MoviesRenderer extends Renderer<Movie> {

    private Context mContext;
    private RecyclerViewClickListener mRecyclerClickListener;

    @Override
    protected void setUpView(View rootView) {

    }

    @Override
    protected void hookListeners(View rootView) {

    }

    @Override
    protected View inflate(LayoutInflater inflater, ViewGroup parent) {
        View inflatedView = inflater.inflate(R.layout.item_movie, parent, false);
        /*
         * You don't have to use ButterKnife library to implement the mapping between your layout
         * and your widgets you can implement setUpView and hookListener methods declared in
         * Renderer<T> class.
         */
        this.mContext = parent.getContext();
        ButterKnife.bind(this, inflatedView);

        return inflatedView;
    }

    @Override
    public void render() {

    }
}
