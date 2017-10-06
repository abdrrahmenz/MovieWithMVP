package com.expert.andro.basisdatacataloguemovie.data.service;

import com.expert.andro.basisdatacataloguemovie.BuildConfig;
import com.expert.andro.basisdatacataloguemovie.data.model.Movie;
import com.expert.andro.basisdatacataloguemovie.data.model.MovieResponse;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by adul on 27/09/17.
 */

public interface MovieDbApi {

    @GET("movie/upcoming")
    Observable<MovieResponse> getUpcomingMovie(@Query("page") int page);

    @GET("movie/now_playing")
    Observable<MovieResponse> getNowPlayingMovie(@Query("page") int page);

    @GET("search/movie")
    Observable<MovieResponse> getSearchMovie(@Query("query") String query,@Query("page") int page);

    class Creator {
        public static MovieDbApi instance() {
            final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            final MovieDbInterceptor movieDbInterceptor = new MovieDbInterceptor();

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(movieDbInterceptor)
                    .build();

            final Retrofit retrofit = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .baseUrl(BuildConfig.BASE_URL_MOVIE)
                    .build();
            return retrofit.create(MovieDbApi.class);
        }
    }
}
