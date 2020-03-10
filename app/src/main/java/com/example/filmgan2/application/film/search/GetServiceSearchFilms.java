package com.example.filmgan2.application.film.search;

import com.example.filmgan2.application.film.model.filmResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetServiceSearchFilms {
    @GET("movie")
    Call<filmResponse> getMovieSearch(@Query("api_key") String apiKey, @Query("language") String language, @Query("query") String query);
}
