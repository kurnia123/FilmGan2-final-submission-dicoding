package com.example.filmgan2;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;
    private static Retrofit retrofitFavorite = null;
    private static Retrofit retrofitSearch = null;
    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/";
    private static final String BASE_URL_FAVORITE = "https://api.themoviedb.org/3/";
    private static final String BASE_URL_SEARCH = "https://api.themoviedb.org/3/search/";

    public static Retrofit getSearchRetrofitInstance() {
        if (retrofitSearch == null) {
            retrofitSearch = new Retrofit.Builder()
                    .baseUrl(BASE_URL_SEARCH)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitSearch;
    }

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getFavoriteInstance() {
        if (retrofitFavorite == null) {
            retrofitFavorite = new Retrofit.Builder()
                    .baseUrl(BASE_URL_FAVORITE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofitFavorite;
    }

}
