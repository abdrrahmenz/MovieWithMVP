package com.expert.andro.basisdatacataloguemovie.ui.utils;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.expert.andro.basisdatacataloguemovie.R;

/**
 * Created by adul on 27/09/17.
 */

public class GlideBindingAdapter {
    @BindingAdapter({"bind:posterURL"})
    public static void loadImage(ImageView view, String imageUrl) {
        final String finalUrl = "http://image.tmdb.org/t/p/w342/" + imageUrl;
        Glide.with(view.getContext())
                .load(finalUrl)
                .placeholder(R.drawable.ic_error_outline_grey_900_48dp)
                .error(R.drawable.ic_error_outline_grey_900_48dp)
                .into(view);
    }

    @BindingAdapter({"bind:loadImage"})
    public static void loadVideoImg(ImageView view, String url) {
        Glide.with(view.getContext())
                .load(url)
                .placeholder(R.drawable.ic_error_outline_grey_900_48dp)
                .error(R.drawable.ic_error_outline_grey_900_48dp)
                .into(view);
    }

    @BindingAdapter({"bind:backdropURL"})
    public static void loadBackdrop(ImageView view, String imageUrl) {
        final String finalUrl = "http://image.tmdb.org/t/p/w780" + imageUrl;
        Glide.with(view.getContext())
                .load(finalUrl)
                .placeholder(R.drawable.ic_error_outline_grey_900_48dp)
                .error(R.drawable.ic_error_outline_grey_900_48dp)
                .into(view);
    }
}
