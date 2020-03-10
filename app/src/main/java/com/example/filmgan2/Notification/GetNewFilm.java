package com.example.filmgan2.Notification;

import com.example.filmgan2.application.film.model.filmResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetNewFilm {

//    https://api.themoviedb.org/3/discover/movie?api_key=e8fc80d206f091ca31959c53f8c5e2f2&primary_release_date.gte=2020-02-21&primary_release_date.lte=2020-02-21
    @GET("movie")
    Call<filmResponse> getNewFilm(@Query("api_key") String apiKey, @Query("primary_release_date.gte") String date, @Query("primary_release_date.lte") String date1, @Query("language") String language);
}
