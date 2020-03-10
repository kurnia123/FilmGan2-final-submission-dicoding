package com.example.filmgan2.application.film.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetService {
//    @GET("/movie?api_key=e8fc80d206f091ca31959c53f8c5e2f2&language=en")
//    Call<ArrayList<filmData>> getAllFilm();

    @GET("movie")
    Call<filmResponse> getTopRatedMovies(@Query("api_key") String apiKey,@Query("language") String language);
}
