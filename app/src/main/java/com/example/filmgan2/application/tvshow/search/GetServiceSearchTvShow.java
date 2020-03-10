package com.example.filmgan2.application.tvshow.search;

import com.example.filmgan2.application.tvshow.model.TvResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetServiceSearchTvShow {
    @GET("tv")
    Call<TvResponse> getTvShowSearch(@Query("api_key") String apiKey, @Query("language") String language, @Query("query") String query);
}
